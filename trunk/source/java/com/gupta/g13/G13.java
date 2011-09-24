/**
 * 
 */
package com.gupta.g13;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.Toolkit;
import java.util.Properties;
import java.util.StringTokenizer;

import javax.swing.BorderFactory;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;

/**
 * @author jgupta
 *
 */
public class G13 extends JPanel {

	private static final long serialVersionUID = 1L;
	
	private static final int MAX_MACROS = 200;
	
	private final ImageMap g13Label = new ImageMap();

	private final KeybindPanel keybindPanel = new KeybindPanel();
	
	private final MacroEditorPanel macroEditorPanel = new MacroEditorPanel();
	
	private Properties [] keyBindings = new Properties[4];
	
	private Properties [] macros = new Properties[MAX_MACROS];
	
	public G13() {
		setLayout(new BorderLayout());
		
		try {
			for (int i = 0; i < keyBindings.length; i++) {
				keyBindings[i] = Configs.loadBindings(i);
			}
			
			for (int i = 0; i < macros.length; i++) {
				macros[i] = Configs.loadMacro(i);
			}
			
			mapBindings(0);
		}
		catch (Exception e) {
			e.printStackTrace();
			JOptionPane.showMessageDialog(this, e);
		}
		
		keybindPanel.setBindings(0, keyBindings[0]);
		
		g13Label.addListener(new ImageMapListener() {
			@Override
			public void selected(Key key) {
				
				if (key == null) {
					keybindPanel.setSelectedKey(null);
					return;
				}
				
				if (key.getG13KeyCode() == 25 || key.getG13KeyCode() == 26 ||
						key.getG13KeyCode() == 27 || key.getG13KeyCode() == 28) {
					
					mapBindings(key.getG13KeyCode() - 25);
					
					return;
				}
				
				keybindPanel.setSelectedKey(key);
			}

			@Override
			public void mouseover(Key key) {
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
	
		
	private void mapBindings(int bindingnum) {
		keybindPanel.setSelectedKey(null);
		keybindPanel.setBindings(bindingnum, keyBindings[bindingnum]);
		
		for (int i = 0; i < 40; i++) {
			String property = "G" + i;
			String val = keyBindings[bindingnum].getProperty(property);
			
			final Key k = Key.getKeyFor(i);
			if (k != null) {
				k.setMappedValue("Unknown");
				k.setRepeats("Unknown");
				
				if (val != null) {
					final StringTokenizer st = new StringTokenizer(val, ",.");
					final String type = st.nextToken();
					if (type.equals("p")) {
						st.nextToken();
						int keycode = Integer.valueOf(st.nextToken());
						k.setMappedValue(JavaToLinuxKeymapping.cKeyCodeToString(keycode));
						k.setRepeats("N/A");
					}
					else {
						int macroNum = Integer.valueOf(st.nextToken());
						final String macroName = macros[macroNum].getProperty("name");
						boolean repeats = Integer.valueOf(st.nextToken()) != 0;
						k.setMappedValue("Macro: " + macroName);
						k.setRepeats(repeats?"Yes":"No");
						
					}
				}
			}
		}

	}
	
	/**
	 * @param args
	 */
	public static void main(String[] args) {
		final JFrame frame = new JFrame("G13 Configuation Tool");
		//frame.setIconImage(ICON.getImage());
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

		final G13 g13 = new G13();	
		frame.getContentPane().add(g13, BorderLayout.CENTER);
		
		frame.pack();
		final Dimension appSize = frame.getPreferredSize();		
		final Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
		
		final int insetX = (screenSize.width - appSize.width) / 2;
		final int insetY = (screenSize.height - appSize.height) / 2;
		frame.setLocation(insetX, insetY);		
		
		frame.setVisible(true);
	}

}
