package com.siva.tool;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.charset.Charset;
import java.util.UUID;

import org.apache.commons.io.FileUtils;
import org.apache.commons.io.LineIterator;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.support.AbstractApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.stereotype.Component;

import com.siva.commons.Constants;
import com.siva.utility.SettingsManager;

/**
 * This is the class used to generate data files from a given skeleton template
 * and data.
 * 
 * @author sksees1
 *
 */
@Component
public class TemplateDataGenerator {
	public static void main(String[] args) {
		AbstractApplicationContext context = null;
		try {
			context = new ClassPathXmlApplicationContext(
					"spring/base-application-context.xml");
			TemplateDataGenerator templateDataGenerator = context
					.getBean(TemplateDataGenerator.class);
			templateDataGenerator.process();
		} catch (Exception exception) {
			exception.printStackTrace();
		} finally {
			context.close();
		}
	}

	private static final Logger LOGGER = LoggerFactory
			.getLogger(TemplateDataGenerator.class);

	private static final String EXTENSION = ".xml";

	@Autowired
	private SettingsManager settingsManager;

	/**
	 * Method to process the given request.
	 * 
	 * @throws FileNotFoundException
	 * @throws IOException
	 */
	public void process() throws FileNotFoundException, IOException {
		File sourceFile = new File((String) settingsManager
				.getApplicationSettings().get(Constants.TEMPLATE_DATA_FILE));

		File destinationFolder = new File((String) settingsManager
				.getApplicationSettings().get(Constants.TEMPLATE_OUTPUT_FOLDER));

		File templateFile = new File((String) settingsManager
				.getApplicationSettings().get(Constants.TEMPLATE_SKELETON_FILE));

		System.out
				.println("File that is being scanned for generating the receive event is "
						+ sourceFile.getAbsolutePath());

		LineIterator lt = FileUtils.lineIterator(sourceFile);
		String templateString = FileUtils.readFileToString(templateFile,
				Charset.defaultCharset());

		int count = 0;
		while (lt.hasNext()) {
			String dataElement = (String) lt.next();

			if (StringUtils.isNotBlank(StringUtils.trim(dataElement))) {
				String[] inputElements = StringUtils.split(
						StringUtils.trim(dataElement),
						Constants.COMMA_DELIMITER);

				String[] replacementArray = new String[inputElements.length];
				for (int i = 0; i < inputElements.length; i++) {
					replacementArray[i] = Constants.CHECK + (i + 1);
				}

				String finalData = StringUtils.replace(StringUtils.replaceEach(
						templateString, replacementArray, inputElements),
						Constants.EVENTID, String.valueOf(UUID.randomUUID()));

				String fileName = count + EXTENSION;
				FileUtils.writeStringToFile(new File(destinationFolder,
						fileName), finalData, Charset.defaultCharset(), false);

				LOGGER.info("Successfully written file : " + fileName
						+ " ,Current count is : " + count);
				count = count + 1;
			}
		}
	}
}