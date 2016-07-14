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

	// TODO: Esto debería además agregar las configs que no están
	public Map<String, List<Config>> configurations() {
		String line = "";
		String currentMethodName = "";
		try {
			while ((line = br.readLine()) != null) {
				if (currentMethodName != "") {
					if (line.contains(configMethodName)) {
						List<Config> configs = configurations.get(currentMethodName);
						// TODO: Que fetchConfig devuelva un Config de una
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

	// TODO: Que esto devuelva un Config, no tiene sentido ir de config a string, y de string a config luego
	// TODO: Hace transparente el tema de los strings con comillas "", que fetchconfig las quite y que setConfig las ponga, para que al mostrarlas no se vean y
	// no generen problemas
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
		} else if (isDoubleClass(configValue)) {
			configType = ConfigType.Double.toString();
		} else if (isBooleanClass(configValue)) {
			configType = ConfigType.Boolean.toString();
		} else {
			configType = ConfigType.String.toString();
		}

		String[] ans = { configName, configValue, configType };
		return ans;
	}

	private String lineToBeWritten(String line, List<Config> configurations) {
		String configName = "";
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
						newConfigValue = fetchConfigObject(configName, configurations);
						if (newConfigValue != null) {
							configValueIndex = i + 1;
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

	private String fetchConfigObject(String configName, List<Config> configs) {
		for (Config config : configs) {
			if (config.name().equals(configName)) {
				if (config.type().equals(ConfigType.String)) {
					return "\"" + config.value() + "\"";
				}
				return config.value();
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
