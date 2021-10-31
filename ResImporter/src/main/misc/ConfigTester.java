package main.misc;

import my.util.MyConfigParser;
import my.util.MyConfigParser.ConfigCollection;

public class ConfigTester {

	public static void main(String[] args) {
		ConfigCollection conf = MyConfigParser.parse("res/raw/config/type1.dcepconf");
		conf.get().forEach((group, fields) -> {
			System.out.print("\n[" + group);
			fields.forEach((name, value) -> {
				System.out.print(", " + name + ":" + value);
			});
			System.out.print("]");
		});

	}

}
