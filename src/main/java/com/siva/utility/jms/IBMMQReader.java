package com.siva.utility.jms;

import java.io.File;
import java.io.IOException;
import java.util.Properties;

import org.apache.commons.io.FileUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.support.AbstractApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.stereotype.Component;

import com.ibm.mq.MQException;
import com.ibm.msg.client.wmq.compat.base.internal.MQC;
import com.ibm.msg.client.wmq.compat.base.internal.MQEnvironment;
import com.ibm.msg.client.wmq.compat.base.internal.MQGetMessageOptions;
import com.ibm.msg.client.wmq.compat.base.internal.MQMessage;
import com.ibm.msg.client.wmq.compat.base.internal.MQQueue;
import com.ibm.msg.client.wmq.compat.base.internal.MQQueueManager;
import com.siva.commons.Constants;
import com.siva.utility.SettingsManager;

/**
 * This is used to read all the messages from a MQ queue.
 * 
 * @author sksees1
 *
 */
@Component
public class IBMMQReader {
	private static String[] ENV = new String[] { "DEV_US_NDM_1" };

	public static void main(String[] args) {
		AbstractApplicationContext context = null;
		try {
			context = new ClassPathXmlApplicationContext(
					"spring/base-application-context.xml");
			IBMMQReader mqReader = context.getBean(IBMMQReader.class);

			System.out
					.println("********************************************************************");
			for (String environment : ENV) {
				mqReader.process(environment, QueueType.BO);
				System.out
						.println("********************************************************************");
			}
		} catch (Exception exception) {
			exception.printStackTrace();
		} finally {
			context.close();
		}
	}

	@Autowired
	private SettingsManager settingsManager;

	private static final Logger LOGGER = LoggerFactory
			.getLogger(IBMMQReader.class);

	private static final String EXTENSION = ".xml";

	/**
	 * Method to process a given request.
	 * 
	 * @param environment
	 * @param queueType
	 * @throws Exception
	 */
	@SuppressWarnings("unchecked")
	public void process(String environment, QueueType queueType)
			throws Exception {

		Properties applicationSettings = settingsManager
				.getApplicationSettings();

		File destinationFolder = new File(StringUtils.join(applicationSettings
				.getProperty(Constants.MQ_DESTINATION_FOLDER),
				Constants.SLASH_DELIMITER, queueType.getType().toLowerCase()));

		String mqHost = applicationSettings.getProperty(StringUtils.join(
				environment, Constants.MQ_HOST));

		String mqPort = applicationSettings.getProperty(StringUtils.join(
				environment, Constants.MQ_PORT));

		String mqChannel = applicationSettings.getProperty(StringUtils.join(
				environment, Constants.MQ_CHANNEL));

		String mqQMgr = applicationSettings.getProperty(StringUtils.join(
				environment, Constants.MQ_QUEUEMGRNAME));

		String mqQueue = null;

		switch (queueType) {
		case BO:
			mqQueue = StringUtils.join(applicationSettings
					.getProperty(StringUtils.join(environment,
							Constants.MQ_QUEUENAME)), Constants.BO);
			break;
		case BAD:
			mqQueue = StringUtils.join(applicationSettings
					.getProperty(StringUtils.join(environment,
							Constants.MQ_QUEUENAME)), Constants.BAD);
			break;
		default:
			throw new Exception("Undefined Queue type");
		}

		MQQueueManager qMgr; // define a queue manager object

		// Set up MQSeries environment
		MQEnvironment.hostname = mqHost;
		MQEnvironment.port = Integer.valueOf(mqPort).intValue();
		MQEnvironment.channel = mqChannel;
		MQEnvironment.properties.put(MQC.TRANSPORT_PROPERTY,
				MQC.TRANSPORT_MQSERIES);
		qMgr = new MQQueueManager(mqQMgr);

		int openOptions = MQC.MQOO_INQUIRE + MQC.MQOO_FAIL_IF_QUIESCING
				+ MQC.MQOO_INPUT_SHARED;

		MQQueue destQueue = qMgr.accessQueue(mqQueue, openOptions, null, null,
				null);
		System.out.println("MQRead connected.\n");

		int depth = destQueue.getCurrentDepth();
		if (depth > 0) {
			if (!destinationFolder.exists() && !destinationFolder.isDirectory()) {
				destinationFolder.mkdirs();
			}
		}

		MQGetMessageOptions getOptions = new MQGetMessageOptions();
		getOptions.options = MQC.MQGMO_NO_WAIT + MQC.MQGMO_FAIL_IF_QUIESCING
				+ MQC.MQGMO_CONVERT;

		int count = 0;
		while (true) {
			MQMessage message = new MQMessage();
			try {
				destQueue.get(message, getOptions);
				byte[] b = new byte[message.getMessageLength()];
				message.readFully(b);

				FileUtils.writeByteArrayToFile(new File(destinationFolder,
						StringUtils.join(count, EXTENSION)), b);

				message.clearMessage();
			} catch (IOException e) {
				System.out.println("IOException during GET: " + e.getMessage());
				break;
			} catch (MQException e) {
				if (e.completionCode == 2
						&& e.reasonCode == MQException.MQRC_NO_MSG_AVAILABLE) {
					if (depth > 0) {
						System.out.println("All messages read.");
					}
				} else {
					System.out.println("GET Exception: " + e);
				}
				break;
			}
			count = count + 1;
		}
		destQueue.close();
		qMgr.disconnect();
	}

	/**
	 * Enumeration for queue type.
	 * 
	 * @author sksees1
	 *
	 */
	static enum QueueType {
		INPUT(StringUtils.EMPTY), BO("BO"), BAD("BAD");

		String type;

		public String getType() {
			return type;
		}

		QueueType(String type) {
			this.type = type;
		}
	}
}