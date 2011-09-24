package com.gupta.g13;

import java.awt.Component;
import java.util.Properties;

import javax.swing.DefaultListCellRenderer;
import javax.swing.JList;

public class MacroListCellRenderer extends DefaultListCellRenderer {
	private static final long serialVersionUID = 1L;

	@Override
	public Component getListCellRendererComponent(JList list, Object value, int index, boolean isSelected, boolean cellHasFocus) {
		Object newVal = value;
		
		if (value instanceof Properties) {
			final Properties props = (Properties)value;
			final String name = (String)(props.get("name")==null?"":props.get("name"));
			final String id = (String)(props.get("id")==null?"":props.get("id"));
			
			newVal = "[" + id + "] " + name;
		}
		
		return super.getListCellRendererComponent(list, newVal, index, isSelected, cellHasFocus);
	}

}
