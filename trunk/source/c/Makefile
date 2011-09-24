
EXE_NAME = G13-Linux-Driver
OBJS     = G13.o G13Action.o Macro.o MacroAction.o Main.o Output.o PassThroughAction.o
CC       = g++
FLAGS    = 
LIBS     = -lusb-1.0

all: $(EXE_NAME)

.cpp.o:
	$(CC) $(FLAGS) -c $<

$(EXE_NAME): $(OBJS)
	$(CC) $(OBJS) -o $(EXE_NAME) $(FLAGS) $(LIBS)



