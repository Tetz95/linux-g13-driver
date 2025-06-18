package com.booker.g13;

// KORREKTUR: Zurück zum ursprünglichen Interface-Vertrag mit zwei Methoden.
// Das @FunctionalInterface wird entfernt.
public interface ImageMapListener {

	void selected(final Key key);
	
	void mouseover(final Key key);
}