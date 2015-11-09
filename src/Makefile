EXE_NAME = Linux-G13-Driver
SRC_DIR  = c-source
OBJS     = $(SRC_DIR)/G13.o $(SRC_DIR)/G13Action.o $(SRC_DIR)/Macro.o $(SRC_DIR)/MacroAction.o $(SRC_DIR)/Main.o $(SRC_DIR)/Output.o $(SRC_DIR)/PassThroughAction.o
CC       = g++
FLAGS    = -L /lib64 -l pthread
LIBS     = -lusb-1.0

all: $(EXE_NAME)

.cpp.o:
	$(CC) $(FLAGS) -c $< -o $@

$(EXE_NAME): $(OBJS)
	$(CC) $(OBJS) -o $(EXE_NAME) $(FLAGS) $(LIBS)
