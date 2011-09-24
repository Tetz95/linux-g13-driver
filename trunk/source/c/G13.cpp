#include <iostream>
#include <fstream>
#include <vector>
#include <sys/stat.h>
#include <stdio.h>
#include <string.h>
#include <signal.h>
#include <stdlib.h>
#include <unistd.h>

#include <libusb-1.0/libusb.h>

#include <iomanip>

#include <linux/uinput.h>
#include <fcntl.h>

#include <pthread.h>


#include "Constants.h"
#include "G13.h"
#include "G13Action.h"
#include "PassThroughAction.h"
#include "MacroAction.h"
#include "Output.h"

using namespace std;

void trim(char *s) {
	// Trim spaces and tabs from beginning:
	int i = 0, j;
	while ((s[i] == ' ') || (s[i] == '\t')) {
		i++;
	}
	if (i > 0) {
		for (j = 0; j < strlen(s); j++) {
			s[j] = s[j + i];
		}
		s[j] = '\0';
	}

	// Trim spaces and tabs from end:
	i = strlen(s) - 1;
	while ((s[i] == ' ') || (s[i] == '\t')) {
		i--;
	}
	if (i < (strlen(s) - 1)) {
		s[i + 1] = '\0';
	}
}


G13::G13(libusb_device *device) {

	this->device = device;

	this->loaded = 0;

	this->bindings = 0;

	for (int i = 0; i < G13_NUM_KEYS; i++) {
		actions[i] = new G13Action();
	}

	if (libusb_open(device, &handle) != 0) {
		cerr << "Error opening G13 device" << endl;
		return;
	}

	if (libusb_kernel_driver_active(handle, 0) == 1) {
		if (libusb_detach_kernel_driver(handle, 0) == 0) {
			cout << "Kernel driver detached" << endl;
		}
	}

	if (libusb_claim_interface(handle, 0) < 0) {
		cerr << "Cannot Claim Interface" << endl;
		return;
	}

	setColor(128, 128, 128);

	this->loaded = 1;

}

G13::~G13() {
	if (!this->loaded) {
		return;
	}

	setColor(128, 128, 128);

	libusb_release_interface(this->handle, 0);
	libusb_close(this->handle);

}

void G13::start() {
	if (!this->loaded) {
		return;
	}

	loadBindings();

	keepGoing = 1;

	while (keepGoing) {
		read();
	}
}

void G13::stop() {
	if (!this->loaded) {
		return;
	}

	keepGoing = 0;
}

Macro *G13::loadMacro(int num) {

	map<int, Macro*>::iterator it = idToMacro.find(num);
	if (it != idToMacro.end()) {
		cout << "macro cached\n";
		return it->second;
	}

	char filename[1024];

	sprintf(filename, "%s/.g13/macro-%d.properties", getenv("HOME"), num);
	//cout << "G13::loadMacro(" << num << ") filename=" << filename << "\n";
	ifstream file (filename);

	if (!file.is_open()) {
		cout << "Could not open config file: " << filename << "\n";
		return null;
	}

	Macro *macro = new Macro();
	macro->setId(num);
	while (file.good()) {
		string line;
		getline(file, line);
		//cout << line << "\n";

		char l[1024];
		strcpy(l, (char *)line.c_str());
		trim(l);
		if (strlen(l) > 0 && l[0] != '#') {
			char *key = strtok(l, "=");
			char *value = strtok(NULL, "\n");
			trim(key);
			trim(value);
			//cout << "G13::loadMacro(" << num << ") key=" << key << ", value=" << value << "\n";
			if (strcmp(key, "name") == 0) {
				macro->setName(value);
			}
			else if (strcmp(key, "sequence") == 0) {
				macro->setSequence(value);
			}
		}
	}

	idToMacro.insert(pair<int, Macro *>(num, macro));

	return idToMacro.find(num)->second;

}

