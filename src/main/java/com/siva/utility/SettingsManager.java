package com.siva.utility;

import java.util.Properties;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.support.AbstractApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.stereotype.Component;

import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;

/**
 * This is the class that is used to retrieve the settings.
 * 
 * @author sksees1
 *
 */
@Component
@Slf4j
public class SettingsManager {
	@Autowired
	@Qualifier(value = "applicationSettings")
	@Getter
	@Setter
	private Properties applicationSettings;

	public static void main(String[] args) {
		AbstractApplicationContext context = null;
		try {
			context = new ClassPathXmlApplicationContext("spring/base-application-context.xml");
			SettingsManager settingsManager = context.getBean(SettingsManager.class);
			log.info(String.valueOf(settingsManager.getApplicationSettings()));
			log.info(String.valueOf(settingsManager.getApplicationSettings()));
		} catch (Exception exception) {
			log.error("Exception occurred while acquiring settingsmanager", exception);
		} finally {
			context.close();
		}
	}
}