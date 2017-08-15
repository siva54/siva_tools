package com.siva.utility;

import java.util.Properties;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.stereotype.Component;

@Component
public class SettingsManager {
	@Autowired
	@Qualifier(value = "applicationSettings")
	private Properties applicationSettings;
	
	public Properties getApplicationSettings() {
		return applicationSettings;
	}

	public static void main(String[] args) {
		ApplicationContext context = new ClassPathXmlApplicationContext(
				"spring/application-context.xml");
		SettingsManager settingsUtility = context
				.getBean(SettingsManager.class);

		System.out.println(settingsUtility.getApplicationSettings());
	}
}