EXE_NAME = Linux-G13-Driver
SRC_DIR  = cpp
BUILD_DIR = build

# Die Objektdateien werden nun im BUILD_DIR erwartet
OBJS     = $(BUILD_DIR)/G13.o $(BUILD_DIR)/G13Action.o $(BUILD_DIR)/Macro.o $(BUILD_DIR)/MacroAction.o $(BUILD_DIR)/Main.o $(BUILD_DIR)/Output.o $(BUILD_DIR)/PassThroughAction.o

CC       = g++

# Compiler and Linker Flags
CXXFLAGS = -pthread # Add other C++ compilation flags like -Wall, -O2, -std=c++11
CPPFLAGS =           # For preprocessor flags like -Iinclude/ -DDEBUG (e.g. -I../include if headers are in a parent include dir)
LDFLAGS  = -L/lib64
LDLIBS   = -lusb-1.0 # -pthread in CXXFLAGS, when passed to the linker, handles pthread linking

# Udev Regel-Konfiguration
UDEV_RULE_FILE = 99-g13.rules
UDEV_RULES_DIR = /etc/udev/rules.d

#.PHONY verhindert Konflikte mit Dateinamen und sorgt dafür, dass die Ziele immer ausgeführt werden
.PHONY: all clean install uninstall

all: $(EXE_NAME)

# Regel zum Linken des Programms. Hängt von den Objektdateien ab.
$(EXE_NAME): $(OBJS)
	$(CC) $(OBJS) -o $(EXE_NAME) $(LDFLAGS) $(CXXFLAGS) $(LDLIBS)

# Pattern-Regel, um.cpp-Dateien aus SRC_DIR in.o-Dateien im BUILD_DIR zu kompilieren.
# Stellt sicher, dass das BUILD_DIR existiert, bevor kompiliert wird.
$(BUILD_DIR)/%.o: $(SRC_DIR)/%.cpp | $(BUILD_DIR)
	$(CC) $(CXXFLAGS) $(CPPFLAGS) -c $< -o $@

# Regel, um das Build-Verzeichnis zu erstellen.
$(BUILD_DIR):
	@mkdir -p $@

# Regel zum Aufräumen
clean:
	rm -rf $(BUILD_DIR)
	rm -f $(EXE_NAME)

# Regel zur Installation der udev-Regel
install:
	@echo "Installing udev rule to $(UDEV_RULES_DIR)..."
	sudo cp $(UDEV_RULE_FILE) $(UDEV_RULES_DIR)/
	@echo "Reloading udev rules..."
	sudo udevadm control --reload-rules
	sudo udevadm trigger
	@echo "Installation complete. Please re-plug your G13 device."
	@echo "Make sure your user is part of the 'input' group: 'sudo usermod -aG input $$USER'"

# Regel zur Deinstallation der udev-Regel
uninstall:
	@echo "Removing udev rule from $(UDEV_RULES_DIR)..."
	sudo rm -f $(UDEV_RULES_DIR)/$(UDEV_RULE_FILE)
	@echo "Reloading udev rules..."
	sudo udevadm control --reload-rules
	@echo "Uninstallation complete."