package tacoconfigsplugin.popup.actions;

public class Config {

	private ConfigType configType;
	private String name;
	private String value;
	private boolean isEnabled;
	
	public Config(String name, String value, ConfigType configType) {
		this.name = name;
		this.value = value;
		this.configType = configType;
	}
	
	public String name() {
		return name;
	}
	
	public ConfigType type() {
		return configType;
	}
	
	public String value() {
		return value;
	}
	
	public void enable() {
		isEnabled = true;
	}
	
	public void disable() {
		isEnabled = false;
	}
	
	public boolean isEnabled() {
		return isEnabled;
	}
	
	public boolean isBoolean() {
		return configType.equals(ConfigType.Boolean);
	}
	
	public boolean isString() {
		return configType.equals(ConfigType.String);
	}
	
	public boolean isInteger() {
		return configType.equals(ConfigType.Integer);
	}
	
	public boolean isDouble() {
		return configType.equals(ConfigType.Double);
	}
	
	public enum ConfigType {
		String, Boolean, Integer, Double 
	}
	
}