void G13::loadBindings() {

	char filename[1024];

	idToMacro.clear();

	sprintf(filename, "%s/.g13/bindings-%d.properties", getenv("HOME"), bindings);
	cout << "loading " << filename << "\n";

	  ifstream file (filename);
	  if (!file.is_open()) {
		  cout << "Could not open config file: " << filename << "\n";
		  setColor(128, 128, 128);
		  return;
	  }


	  while (file.good()) {
		  string line;
	      getline(file, line);

	      char l[1024];
		  strcpy(l, (char *)line.c_str());
		  trim(l);
		  if (strlen(l) > 0) {
			  char *key = strtok(l, "=");
			  if (key[0] == '#') {
				  // ignore line
			  }
			  else if (strcmp(key, "color") == 0) {
				  char *num = strtok(NULL, ",");
				  int r = atoi(num);
				  num = strtok(NULL, ",");
				  int g = atoi(num);
				  num = strtok(NULL, ",");
				  int b = atoi(num);

				  setColor(r, g, b);
			  }
			  else if (key[0] == 'G') {
				  int gKey = atoi(&key[1]);
				  //cout << "gKey = " << gKey << "\n";
				  char *type = strtok(NULL, ",");
				  trim(type);
				  //cout << "type = " << type << "\n";
				  if (strcmp(type, "p") == 0) { /* passthrough */
					  char *keytype = strtok(NULL, ",\n ");
					  trim(keytype);
					  int keycode = atoi(&keytype[2]);

					  if (actions[gKey] != null) {
						  delete actions[gKey];
					  }

					  //cout << "assigning G" << gKey << " to keycode " << keycode << "\n";
					  G13Action *action = new PassThroughAction(keycode);
					  actions[gKey] = action;
				  }
				  else if (strcmp(type, "m") == 0) { /* macro */
					  int macroId = atoi(strtok(NULL, ",\n "));
					  int repeats = atoi(strtok(NULL, ",\n "));
					  //cout << "macroId = " << macroId << "\n";
					  Macro *macro = loadMacro(macroId);
					  MacroAction *action = new MacroAction(macro->getSequence());
					  action->setRepeats(repeats);
					  actions[gKey] = action;
				  }
				  else {
					  cout << "G13::loadBindings() unknown type '" << type << "\n";
				  }

			  }
			  else {
				  cout << "G13::loadBindings() Unknown first token: " << key << "\n";
			  }
		  }

	      //cout << line << endl;
	  }

	  file.close();
}

void G13::setColor(int red, int green, int blue) {
	int error;
	unsigned char usb_data[] = { 5, 0, 0, 0, 0 };
	usb_data[1] = red;
	usb_data[2] = green;
	usb_data[3] = blue;

	error = libusb_control_transfer(handle, LIBUSB_REQUEST_TYPE_CLASS | LIBUSB_RECIPIENT_INTERFACE, 9, 0x307, 0,
			usb_data, 5, 1000);

	if (error != 5) {
		cerr << "Problem sending data" << endl;
	}

}

int G13::read() {
	unsigned char buffer[G13_REPORT_SIZE];
	int size;
	int error = libusb_interrupt_transfer(handle, LIBUSB_ENDPOINT_IN | G13_KEY_ENDPOINT, buffer, G13_REPORT_SIZE, &size, 1000);
	if (error && error != LIBUSB_ERROR_TIMEOUT) {
		std::map<int, std::string> errors;
		errors[LIBUSB_SUCCESS] = "LIBUSB_SUCCESS";
		errors[LIBUSB_ERROR_IO] = "LIBUSB_ERROR_IO";
		errors[LIBUSB_ERROR_INVALID_PARAM] = "LIBUSB_ERROR_INVALID_PARAM";
		errors[LIBUSB_ERROR_ACCESS] = "LIBUSB_ERROR_ACCESS";
		errors[LIBUSB_ERROR_NO_DEVICE] = "LIBUSB_ERROR_NO_DEVICE";
		errors[LIBUSB_ERROR_NOT_FOUND] = "LIBUSB_ERROR_NOT_FOUND";
		errors[LIBUSB_ERROR_BUSY] = "LIBUSB_ERROR_BUSY";
		errors[LIBUSB_ERROR_TIMEOUT] = "LIBUSB_ERROR_TIMEOUT";
		errors[LIBUSB_ERROR_OVERFLOW] = "LIBUSB_ERROR_OVERFLOW";
		errors[LIBUSB_ERROR_PIPE] = "LIBUSB_ERROR_PIPE";
		errors[LIBUSB_ERROR_INTERRUPTED] = "LIBUSB_ERROR_INTERRUPTED";
		errors[LIBUSB_ERROR_NO_MEM] = "LIBUSB_ERROR_NO_MEM";
		errors[LIBUSB_ERROR_NOT_SUPPORTED] = "LIBUSB_ERROR_NOT_SUPPORTED";
		errors[LIBUSB_ERROR_OTHER] = "LIBUSB_ERROR_OTHER    ";
		cerr << "Error while reading keys: " << error << " (" << errors[error]
				<< ")" << endl;
		cerr << "Stopping daemon" << endl;
		return -1;
	}

	if (size == G13_REPORT_SIZE) {
		parse_joystick(buffer);
		parse_keys(buffer);
		send_event(EV_SYN, SYN_REPORT, 0);
	}
	return 0;
}

