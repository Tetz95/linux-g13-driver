package com.gupta.g13;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.util.ArrayList;
import java.util.List;

import javax.swing.ImageIcon;
import javax.swing.JLabel;

public class ImageMap extends JLabel {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private static final ImageIcon G13_KEYPAD = ImageIconHelper.loadEmbeddedImage("/com/gupta/g13/images/g13.gif");
	
	private final List<ImageMapListener> listeners = new ArrayList<ImageMapListener>();

	private final Color outlineColor = Color.red.darker();
	
	private final Color selectedColor = new Color(0, 255, 0, 128);
	
	private final Color mouseoverColor = new Color(0, 0, 255, 64);

	private Key selected = null;
	
	private Key mouseover = null;
	
	public ImageMap() {
		super(G13_KEYPAD);
		
		addMouseMotionListener(new MouseMotionListener() {

			@Override
			public void mouseDragged(MouseEvent arg0) {
			}

			@Override
			public void mouseMoved(MouseEvent arg0) {
				Key key = Key.getKeyAt(arg0.getPoint().x, arg0.getPoint().y);
				if (key == null && mouseover == null) {
					return;
				}
				
				if ((key != null && mouseover == null) || (key == null && mouseover != null)) {
					mouseover = key;
					repaint();
					fireMouseover();
					return;
				}
				
				if (key.getG13KeyCode() != mouseover.getG13KeyCode()) {
					mouseover = key;
					repaint();
					fireMouseover();
					return;
				}
			}
		});
		
		addMouseListener(new MouseListener() {
			@Override
			public void mouseClicked(MouseEvent arg0) {
				System.out.println(arg0.getPoint().x + ", " + arg0.getPoint().y);
				
				Key key = Key.getKeyAt(arg0.getPoint().x, arg0.getPoint().y);
				
				if (key == null && selected == null) {
					return;
				}
				
				if ((key != null && selected == null) || (key == null && selected != null)) {
					
					selected = key;
					repaint();
					fireSelected();
					return;
				}
				
				if (key.getG13KeyCode() != selected.getG13KeyCode()) {
					selected = key;
					repaint();
					fireSelected();
					return;
				}
			}

			@Override
			public void mouseEntered(MouseEvent e) {
			}

			@Override
			public void mouseExited(MouseEvent e) {
			}

			@Override
			public void mousePressed(MouseEvent e) {
			}

			@Override
			public void mouseReleased(MouseEvent e) {
			}
		});

	}
	
	public void addListener(final ImageMapListener listener) {
		synchronized (listeners) {
			listeners.add(listener);
		}
	}
	
	public void removeListener(final ImageMapListener listener) {
		synchronized (listeners) {
			listeners.remove(listener);
		}
	}
	
	protected void fireSelected() {
		synchronized (listeners) {
			for (final ImageMapListener listener: listeners) {
				listener.selected(selected);
			}
		}
	}
	

	protected void fireMouseover() {
		synchronized (listeners) {
			for (final ImageMapListener listener: listeners) {
				listener.mouseover(mouseover);
			}
		}
	}
	
	
	@Override
	public void paint(Graphics _g) {
		super.paint(_g);
		
		paintSelected(_g);

		paintMouseover(_g);
		
		paintKeyOutlines(_g);
		
	}

	void paintSelected(final Graphics _g) {
		if (selected == null) {
			return;
		}
		
		final Graphics2D g = (Graphics2D)_g.create();

		g.setColor(selectedColor);
		g.fill(selected.getShape());
		
		g.setColor(selectedColor.darker());
		g.draw(selected.getShape());
		
		g.dispose();
	}
	
	void paintMouseover(final Graphics _g) {
		
		if (mouseover == null) {
			return;
		}
		
		final Graphics2D g = (Graphics2D)_g.create();
		
		g.setColor(mouseoverColor);
		g.fill(mouseover.getShape());
		
		g.setColor(mouseoverColor.darker());
		g.draw(mouseover.getShape());
		
		String [][] lines;
		if (mouseover.getG13KeyCode() == 25 || mouseover.getG13KeyCode() == 26 ||
				mouseover.getG13KeyCode() == 27 || mouseover.getG13KeyCode() == 28) {
			
			String [][] tmp = {
					{"G13 Key", "G" + Integer.toString(mouseover.getG13KeyCode() + 1)},
					{"Configuration", "bindings-" + (mouseover.getG13KeyCode() - 25) + ".properties"},
					{"This button is reserved to load bindings", ""},
				};
				
				lines = tmp;
		}
		else {
			String [][] tmp = {
				{"G13 Key", Integer.toString(mouseover.getG13KeyCode() + 1)},
				{"Mapped Value",   mouseover.getMappedValue()},
				{"Repeats",        mouseover.getRepeats()},
			};
			
			lines = tmp;
		}
		
		final int x0 = 110;
		final int x1 = 245;
		int y = 550;
		for (final String [] line: lines) {
			g.setColor(mouseoverColor.darker());
			g.drawString(line[0], x0, y);
			//y+= (3*getFont().getSize()/2);
			g.setColor(mouseoverColor.brighter());
			g.drawString(line[1], x1, y);
			
			y+= 2*getFont().getSize();
		}
		
		g.dispose();
	}


	private void paintKeyOutlines(Graphics _g) {
		final Graphics2D g = (Graphics2D)_g.create();
		
		g.setColor(outlineColor);
		
		final List<Key> list = Key.getAllMasks();
		//System.out.println("list size=" + list.size());
		for (final Key key: list) {
			g.draw(key.getShape());
		}
		
		g.dispose();
	}
	
	
}
