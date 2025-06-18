#include <linux/uinput.h>

#include "PassThroughAction.h"
#include "Output.h"

PassThroughAction::PassThroughAction(int code) {
	this->keycode = code;
}

PassThroughAction::~PassThroughAction() {
}

int PassThroughAction::getKeyCode() const {
	return this->keycode;
}

void PassThroughAction::setKeyCode(int code) {
	this->keycode = code;
}

void PassThroughAction::key_down() {
	// ANPASSUNG START: Aufrufe an die statische Methode der UInput-Klasse angepasst.
	UInput::send_event(EV_KEY, this->keycode, 1);
	UInput::send_event(0, 0, 0); // SYN_REPORT
    // ANPASSUNG ENDE
}

void PassThroughAction::key_up() {
	// ANPASSUNG START: Aufrufe an die statische Methode der UInput-Klasse angepasst.
	UInput::send_event(EV_KEY, this->keycode, 0);
	UInput::send_event(0, 0, 0); // SYN_REPORT
    // ANPASSUNG ENDE
}