#ifndef __MACRO_H__
#define __MACRO_H__

class Macro
{
private:
    int id;
    char *name;
    char *sequence;
public:
    Macro();
    int getId() const;
    char *getName() const;
    char *getSequence() const;
    void setId(int id);
    void setName(char *name);
    void setSequence(char *sequence);
};

#endif
