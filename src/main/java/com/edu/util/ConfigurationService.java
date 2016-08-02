package com.edu.util;

/* 
** 	@author Abhinav Kadam 
*/
import java.io.FileInputStream;
import java.io.IOException;
import java.util.Properties;

/* This class handles all the configuration related services related to the project. */

public class ConfigurationService {

	private static ConfigurationService instance = null;

	private String username = null;
	private String password = null;
	private String url = null;
	private Properties configuration = new Properties();
	private String ratingsFile = null;

	public static ConfigurationService getInstance() {
		if (instance == null) {
			instance = new ConfigurationService();
		}
		return instance;
	}

	private ConfigurationService() {
	}

	public void readProperties(String filePath) {
		try {
			configuration.load(new FileInputStream(filePath));
			username = (String) configuration.get(SystemConstants.DB_USERNAME);
			password = (String) configuration.get(SystemConstants.DB_PASSWORD);
			url = (String) configuration.get(SystemConstants.DB_URL);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public static void setInstance(ConfigurationService instance) {
		ConfigurationService.instance = instance;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	public Properties getConfiguration() {
		return configuration;
	}

	public void setConfiguration(Properties configuration) {
		this.configuration = configuration;
	}
	
	public String getRatingsFile() {
		return ratingsFile;
	}

	public void setRatingsFile(String ratingsFile) {
		this.ratingsFile = ratingsFile;
	}

}
