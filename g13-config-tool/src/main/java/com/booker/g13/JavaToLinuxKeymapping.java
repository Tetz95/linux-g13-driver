package com.booker.g13;

import java.awt.event.KeyEvent;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class JavaToLinuxKeymapping {

    // Refactoring: Typsichere Datenstruktur statt Object[][]
    public record KeyMapping(String name, int linuxCode, int javaCode, int javaLocation) {
        public KeyMapping(String name, int linuxCode, int javaCode) {
            this(name, linuxCode, javaCode, KeyEvent.KEY_LOCATION_UNKNOWN);
        }
    }

    // Refactoring: Verwendung der neuen typsicheren KeyMapping-Klasse
    private static final List<KeyMapping> MAPPINGS = List.of(
            new KeyMapping("ESC", 1, KeyEvent.VK_ESCAPE),
            new KeyMapping("1", 2, KeyEvent.VK_1),
            new KeyMapping("2", 3, KeyEvent.VK_2),
            new KeyMapping("3", 4, KeyEvent.VK_3),
            new KeyMapping("4", 5, KeyEvent.VK_4),
            new KeyMapping("5", 6, KeyEvent.VK_5),
            new KeyMapping("6", 7, KeyEvent.VK_6),
            new KeyMapping("7", 8, KeyEvent.VK_7),
            new KeyMapping("8", 9, KeyEvent.VK_8),
            new KeyMapping("9", 10, KeyEvent.VK_9),
            new KeyMapping("0", 11, KeyEvent.VK_0),
            new KeyMapping("-", 12, KeyEvent.VK_MINUS),
            new KeyMapping("=", 13, KeyEvent.VK_EQUALS),
            new KeyMapping("Backspace", 14, KeyEvent.VK_BACK_SPACE),
            new KeyMapping("Tab", 15, KeyEvent.VK_TAB),
            new KeyMapping("Q", 16, KeyEvent.VK_Q),
            new KeyMapping("W", 17, KeyEvent.VK_W),
            new KeyMapping("E", 18, KeyEvent.VK_E),
            new KeyMapping("R", 19, KeyEvent.VK_R),
            new KeyMapping("T", 20, KeyEvent.VK_T),
            new KeyMapping("Y", 21, KeyEvent.VK_Y),
            new KeyMapping("U", 22, KeyEvent.VK_U),
            new KeyMapping("I", 23, KeyEvent.VK_I),
            new KeyMapping("O", 24, KeyEvent.VK_O),
            new KeyMapping("P", 25, KeyEvent.VK_P),
            new KeyMapping("[", 26, KeyEvent.VK_BRACELEFT),
            new KeyMapping("]", 27, KeyEvent.VK_BRACERIGHT),
            new KeyMapping("Enter", 28, KeyEvent.VK_ENTER),
            new KeyMapping("L CTRL", 29, KeyEvent.VK_CONTROL, KeyEvent.KEY_LOCATION_LEFT),
            new KeyMapping("A", 30, KeyEvent.VK_A),
            new KeyMapping("S", 31, KeyEvent.VK_S),
            new KeyMapping("D", 32, KeyEvent.VK_D),
            new KeyMapping("F", 33, KeyEvent.VK_F),
            new KeyMapping("G", 34, KeyEvent.VK_G),
            new KeyMapping("H", 35, KeyEvent.VK_H),
            new KeyMapping("J", 36, KeyEvent.VK_J),
            new KeyMapping("K", 37, KeyEvent.VK_K),
            new KeyMapping("L", 38, KeyEvent.VK_L),
            new KeyMapping(";", 39, KeyEvent.VK_SEMICOLON),
            new KeyMapping("'", 40, KeyEvent.VK_QUOTE),
            new KeyMapping("`", 41, KeyEvent.VK_BACK_QUOTE),
            new KeyMapping("L Shift", 42, KeyEvent.VK_SHIFT, KeyEvent.KEY_LOCATION_LEFT),
            new KeyMapping("\\", 43, KeyEvent.VK_BACK_SLASH),
            new KeyMapping("Z", 44, KeyEvent.VK_Z),
            new KeyMapping("X", 45, KeyEvent.VK_X),
            new KeyMapping("C", 46, KeyEvent.VK_C),
            new KeyMapping("V", 47, KeyEvent.VK_V),
            new KeyMapping("B", 48, KeyEvent.VK_B),
            new KeyMapping("N", 49, KeyEvent.VK_N),
            new KeyMapping("M", 50, KeyEvent.VK_M),
            new KeyMapping(",", 51, KeyEvent.VK_COMMA),
            new KeyMapping(".", 52, KeyEvent.VK_PERIOD),
            new KeyMapping("/", 53, KeyEvent.VK_SLASH),
            new KeyMapping("R Shift", 54, KeyEvent.VK_SHIFT, KeyEvent.KEY_LOCATION_RIGHT),
            new KeyMapping("NumPad *", 55, KeyEvent.VK_MULTIPLY),
            new KeyMapping("L Alt", 56, KeyEvent.VK_ALT, KeyEvent.KEY_LOCATION_LEFT),
            new KeyMapping("Space", 57, KeyEvent.VK_SPACE),
            new KeyMapping("Cap Lock", 58, KeyEvent.VK_CAPS_LOCK),
            new KeyMapping("F1", 59, KeyEvent.VK_F1),
            new KeyMapping("F2", 60, KeyEvent.VK_F2),
            new KeyMapping("F3", 61, KeyEvent.VK_F3),
            new KeyMapping("F4", 62, KeyEvent.VK_F4),
            new KeyMapping("F5", 63, KeyEvent.VK_F5),
            new KeyMapping("F6", 64, KeyEvent.VK_F6),
            new KeyMapping("F7", 65, KeyEvent.VK_F7),
            new KeyMapping("F8", 66, KeyEvent.VK_F8),
            new KeyMapping("F9", 67, KeyEvent.VK_F9),
            new KeyMapping("F10", 68, KeyEvent.VK_F10),
            new KeyMapping("Num Lock", 69, KeyEvent.VK_NUM_LOCK),
            new KeyMapping("Scroll Lock", 70, KeyEvent.VK_SCROLL_LOCK),
            new KeyMapping("NumPad 7", 71, KeyEvent.VK_NUMPAD7),
            new KeyMapping("NumPad 8", 72, KeyEvent.VK_NUMPAD8),
            new KeyMapping("NumPad 9", 73, KeyEvent.VK_NUMPAD9),
            new KeyMapping("NumPad -", 74, KeyEvent.VK_MINUS),
            new KeyMapping("NumPad 4", 75, KeyEvent.VK_NUMPAD4),
            new KeyMapping("NumPad 5", 76, KeyEvent.VK_NUMPAD5),
            new KeyMapping("NumPad 6", 77, KeyEvent.VK_NUMPAD6),
            new KeyMapping("NumPad +", 78, KeyEvent.VK_PLUS),
            new KeyMapping("NumPad 1", 79, KeyEvent.VK_NUMPAD1),
            new KeyMapping("NumPad 2", 80, KeyEvent.VK_NUMPAD2),
            new KeyMapping("NumPad 3", 81, KeyEvent.VK_NUMPAD3),
            new KeyMapping("Numpad Ins", 82, KeyEvent.VK_INSERT),
            new KeyMapping("Numpad Del", 83, KeyEvent.VK_DELETE),
            new KeyMapping("F11", 87, KeyEvent.VK_F11),
            new KeyMapping("F12", 88, KeyEvent.VK_F12),
            new KeyMapping("F13", 89, KeyEvent.VK_F13),
            new KeyMapping("F14", 90, KeyEvent.VK_F14),
            new KeyMapping("F15", 91, KeyEvent.VK_F15),
            new KeyMapping("F16", 92, KeyEvent.VK_F16),
            new KeyMapping("F17", 93, KeyEvent.VK_F17),
            new KeyMapping("F18", 94, KeyEvent.VK_F18),
            new KeyMapping("F19", 95, KeyEvent.VK_F19),
            new KeyMapping("R Enter", 96, KeyEvent.VK_ENTER, KeyEvent.KEY_LOCATION_NUMPAD),
            new KeyMapping("R Ctrl", 97, KeyEvent.VK_CONTROL, KeyEvent.KEY_LOCATION_RIGHT),
            new KeyMapping("/", 98, KeyEvent.VK_SLASH, KeyEvent.KEY_LOCATION_NUMPAD),
            new KeyMapping("PRT SCR", 99, KeyEvent.VK_PRINTSCREEN),
            new KeyMapping("R ALT", 100, KeyEvent.VK_ALT, KeyEvent.KEY_LOCATION_RIGHT),
            new KeyMapping("Home", 102, KeyEvent.VK_HOME),
            new KeyMapping("Up", 103, KeyEvent.VK_UP),
            new KeyMapping("PgUp", 104, KeyEvent.VK_PAGE_UP),
            new KeyMapping("Left", 105, KeyEvent.VK_LEFT),
            new KeyMapping("Right", 106, KeyEvent.VK_RIGHT),
            new KeyMapping("End", 107, KeyEvent.VK_END),
            new KeyMapping("Down", 108, KeyEvent.VK_DOWN),
            new KeyMapping("PgDn", 109, KeyEvent.VK_PAGE_DOWN),
            new KeyMapping("Insert", 110, KeyEvent.VK_INSERT),
            new KeyMapping("Del", 111, KeyEvent.VK_DELETE),
            new KeyMapping("Pause", 119, KeyEvent.VK_PAUSE)
    );

    // Refactoring: Maps verwenden die neue typsichere Klasse.
    public static final Map<Integer, KeyMapping> C_CODE_TO_DATA;
    public static final Map<Integer, List<KeyMapping>> JAVA_CODE_TO_DATA;

    static {
        // Initialisierung der Maps mit Streams für bessere Lesbarkeit
        C_CODE_TO_DATA = MAPPINGS.stream()
                .collect(Collectors.toUnmodifiableMap(KeyMapping::linuxCode, mapping -> mapping));

        JAVA_CODE_TO_DATA = MAPPINGS.stream()
                .collect(Collectors.groupingBy(KeyMapping::javaCode, Collectors.toUnmodifiableList()));
    }

    public static String cKeyCodeToString(int keyCode) {
        return C_CODE_TO_DATA.getOrDefault(keyCode, new KeyMapping("Unknown (" + keyCode + ")", keyCode, -1))
                .name();
    }

    public static int keyEventToCCode(KeyEvent event) {
        List<KeyMapping> candidates = JAVA_CODE_TO_DATA.get(event.getKeyCode());
        if (candidates == null) {
            System.err.println("JavaToLinuxKeyMapping: Unknown java event code: " + event);
            return 0; // Return a safe default
        }

        // Finde das spezifische Mapping basierend auf der Key-Location
        return candidates.stream()
                .filter(mapping -> mapping.javaLocation() == event.getKeyLocation() || mapping.javaLocation() == KeyEvent.KEY_LOCATION_UNKNOWN)
                .findFirst()
                .map(KeyMapping::linuxCode)
                .orElse(candidates.get(0).linuxCode()); // Fallback auf das erste gefundene Mapping
    }
    
    // Unveränderte Hilfsmethoden
	public static String javaPositionToString(int pos) {
		return switch (pos) {
			case KeyEvent.KEY_LOCATION_NUMPAD -> "KEY_LOCATION_NUMPAD";
			case KeyEvent.KEY_LOCATION_RIGHT -> "KEY_LOCATION_RIGHT";
			case KeyEvent.KEY_LOCATION_LEFT -> "KEY_LOCATION_LEFT";
			default -> "UNKNOWN";
		};
	}

	public static void printWikiCode() {
		System.out.println("||*Linux Keycode*||*Name*||*Java Keycode*||*Java Location*||");
		MAPPINGS.forEach(mapping -> {
			String javaKC = "KeyEvent.VK_" + KeyEvent.getKeyText(mapping.javaCode());
			String javaExt = mapping.javaLocation() != KeyEvent.KEY_LOCATION_UNKNOWN ? "KeyEvent."+javaPositionToString(mapping.javaLocation()) : "";
			System.out.println("||" + mapping.linuxCode() + "||" + mapping.name() + "||" +  javaKC+ "||" + javaExt + "||");
		});
	}

	public static void main(final String [] args) {
		printWikiCode();
	}
}