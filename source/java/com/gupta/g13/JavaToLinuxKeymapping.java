package com.gupta.g13;

import java.awt.event.KeyEvent;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class JavaToLinuxKeymapping {
	
	public static final Object [][] linuxToJavaCodes = {
			{"", 0, 0,},	
			{"ESC", 1, KeyEvent.VK_ESCAPE,},	
			{"1", 2, KeyEvent.VK_1,},	
			{"2", 3, KeyEvent.VK_2,},	
			{"3", 4, KeyEvent.VK_3,},	
			{"4", 5, KeyEvent.VK_4,},	
			{"5", 6, KeyEvent.VK_5,},	
			{"6", 7, KeyEvent.VK_6,},	
			{"7", 8, KeyEvent.VK_7,},	
			{"8", 9, KeyEvent.VK_8,},	
			{"9", 10, KeyEvent.VK_9,},	
			{"0", 11, KeyEvent.VK_0,},	
			{"-", 12, KeyEvent.VK_MINUS,},	
			{"=", 13, KeyEvent.VK_EQUALS,},	
			{"Backspace", 14, KeyEvent.VK_BACK_SPACE,},	
			{"Tab", 15, KeyEvent.VK_TAB,},	
			{"Q", 16, KeyEvent.VK_Q,},	
			{"W", 17, KeyEvent.VK_W,},	
			{"E", 18, KeyEvent.VK_E,},	
			{"R", 19, KeyEvent.VK_R,},	
			{"T", 20, KeyEvent.VK_T,},	
			{"Y", 21, KeyEvent.VK_Y,},	
			{"U", 22, KeyEvent.VK_U,},	
			{"I", 23, KeyEvent.VK_I,},	
			{"O", 24, KeyEvent.VK_O,},	
			{"P", 25, KeyEvent.VK_P,},	
			{"[", 26, KeyEvent.VK_BRACELEFT,},	
			{"]", 27, KeyEvent.VK_BRACERIGHT,},	
			{"Enter", 28, KeyEvent.VK_ENTER,},	
			{"L CTRL", 29, KeyEvent.VK_CONTROL,KeyEvent.KEY_LOCATION_LEFT, },	
			{"A", 30, KeyEvent.VK_A,},	
			{"S", 31, KeyEvent.VK_S,},	
			{"D", 32, KeyEvent.VK_D,},	
			{"F", 33, KeyEvent.VK_F,},	
			{"G", 34, KeyEvent.VK_G,},	
			{"H", 35, KeyEvent.VK_H,},	
			{"J", 36, KeyEvent.VK_J,},	
			{"K", 37, KeyEvent.VK_K,},	
			{"L", 38, KeyEvent.VK_L,},	
			{";", 39, KeyEvent.VK_SEMICOLON,},	
			{"'", 40, KeyEvent.VK_QUOTE,},	
			{"`", 41, KeyEvent.VK_BACK_QUOTE,},	
			{"L Shift", 42, KeyEvent.VK_SHIFT, KeyEvent.KEY_LOCATION_LEFT, },	
			{"\\", 43, KeyEvent.VK_BACK_SLASH,},	
			{"Z", 44, KeyEvent.VK_Z,},	
			{"X", 45, KeyEvent.VK_X,},	
			{"C", 46, KeyEvent.VK_C,},	
			{"V", 47, KeyEvent.VK_V,},	
			{"B", 48, KeyEvent.VK_B,},	
			{"N", 49, KeyEvent.VK_N,},	
			{"M", 50, KeyEvent.VK_M,},	
			{",", 51, KeyEvent.VK_COMMA,},	
			{".", 52, KeyEvent.VK_PERIOD,},	
			{"/", 53, KeyEvent.VK_SLASH,},	
			{"R Shift", 54, KeyEvent.VK_SHIFT, KeyEvent.KEY_LOCATION_RIGHT},	
			{"NumPad *", 55, KeyEvent.VK_MULTIPLY,},	
			{"L Alt", 56, KeyEvent.VK_ALT, KeyEvent.KEY_LOCATION_LEFT,},	
			{"Space", 57, KeyEvent.VK_SPACE,},	
			{"Cap Lock", 58, KeyEvent.VK_CAPS_LOCK,},	
			{"F1", 59, KeyEvent.VK_F1,},	
			{"F2", 60, KeyEvent.VK_F2,},	
			{"F3", 61, KeyEvent.VK_F3,},	
			{"F4", 62, KeyEvent.VK_F4,},	
			{"F5", 63, KeyEvent.VK_F5,},	
			{"F6", 64, KeyEvent.VK_F6,},	
			{"F7", 65, KeyEvent.VK_F7,},	
			{"F8", 66, KeyEvent.VK_F8,},	
			{"F9", 67, KeyEvent.VK_F9,},	
			{"F10", 68, KeyEvent.VK_F10,},	
			{"Num Lock", 69, KeyEvent.VK_NUM_LOCK,},	
			{"Scroll Lock", 70, KeyEvent.VK_SCROLL_LOCK,},	
			{"NumPad 7", 71, KeyEvent.VK_NUMPAD7,},	
			{"NumPad 8", 72, KeyEvent.VK_NUMPAD8,},	
			{"NumPad 9", 73, KeyEvent.VK_NUMPAD9,},	
			{"NumPad -", 74, KeyEvent.VK_MINUS,},	
			{"NumPad 4", 75, KeyEvent.VK_NUMPAD4,},	
			{"NumPad 5", 76, KeyEvent.VK_NUMPAD5,},	
			{"NumPad 6", 77, KeyEvent.VK_NUMPAD6,},	
			{"NumPad +", 78, KeyEvent.VK_PLUS,},	
			{"NumPad 1", 79, KeyEvent.VK_NUMPAD1,},	
			{"NumPad 2", 80, KeyEvent.VK_NUMPAD2,},	
			{"NumPad 3", 81, KeyEvent.VK_NUMPAD3,},	
			{"Numpad Ins", 82, KeyEvent.VK_INSERT,},	
			{"Numpad Del", 83, KeyEvent.VK_DELETE,},	
			{"", 84, -1,},	
			{"", 85, -1,},	
			{"", 86, -1,},	
			{"F11", 87, KeyEvent.VK_F11,},	
			{"F12", 88, KeyEvent.VK_F12,},	
			{"F13", 89, KeyEvent.VK_F13,},	
			{"F14", 90, KeyEvent.VK_F14,},	
			{"F15", 91, KeyEvent.VK_F15,},	
			{"F16", 92, KeyEvent.VK_F16,},	
			{"F17", 93, KeyEvent.VK_F17,},	
			{"F18", 94, KeyEvent.VK_F18,},	
			{"F19", 95, KeyEvent.VK_F19,},	
			{"R Enter", 96, KeyEvent.VK_ENTER, KeyEvent.KEY_LOCATION_NUMPAD},	
			{"R Ctrl", 97, KeyEvent.VK_CONTROL, KeyEvent.KEY_LOCATION_RIGHT},	
			{"/", 98, KeyEvent.VK_SLASH, KeyEvent.KEY_LOCATION_NUMPAD},	
			{"PRT SCR", 99, KeyEvent.VK_PRINTSCREEN,},	
			{"R ALT", 100, KeyEvent.VK_ALT, KeyEvent.KEY_LOCATION_RIGHT},	
			{"", 101, -1,},	
			{"Home", 102, KeyEvent.VK_HOME,},	
			{"Up", 103, KeyEvent.VK_UP,},	
			{"PgUp", 104, KeyEvent.VK_PAGE_UP,},	
			{"Left", 105, KeyEvent.VK_LEFT,},	
			{"Right", 106, KeyEvent.VK_RIGHT,},	
			{"End", 107, KeyEvent.VK_END,},	
			{"Down", 108, KeyEvent.VK_DOWN,},	
			{"PgDn", 109, KeyEvent.VK_PAGE_DOWN,},	
			{"Insert", 110, KeyEvent.VK_INSERT,},	
			{"Del", 111, KeyEvent.VK_DELETE,},	
			{"", 112, -1,},	
			{"", 113, -1,},	
			{"", 114, -1,},	
			{"", 115, -1,},	
			{"", 116, -1,},	
			{"", 117, -1,},	
			{"", 118, -1,},	
			{"Pause", 119, KeyEvent.VK_PAUSE,},	
	};
	
	public static final Map<Integer, Object[]> cCodeToData = new HashMap<Integer, Object []>();
	public static final Map<Integer, List<Object[]>> javaCodeToData = new HashMap<Integer, List<Object []>>();
	
	static {
		for (final Object [] row: linuxToJavaCodes) {
			int cCode = (Integer)row[1];
			int javaCode = (Integer)row[2];
			if (javaCode != -1) {
				cCodeToData.put(cCode, row);
				if (javaCodeToData.get(javaCode) == null) {
					javaCodeToData.put(javaCode, new ArrayList<Object []>());
				}
				javaCodeToData.get(javaCode).add(row);
			}
		}
	}
	
	public static String cKeyCodeToString(int keyCode) {
		if (cCodeToData.get(keyCode) == null) {
			return "Unknown (" + keyCode + ")";
		}
		
		return (String)cCodeToData.get(keyCode)[0];
	}
	
	public static int keyEventToCCode(KeyEvent event) {
		if (javaCodeToData.get(event.getKeyCode()) == null) {
			System.out.println("JavaToLinuxKeyMapping: Unknown java event code: " + event);
			return 0;
		}
		
		List<Object []> data = javaCodeToData.get(event.getKeyCode());
		if (data.size() > 1) {
			for (final Object [] row: data) {
				if (row.length > 3) {
					int location = (Integer)row[3];
					if (location == event.getKeyLocation()) {
						return (Integer)row[1];
					}
				}
			}
		}
		
		return (Integer)data.get(0)[1];
	}

}
