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
pthread_t *pthreads;

void discover() {
	libusb_context *ctx = null;
	libusb_device **devs;

	int ret = libusb_init(&ctx);
	if (ret < 0) {
		cout << "Initialization error: " << ret << "\n";
		return;
	}

	libusb_set_debug(ctx, 3);

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

	g13->start();

	return null;
}

void start() {
	if (g13s.size() > 0) {
		pthread_attr_t attr;
		pthread_attr_init(&attr);

		pthreads = (pthread_t *)malloc(2*g13s.size()*sizeof(pthread_t));
		for (unsigned int i = 0; i < g13s.size(); i++) {
			pthread_create(&pthreads[i], &attr, executeG13, g13s[i] );
		}

		for (unsigned int i = 0; i < g13s.size(); i++) {
			int *status = 0;
			pthread_join(pthreads[i], (void**) &status);
		}
	}
}

void shutdown(int signal) {

	for (int i = 0; i < sizeof(pthreads); i++) {
		pthread_kill(pthreads[i], 0);
	}

	for (unsigned int i = 0; i < g13s.size(); i++) {
		delete g13s[i];
	}

}

void cleanup() {
	for (unsigned int i = 0; i < g13s.size(); i++) {
		delete g13s[i];
	}
}

int main(int argc, char *argv[]) {

	create_uinput();

	signal(SIGINT, shutdown);

	discover();

	cout << "Found " << g13s.size() << " G13s" << "\n";

	start();

	//cleanup();

	return 0;
}
