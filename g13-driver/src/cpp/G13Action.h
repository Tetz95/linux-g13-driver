#ifndef __G13_ACTION_H__
#define __G13_ACTION_H__

class G13Action {
private:
	int pressed;

protected:
	virtual void key_down();
	virtual void key_up();

public:
	G13Action();
	virtual ~G13Action();
	virtual int set(int state); // Returns 1 if state changed, 0 otherwise
	int isPressed() const;
};

#endif
