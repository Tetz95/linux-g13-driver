#ifndef __G13_H__
#define __G13_H__

// ANPASSUNG START: 'using namespace std;' entfernt und Typen explizit mit std:: qualifiziert.
// Dies verhindert Namenskonflikte im globalen Namespace und ist "good practice" für Header-Dateien.
#include <string>
#include <vector>
#include <memory>
#include <map>
#include <istream>
// ANPASSUNG ENDE

#include "Constants.h"
#include "G13Action.h"
#include "Macro.h"

// ANPASSUNG START: 'using namespace std;' wurde entfernt.
// using namespace std;
// ANPASSUNG ENDE

class G13 {
private:
    // ANPASSUNG START: std:: Präfixe hinzugefügt
	std::vector<std::unique_ptr<G13Action>> actions;
    // ANPASSUNG ENDE
	libusb_device        *device;
	libusb_device_handle *handle;
	int                   uinput_file;

	int                   loaded;
	volatile int          keepGoing;

	stick_mode_t          stick_mode;
	int                   stick_keys[4];

	int                   bindings;

    // ANPASSUNG START: std:: Präfixe hinzugefügt
	std::unique_ptr<Macro> loadMacro(int id);
	void parse_bindings_from_stream(std::istream& stream);
    // ANPASSUNG ENDE

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