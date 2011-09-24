#include <iostream>
#include <unistd.h>
#include <fcntl.h>

#include <sys/time.h>
#include <time.h>
#include <unistd.h>
#include <linux/uinput.h>
#include <stdlib.h>
#include <string.h>

#include "G13Action.h"

using namespace std;

G13Action::G13Action() {
	pressed = 0;
}

G13Action::~G13Action() {
}

void G13Action::key_down() {
	//cout << "G13Action::key_down()\n";
}

void G13Action::key_up() {
	//cout << "G13Action::key_up()\n";
}

int G13Action::set(int state) {
	int s = 0;
	if (state != 0) {
		s = 1;
	}

	if (s != pressed) {
		pressed = s;

		if (s) {
			key_down();
		}
		else {
			key_up();
		}

		return 1;
	}

	return 0;
}


int G13Action::isPressed() {
	return pressed;
}
