#ifndef __G13_H__
#define __G13_H__

#include <string>
#include <map>

#include "Constants.h"
#include "G13Action.h"
#include "Macro.h"

using namespace std;

class G13 {
private:
	map<int, Macro *>     idToMacro;
	G13Action            *actions[G13_NUM_KEYS];

	libusb_device        *device;
	libusb_device_handle *handle;
	int                   uinput_file;

	int                   loaded;
	int                   keepGoing;

	stick_mode_t          stick_mode;
	int                   stick_keys[4];

	int                   bindings;

	Macro *loadMacro(int id);

	int  read();
	void parse_joystick(unsigned char *buf);
	void parse_key(int key, unsigned char *byte);
	void parse_keys(unsigned char *buf);

public:
	G13(libusb_device *device);
	~G13();

	void start();
	void stop();
	void loadBindings();
	void setColor(int r, int g, int b);
};


#endif
