package com.booker.g13;

import java.awt.Component;
import java.util.Properties;

import javax.swing.DefaultListCellRenderer;
import javax.swing.JList;

// Diese Klasse ist für die Darstellung von Makros in der JComboBox zuständig.
// Sie ist einfach und stabil. Refactoring beschränkt sich auf Generics und @Override.
public class MacroListCellRenderer extends DefaultListCellRenderer {
	private static final long serialVersionUID = 1L;

	@Override
	public Component getListCellRendererComponent(JList<?> list, Object value, int index, boolean isSelected, boolean cellHasFocus) {
		super.getListCellRendererComponent(list, value, index, isSelected, cellHasFocus);
		
		if (value instanceof Properties props) {
			final String name = props.getProperty("name", "Unnamed Macro");
			final String id = props.getProperty("id", "?");
			setText("[" + id + "] " + name);
		}
		
		return this;
	}
}