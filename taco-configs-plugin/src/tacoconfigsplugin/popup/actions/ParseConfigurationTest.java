package tacoconfigsplugin.popup.actions;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.HashMap;

public class ParseConfigurationTest {

	public static void main(String[] args) throws FileNotFoundException, IOException {
		HashMap<String, Config> configs = new ParseConfigurations("/Users/gromarion/Documents/ITBA/AVMC/taco-configuration/taco-configs-plugin/src/tacoconfigsplugin/popup/actions/TestClass.java").configurations();
		System.out.println(configs);
	}
}
