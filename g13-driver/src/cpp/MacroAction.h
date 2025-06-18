#ifndef __MACRO_ACTION_H__
#define __MACRO_ACTION_H__

#include <linux/uinput.h>
#include <vector>
#include <string>
#include <unistd.h>
#include <memory>       // ANPASSUNG: Für std::unique_ptr hinzugefügt
#include <pthread.h>    // ANPASSUNG: Für pthread_t hinzugefügt

#include "G13Action.h"
#include "Output.h"

class MacroAction : public G13Action {
public:
    // ANPASSUNG: Umbenannt von MacroEvent zu Event für die Verwendung in tokenToEvent
    class Event {
    public:
        virtual ~Event() = default;
        virtual void execute() = 0;
    };

    class KeyDownEvent : public Event {
    private:
        int keycode;
    public:
        KeyDownEvent(int code) : keycode(code) {}
        void execute() override {
            UInput::send_event(EV_KEY, keycode, 1);
            UInput::send_event(EV_SYN, SYN_REPORT, 0); // SYN_REPORT hinzugefügt für bessere Kompatibilität
        }
    };

    class KeyUpEvent : public Event {
    private:
        int keycode;
    public:
        KeyUpEvent(int code) : keycode(code) {}
        void execute() override {
            UInput::send_event(EV_KEY, keycode, 0);
            UInput::send_event(EV_SYN, SYN_REPORT, 0); // SYN_REPORT hinzugefügt für bessere Kompatibilität
        }
    };

    class WaitEvent : public Event {
    private:
        int delay_ms;
    public:
        WaitEvent(int delay) : delay_ms(delay) {}
        void execute() override {
            usleep(delay_ms * 1000);
        }
    };

    // --- Öffentliche Methoden ---
    MacroAction(const std::string& sequence);
    virtual ~MacroAction();

    // ANPASSUNG: Inline-Definition entfernt, um Redefinition-Fehler zu vermeiden.
    // Die Definition befindet sich nun in der .cpp-Datei.
    void setRepeats(int r);
    int getRepeats() const;


protected:
    void key_down() override;
    void key_up() override;

private:
    // --- Private Methoden ---
    // ANPASSUNG: Fehlende Funktionsdeklarationen hinzugefügt.
    void execute_macro_loop();
    std::unique_ptr<MacroAction::Event> tokenToEvent(const char* token);

    // ANPASSUNG: Die Thread-Funktion muss statisch sein.
    static void* run_macro_thread(void* context);

    // --- Private Member-Variablen ---
    // ANPASSUNG: Fehlende Member-Variablen deklariert.
    std::vector<std::unique_ptr<Event>> _events;
    int _repeats;
    int _repeats_on_press;
    bool _is_macro_running;
    bool _thread_keep_repeating_flag;
    pthread_t _macro_thread_id;
};

#endif // __MACRO_ACTION_H__