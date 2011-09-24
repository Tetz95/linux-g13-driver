package com.gupta.g13;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Date;
import java.util.Properties;

public class Configs {

	private static final String [][] defaultBindings = {
			{"G34", "10"},         {"G29", "59"}, {"G30", "60"}, {"G31", "61"}, {"G32", "62"},        {"G35", "11"},
			{"G0" , "3" }, {"G1" , "4" }, {"G2",  "5" }, {"G3",  "6" }, {"G4",  "7" }, {"G5",  "8" }, {"G6",  "9" }, 
			{"G7" , "16"}, {"G8" , "17"}, {"G9",  "18"}, {"G10", "19"}, {"G11", "20"}, {"G12", "21"}, {"G13", "22"}, 
                           {"G14", "30"}, {"G15", "31"}, {"G16", "32"}, {"G17", "33"}, {"G18", "34"}, 
                                      {"G19", "44"}, {"G20", "45"}, {"G21", "46"}, 
            /* Stick Buttons */ {"G22", "57"}, {"G23", "58"}, 
            /* Stick */ {"G36" , "103"}, {"G37" , "105"}, {"G38",  "106"}, {"G39", "108"}, 
			             
	};
	
	public static Properties loadBindings(int item) throws IOException {
		
		final String home = System.getenv("HOME").endsWith("/")?System.getenv("HOME"):System.getenv("HOME")+"/";
		final File file = new File(home + ".g13/bindings-" + item + ".properties");
		
		if (file.exists() == false) { // create new file
			if (file.getParentFile().exists() == false) {
				file.getParentFile().mkdirs();
			}
			
			final Properties props = new Properties();
			props.put("color", "255,255,255");
			
			for (final String [] binding: defaultBindings) {
				props.put(binding[0], "p,k." + binding[1]);
			}
			
			saveBindings(item, props);
			
			return props;
		}
		
		final Properties props = new Properties();
		final FileInputStream fis = new FileInputStream(file);
		props.load(fis);
		
		return props;
	}
	
	
	public static void saveBindings(int item, Properties props) throws IOException {
		final String home = System.getenv("HOME").endsWith("/")?System.getenv("HOME"):System.getenv("HOME")+"/";
		final File file = new File(home + ".g13/bindings-" + item + ".properties");
		if (file.exists()) {
			file.delete();
		}
		
		final FileOutputStream fos = new FileOutputStream(file);
		props.store(fos, new Date().toString());
	}
	
	public static Properties loadMacro(int macroNum) throws IOException {
		final String home = System.getenv("HOME").endsWith("/")?System.getenv("HOME"):System.getenv("HOME")+"/";
		final File file = new File(home + ".g13/macro-" + macroNum + ".properties");
		
		if (file.exists() == false) { // create new file
			if (file.getParentFile().exists() == false) {
				file.getParentFile().mkdirs();
			}
			
			final Properties props = new Properties();
			
			if (JavaToLinuxKeymapping.linuxToJavaCodes.length > macroNum && 
					((String)JavaToLinuxKeymapping.linuxToJavaCodes[macroNum][0]).length() > 0) {
				
				props.put("name", (String)JavaToLinuxKeymapping.linuxToJavaCodes[macroNum][0]);
				int keycode = (Integer)JavaToLinuxKeymapping.linuxToJavaCodes[macroNum][1];
				final String seq = "kd." + keycode + ",ku." + keycode + ",d.100";
				props.put("sequence", seq);
			}
			else {
				props.put("name", "");
				props.put("sequence", "");
			}
			props.put("id", Integer.toString(macroNum));
			saveMacro(macroNum, props);
			
			return props;
		}
		
		final Properties props = new Properties();
		final FileInputStream fis = new FileInputStream(file);
		props.load(fis);
		
		props.put("id", Integer.toString(macroNum));
		
		return props;
	}
	
	public static void saveMacro(int macroNum, Properties props) throws IOException {
		final String home = System.getenv("HOME").endsWith("/")?System.getenv("HOME"):System.getenv("HOME")+"/";
		final File file = new File(home + ".g13/macro-" + macroNum + ".properties");
		if (file.exists()) {
			file.delete();
		}
		
		final FileOutputStream fos = new FileOutputStream(file);
		props.store(fos, new Date().toString());
		
	}
	
}
