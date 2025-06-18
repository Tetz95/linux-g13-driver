#include "Macro.h"

// Default constructor
Macro::Macro() : id(0) {
    // name and sequence are default-constructed (empty strings)
}

// Parameterized constructor
Macro::Macro(int id, const std::string& name, const std::string& sequence)
    : id(id), name(name), sequence(sequence) {
}

int Macro::getId() const
{
    return this->id;
}

const std::string& Macro::getName() const
{
    return this->name;
}

const std::string& Macro::getSequence() const
{
    return this->sequence;
}

void Macro::setId(int id)
{
    this->id = id;
}
void Macro::setName(const std::string& name)
{
    this->name = name;
}
void Macro::setSequence(const std::string& sequence)
{
    this->sequence = sequence;
}
