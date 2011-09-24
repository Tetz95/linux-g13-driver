#include <unistd.h>
#include <linux/uinput.h>
#include <vector>
#include <pthread.h>
#include <string.h>
#include <stdlib.h>
#include <iostream>

#include "MacroAction.h"

using namespace std;

void *execute_macro(void *args) {
	MacroAction::MultiEventThread *t = (MacroAction::MultiEventThread *)args;

	t->execute();

	return NULL;
}


MacroAction::Event *MacroAction::tokenToEvent(char *token) {
	if (token == NULL) {
		return NULL;
	}

	if (strncmp(token, "kd.", 3) == 0) {
		int code = atoi(&(token[3]));
		return (MacroAction::Event *)new KeyDownEvent(code);
	}
	else if (strncmp(token, "ku.", 3) == 0) {
		int code = atoi(&(token[3]));
		return (MacroAction::Event *)new KeyUpEvent(code);
	}
	else if (strncmp(token, "d.", 2) == 0) {
		int delay = atoi(&(token[2]));
		return (MacroAction::Event *)new DelayEvent(delay);
	}
	else {
		cout << "MacroAction::tokenToEvent() unknown token: " << token << "\n";
	}

	return NULL;
}

MacroAction::MacroAction(char *tokens) {

	//cout << "MacroAction::MacroAction() tokens=" << tokens << "\n";

	pthread_attr_init(&attr);

	repeats = 0;

	thread = NULL;

	if (tokens == NULL) {
		return;
	}

	// kd.keycode,ku.keycode,d.time
	char *token = strtok(tokens, ",");
	while (token != NULL) {
		MacroAction::Event *event = tokenToEvent(token);
		if (event != NULL) {
			events.push_back(event);
		}
		token = strtok(NULL, ",");
	}
}

MacroAction::~MacroAction() {
}

void MacroAction::key_down() {

	if (thread != NULL) {
		cout << "MacroAction::key_down(): current thread in action\n";
		return;
	}

	//cout << "MacroAction::key_down()\n";

	thread = new MultiEventThread();
	thread->keepRepeating = repeats;
	thread->local_events = events;

	pthread_t pthread;
	pthread_create(&pthread, &attr, execute_macro, thread );
	//cout << "MacroAction::key_down() thread created\n";
}

void MacroAction::key_up() {

	if (thread != NULL) {
		thread->keepRepeating = false;
	}

	thread = NULL;
}

int MacroAction::getRepeats() {
	return repeats;
}

void MacroAction::setRepeats(int repeats) {
	this->repeats = repeats;
}

vector<MacroAction::Event *> MacroAction::getEvents() {
	return events;
}

