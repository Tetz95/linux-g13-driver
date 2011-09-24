#include <linux/uinput.h>

#include <iostream>

#include "PassThroughAction.h"
#include "Output.h"

using namespace std;

PassThroughAction::PassThroughAction(int code) {
	this->keycode = code;
}
PassThroughAction::~PassThroughAction() {

}

int PassThroughAction::getKeyCode() {
	return keycode;
}

void PassThroughAction::setKeyCode(int code) {
	this->keycode = code;
}

void PassThroughAction::key_down() {
	send_event(EV_KEY, keycode, 1);
}

void PassThroughAction::key_up() {
	send_event(EV_KEY, keycode, 0);
}
