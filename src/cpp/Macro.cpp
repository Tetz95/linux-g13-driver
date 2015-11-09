#include "Macro.h"

Macro::Macro() {
}

int Macro::getId() const
{
    return id;
}

char *Macro::getName() const
{
    return name;
}

char *Macro::getSequence() const
{
    return sequence;
}

void Macro::setId(int id)
{
    this->id = id;
}

void Macro::setName(char *name)
{
    this->name = name;
}

void Macro::setSequence(char *sequence)
{
    this->sequence = sequence;
}

