package com.siva.utility;

import java.util.Properties;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.support.AbstractApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.stereotype.Component;

/**
 * This is the class that is used to retrieve the settings.
 * 
 * @author sksees1
 *
 */
@Component
public class SettingsManager {

	public static void main(String[] args) {
		AbstractApplicationContext context = null;
		try {
			context = new ClassPathXmlApplicationContext(
					"spring/base-application-context.xml");
			SettingsManager settingsManager = context
					.getBean(SettingsManager.class);
			LOGGER.info(String.valueOf(settingsManager.getApplicationSettings()));

			LOGGER.error(String.valueOf(settingsManager
					.getApplicationSettings()));
		} catch (Exception exception) {
			exception.printStackTrace();
		} finally {
			context.close();
		}
	}

	@Autowired
	@Qualifier(value = "applicationSettings")
	private Properties applicationSettings;
	
	private static final Logger LOGGER = LoggerFactory
			.getLogger(SettingsManager.class);

	/**
	 * Method to retrieve application settings.
	 * 
	 * @return
	 */
	public Properties getApplicationSettings() {
		return applicationSettings;
	}
}