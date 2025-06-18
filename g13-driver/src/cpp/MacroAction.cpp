#include <string.h>
#include <vector>
#include <iostream>
#include <memory> // ANPASSUNG: Fehlender Header für std::unique_ptr hinzugefügt

#include "MacroAction.h"

// Diese Funktion muss außerhalb der Klasse stehen oder statisch sein,
// um sie an pthread_create übergeben zu können. Die statische Methode ist sauberer.
void* MacroAction::run_macro_thread(void *context) {
    MacroAction* action = static_cast<MacroAction*>(context);
    action->execute_macro_loop();
    return nullptr;
}

void MacroAction::execute_macro_loop() {
    _thread_keep_repeating_flag = true;
    int current_repeats = 0;

    while (_thread_keep_repeating_flag && (_repeats == 0 || current_repeats < _repeats)) {
        for (const auto& event : _events) {
            event->execute();
        }
        if (_repeats > 0) {
            current_repeats++;
        }
    }
    _is_macro_running = false;
}

// Hilfsfunktion zum Parsen der Makro-Sequenz
std::unique_ptr<MacroAction::Event> MacroAction::tokenToEvent(const char *token) {
    switch (token[0]) {
        case '+':
            return std::make_unique<KeyDownEvent>(atoi(&token[1]));
        case '-':
            return std::make_unique<KeyUpEvent>(atoi(&token[1]));
        case 'W':
            return std::make_unique<WaitEvent>(atoi(&token[1]));
    }
    return nullptr;
}


// --- Konstruktor / Destruktor ---

MacroAction::MacroAction(const std::string& sequence)
    // ANPASSUNG: Member-Variablen korrigiert und initialisiert.
    : _repeats(0), _repeats_on_press(0), _is_macro_running(false), _thread_keep_repeating_flag(false), _macro_thread_id(0) {

    char* seq_c_str = new char[sequence.length() + 1];
    strcpy(seq_c_str, sequence.c_str());

    char* token = strtok(seq_c_str, " ");
    while (token != nullptr) {
        if (strlen(token) > 0) {
            auto event = tokenToEvent(token);
            if (event) {
                // ANPASSUNG: Korrekten Variablennamen "_events" verwenden
                _events.push_back(std::move(event));
            }
        }
        token = strtok(nullptr, " ");
    }

    delete[] seq_c_str;
}

MacroAction::~MacroAction() {
    if (_is_macro_running) {
        _thread_keep_repeating_flag = false; // Signal an den Thread zum Stoppen
        pthread_join(_macro_thread_id, nullptr); // Warten, bis der Thread beendet ist
    }
    // unique_ptr bereinigt die Events automatisch
}

// --- Key-Events ---

void MacroAction::key_down() {
    if (isPressed()) {
        if (_is_macro_running) {
            // Wenn Makro läuft und Taste erneut gedrückt wird, stoppen wir es
            _thread_keep_repeating_flag = false;
            return;
        }

        if (_events.empty()) {
            return; // Kein Makro zum Ausführen
        }

        _is_macro_running = true;
        if (pthread_create(&_macro_thread_id, nullptr, &MacroAction::run_macro_thread, this) != 0) {
            perror("pthread_create() error");
            _is_macro_running = false;
        }
    }
}

void MacroAction::key_up() {
    // Wenn das Makro nicht wiederholt werden soll, stoppen wir es beim Loslassen der Taste
    if (_repeats == 1) {
       _thread_keep_repeating_flag = false;
    }
}

// --- Getter / Setter ---

int MacroAction::getRepeats() const {
    return _repeats;
}

void MacroAction::setRepeats(int repeats) {
    this->_repeats = repeats;
}