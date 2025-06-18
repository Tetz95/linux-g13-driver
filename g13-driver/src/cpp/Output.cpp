#include <iostream>
#include <fstream>
#include <vector>
#include <sys/stat.h>
#include <stdio.h>
#include <string.h>
#include <signal.h>
#include <stdlib.h>
#include <unistd.h>
#include <iomanip>
#include <linux/uinput.h>
#include <fcntl.h>
#include <pthread.h> // Sicherstellen, dass der Header inkludiert ist

#include "Output.h"
#include "Constants.h"

using namespace std;

// ANPASSUNG START: Globale Variablen werden zu statischen Membern der UInput-Klasse.
int UInput::file = -1;
pthread_mutex_t UInput::plock = PTHREAD_MUTEX_INITIALIZER;
// ANPASSUNG ENDE


// ANPASSUNG START: Implementierung der statischen Klassenmethoden.
// Die Logik bleibt identisch, ist aber nun innerhalb der Klasse gekapselt.

void UInput::send_event(int type, int code, int val) {
	if (file < 0) {
		return;
	}

	pthread_mutex_lock(&plock);

	struct input_event event;
	memset(&event, 0, sizeof(event));
	gettimeofday(&event.time, nullptr);
	event.type = type;
	event.code = code;
	event.value = val;

	write(file, &event, sizeof(event));

	pthread_mutex_unlock(&plock);
}

void UInput::flush() {
    if (file < 0) return;
	pthread_mutex_lock(&plock);
	fsync(file);
	pthread_mutex_unlock(&plock);
}

void UInput::close_uinput() {
    if (file >= 0) {
        // WICHTIGE KORREKTUR: Zerstört das uinput-Gerät, bevor die Datei geschlossen wird.
        ioctl(file, UI_DEV_DESTROY);
        close(file);
        file = -1;
    }
}

bool UInput::create_uinput() {
	struct uinput_user_dev uinp;
	const char* dev_uinput_fname =
			access("/dev/input/uinput", F_OK) == 0 ? "/dev/input/uinput" :
			access("/dev/uinput", F_OK) == 0 ? "/dev/uinput" : 0;

	if (!dev_uinput_fname) {
		cerr << "Could not find an uinput device" << endl;
		return false;
	}

	if (access(dev_uinput_fname, W_OK) != 0) {
		cerr << dev_uinput_fname << " doesn't grant write permissions" << endl;
		return false;
	}

	file = open(dev_uinput_fname, O_WRONLY | O_NDELAY);
	if (file < 0) {
		cerr << "Could not open uinput" << endl;
		return false;
	}

	memset(&uinp, 0, sizeof(uinp));
	char name[] = "G13";
	strncpy(uinp.name, name, sizeof(name));
	uinp.id.version = 1;
	uinp.id.bustype = BUS_USB;
	uinp.id.product = G13_PRODUCT_ID;
	uinp.id.vendor = G13_VENDOR_ID;
	uinp.absmin[ABS_X] = 0;
	uinp.absmin[ABS_Y] = 0;
	uinp.absmax[ABS_X] = 0xff;
	uinp.absmax[ABS_Y] = 0xff;

	ioctl(file, UI_SET_EVBIT, EV_KEY);
	ioctl(file, UI_SET_EVBIT, EV_ABS);
	ioctl(file, UI_SET_MSCBIT, MSC_SCAN);
	ioctl(file, UI_SET_ABSBIT, ABS_X);
	ioctl(file, UI_SET_ABSBIT, ABS_Y);

	for (int i = 0; i < 256; i++)
		ioctl(file, UI_SET_KEYBIT, i);
	ioctl(file, UI_SET_KEYBIT, BTN_THUMB);

	int retcode = write(file, &uinp, sizeof(uinp));
	if (retcode < 0) {
		cerr << "Could not write to uinput device (" << retcode << ")" << endl;
        close(file); file = -1;
		return false;
	}

	retcode = ioctl(file, UI_DEV_CREATE);
	if (retcode) {
		cerr << "Error creating uinput device for G13" << endl;
        close(file); file = -1;
		return false;
	}
	return true;
}
// ANPASSUNG ENDE