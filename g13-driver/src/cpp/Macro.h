#ifndef __MACRO_H__
#define __MACRO_H__

#include <string>

class Macro
{
private:
    int id;
    std::string name;
    std::string sequence;
public:
    Macro();
    Macro(int id, const std::string& name, const std::string& sequence);
    int getId() const;
    const std::string& getName() const;
    const std::string& getSequence() const;
    void setId(int id);
    void setName(const std::string& name);
    void setSequence(const std::string& sequence);
};

#endif
