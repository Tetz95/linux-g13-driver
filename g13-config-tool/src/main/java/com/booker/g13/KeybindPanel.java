package com.booker.g13;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.io.IOException;
import java.util.Properties;

import javax.swing.BorderFactory;
import javax.swing.ButtonGroup;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JColorChooser;
import javax.swing.JComboBox;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;

public class KeybindPanel extends JPanel {

	private static final long serialVersionUID = 1L;

	private final JCheckBox passthroughButton = new JCheckBox("Pass Through");
	private final JTextField passthroughText = new JTextField();
	private int passthroughCode = 0;
	
	private final JCheckBox macroButton = new JCheckBox("Macro");
	private final JComboBox<Properties> macroSelectionBox = new JComboBox<>();
	private final JCheckBox repeatsCheckBox = new JCheckBox("Auto Repeat");
	
	private final JButton colorChangeButton = new JButton("Click Here To Change");
	
	private int bindingsId = -1;
	private Properties bindings;
	private Properties[] macros;
	private Key key = null;
	
	private volatile boolean loadingData = false;
	
	public KeybindPanel() {
		setLayout(new BorderLayout());
		setBorder(BorderFactory.createTitledBorder("Keybindings Panel"));
		
		setupUI();
        attachListeners();
		
		setSelectedKey(null);
	}
	
	private void setupUI() {
		add(createColorPanel(), BorderLayout.NORTH);
		
		final ButtonGroup buttonGroup = new ButtonGroup();
		buttonGroup.add(passthroughButton);
		buttonGroup.add(macroButton);
		
		passthroughText.setFocusTraversalKeysEnabled(false);
		
		final JPanel grid = new JPanel(new GridLayout(0, 2, 5, 5)); // mit Abständen
		grid.add(passthroughButton);
		grid.add(passthroughText);
		grid.add(new JLabel(" ")); // Platzhalter
		grid.add(new JLabel(" "));
		grid.add(macroButton);
		grid.add(macroSelectionBox);
		grid.add(new JLabel(" "));
		grid.add(repeatsCheckBox);
		
		grid.setBorder(BorderFactory.createTitledBorder("Button Type"));
		
		macroSelectionBox.setRenderer(new MacroListCellRenderer());
		
		add(grid, BorderLayout.CENTER);
	}

	private void attachListeners() {
		// Refactoring: Lambda-Ausdrücke für Listener
		macroButton.addActionListener(e -> updateComponentStateAndSave());
		macroSelectionBox.addActionListener(e -> saveBindings());
		repeatsCheckBox.addActionListener(e -> saveBindings());
		passthroughButton.addActionListener(e -> updateComponentStateAndSave());

		passthroughText.addKeyListener(new KeyAdapter() {
			@Override
			public void keyReleased(KeyEvent event) {
				if (loadingData) return;
				loadingData = true;
				passthroughCode = JavaToLinuxKeymapping.keyEventToCCode(event);
				passthroughText.setText(JavaToLinuxKeymapping.cKeyCodeToString(passthroughCode));
				loadingData = false;
				saveBindings();
			}
		});
	}

    private void updateComponentStateAndSave() {
        passthroughText.setEnabled(passthroughButton.isSelected());
        macroSelectionBox.setEnabled(macroButton.isSelected());
        repeatsCheckBox.setEnabled(macroButton.isSelected());
        saveBindings();
    }
	
	public void setMacros(final Properties[] macros) {
		loadingData = true;
		this.macros = macros;
		
		macroSelectionBox.removeAllItems();
		for (final Properties properties : macros) {
			macroSelectionBox.addItem(properties);
		}
		
		loadingData = false;
	}
	
	public void setBindings(final int propertyNum, final Properties bindings) {
		loadingData = true;
		
		this.bindingsId = propertyNum;
		this.bindings = bindings;
		
		final String val = bindings.getProperty("color", "255,255,255");
		// Refactoring: Robusteres Parsen der Farbe
		try {
			String[] parts = val.split(",");
			if (parts.length == 3) {
				int r = Integer.parseInt(parts[0].trim());
				int g = Integer.parseInt(parts[1].trim());
				int b = Integer.parseInt(parts[2].trim());
				colorChangeButton.setBackground(new Color(r, g, b));
			}
		} catch (NumberFormatException e) {
			System.err.println("Invalid color format in properties: " + val);
			colorChangeButton.setBackground(Color.WHITE);
		}
		
		setSelectedKey(null);
		loadingData = false;
	}
	
