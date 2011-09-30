package com.gupta.g13;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.io.IOException;
import java.util.Properties;
import java.util.StringTokenizer;

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
import javax.swing.SwingUtilities;

public class KeybindPanel extends JPanel {

	private static final long serialVersionUID = 1L;

	private final JCheckBox passthroughButton = new JCheckBox("Pass Through");
	
	private final JTextField passthroughText = new JTextField();
	
	private int passthroughCode = 0;
	
	private final JCheckBox macroButton = new JCheckBox("Macro");
	
	private final JComboBox macroSelectionBox = new JComboBox();
	
	private final JCheckBox repeatsCheckBox = new JCheckBox("Auto Repeat");
	
	private final JButton colorChangeButton = new JButton("Click Here To Change");
	
	private int bindingsId = -1;
	
	private Properties bindings;
	
	private Properties [] macros;
	
	private Key key = null;
	
	private boolean loadingData = false;
	
	public KeybindPanel() {
		setLayout(new BorderLayout());
		setBorder(BorderFactory.createTitledBorder("Keybindings Panel"));
		add(createColorPanel(), BorderLayout.NORTH);
		
		final ButtonGroup buttonGroup = new ButtonGroup();
		buttonGroup.add(passthroughButton);
		buttonGroup.add(macroButton);
		
		passthroughText.setFocusTraversalKeysEnabled(false);
		
		final JPanel grid = new JPanel(new GridLayout(0, 2));
		grid.add(passthroughButton);
		grid.add(passthroughText);
		grid.add(new JLabel(" "));
		grid.add(new JLabel(" "));
		grid.add(macroButton);
		grid.add(macroSelectionBox);
		grid.add(new JLabel(" "));
		grid.add(repeatsCheckBox);
		
		grid.setBorder(BorderFactory.createTitledBorder("Button Type"));
		
		macroSelectionBox.setRenderer(new MacroListCellRenderer());
		
		add(grid, BorderLayout.CENTER);
		
		
		final ActionListener changeListener = new ActionListener() {
			public void actionPerformed(final ActionEvent ae) {
				try {
					saveBindings();
				}
				catch (Exception e) {
					e.printStackTrace();
					JOptionPane.showMessageDialog(KeybindPanel.this, e);
				}
			}
		};
		
		macroButton.addActionListener(changeListener);
		macroSelectionBox.addActionListener(changeListener);
		repeatsCheckBox.addActionListener(changeListener);
		passthroughButton.addActionListener(changeListener);
		passthroughText.addActionListener(changeListener);
		
		macroButton.addActionListener(new ActionListener() {
			public void actionPerformed(final ActionEvent ae) {
				passthroughText.setEnabled(false);
				macroSelectionBox.setEnabled(true);
				repeatsCheckBox.setEnabled(true);
			}			
		});
		
		passthroughButton.addActionListener(new ActionListener() {
			public void actionPerformed(final ActionEvent ae) {
				passthroughText.setEnabled(true);
				macroSelectionBox.setEnabled(false);
				repeatsCheckBox.setEnabled(false);
			}			
		});
		
		passthroughText.addKeyListener(new KeyListener() {
			@Override
			public void keyTyped(KeyEvent event) {
			}

			@Override
			public void keyPressed(KeyEvent event) {
			}

			@Override
			public void keyReleased(KeyEvent event) {
				if (loadingData) {
					return;
				}
				loadingData = true;
				passthroughCode = JavaToLinuxKeymapping.keyEventToCCode(event);
				passthroughText.setText(JavaToLinuxKeymapping.cKeyCodeToString(passthroughCode));
				loadingData = false;
				try {
					saveBindings();
				}
				catch (Exception e) {
					e.printStackTrace();
					JOptionPane.showMessageDialog(null, "Can't Save Bindings: " + e);
				}
			}
		});
		

		
		setSelectedKey(null);
	}
	
	public void setMacros(final Properties [] macros) {
		loadingData = true;
		this.macros = macros;
		
		macroSelectionBox.removeAllItems();
		for (final Properties properties: macros) {
			macroSelectionBox.addItem(properties);
		}
		
		loadingData = false;
	}
	
