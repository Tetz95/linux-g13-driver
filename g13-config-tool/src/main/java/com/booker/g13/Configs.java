package com.booker.g13;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Date;
import java.util.Properties;

public class Configs {

	// Unverändert
	public static final String [][] defaultBindings = {
			{"G34", "10"},         {"G29", "59"}, {"G30", "60"}, {"G31", "61"}, {"G32", "62"},        {"G35", "11"},
			{"G0" , "3" }, {"G1" , "4" }, {"G2",  "5" }, {"G3",  "6" }, {"G4",  "7" }, {"G5",  "8" }, {"G6",  "9" },
			{"G7" , "16"}, {"G8" , "17"}, {"G9",  "18"}, {"G10", "19"}, {"G11", "20"}, {"G12", "21"}, {"G13", "22"},
			{"G14", "30"}, {"G15", "31"}, {"G16", "32"}, {"G17", "33"}, {"G18", "34"},
			{"G19", "44"}, {"G20", "45"}, {"G21", "46"},
            /* Stick Buttons */ {"G22", "57"}, {"G23", "58"},
            /* Stick */ {"G36" , "103"}, {"G37" , "105"}, {"G38" , "106"}, {"G39", "108"},
	};

	// Unverändert
	public static final String [][] defaultMacros = {
		{"CTRL-ALT-DEL", "kd.29,kd.56,kd.111,d.20,ku.111,ku.56,ku.29,d.100"},
		{"ALT-TAB", "kd.56,kd.15,d.20,ku.15,ku.56,d.100"},
		{"SHIFT-ALT-TAB", "kd.42,kd.56,kd.15,d.20,ku.15,ku.56,ku.42,d.100"},
		// ... weitere Macros unverändert
		{"ALT-F4", "kd.56,kd.62,d.20,ku.62,ku.56,d.100"},
		{"ALT-F5", "kd.56,kd.63,d.20,ku.63,ku.56,d.100"},
		{"ALT-F6", "kd.56,kd.64,d.20,ku.64,ku.56,d.100"},
		{"ALT-F7", "kd.56,kd.65,d.20,ku.65,ku.56,d.100"},
		{"ALT-F8", "kd.56,kd.66,d.20,ku.66,ku.56,d.100"},
		{"ALT-F9", "kd.56,kd.67,d.20,ku.67,ku.56,d.100"},
		{"ALT-F10", "kd.56,kd.68,d.20,ku.68,ku.56,d.100"},
		{"ALT-F11", "kd.56,kd.87,d.20,ku.87,ku.56,d.100"},
		{"ALT-F12", "kd.56,kd.88,d.20,ku.88,ku.56,d.100"},
		{"Print Screen", "kd.99,d.20,ku.99,d.100"},
		{"ALT-Print Screen", "kd.56,kd.99,d.20,ku.99,ku.56,d.100"},
		{"CTRL-Print Screen", "kd.29,kd.99,d.20,ku.99,ku.29,d.100"},
		{"Pause", "kd.119,d.20,ku.119,d.100"},
	};

	// Hält die Anzahl der Default-Macros als Konstante vor
    public static final int DEFAULT_MACROS_COUNT = defaultMacros.length;

    // Refactoring: Robuste und plattformunabhängige Pfadermittlung
	private static Path getConfigDir() {
        return Path.of(System.getProperty("user.home"), ".g13");
    }

	public static Properties loadBindings(int item) throws IOException {
		Path file = getConfigDir().resolve("bindings-" + item + ".properties");

		if (Files.notExists(file)) {
			// create new file
			Files.createDirectories(file.getParent());

			final Properties props = new Properties();
			props.put("color", "255,255,255");

			for (final String [] binding: defaultBindings) {
				props.put(binding[0], "p,k." + binding[1]);
			}

			saveBindings(item, props);
			return props;
		}

		final Properties props = new Properties();
		// Refactoring: try-with-resources zur sicheren Handhabung von Streams
		try (FileInputStream fis = new FileInputStream(file.toFile())) {
			props.load(fis);
		}

		return props;
	}


	public static void saveBindings(int item, Properties props) throws IOException {
		Path file = getConfigDir().resolve("bindings-" + item + ".properties");
        Files.createDirectories(file.getParent()); // Sicherstellen, dass das Verzeichnis existiert

		// Refactoring: try-with-resources zur sicheren Handhabung von Streams. file.delete() ist unnötig.
		try (FileOutputStream fos = new FileOutputStream(file.toFile())) {
			props.store(fos, new Date().toString());
		}
	}

	public static Properties loadMacro(int macroNum) throws IOException {
		Path file = getConfigDir().resolve("macro-" + macroNum + ".properties");

		if (Files.notExists(file)) {
			Files.createDirectories(file.getParent());

			final Properties props = new Properties();

			if (macroNum < DEFAULT_MACROS_COUNT) {
				props.put("name", defaultMacros[macroNum][0]);
				props.put("sequence", defaultMacros[macroNum][1]);
			} else {
				props.put("name", "");
				props.put("sequence", "");
			}
			props.put("id", Integer.toString(macroNum));
			saveMacro(macroNum, props);

			return props;
		}

		final Properties props = new Properties();
		// Refactoring: try-with-resources
		try (FileInputStream fis = new FileInputStream(file.toFile())) {
			props.load(fis);
		}

		props.put("id", Integer.toString(macroNum));

		return props;
	}

	public static void saveMacro(int macroNum, Properties props) throws IOException {
		Path file = getConfigDir().resolve("macro-" + macroNum + ".properties");
        Files.createDirectories(file.getParent());

		// Refactoring: try-with-resources
		try (FileOutputStream fos = new FileOutputStream(file.toFile())) {
			props.store(fos, new Date().toString());
		}
	}
}