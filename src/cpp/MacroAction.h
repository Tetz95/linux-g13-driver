#ifndef __MACRO_ACTION_H__
#define __MACRO_ACTION_H__

#include <unistd.h>
#include <linux/uinput.h>
#include <vector>

#include "G13Action.h"
#include "Output.h"

using namespace std;

class MacroAction : public G13Action
{
public:

	class Event {
	public:
		Event() {};
		virtual void execute() {};
	};

	class KeyDownEvent: Event {
	private:
		int keycode;
	public:
		KeyDownEvent(int code) {this->keycode = code;}

		virtual void execute() {
			//cout<<"  kde."<<keycode<<"\n";
			send_event(EV_KEY, keycode, 1);
			send_event(0, 0, 0);
		}
	};

	class KeyUpEvent: Event {
	private:
		int keycode;
	public:
		KeyUpEvent(int code) {this->keycode = code;}

		virtual void execute() {
			//cout<<"  kue."<<keycode<<"\n";
			send_event(EV_KEY, keycode, 0);
			send_event(0, 0, 0);
		}
	};

	class DelayEvent: Event {
	private:
		int delayInMillisecs;
	public:
		DelayEvent(int delayInMillisecs) {this->delayInMillisecs = delayInMillisecs;}
		virtual void execute() {
			//cout<<"  de."<<delayInMillisecs<<"\n";
			usleep(1000*delayInMillisecs);
		}
	};

	class MultiEventThread {
	public:
		int keepRepeating;
		vector<MacroAction::Event *> local_events;

		MultiEventThread() {keepRepeating = 0;}

		virtual void execute() {
			do {
				//cout << "MultiEventThread::execute() local_events.size() = " << local_events.size() << "\n";

				for (unsigned int i = 0; i < local_events.size(); i++) {
					local_events.at(i)->execute();
					usleep(100);
				}

			} while (keepRepeating);
		}
	};

private:
	vector<MacroAction::Event *>  events;
    int                           repeats;
    MultiEventThread             *thread;
    pthread_attr_t                attr;

protected:
    MacroAction::Event *tokenToEvent(char *token);
	virtual void        key_down();
	virtual void        key_up();

public:
    MacroAction(char *tokens);
    virtual ~MacroAction();

    int                          getRepeats();
    void                         setRepeats(int repeats);
    vector<MacroAction::Event *> getEvents();
};

#endif
