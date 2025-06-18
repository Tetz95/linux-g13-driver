package com.booker.g13;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.GridLayout;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.Properties;
import javax.swing.*;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;

public class MacroEditorPanel extends JPanel {

	private static final long serialVersionUID = 1L;

	private static final ImageIcon UP_ICON = ImageIconHelper.loadEmbeddedImage("/com/booker/g13/images/up.png", 16, 16);
	private static final ImageIcon DOWN_ICON = ImageIconHelper.loadEmbeddedImage("/com/booker/g13/images/down.png", 16, 16);
	private static final ImageIcon DELAY_ICON = ImageIconHelper.loadEmbeddedImage("/com/booker/g13/images/pause.png", 16, 16);

	private final JComboBox<Properties> macroSelectionBox = new JComboBox<>();
	private final DefaultListModel<String> listModel = new DefaultListModel<>();
	private final JList<String> macroList = new JList<>(listModel);
	private final JTextField nameText = new JTextField();
	private final JButton addDelayButton = new JButton("Add Delay");
	private final JCheckBox captureDelays = new JCheckBox("Rec Delays", true);
	private final JButton editButton = new JButton("Edit");
	private final JButton deleteButton = new JButton("Delete");
	private final JButton recordButton = new JButton("Clear & Record");
	
	private volatile boolean loadingData = false;
	private volatile boolean captureMode = false;
	private long lastCapture = 0;

	public MacroEditorPanel(){
		setLayout(new BorderLayout());
		setBorder(BorderFactory.createTitledBorder("Macro Editor Panel"));
		
		// UI-Setup (vereinfacht und modernisiert mit Lambdas)
		setupUI();
        attachListeners();

        // Initialer Zustand
        setComponentStates(false);
	}
    
    private void setupUI() {
        final JPanel northPanel = new JPanel(new BorderLayout());
		northPanel.add(macroSelectionBox, BorderLayout.NORTH);

		final JPanel namePanel = new JPanel(new BorderLayout());
		namePanel.add(new JLabel("Name : "), BorderLayout.WEST);
		namePanel.add(nameText, BorderLayout.CENTER);
		northPanel.add(namePanel, BorderLayout.SOUTH);

		add(northPanel, BorderLayout.NORTH);
		add(new JScrollPane(macroList), BorderLayout.CENTER);

		final JPanel controls = new JPanel(new GridLayout(0, 1));
		final JPanel tmp1 = new JPanel(new GridLayout(1, 2));
		tmp1.add(captureDelays);
		tmp1.add(addDelayButton);
		controls.add(tmp1);

		final JPanel tmp2 = new JPanel(new GridLayout(1, 2));
		tmp2.add(editButton);
		tmp2.add(deleteButton);
		controls.add(tmp2);

		recordButton.setFocusTraversalKeysEnabled(false);
		controls.add(recordButton);

		add(controls, BorderLayout.SOUTH);
    }

