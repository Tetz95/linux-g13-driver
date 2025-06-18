package com.booker.g13;

import java.awt.BorderLayout;
import java.io.IOException;
import java.util.Properties;
import java.util.Set;

import javax.swing.BorderFactory;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;
import javax.swing.UIManager;

public class G13 extends JPanel {

	private static final long serialVersionUID = 1L;
	
	public static final String VERSION = G13.class.getPackage().getImplementationVersion() != null 
			? G13.class.getPackage().getImplementationVersion() 
			: "Development";
	
	private static final int MAX_MACROS = 200;

	// Refactoring: Benannte Konstanten für "magische Zahlen"
    private static final int BINDING_KEY_M1 = 25;
    private static final int BINDING_KEY_M2 = 26;
    private static final int BINDING_KEY_M3 = 27;
    private static final int BINDING_KEY_MR = 28;
    private static final Set<Integer> BINDING_SWITCH_KEYS = Set.of(
            BINDING_KEY_M1, BINDING_KEY_M2, BINDING_KEY_M3, BINDING_KEY_MR
    );
	
	private final ImageMap g13Label = new ImageMap();
	private final KeybindPanel keybindPanel = new KeybindPanel();
	private final MacroEditorPanel macroEditorPanel = new MacroEditorPanel();
	
	private final Properties[] keyBindings = new Properties[4];
	private final Properties[] macros = new Properties[MAX_MACROS];
	
	public G13() {
		setLayout(new BorderLayout());
		
		// Lade Konfigurationen und initialisiere UI
		loadConfiguration();
		
		keybindPanel.setBindings(0, keyBindings[0]);
		
		g13Label.addListener(new ImageMapListener() {
			@Override
			public void selected(Key key) {
				if (key == null) {
					keybindPanel.setSelectedKey(null);
					return;
				}
				
				// Refactoring: Verwendung des Sets für bessere Lesbarkeit
				if (BINDING_SWITCH_KEYS.contains(key.getG13KeyCode())) {
					mapBindings(key.getG13KeyCode() - BINDING_KEY_M1);
				} else {
					keybindPanel.setSelectedKey(key);
				}
			}

			@Override
			public void mouseover(Key key) {
				// Funktion bleibt erhalten
			}			
		});
		
		final JPanel p = new JPanel(new BorderLayout());
		p.setBorder(BorderFactory.createTitledBorder("G13 Keypad"));
		p.add(g13Label, BorderLayout.CENTER);
		add(p, BorderLayout.CENTER);
		
		final JPanel rightPanel = new JPanel(new BorderLayout());
		rightPanel.add(keybindPanel, BorderLayout.NORTH);
		rightPanel.add(macroEditorPanel, BorderLayout.CENTER);
		add(rightPanel, BorderLayout.EAST);
		
		keybindPanel.setMacros(macros);
		macroEditorPanel.setMacros(macros);
	}

	private void loadConfiguration() {
		try {
			for (int i = 0; i < keyBindings.length; i++) {
				keyBindings[i] = Configs.loadBindings(i);
			}
			
			for (int i = 0; i < macros.length; i++) {
				macros[i] = Configs.loadMacro(i);
			}
			
			mapBindings(0);
		}
		// Refactoring: Spezifischere Exception fangen
		catch (IOException e) {
			e.printStackTrace();
			JOptionPane.showMessageDialog(this, "Failed to load configuration:\n" + e.getMessage(), "Configuration Error", JOptionPane.ERROR_MESSAGE);
            // In einem Fehlerfall kann die Anwendung hier beendet werden, da sie ohne Konfig nicht sinnvoll ist
            // System.exit(1);
		}
	}
		
	private void mapBindings(int bindingNum) {
		keybindPanel.setSelectedKey(null);
		keybindPanel.setBindings(bindingNum, keyBindings[bindingNum]);
		
		for (int i = 0; i < 40; i++) { // Annahme: Es gibt maximal G39
			final Key k = Key.getKeyFor(i);
			if (k == null) continue;

			String property = "G" + i;
			String val = keyBindings[bindingNum].getProperty(property);
			
			k.setMappedValue("Unassigned");
			k.setRepeats("N/A");
			
			if (val != null && !val.isBlank()) {
				// Refactoring: StringTokenizer durch robustes split() ersetzt
				String[] parts = val.split("[,.]");
				if (parts.length < 2) continue; // Ungültiges Format ignorieren

				final String type = parts[0];
				
				try {
					if ("p".equals(type)) { // Passthrough
						if (parts.length >= 3) {
							int keycode = Integer.parseInt(parts[2]);
							k.setMappedValue(JavaToLinuxKeymapping.cKeyCodeToString(keycode));
						}
					} else if ("m".equals(type)) { // Macro
						if (parts.length >= 3) {
							int macroNum = Integer.parseInt(parts[1]);
							if (macroNum >= 0 && macroNum < macros.length) {
								final String macroName = macros[macroNum].getProperty("name", "Unnamed Macro");
								boolean repeats = Integer.parseInt(parts[2]) != 0;
								k.setMappedValue("Macro: " + macroName);
								k.setRepeats(repeats ? "Yes" : "No");
							}
						}
					}
				} catch (NumberFormatException e) {
					System.err.println("Could not parse binding: " + val);
					k.setMappedValue("Parse Error");
				}
			}
		}
	}
	
	public static void main(String[] args) {
		// UI auf dem Event Dispatch Thread (EDT) starten
        SwingUtilities.invokeLater(() -> {
            try {
                // Ein moderneres Look-and-Feel setzen
                UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
            } catch (Exception e) {
                e.printStackTrace();
            }

            final JFrame frame = new JFrame("G13 Configuration Tool, Version " + VERSION);
            frame.setIconImage(ImageMap.G13_KEYPAD.getImage());
            frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

            final G13 g13 = new G13();	
            frame.getContentPane().add(g13, BorderLayout.CENTER);
            
            frame.pack();
            frame.setLocationRelativeTo(null); // Zentriert den Frame auf dem Bildschirm
            frame.setVisible(true);
        });
	}
}