void G13::parse_joystick(unsigned char *buf) {
	int stick_x = buf[1];
	int stick_y = buf[2];
	int key_left = stick_keys[STICK_LEFT];
	int key_right = stick_keys[STICK_RIGHT];
	int key_up = stick_keys[STICK_UP];
	int key_down = stick_keys[STICK_DOWN];
	if (stick_mode == STICK_ABSOLUTE) {
		send_event(EV_ABS, ABS_X, stick_x);
		send_event(EV_ABS, ABS_Y, stick_y);
	} else if (stick_mode == STICK_KEYS) {
		if (stick_x < 255 / 6) {
			send_event(EV_KEY, key_left, 1);
			send_event(EV_KEY, key_right, 0);
		} else if (stick_x > 255 / 6 * 5) {
			send_event(EV_KEY, key_left, 0);
			send_event(EV_KEY, key_right, 1);
		} else {
			send_event(EV_KEY, key_left, 0);
			send_event(EV_KEY, key_right, 0);
		}
		if (stick_y < 255 / 6) {
			send_event(EV_KEY, key_up, 1);
			send_event(EV_KEY, key_down, 0);
		} else if (stick_y > 255 / 6 * 5) {
			send_event(EV_KEY, key_up, 0);
			send_event(EV_KEY, key_down, 1);
		} else {
			send_event(EV_KEY, key_up, 0);
			send_event(EV_KEY, key_down, 0);
		}
	} else {
		/*    send_event(g13->uinput_file, EV_REL, REL_X, stick_x/16 - 8);
		 send_event(g13->uinput_file, EV_REL, REL_Y, stick_y/16 - 8);*/
	}
}
void G13::parse_key(int key, unsigned char *byte) {
	unsigned char actual_byte = byte[key / 8];
	unsigned char mask = 1 << (key % 8);

	int pressed = actual_byte & mask;

	if (key == 25 || key == 26 || key == 27 || key == 28) {
		if (pressed) {
			//cout << "key " << key << "\n";
			bindings = key - 25;
			loadBindings();
		}
		return;
	}

	int changed = actions[key]->set(pressed);

	/*
	if (changed) {
		string type = "released";
		if (actions[key]->isPressed()) {
			type = "pressed";
		}
		cout << "G" << (key+1) << " " << type << "\n";
	}
	*/
}


void G13::parse_keys(unsigned char *buf) {

	parse_key(G13_KEY_G1, buf + 3);
	parse_key(G13_KEY_G2, buf + 3);
	parse_key(G13_KEY_G3, buf + 3);
	parse_key(G13_KEY_G4, buf + 3);
	parse_key(G13_KEY_G5, buf + 3);
	parse_key(G13_KEY_G6, buf + 3);
	parse_key(G13_KEY_G7, buf + 3);
	parse_key(G13_KEY_G8, buf + 3);

	parse_key(G13_KEY_G9, buf + 3);
	parse_key(G13_KEY_G10, buf + 3);
	parse_key(G13_KEY_G11, buf + 3);
	parse_key(G13_KEY_G12, buf + 3);
	parse_key(G13_KEY_G13, buf + 3);
	parse_key(G13_KEY_G14, buf + 3);
	parse_key(G13_KEY_G15, buf + 3);
	parse_key(G13_KEY_G16, buf + 3);

	parse_key(G13_KEY_G17, buf + 3);
	parse_key(G13_KEY_G18, buf + 3);
	parse_key(G13_KEY_G19, buf + 3);
	parse_key(G13_KEY_G20, buf + 3);
	parse_key(G13_KEY_G21, buf + 3);
	parse_key(G13_KEY_G22, buf + 3);
	//  parse_key(G13_KEY_LIGHT_STATE, buf+3);

	parse_key(G13_KEY_BD, buf + 3);
	parse_key(G13_KEY_L1, buf + 3);
	parse_key(G13_KEY_L2, buf + 3);
	parse_key(G13_KEY_L3, buf + 3);
	parse_key(G13_KEY_L4, buf + 3);
	parse_key(G13_KEY_M1, buf + 3);
	parse_key(G13_KEY_M2, buf + 3);

	parse_key(G13_KEY_M3, buf + 3);
	parse_key(G13_KEY_MR, buf + 3);
	parse_key(G13_KEY_LEFT, buf + 3);
	parse_key(G13_KEY_DOWN, buf + 3);
	parse_key(G13_KEY_TOP, buf + 3);
	parse_key(G13_KEY_LIGHT, buf + 3);
	//  parse_key(G13_KEY_LIGHT2, buf+3, file);
	/*  cout << hex << setw(2) << setfill('0') << (int)buf[7];
	 cout << hex << setw(2) << setfill('0') << (int)buf[6];
	 cout << hex << setw(2) << setfill('0') << (int)buf[5];
	 cout << hex << setw(2) << setfill('0') << (int)buf[4];
	 cout << hex << setw(2) << setfill('0') << (int)buf[3] << endl;*/
}

