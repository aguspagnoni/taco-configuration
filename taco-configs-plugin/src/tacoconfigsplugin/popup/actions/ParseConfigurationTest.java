package tacoconfigsplugin.popup.actions;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.eclipse.core.runtime.Path;

import tacoconfigsplugin.popup.actions.Config.ConfigType;

public class ParseConfigurationTest {

	public static void main(String[] args) throws FileNotFoundException, IOException {
		Map<String, List<Config>> configs = new ParseConfigurations(new Path("/Users/gromarion/Documents/ITBA/AVMC/taco-configuration/taco-configs-plugin/src/tacoconfigsplugin/popup/actions/TestClass.java")).configurations();
		Map<String, List<Config>> confs = new HashMap<>();
		List<Config> confsList = new ArrayList<>();
		confsList.add(new Config("RelevantClasses", "gromackClass,facuClass", ConfigType.String));
		confsList.add(new Config("RelevantGromack", "gromack", ConfigType.String));
		confsList.add(new Config("RelevantGromackLala", "gromacka", ConfigType.String));
		confsList.add(new Config("RelevantGromackPepe", "gromackb", ConfigType.String));
		confs.put("test_find", confsList);
		new ParseConfigurations(new Path("/Users/gromarion/Documents/ITBA/AVMC/taco-configuration/taco-configs-plugin/src/tacoconfigsplugin/popup/actions/TestClass.java")).setConfigurations(confs);
		System.out.println(configs);
	}
}
