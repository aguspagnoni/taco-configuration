package tacoconfigsplugin.popup.actions;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import tacoconfigsplugin.popup.actions.Config.ConfigType;

public class ParseConfigurationTest {

	public static void main(String[] args) throws FileNotFoundException, IOException {
		HashMap<String, List<Config>> configs = new ParseConfigurations("/Users/gromarion/Documents/ITBA/AVMC/taco-configuration/taco-configs-plugin/src/tacoconfigsplugin/popup/actions/TestClass.java").configurations();
		HashMap<String, List<Config>> confs = new HashMap<String, List<Config>>();
		List<Config> confsList = new ArrayList<Config>();
		confsList.add(new Config("RelevantClasses", "lalalala", ConfigType.String));
		confs.put("test_find", confsList);
		new ParseConfigurations("/Users/gromarion/Documents/ITBA/AVMC/taco-configuration/taco-configs-plugin/src/tacoconfigsplugin/popup/actions/TestClass.java").setConfigurations(confs);
		System.out.println(configs);
	}
}
