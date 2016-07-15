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
	
	public Config(String name, Boolean value, ConfigType configType) {
		this(name, value.toString(), configType);
	}
	
	public Config(String name, Integer value, ConfigType configType) {
		this(name, value.toString(), configType);
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
	
	public boolean booleanValue() {
		return Boolean.valueOf(value);
	}
	
	public int intValue() {
		return Integer.valueOf(value);
	}
	
	public enum ConfigType {
		String, Boolean, Integer, Double 
	}
	
	public String toString() {
		return "name: " + name + ", value: " + value;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Config other = (Config) obj;
		if (configType != other.configType)
			return false;
		if (name == null) {
			if (other.name != null)
				return false;
		} else if (!name.equals(other.name))
			return false;
		return true;
	}
}
