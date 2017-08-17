package com.siva.sandbox;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.SeekableByteChannel;
import java.nio.file.Files;
import java.nio.file.Paths;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.support.AbstractApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.stereotype.Component;

import com.siva.utility.SettingsManager;

/**
 * This is used to read a large file and process data.
 * 
 * @author sksees1
 *
 */
@Component
public class ProcessLargeDataSandbox {
	public static void main(String[] args) {
		AbstractApplicationContext context = null;
		try {
			context = new ClassPathXmlApplicationContext(
					"spring/base-application-context.xml");
			ProcessLargeDataSandbox dataProcessor = context
					.getBean(ProcessLargeDataSandbox.class);

			dataProcessor.process();
		} catch (Exception exception) {
			exception.printStackTrace();
		} finally {
			context.close();
		}
	}

	private static final Logger LOGGER = LoggerFactory
			.getLogger(ProcessLargeDataSandbox.class);

	@Autowired
	SettingsManager settingsManager;

	public void process() {
		try (SeekableByteChannel ch = Files.newByteChannel(Paths
				.get("test.txt"))) {
			ByteBuffer bb = ByteBuffer.allocateDirect(1000);
			for (;;) {
				StringBuilder line = new StringBuilder();
				int n = ch.read(bb);
				
				
			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}