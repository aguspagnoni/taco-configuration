package tacoconfigsplugin.popup.actions;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashMap;

public class ParseConfigurations {
	private BufferedReader br;
	private HashMap<String, HashMap<String, String>> configurations = new HashMap<String, HashMap<String, String>>();
	public static String testMethodSignature = "public void test_";
	public static String configMethodName = "setConfigKey";
	
	public ParseConfigurations(String filePath) throws FileNotFoundException, IOException {
		br = new BufferedReader(new FileReader(filePath));
	}
	
	public HashMap<String, HashMap<String, String>> configurations() throws IOException {
		String line = "";
		String currentMethodName = "";
		while ((line = br.readLine()) != null) {
			if (currentMethodName != "") {
				if (line.contains(configMethodName)) {
					HashMap<String, String> configs = configurations.get(currentMethodName);
					String[] configNameAndValue = fetchConfig(line);
					configs.put(configNameAndValue[0], configNameAndValue[1]);
					configurations.put(currentMethodName, configs);
				}
			}
			if (line.contains(testMethodSignature)) {
				currentMethodName = fetchMethodName(line);
				configurations.put(currentMethodName, new HashMap<String, String>()); 
			}
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
		String[] ans = {configName, configValue};
		return ans;
	}
}