    private void attachListeners() {
        editButton.addActionListener(e -> edit());
        deleteButton.addActionListener(e -> delete());
        addDelayButton.addActionListener(e -> addDelay());
        recordButton.addActionListener(e -> startStopRecording());

        macroSelectionBox.addActionListener(e -> selectMacro());
        macroList.setCellRenderer(new MacroStepCellRenderer());

        macroList.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                if (e.getClickCount() > 1) {
                    edit();
                }
            }
        });
        
        macroList.getSelectionModel().addListSelectionListener(e -> {
            if (e.getValueIsAdjusting()) return;
            updateButtonStates();
        });
        
        // ... (weitere Listener wie DocumentListener für nameText)
        nameText.getDocument().addDocumentListener(new DocumentListener() {
			// ... unverändert
			@Override public void changedUpdate(DocumentEvent e) { updateNameColor(); }
			@Override public void insertUpdate(DocumentEvent e) { updateNameColor(); }
			@Override public void removeUpdate(DocumentEvent e) { updateNameColor(); }
			private void updateNameColor() {
				nameText.setForeground(loadingData ? Color.black : Color.red);
			}
		});

        nameText.addActionListener(e -> {
			nameText.setForeground(Color.black);
			saveMacro();
			macroSelectionBox.repaint();
		});

        // KeyListener für die Aufnahme
        KeyListener keyListener = new KeyListener() {
			@Override
			public void keyPressed(KeyEvent event) {
				if (!captureMode) return;
				if (lastCapture != 0 && captureDelays.isSelected()) {
					listModel.addElement("d." + (event.getWhen() - lastCapture));
				}
				lastCapture = event.getWhen();
				listModel.addElement("kd." + JavaToLinuxKeymapping.keyEventToCCode(event));
			}

			@Override
			public void keyReleased(KeyEvent event) {
				if (!captureMode) return;
				if (lastCapture != 0 && captureDelays.isSelected()) {
					listModel.addElement("d." + (event.getWhen() - lastCapture));
				}
				lastCapture = event.getWhen();
				listModel.addElement("ku." + JavaToLinuxKeymapping.keyEventToCCode(event));
			}
			@Override public void keyTyped(KeyEvent e) {}
        };

        // Listener zu allen relevanten Komponenten hinzufügen
		final JComponent[] componentsToListen = { this, macroSelectionBox, macroList, nameText, addDelayButton, captureDelays, recordButton, editButton, deleteButton };
		for (final JComponent c : componentsToListen) {
			c.addKeyListener(keyListener);
		}
    }

    private void updateButtonStates() {
        boolean canModify = canModifyMacro();
        int[] selectedIndices = macroList.getSelectedIndices();
        boolean selectionExists = selectedIndices.length > 0;

        deleteButton.setEnabled(selectionExists && canModify);
        
        boolean singleSelection = selectedIndices.length == 1;
        boolean isDelay = singleSelection && listModel.getElementAt(selectedIndices[0]).startsWith("d.");
        editButton.setEnabled(singleSelection && isDelay && canModify);
    }
    
    private void setComponentStates(boolean enabled) {
        final JComponent[] components = { macroSelectionBox, macroList, nameText, addDelayButton, captureDelays, editButton, deleteButton };
        for (JComponent c : components) {
            c.setEnabled(enabled);
        }
        recordButton.setEnabled(canModifyMacro());
    }

	private boolean canModifyMacro() {
		return macroSelectionBox.getSelectedIndex() >= Configs.DEFAULT_MACROS_COUNT;
	}

	public void startStopRecording() {
		captureMode = !captureMode;
        setComponentStates(!captureMode && canModifyMacro());
		
		if (captureMode) {
			recordButton.setText("Stop Recording");
			listModel.removeAllElements();
			lastCapture = 0;
            nameText.setEnabled(false); // Während der Aufnahme den Namen nicht ändern
		} else {
			recordButton.setText("Clear & Record");
            nameText.setEnabled(canModifyMacro());
			saveMacro();
		}
	}

	public void delete() {
        if (!canModifyMacro()) return;
        int[] indices = macroList.getSelectedIndices();
        if (indices == null || indices.length == 0) return;

        // Rückwärts löschen, um Index-Probleme zu vermeiden
        for (int i = indices.length - 1; i >= 0; i--) {
            listModel.removeElementAt(indices[i]);
        }
        saveMacro();
    }
	
	public void edit() {
		if (!canModifyMacro()) return;
		int selectedIndex = macroList.getSelectedIndex();
		if (selectedIndex == -1) return;

		final String str = listModel.getElementAt(selectedIndex);
		if (!str.startsWith("d.")) return;

		try {
            int currentDelay = Integer.parseInt(str.substring(2));
            String newDelayStr = JOptionPane.showInputDialog(this, "Enter the delay in milliseconds", currentDelay);
            if (newDelayStr == null) return;

            int newDelay = Integer.parseInt(newDelayStr);
            listModel.set(selectedIndex, "d." + newDelay);
            saveMacro();
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(this, "Invalid delay value: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
	}
	
	public void addDelay() {
        if (!canModifyMacro()) return;
        int pos = macroList.getSelectedIndex();
        if (pos == -1) {
            pos = listModel.getSize();
        } else {
            pos++; // nach dem selektierten Element einfügen
        }

        try {
            String newDelayStr = JOptionPane.showInputDialog(this, "Enter the delay in milliseconds", 100);
            if (newDelayStr == null) return;
            
            int newDelay = Integer.parseInt(newDelayStr);
            listModel.insertElementAt("d." + newDelay, pos);
            saveMacro();
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(this, "Invalid delay value: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
	}
	
	public void setMacros(final Properties[] macros) {
        loadingData = true;
		macroSelectionBox.removeAllItems();
		for (final Properties properties : macros) {
			macroSelectionBox.addItem(properties);
		}
        if (macroSelectionBox.getItemCount() > 0) {
		    macroSelectionBox.setSelectedIndex(0);
        }
        loadingData = false;
        selectMacro();
	}
	
	private void selectMacro() {
		if (loadingData || macroSelectionBox.getSelectedItem() == null) return;
        loadingData = true;

        boolean canModify = canModifyMacro();
        captureDelays.setEnabled(canModify);
        addDelayButton.setEnabled(canModify);
        nameText.setEditable(canModify);
        recordButton.setEnabled(canModify);
        updateButtonStates();

		listModel.clear();
		
		final Properties macro = (Properties)macroSelectionBox.getSelectedItem();
		nameText.setText(macro.getProperty("name", ""));
		
		final String sequence = macro.getProperty("sequence", "");
		if (!sequence.isEmpty()) {
            // StringTokenizer ist veraltet, split ist besser
			for (String token : sequence.split(",")) {
                listModel.addElement(token);
            }
        }
		
		loadingData = false;
        nameText.setForeground(Color.black); // Farbe zurücksetzen
	}
	
	private void saveMacro() {
        if (loadingData || !canModifyMacro()) return;
		final int id = macroSelectionBox.getSelectedIndex();
		if (id == -1) return;

		final Properties macro = (Properties)macroSelectionBox.getSelectedItem();
		macro.setProperty("name", nameText.getText());
		
        // Effizienter String-Aufbau mit StringJoiner oder StringBuilder
		final StringBuilder buf = new StringBuilder();
		for (int i = 0; i < listModel.getSize(); i++) {
			if (i > 0) {
				buf.append(",");
			}
			buf.append(listModel.getElementAt(i));
		}
		macro.setProperty("sequence", buf.toString());
		
		try {
			Configs.saveMacro(id, macro);
		} catch (Exception e) {
			e.printStackTrace();
			JOptionPane.showMessageDialog(this, "Could not save macro: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
		}
	}
    
    // Eigene CellRenderer-Klasse für bessere Übersichtlichkeit
    private static class MacroStepCellRenderer extends DefaultListCellRenderer {
        @Override
        public Component getListCellRendererComponent(JList<?> list, Object value, int index, boolean isSelected, boolean cellHasFocus) {
            super.getListCellRendererComponent(list, value, index, isSelected, cellHasFocus);

            if (value instanceof String val) {
                String[] parts = val.split("\\.");
                if (parts.length == 2) {
                    String type = parts[0];
                    try {
                        int keycode = Integer.parseInt(parts[1]);
                        switch (type) {
                            case "d" -> {
                                setText(String.format("%.3f seconds", keycode / 1000.0));
                                setIcon(DELAY_ICON);
                            }
                            case "kd" -> {
                                setText(JavaToLinuxKeymapping.cKeyCodeToString(keycode));
                                setIcon(DOWN_ICON);
                            }
                            case "ku" -> {
                                setText(JavaToLinuxKeymapping.cKeyCodeToString(keycode));
                                setIcon(UP_ICON);
                            }
                            default -> setIcon(null);
                        }
                    } catch (NumberFormatException e) {
                        setText(val); // Fallback
                        setIcon(null);
                    }
                } else {
                    setText(val);
                    setIcon(null);
                }
            }
            return this;
        }
    }
}