	public void setSelectedKey(final Key key) {
		this.key = key;
		loadingData = true;
		
		final boolean isKeySelected = (key != null);
		final JComponent[] all = { colorChangeButton, macroButton, macroSelectionBox, passthroughButton, passthroughText, repeatsCheckBox };
		for (final JComponent c : all) {
			c.setEnabled(isKeySelected);
		}
		
		if (!isKeySelected) {
			loadingData = false;
			return;
		}
		
		final String propKey = "G" + key.getG13KeyCode();
		final String val = bindings.getProperty(propKey, "p,k.1"); // Default zu 'ESC'
		
		// Refactoring: Robusteres Parsen mit split()
		String[] parts = val.split("[,.]");
		String type = parts.length > 0 ? parts[0] : "p";
		
		try {
			if ("p".equals(type)) {
				passthroughButton.setSelected(true);
				passthroughCode = (parts.length >= 3) ? Integer.parseInt(parts[2]) : 1; // Default zu 1 (ESC)
				passthroughText.setText(JavaToLinuxKeymapping.cKeyCodeToString(passthroughCode));
			} else { // "m"
				macroButton.setSelected(true);
				int macroNum = (parts.length >= 2) ? Integer.parseInt(parts[1]) : 0;
				macroSelectionBox.setSelectedIndex(macroNum);
				boolean repeats = (parts.length >= 3) && (Integer.parseInt(parts[2]) != 0);
				repeatsCheckBox.setSelected(repeats);
			}
		} catch(NumberFormatException | ArrayIndexOutOfBoundsException e) {
			System.err.println("Failed to parse binding property: " + val);
			passthroughButton.setSelected(true); // Sicherer Fallback
		}
		
		updateComponentStateAndSave(); // stellt den Zustand der UI-Komponenten her
		loadingData = false;
	}
	
	private void changeScreenColor() {
		final Color currentColor = colorChangeButton.getBackground();
		final Color newColor = JColorChooser.showDialog(this, "Choose Screen Color", currentColor);
		
		if (newColor == null) return;
		
		bindings.setProperty("color", newColor.getRed() + "," + newColor.getGreen() + "," + newColor.getBlue());
		colorChangeButton.setBackground(newColor);
		
		try {
			Configs.saveBindings(bindingsId, bindings);
		} catch (IOException e) {
			e.printStackTrace();
			JOptionPane.showMessageDialog(this, "Could not save color setting: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
		}
	}
	
	private JPanel createColorPanel() {
		final JPanel p = new JPanel(new FlowLayout(FlowLayout.LEFT));
		p.setBorder(BorderFactory.createTitledBorder("Screen Color"));
		p.add(colorChangeButton);
		colorChangeButton.addActionListener(e -> changeScreenColor());
		return p;
	}
	
	private void saveBindings() {
		if (key == null || loadingData) {
			return;
		}
		
		String prop = "G" + key.getG13KeyCode();
		if (passthroughButton.isSelected()) {
			String val = "p,k." + passthroughCode;
			bindings.put(prop, val);
			
			key.setMappedValue(passthroughText.getText().trim());
			key.setRepeats("N/A");
		} else if (macroButton.isSelected()) {
			int macroNum = macroSelectionBox.getSelectedIndex();
			int repeats = repeatsCheckBox.isSelected() ? 1 : 0;
			String val = "m," + macroNum + "," + repeats;
			bindings.put(prop, val);
			
			if (macros != null && macroNum >= 0 && macroNum < macros.length) {
				final String macroName = macros[macroNum].getProperty("name", "Unnamed Macro");
				key.setMappedValue("Macro: " + macroName);
				key.setRepeats(repeats == 1 ? "Yes" : "No");
			}
		} else {
			// Unmapped state
			bindings.remove(prop);
			key.setMappedValue("Unassigned");
			key.setRepeats("N/A");
		}
		
		try {
			Configs.saveBindings(bindingsId, bindings);
		} catch (IOException e) {
			e.printStackTrace();
			JOptionPane.showMessageDialog(this, "Can't Save Bindings: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
		}
	}
}