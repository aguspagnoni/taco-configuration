package tacoconfigsplugin.popup.actions;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import tacoconfigsplugin.popup.actions.Config.ConfigType;

public class ParseConfigurations {
	private BufferedReader br;
	private HashMap<String, List<Config>> configurations = new HashMap<>();
	public static String testMethodSignature = "public void test_";
	public static String configMethodName = "setConfigKey";
	
	public ParseConfigurations(String filePath) {
		try {
			br = new BufferedReader(new FileReader(filePath));
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public HashMap<String, List<Config>> configurations() {
		String line = "";
		String currentMethodName = "";
		try {
			while ((line = br.readLine()) != null) {
				if (currentMethodName != "") {
					if (line.contains(configMethodName)) {
						List<Config> configs = configurations.get(currentMethodName);
						String[] configNameAndValue = fetchConfig(line);
						Config config = new Config(configNameAndValue[0], configNameAndValue[1], ConfigType.valueOf(configNameAndValue[2]));
						configs.add(config);
					}
				}
				if (line.contains(testMethodSignature)) {
					currentMethodName = fetchMethodName(line);
					configurations.put(currentMethodName, new ArrayList<Config>()); 
				}
			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return configurations;
	}
	
	private String fetchMethodName(String line) {
		String methodName = line.split(" ")[2];
		return methodName.substring(0, methodName.length() - 2);
	}
	
	private String[] fetchConfig(String line) {
		String configName = "";
		String configValue = "";
		String totalMethodName = "";
		String configType = "";
		boolean configNameRead = false;
		boolean configValueRead = false;
		line = line.replaceAll("\t", "").replaceAll(" ", "").replaceAll("\n", "");
		for (int i = 0; i < line.length() && !(configNameRead && configValueRead); i++) {
			char c = line.charAt(i);
			if (totalMethodName.equals(configMethodName))  {
				if (!configNameRead) {
					if (c == '(') {
						configNameRead = true;
					} else {
						configName += c; 
					}
				} else {
					if (!configValueRead) {
						if (c == ')') {
							configValueRead = true;
						} else {
							configValue += c;
						}
					}
				}
			} else {
				totalMethodName += c;				
			}
		}
		
		if (isIntegerClass(configValue)) {
			configType = ConfigType.Integer.toString();
		} else if (isDoubleClass(configValue)){
			configType = ConfigType.Double.toString();
		} else if (isBooleanClass(configValue)) {
			configType = ConfigType.Boolean.toString();
		} else {
			configType = ConfigType.String.toString();
		}
		
		String[] ans = {configName, configValue, configType};
		return ans;
	}
	
	boolean isIntegerClass(String value) {
		try {
			Integer.valueOf(value);
			return true;
		} catch (Exception e) {
			return false;
		}
	}
	
	boolean isDoubleClass(String value) {
		try {
			Double.valueOf(value);
			return true;
		} catch (Exception e) {
			return false;
		}
	}
	
	boolean isBooleanClass(String value) {
		if(value.toLowerCase().equals("true") || value.toLowerCase().equals("false")) {
			return true;
		}
		return false;
	}
}
