/**
 * 
 */
package com.gupta.g13;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;
import java.util.StringTokenizer;

import javax.swing.BorderFactory;
import javax.swing.DefaultListCellRenderer;
import javax.swing.DefaultListModel;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextField;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

/**
 * @author jgupta
 *
 */
public class MacroEditorPanel extends JPanel {

	private static final long serialVersionUID = 1L;

	private static final ImageIcon UP_ICON = ImageIconHelper.loadEmbeddedImage("/com/gupta/g13/images/up.png", 16, 16);

	private static final ImageIcon DOWN_ICON = ImageIconHelper.loadEmbeddedImage("/com/gupta/g13/images/down.png", 16, 16);

	private static final ImageIcon DELAY_ICON = ImageIconHelper.loadEmbeddedImage("/com/gupta/g13/images/pause.png", 16, 16);

	private final JComboBox macroSelectionBox = new JComboBox();

	private final DefaultListModel listModel = new DefaultListModel();
	
	private final JList macroList = new JList(listModel);
	
	private final JTextField nameText = new JTextField();
	
	private final JButton addDelayButton = new JButton("Add Delay");
	
	private final JCheckBox captureDelays = new JCheckBox("Rec Delays", true);
	
	private final JButton editButton = new JButton("Edit");
	
	private final JButton deleteButton = new JButton("Delete");
	
	private final JButton recordButton = new JButton("Clear & Record");
	
	private boolean loadingData = false;
	
	private boolean captureMode = false;
	
	private long lastCapture = 0;
	
	public MacroEditorPanel(){
		
		setLayout(new BorderLayout());
		
		setBorder(BorderFactory.createTitledBorder("Macro Editor Panel"));
		
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
		
		controls.add(recordButton);
		
		add(controls, BorderLayout.SOUTH);
		
		editButton.setEnabled(false);
		deleteButton.setEnabled(false);
		
		macroList.addMouseListener(new MouseListener() {

			@Override
			public void mouseClicked(MouseEvent e) {
				if (e.getClickCount() > 1) {
					edit();
				}
			}

			@Override
			public void mousePressed(MouseEvent e) {
			}

			@Override
			public void mouseReleased(MouseEvent e) {
			}

			@Override
			public void mouseEntered(MouseEvent e) {
			}

			@Override
			public void mouseExited(MouseEvent e) {
			}
		});
		
		macroList.getSelectionModel().addListSelectionListener(
				new ListSelectionListener() {
					public void valueChanged(ListSelectionEvent e) {
						if (e.getValueIsAdjusting() == true) {
							return;
						}
						
						int [] list = macroList.getSelectedIndices();
						boolean enable = (list != null && list.length > 0);
						deleteButton.setEnabled(enable);
						
						editButton.setEnabled(false);
						if (list != null && list.length == 1) {
							if (listModel.getElementAt(list[0]).toString().startsWith("d.")) {
								editButton.setEnabled(true);
							}
						}
						
					}
				});
		
		
		final KeyListener keyListener = new KeyListener() {

			@Override
			public void keyPressed(KeyEvent event) {
				if (captureMode == false) {
					return;
				}
				
				if (lastCapture != 0 && captureDelays.isSelected()) {
					listModel.addElement("d." + (event.getWhen() - lastCapture));
				}
				
				lastCapture = event.getWhen();
				
				listModel.addElement("kd." + JavaToLinuxKeymapping.keyEventToCCode(event));
				//System.out.println("keypressed: " + event.getKeyCode() + " that translates to " + JavaToLinuxKeymapping.keyEventToCCode(event));
			}

			@Override
			public void keyReleased(KeyEvent event) {
				if (captureMode == false) {
					return;
				}
				
				if (lastCapture != 0 && captureDelays.isSelected()) {
					listModel.addElement("d." + (event.getWhen() - lastCapture));
				}
				
				lastCapture = event.getWhen();
				listModel.addElement("ku." + JavaToLinuxKeymapping.keyEventToCCode(event));				
				//System.out.println("keyreleased: " + event.getKeyCode());
				
			}

			@Override
			public void keyTyped(KeyEvent event) {
			}
			
		};
		
		final JComponent [] components = {
			this, macroSelectionBox, macroList, nameText, addDelayButton, 
			captureDelays, recordButton, editButton, deleteButton
		};
		
		for (final JComponent c: components) {
			c.addKeyListener(keyListener);
		}
		
		
		macroSelectionBox.setRenderer(new MacroListCellRenderer());

		macroSelectionBox.addActionListener(new ActionListener() {
			public void actionPerformed(final ActionEvent ae) {
				selectMacro();
			}
		});
		
		macroList.setCellRenderer(new DefaultListCellRenderer() {
			private static final long serialVersionUID = 1L;

			@Override
			public Component getListCellRendererComponent(JList list, Object value, int index, boolean isSelected, boolean cellHasFocus) {
				Object newVal = value;
				
				if (value instanceof String) {
					final StringTokenizer st = new StringTokenizer((String)value, ".");
					String type = st.nextToken();
					int keycode = Integer.valueOf(st.nextToken());
					
					if (type.equals("d")) {
						newVal = ((double)(keycode)/1000.0) + " seconds" ;
					}
					else {
						newVal = JavaToLinuxKeymapping.cKeyCodeToString(keycode);
					}
				}

				super.getListCellRendererComponent(list, newVal, index, isSelected, cellHasFocus);
				
				if (value instanceof String) {
					final StringTokenizer st = new StringTokenizer((String)value, ".");
					String type = st.nextToken();
					String keycode = st.nextToken();
					
					if (type.equals("kd")) {
						setIcon(DOWN_ICON);
					}
					else if (type.equals("ku")) {
						setIcon(UP_ICON);
					}
					else if (type.equals("d")) {
						setIcon(DELAY_ICON);
					}
					else {
						setIcon(null);
					}
					
				}
				else {
					setIcon(null);
				}

				return this;
			}
		});
		
		nameText.addActionListener(new ActionListener() {
			public void actionPerformed(final ActionEvent ae) {
				nameText.setForeground(Color.black);
				saveMacro();
				macroSelectionBox.repaint();
			}
		});
		nameText.getDocument().addDocumentListener(new DocumentListener() {
			@Override
			public void changedUpdate(DocumentEvent arg0) {
			}

			@Override
			public void insertUpdate(DocumentEvent arg0) {
				if (loadingData == true) {
					nameText.setForeground(Color.black);
				}
				else {
					nameText.setForeground(Color.red);
				}
			}

			@Override
			public void removeUpdate(DocumentEvent arg0) {
				if (loadingData == true) {
					nameText.setForeground(Color.black);
				}
				else {
					nameText.setForeground(Color.red);
				}
			}			
		});
		
		recordButton.addActionListener(new ActionListener() {
			public void actionPerformed(final ActionEvent ae) {
				startStopRecording();
			}
		});

		editButton.addActionListener(new ActionListener() {
			public void actionPerformed(final ActionEvent ae) {
				edit();
			}
		});

		deleteButton.addActionListener(new ActionListener() {
			public void actionPerformed(final ActionEvent ae) {
				delete();
			}
		});
		
		addDelayButton.addActionListener(new ActionListener() {
			public void actionPerformed(final ActionEvent ae) {
				addDelay();
			}
		});

	}
	
