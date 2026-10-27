package com.qa.framework.config;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.Properties;

public class ConfigReader {
	private Properties prop;

	public ConfigReader()
	{}
    public Properties initProperties() {
        prop = new Properties();
        try {
          
            FileInputStream ip = new FileInputStream("./src/main/resources/config.properties");
            prop.load(ip);
        } catch (IOException e) {
            System.out.println("Error while loading Configuration Details: " + e.getMessage());
        }
        return prop;
    }
}
