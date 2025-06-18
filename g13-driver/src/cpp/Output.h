#ifndef __OUTPUT_H__
#define __OUTPUT_H__

// ANPASSUNG START: Globale Funktionen und Variablen in eine statische Klasse gekapselt.
// Dies verbessert die Code-Struktur und vermeidet globale Zustände.
#include <pthread.h>

class UInput {
private:
    static int file;
    static pthread_mutex_t plock;

public:
    // Verhindert die Instanziierung der Klasse, da sie nur statische Member enthält.
    UInput() = delete;

    static void send_event(int type, int code, int val);
    static void flush();
    static bool create_uinput();
    static void close_uinput();
};
// ANPASSUNG ENDE

#endif