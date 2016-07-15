package tacoconfigsplugin.popup.actions;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.eclipse.core.runtime.IPath;

import tacoconfigsplugin.popup.actions.Config.ConfigType;

public class ParseConfigurations {
	private BufferedReader br;
	private String filePath;
	private Map<String, List<Config>> configurations = new HashMap<>();
	private Map<String, List<String>> methods = new HashMap<>();
	public static String testMethodSignature = "public void test_";
	public static String configMethodName = "setConfigKey";

	public ParseConfigurations(IPath filePath) {
		try {
			this.filePath = filePath.toString();
			br = new BufferedReader(new FileReader(this.filePath));
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	// TODO: Esto deber�a adem�s agregar las configs que no est�n
	public Map<String, List<Config>> configurations() {
		String line = "";
		String currentMethodName = "";
		try {
			while ((line = br.readLine()) != null) {
				if (currentMethodName != "") {
					if (line.contains(configMethodName)) {
						List<Config> configs = configurations.get(currentMethodName);
						configs.add(fetchConfig(line));
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

	public void setConfigurations(Map<String, List<Config>> configurations) {
		String line = "";
		List<String> newContent = new ArrayList<>();
		String currentMethodName = "";
		try {
			while ((line = br.readLine()) != null) {
				if (line.contains(testMethodSignature)) {
					currentMethodName = fetchMethodName(line);
					newContent.add(line);
				} else {
					if (currentMethodName != "") {
						if (methods.get(currentMethodName) != null) {						
							methods.get(currentMethodName).add(line);
						} else {
							List<String> lines = new ArrayList<String>();
							lines.add(line);
							methods.put(currentMethodName, lines);
						}
						if (line.contains(configMethodName)) {
							List<Config> configs = configurations.get(currentMethodName);
							if (configs != null) {
								newContent.add(lineToBeWritten(line, configs));
							} else {
								newContent.add(line);
							}
						} else {
							newContent.add(line);
						}
					} else {
						newContent.add(line);
					}
				}
			}
			PrintWriter writer = new PrintWriter(filePath, "UTF-8");
			for (String string : newContent) {
				writer.println(string);
			}
			writer.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	private String fetchMethodName(String line) {
		String methodName = line.split(" ")[2];
		return methodName.substring(0, methodName.length() - 2);
	}

	private Config fetchConfig(String line) {
		String configName = "";
		String configValue = "";
		String totalMethodName = "";
		ConfigType configType = ConfigType.String;
		boolean configNameRead = false;
		boolean configValueRead = false;
		line = line.replaceAll("\t", "").replaceAll(" ", "").replaceAll("\n", "");
		for (int i = 0; i < line.length() && !(configNameRead && configValueRead); i++) {
			char c = line.charAt(i);
			if (totalMethodName.equals(configMethodName)) {
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
						} else if (c != '"'){
							configValue += c;
						}
					}
				}
			} else {
				totalMethodName += c;
			}
		}
		if (isIntegerClass(configValue)) {
			configType = ConfigType.Integer;
		} else if (isDoubleClass(configValue)) {
			configType = ConfigType.Double;
		} else if (isBooleanClass(configValue)) {
			configType = ConfigType.Boolean;
		}

		return new Config(configName, configValue, configType);
	}

	private String lineToBeWritten(String line, List<Config> configurations) {
		String configName = "";
		Config newConfigObject = null;
		String newConfigValue = "";
		String totalMethodName = "";
		boolean configNameRead = false;
		int configValueIndex = -1;
		for (int i = 0; i < line.length() && !configNameRead; i++) {
			char c = line.charAt(i);
			if (totalMethodName.contains(configMethodName)) {
				if (!configNameRead) {
					if (c == '(') {
						configNameRead = true;
						newConfigObject = fetchConfigObject(configName, configurations);
						if (newConfigObject != null) {
							configValueIndex = i + 1;
							if (newConfigObject.type().equals(ConfigType.String)) {
								newConfigValue = "\"" + newConfigObject.value() + "\"";
							} else {
								newConfigValue = newConfigObject.value();
							}
							configurations.remove(newConfigObject);
						}
					} else {
						configName += c;
					}
				}
			} else {
				totalMethodName += c;
			}
		}
		if (configValueIndex != -1) {
			return line.substring(0, configValueIndex) + newConfigValue + ");";
		}
		return line;
	}

	private Config fetchConfigObject(String configName, List<Config> configs) {
		for (Config config : configs) {
			if (config.name().equals(configName)) {
				return config;
			}
		}
		return null;
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
		if (value.toLowerCase().equals("true") || value.toLowerCase().equals("false")) {
			return true;
		}
		return false;
	}
}
