package org.smartregister.utils;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.Properties;
import java.util.logging.Level;
import java.util.logging.Logger;

public class ApplicationProperties {

	private static final String propertiesFile = "application.properties";

	public static ApplicationProperties instance;

	private Properties properties;

	public ApplicationProperties(String propertyFileName) {
		try {
			FileInputStream fileInputStream = new FileInputStream(propertyFileName);
			this.properties = new Properties();
			this.properties.load(fileInputStream);
		}
		catch (IOException e) {
			Logger.getLogger(this.getClass().getName()).log(Level.SEVERE, "Error Reading Application Properties", e);
		}
	}

	public static ApplicationProperties getInstance() {
		if (instance == null) {
			instance = new ApplicationProperties(propertiesFile);
		}
		return instance;
	}

	public String getProperty(String propertyName) {
		return getProperty(propertyName, "");
	}

	public String getProperty(String propertyName, String defaultValue) {
		return this.properties.getProperty(propertyName, defaultValue);
	}
}