	public void startStopRecording() {
		final JComponent [] components = {
				macroSelectionBox, macroList, nameText, addDelayButton, 
				captureDelays, editButton, deleteButton
			};

		for (final JComponent c: components) {
			c.setEnabled(captureMode);
		}
		
		if (captureMode == false) {
			recordButton.setText("Stop Recording");
			captureMode = true;
			listModel.removeAllElements();
			lastCapture = 0;
		}
		else {
			recordButton.setText("Clear & Start Recording");
			captureMode = false;
			saveMacro();
		}
		
	}
	
	public void delete() {
		final int [] indicies = macroList.getSelectedIndices();
		if (indicies == null) {
			return;
		}
		
		final List<Object> items = new ArrayList<Object>();
		for (final int i: indicies) {
			items.add(listModel.getElementAt(i));
		}
		
		for (final Object o: items) {
			listModel.removeElement(o);
		}
		
		saveMacro();
	}
	
	public void edit() {
		final int [] indicies = macroList.getSelectedIndices();
		if (indicies == null) {
			return;
		}

		final String str = (String)listModel.getElementAt(indicies[0]);
		if (str.startsWith("d.") == false) {
			return;
		}
		
		int delay = Integer.valueOf(str.substring(2));
		final String newDelay = JOptionPane.showInputDialog("Enter the delay in milliseconds", delay);
		if (newDelay == null) {
			return;
		}
		
		try {
			int d = Integer.valueOf(newDelay);
			listModel.removeElementAt(indicies[0]);
			listModel.insertElementAt("d." + d, indicies[0]);
			saveMacro();
		}
		catch (Exception e) {
			JOptionPane.showMessageDialog(this, "Invalid delay value: " + newDelay);
		}
	}
	
	public void addDelay() {
		int pos = -1;
		
		final int [] indicies = macroList.getSelectedIndices();
		if (indicies != null && indicies.length > 0) {
			pos = indicies[0]+1;
		}

		
		int delay = 100;
		final String newDelay = JOptionPane.showInputDialog("Enter the delay in milliseconds", delay);
		if (newDelay == null) {
			return;
		}
		
		try {
			int d = Integer.valueOf(newDelay);
			if (pos == -1) {
				listModel.addElement("d." + d);
			}
			else {
				listModel.insertElementAt("d." + d, pos);
			}
			saveMacro();
		}
		catch (Exception e) {
			JOptionPane.showMessageDialog(this, "Invalid delay value: " + newDelay);
		}

	}
	
	public void setMacros(final Properties [] macros) {
		
		macroSelectionBox.removeAllItems();
		for (final Properties properties: macros) {
			macroSelectionBox.addItem(properties);
		}
		
		macroSelectionBox.setSelectedIndex(0);
	}
	
	private void selectMacro() {
		loadingData = true;
		
		listModel.clear();
		
		final Properties macro = (Properties)macroSelectionBox.getSelectedItem();
		nameText.setText(macro.getProperty("name"));
		
		final String sequence = macro.getProperty("sequence");
		final StringTokenizer st = new StringTokenizer(sequence, ",");
		while (st.hasMoreTokens()) {
			listModel.addElement(st.nextToken());
		}
		
		loadingData = false;
	}
	
	private void saveMacro() {
		final int id = macroSelectionBox.getSelectedIndex();
		final Properties macro = (Properties)macroSelectionBox.getSelectedItem();

		macro.setProperty("name", nameText.getText());
		
		final StringBuffer buf = new StringBuffer();
		for (int i = 0; i < listModel.getSize(); i++) {
			if (buf.length() > 0) {
				buf.append(",");
			}
			buf.append(listModel.getElementAt(i));
		}
		macro.setProperty("sequence", buf.toString());
		
		try {
			Configs.saveMacro(id, macro);
		}
		catch (Exception e) {
			e.printStackTrace();
			JOptionPane.showMessageDialog(this, e);
		}
	}
}
