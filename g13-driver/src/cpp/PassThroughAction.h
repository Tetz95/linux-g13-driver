
#ifndef __PASS_THROUGH_ACTION_H__
#define __PASS_THROUGH_ACTION_H__

#include "G13Action.h"

class PassThroughAction : public G13Action {
private:
	int keycode;

protected:
	void key_down() override;
	void key_up() override;

public:
	PassThroughAction(int code);
	virtual ~PassThroughAction();

	int getKeyCode() const;
	void setKeyCode(int code);
};

#endif
