#include <iostream>
#include <fstream>
#include <vector>
#include <string.h>
#include <signal.h>
#include <stdlib.h>
#include <unistd.h>
#include <pthread.h>
#include <libusb-1.0/libusb.h>

#include "G13.h"
#include "Output.h"

using namespace std;

vector<G13 *> g13s;
vector<pthread_t> g13_threads;
volatile sig_atomic_t keep_running = 1;

void discover() {
	libusb_context *ctx = nullptr;
	libusb_device **devs;

	int ret = libusb_init(&ctx);
	if (ret < 0) {
		cout << "Initialization error: " << ret << "\n";
		return;
	}

	ssize_t count = libusb_get_device_list(ctx, &devs);
	if (count < 0) {
		cout << "Error while getting device list" << "\n";
		return;
	}


	for (int i = 0; i < count; i++) {
		libusb_device_descriptor desc;
		int r = libusb_get_device_descriptor(devs[i], &desc);
		if (r < 0) {
			cout << "Failed to get device descriptor" << endl;
			return;
		}

		if (desc.idVendor == G13_VENDOR_ID && desc.idProduct == G13_PRODUCT_ID) {
			G13 *g13 = new G13(devs[i]);
			g13s.push_back(g13);
		}
	}

	libusb_free_device_list(devs, 1);
}

void *executeG13(void *arg) {
	G13 *g13 = (G13 *)arg;
    if (g13) {
	    g13->start();
    }
	return nullptr;
}

void start() {
	if (g13s.size() > 0) {
		pthread_attr_t attr;
		pthread_attr_init(&attr);

        g13_threads.resize(g13s.size());
		for (unsigned int i = 0; i < g13s.size(); i++) {
			pthread_create(&g13_threads[i], &attr, executeG13, g13s[i] );
		}
        pthread_attr_destroy(&attr);

		for (unsigned int i = 0; i < g13s.size(); i++) {
			pthread_join(g13_threads[i], nullptr);
		}
	}
}

void signal_handler(int signal) {
    keep_running = 0;
	for (unsigned int i = 0; i < g13s.size(); i++) {
        if (g13s[i]) {
		    g13s[i]->stop();
        }
	}
}

void cleanup() {
	for (unsigned int i = 0; i < g13s.size(); i++) {
		delete g13s[i];
	}
    g13s.clear();
    // ANPASSUNG START: Aufruf an die statische Methode der UInput-Klasse angepasst.
    UInput::close_uinput();
    // ANPASSUNG ENDE
}

int main(int argc, char *argv[]) {
    // ANPASSUNG START: Aufruf an die statische Methode der UInput-Klasse angepasst.
	if (!UInput::create_uinput()) {
    // ANPASSUNG ENDE
        cerr << "Failed to initialize uinput. Exiting." << endl;
        return 1;
    }

	signal(SIGINT, signal_handler);
    signal(SIGTERM, signal_handler);

	discover();

	cout << "Found " << g13s.size() << " G13s" << "\n";

    if (!g13s.empty()) {
	    start();
    }

	cleanup();

	return 0;
}