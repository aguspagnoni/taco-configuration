package tacoconfigsplugin.popup.actions;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.eclipse.core.runtime.IPath;
import org.yaml.snakeyaml.Yaml;

import tacoconfigsplugin.popup.actions.Config.ConfigType;

public class ParseConfigurations {
	private BufferedReader br;
	private String filePath;
	private Map<String, List<Config>> configurations = new HashMap<>();
	private List<Config> defaultConfigs = new ArrayList<>();
	public static String testMethodSignature = "public void test_";
	public static String configMethodName = "setConfigKey";

	public ParseConfigurations(IPath filePath) {
		try {
			this.filePath = filePath.toString();
			br = new BufferedReader(new FileReader(this.filePath));
			initializeDefaultConfigurations();
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

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
		appendMissingConfigurations();
		return configurations;
	}

	public void setConfigurations(Map<String, List<Config>> configurations) {
		String line = "";
		List<String> newContent = new ArrayList<>();
		String currentMethodName = "";
		int bracesAmount = 0;
		try {
			while ((line = br.readLine()) != null) {
				if (line.contains(testMethodSignature)) {
					currentMethodName = fetchMethodName(line);
					bracesAmount++;
					newContent.add(line);
				} else {
					if (currentMethodName != "") {
						if (line.contains(configMethodName)) {
							List<Config> configs = configurations.get(currentMethodName);
							if (configs != null) {
								newContent.add(lineToBeWritten(line, configs));
							} else {
								newContent.add(line);
							}
						} else {
							if (line.contains("{")) {
								bracesAmount++;
							}
							if (line.contains("}")) {
								bracesAmount--;
								if (bracesAmount == 0) {
									if (currentMethodName != "" && configurations.get(currentMethodName) != null && configurations.get(currentMethodName).size() > 0) {
										addMissingConfigurations(newContent, configurations.get(currentMethodName));
									}
									currentMethodName = "";
								}
							}
							newContent.add(line);
						}
					} else {
						newContent.add(line);
					}
				}
			}
			PrintWriter writer = new PrintWriter(filePath, "UTF-8");
			for (String string : newContent) {
				if (string != null) {
					writer.println(string);					
				}
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
		} else if (isRangeClass(configValue)) {
			configType = ConfigType.Range;
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
	
	private void addMissingConfigurations(List<String> lines, List<Config> configurations) {
		int lastConfigIndex = -1;
		for (int i = lines.size() - 1; i >= 0; i--) {
			if (lines.get(i).contains(configMethodName)) {
				lastConfigIndex = i + 1;
				break;
			}
		}
		List<String> finalLines = new ArrayList<String>();
		for(String line : lines.subList(lastConfigIndex, lines.size())) {
			finalLines.add(line);			
		}
		String lastLine = finalLines.get(finalLines.size() - 1);
		int tabsAmount = lastLine.length() - lastLine.replace("\t", "").length();
		String tabs = "";
		while (tabsAmount-- > 0) {
			tabs += "\t";
		}
		while(configurations.size() > 0) {
			Config config = configurations.get(configurations.size() - 1);
			String value = config.value();
			if (config.type() == ConfigType.String) {
				value = "\"" + value + "\"";
			}
			lines.add(lastConfigIndex + 1, tabs + configMethodName + config.name() + "(" + value + ");");
			configurations.remove(config);
		}
	}
	
	private void appendMissingConfigurations() {
		for(String method : configurations.keySet()) {
			List<Config> configsToAdd = new ArrayList<>();
			List<Config> configs = configurations.get(method);
			for (Config config : defaultConfigs) {
				if (!configs.contains(config)) {
					configsToAdd.add(config);
				}
			}
			configs.addAll(configsToAdd);
		}
	}
	
	private void initializeDefaultConfigurations() {
		ClassLoader classLoader = getClass().getClassLoader();;
	    Yaml yaml = new Yaml();

	    try {
	        InputStream ios = new FileInputStream(new File(classLoader.getResource("taco_conf.yml").getFile()));

	        Map<String, Map<String, String>> result = (Map<String, Map<String, String>>) yaml.load(ios);
	        for (String name : result.keySet()) {   
	            Map<String, String> config = result.get(name);
	            ConfigType type = null;
	            switch (config.get("klass")) {
				case "String":
					type = ConfigType.String;
					break;
				case "Boolean":
					type = ConfigType.Boolean;
					break;
				case "Integer":
					type = ConfigType.Integer;
					break;
				case "Range":
					type = ConfigType.Range;
					break;
				default:
					break;
				}
	            defaultConfigs.add(new Config(name, config.get("default"), type));
	        }
	    } catch (Exception e) {
	        e.printStackTrace();
	    }
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
	
	boolean isRangeClass(String value) {
		String[] values = value.split(",");
		if (values.length == 2) {
			return isIntegerClass(values[0]) && isIntegerClass(values[1]);
		} else {
			return false;
		}
	} 
}
