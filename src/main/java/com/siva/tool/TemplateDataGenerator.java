package com.siva.tool;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.charset.Charset;
import java.util.Spliterator;
import java.util.Spliterators;
import java.util.UUID;
import java.util.function.Consumer;
import java.util.stream.Stream;
import java.util.stream.StreamSupport;

import org.apache.commons.io.FileUtils;
import org.apache.commons.io.LineIterator;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.support.AbstractApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.stereotype.Component;

import com.siva.commons.Constants;
import com.siva.utility.SettingsManager;

import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;

/**
 * This is the class used to generate data files from a given skeleton template
 * and data.
 * 
 * @author sksees1
 *
 */
@Component
@Slf4j
public class TemplateDataGenerator {
	private static final String EXTENSION = ".xml";

	@Getter
	@Setter
	@Autowired
	SettingsManager settingsManager;

	public static void main(String[] args) {
		AbstractApplicationContext context = null;
		try {
			context = new ClassPathXmlApplicationContext("spring/base-application-context.xml");
			TemplateDataGenerator templateDataGenerator = context.getBean(TemplateDataGenerator.class);
			templateDataGenerator.process(
					(String) templateDataGenerator.getSettingsManager().getApplicationSettings()
							.get(Constants.TEMPLATE_SKELETON_FILE),
					(String) templateDataGenerator.getSettingsManager().getApplicationSettings()
							.get(Constants.TEMPLATE_DATA_FILE),
					"/Users/s0s022s/output5/input", TemplateGenerationStrategy.SINGLE_FILE);
		} catch (Exception exception) {
			log.error("Exception while running template data generator.", exception);
		} finally {
			context.close();
		}
	}

	/**
	 * Method to process the given request.
	 * 
	 * @throws FileNotFoundException
	 * @throws IOException
	 */
	public void process(String template, String dataSource, String destinationDirectory,
			TemplateGenerationStrategy strategy) throws FileNotFoundException, IOException {
		File sourceFile = new File(dataSource);
		File templateFile = new File(template);
		File destinationFolder = new File(destinationDirectory);

		log.info("File that is being scanned for generating the receive event is " + sourceFile.getAbsolutePath());

		LineIterator lt = FileUtils.lineIterator(sourceFile);
		final String templateString = FileUtils.readFileToString(templateFile, Charset.defaultCharset());

		Stream<String> dataStream = StreamSupport.stream(Spliterators.spliteratorUnknownSize(lt, Spliterator.ORDERED),
				true);

		dataStream.forEach(new Consumer<String>() {
			int fileCount = 0;
			int count = 0;

			@Override
			public void accept(String dataElement) {
				if (StringUtils.isNotBlank(StringUtils.trim(dataElement))) {
					String[] inputElements = StringUtils.split(StringUtils.trim(dataElement),
							Constants.COMMA_DELIMITER);

					String[] replacementArray = new String[inputElements.length];
					for (int i = 0; i < inputElements.length; i++) {
						replacementArray[i] = Constants.CHECK + (i + 1);
					}

					String finalData = StringUtils.replace(
							StringUtils.replaceEach(templateString, replacementArray, inputElements), Constants.EVENTID,
							String.valueOf(UUID.randomUUID()));

					if (strategy == TemplateGenerationStrategy.MULTIPLE_FILE) {
						fileCount = fileCount + 1;
					} else {
						finalData = "\n" + finalData;
					}

					String fileName = fileCount + EXTENSION;
					try {
						FileUtils.writeStringToFile(new File(destinationFolder, fileName), finalData,
								Charset.defaultCharset(), true);
					} catch (IOException e) {
						throw new RuntimeException(e);
					}

					++count;
					log.info("Successfully written file : " + fileName + " ,Current count is : " + count);
				}
			}
		});
	}

	static enum TemplateGenerationStrategy {
		SINGLE_FILE, MULTIPLE_FILE;
	}
}