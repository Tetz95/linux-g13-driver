
#ifndef __PASS_THROUGH_ACTION_H__
#define __PASS_THROUGH_ACTION_H__

#include "G13Action.h"

class PassThroughAction : public G13Action {
private:
	int keycode;

protected:
	virtual void key_down();
	virtual void key_up();

public:
	PassThroughAction(int code);
	virtual ~PassThroughAction();

	int getKeyCode();
	void setKeyCode(int code);
};

#endif