	public void setBindings(final int propertyNum, final Properties bindings) {
		loadingData = true;
		
		this.bindingsId = propertyNum;
		this.bindings = bindings;
		
		final String val = bindings.getProperty("color");
		final StringTokenizer st = new StringTokenizer(val, ",");
		int r = Integer.valueOf(st.nextToken());
		int g = Integer.valueOf(st.nextToken());
		int b = Integer.valueOf(st.nextToken());
		
		final Color c = new Color(r, g, b);
		
		SwingUtilities.invokeLater(new Runnable() {
			public void run() {
				colorChangeButton.setBackground(c);
			}
		});
		
		setSelectedKey(null);;
		
		loadingData = false;
	}
	
	public void setSelectedKey(final Key key) {
		
		this.key = key;
		
		loadingData = true;
		
		final JComponent [] all = {
				colorChangeButton, macroButton, macroSelectionBox,
				passthroughButton, passthroughText, repeatsCheckBox
		};
		
		for (final JComponent c: all) {
			c.setEnabled(key != null);
		}
		
		if (key == null) {
			return;
		}
		
		final String propKey = "G" + key.getG13KeyCode();
		final String val = bindings.getProperty(propKey)==null?"":bindings.getProperty(propKey);
		
		final StringTokenizer st = new StringTokenizer(val, ",.");
		String type = st.hasMoreTokens()?st.nextToken():"p";
		if (type.equals("p")) {
			passthroughButton.setSelected(true);
			
			String tmp = st.hasMoreTokens()?st.nextToken():"";
			
			passthroughCode = Integer.valueOf(st.hasMoreTokens()?st.nextToken():"1");
			passthroughText.setText(JavaToLinuxKeymapping.cKeyCodeToString(passthroughCode));
			
			macroSelectionBox.setEnabled(false);
			repeatsCheckBox.setEnabled(false);
		}
		else {
			macroButton.setSelected(true);
			int macroNum = Integer.valueOf(st.nextToken());
			macroSelectionBox.setSelectedIndex(macroNum);
			boolean repeats = Integer.valueOf(st.nextToken()) != 0;
			repeatsCheckBox.setSelected(repeats);
			
			passthroughText.setEnabled(false);
		}
		
		loadingData = false;
	}
	
	private void changeScreenColor() {
		final String val = bindings.getProperty("color");
		final StringTokenizer st = new StringTokenizer(val, ",");
		int r = Integer.valueOf(st.nextToken());
		int g = Integer.valueOf(st.nextToken());
		int b = Integer.valueOf(st.nextToken());
		
		final Color c = new Color(r, g, b);
		
		final Color newColor = JColorChooser.showDialog(this, "Choose Screen Color", c);
		if (newColor == null) {
			return;
		}
		
		bindings.setProperty("color", newColor.getRed() + "," + newColor.getGreen() + "," + newColor.getBlue());
		colorChangeButton.setBackground(newColor);
		
		try {
			Configs.saveBindings(bindingsId, bindings);
		}
		catch (Exception e) {
			e.printStackTrace();
			JOptionPane.showMessageDialog(this, e);
		}
	}
	
	private JPanel createColorPanel() {
		final JPanel p = new JPanel(new FlowLayout(FlowLayout.LEFT));
		p.setBorder(BorderFactory.createTitledBorder("Screen Color"));
				
		p.add(colorChangeButton);
		
		colorChangeButton.addActionListener(new ActionListener() {
			public void actionPerformed(final ActionEvent ae) {
				changeScreenColor();
			}
		});
		
		return p;
	}
	
	private void saveBindings() throws IOException {
		
		if (key == null || loadingData == true) {
			return;
		}
		
		String prop = "G" + key.getG13KeyCode();
		if (passthroughButton.isSelected()) {
			String val = "p,k." + passthroughCode;
			bindings.put(prop, val);
			
			key.setMappedValue(passthroughText.getText().trim());
			key.setRepeats("N/A");
		}
		else if (macroButton.isSelected()) {
			int macroNum = macroSelectionBox.getSelectedIndex();
			int repeats = repeatsCheckBox.isSelected()?1:0;
			String val = "m," + macroNum + "," + repeats;
			bindings.put(prop, val);
			
			final String macroName = macros[macroNum].getProperty("name");
			key.setMappedValue("Macro: " + macroName);
			key.setRepeats(repeats==1?"Yes":"No");
		}
		else {
			System.err.println("KeybindPanel::saveBindings() ==> TODO implent this type of key");
		}
		
		Configs.saveBindings(bindingsId, bindings);
	}
}
