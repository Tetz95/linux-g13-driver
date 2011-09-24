package com.gupta.g13;

import java.awt.Polygon;
import java.awt.Shape;
import java.util.ArrayList;
import java.util.List;

public class Key {

	private static List<Key> keys = new ArrayList<Key>();
	
	private static int [][][] items = {
		// screen 
		//{{-1}, {155, 28}, {330, 28}, {330, 83}, {155, 83}, },

		{{25}, {162, 116}, {195, 116}, {195, 124}, {162, 124}, },
		{{26}, {205, 116}, {240, 116}, {240, 124}, {205, 124}, },
		{{27}, {250, 116}, {283, 116}, {283, 124}, {250, 124}, },
		{{28}, {293, 116}, {327, 116}, {327, 124}, {293, 124}, },

		// small round buttons below the screen
		{{34}, {110, 110}, {131, 110}, {131, 133}, {110, 133}, },
		{{35}, {358, 110}, {379, 110}, {379, 133}, {358, 133}, },
		
		// M1, M2, M3, M4
		{{29}, {97,  145}, {168, 145}, {168, 162}, {118, 162}, },
		{{30}, {173, 145}, {242, 145}, {242, 162}, {173, 162}, },
		{{31}, {247, 145}, {316, 145}, {316, 162}, {247, 162}, },
		{{32}, {321, 145}, {392, 145}, {374, 162}, {321, 162}, },

		//G1
		{{0}, {55,  188}, {107, 188}, {104, 219}, {52, 212}, },
		{{1}, {117, 188}, {158, 188}, {158, 222}, {115, 220}, },
		{{2}, {172, 188}, {211, 188}, {211, 223}, {170, 220}, },
		{{3}, {225, 188}, {262, 188}, {262, 222}, {225, 222}, },
		{{4}, {278, 188}, {318, 188}, {318, 220}, {278, 220}, },
		{{5}, {331, 188}, {371, 188}, {371, 217}, {331, 219}, },
		{{6}, {383, 188}, {432, 188}, {435, 212}, {385, 215}, },

		//G8
		{{7},  {48,  233}, {101, 236}, {100, 267}, {70, 267}, },
		{{8},  {114, 237}, {155, 239}, {155, 268}, {111, 267}, },
		{{9},  {168, 241}, {210, 241}, {210, 270}, {167, 270}, },
		{{10}, {223, 241}, {264, 241}, {264, 272}, {223, 272}, },
		{{11}, {278, 240}, {320, 240}, {320, 271}, {278, 271}, },
		{{12}, {333, 240}, {374, 237}, {376, 265}, {335, 268}, },
		{{13}, {387, 237}, {437, 232}, {420, 262}, {390, 265}, },

		//G15
		{{14}, {84, 289}, {154, 293}, {152, 323}, {109, 323}, },
		{{15}, {165, 293}, {208, 294}, {209, 323}, {164, 323}, },
		{{16}, {223, 294}, {264, 293}, {265, 323}, {223, 323}, },
		{{17}, {279, 293}, {323, 292}, {323, 323}, {279, 324}, },
		{{18}, {335, 292}, {402, 288}, {380, 321}, {337, 322}, },

		//G20
		{{19}, {122, 345}, {208, 345}, {208, 375}, {152, 375}, },
		{{20}, {222, 346}, {264, 346}, {266, 376}, {221, 376}, },
		{{21}, {279, 345}, {360, 343}, {341, 373}, {279, 373}, },

		//Thumb1/Thumb2
		{{22}, {365, 415}, {391, 389}, {388, 460}, {381, 467}, {365, 448}, },
		{{23}, {393, 493}, {415, 473}, {476, 470}, {413, 516}, },
	};
	
	
	static {
		for (final int [][] item: items) {
			final Key key = new Key(item);
			keys.add(key);
		}
	};
	

	public static List<Key> getAllMasks() {
		return keys;
	}
	
	public static Key getKeyAt(int x, int y) {
		for (final Key key: keys) {
			if (key.getShape().contains(x, y)) {
				return key;
			}
		}
		
		return null;
	}
	
	public static Key getKeyFor(int g13KeyCode) {
		for (final Key key: keys) {
			if (key.getG13KeyCode() == g13KeyCode) {
				return key;
			}
		}
		
		return null;
	}
	
	private Shape shape;
	
	private int g13KeyCode;
	
	private String mappedValue = "Unknown";
	
	private String repeats = "Unknown";
	
	private Key(int [][] buttonData) {
		
		g13KeyCode = buttonData[0][0];
		
		boolean first = true;
		final Polygon polygon = new Polygon();
		for (int [] point: buttonData) {
			if (!first) {
				polygon.addPoint(point[0], point[1]);
			}
			first = false;
		}
		
		this.shape = polygon;
		
	}

	public Shape getShape() {
		return shape;
	}

	public void setShape(Shape shape) {
		this.shape = shape;
	}

	public int getG13KeyCode() {
		return g13KeyCode;
	}

	public void setG13KeyCode(int g13Key) {
		this.g13KeyCode = g13Key;
	}

	public String getMappedValue() {
		return mappedValue;
	}

	public void setMappedValue(String mappedValue) {
		this.mappedValue = mappedValue;
	}

	public String getRepeats() {
		return repeats;
	}

	public void setRepeats(String repeats) {
		this.repeats = repeats;
	}
	
}
