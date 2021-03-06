package com.siva.utility.mq;

import java.util.Properties;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.support.AbstractApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.stereotype.Component;

import com.ibm.msg.client.wmq.compat.base.internal.MQC;
import com.ibm.msg.client.wmq.compat.base.internal.MQEnvironment;
import com.ibm.msg.client.wmq.compat.base.internal.MQQueue;
import com.ibm.msg.client.wmq.compat.base.internal.MQQueueManager;
import com.siva.commons.Constants;
import com.siva.utility.SettingsManager;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;

/**
 * This is used to inquire for any depth in BO or BAD queues.
 * 
 * @author sksees1
 *
 */
@Component
@Slf4j
public class IBMMQInquirer {
	private static String[] ENV = new String[] { "PROD_US_NDM_1", "PROD_US_NDM_2", "PROD_UK_NDM_1", "PROD_UK_NDM_2",
			"PROD_CA_NDM_1", "PROD_CA_NDM_2", "PROD_USUK_HODM_1", "PROD_USUK_HODM_2", "PROD_CA_HODM_1",
			"PROD_CA_HODM_2" };

	@Autowired
	private SettingsManager settingsManager;

	public static void main(String[] args) {
		AbstractApplicationContext context = null;
		try {
			context = new ClassPathXmlApplicationContext("spring/base-application-context.xml");
			IBMMQInquirer mqInquirer = context.getBean(IBMMQInquirer.class);

			log.info("********************************************************************");
			for (String environment : ENV) {
				mqInquirer.process(environment, QueueType.INPUT);
				mqInquirer.process(environment, QueueType.BO);
				mqInquirer.process(environment, QueueType.BAD);
				log.info("********************************************************************");
			}
		} catch (Exception exception) {
			exception.printStackTrace();
		} finally {
			context.close();
		}
	}

	/**
	 * Method to process a given request.
	 * 
	 * @param environment
	 * @param queueType
	 * @throws Exception
	 */
	public void process(String environment, QueueType queueType) throws Exception {
		Properties applicationSettings = settingsManager.getApplicationSettings();
		String mqHost = applicationSettings.getProperty(StringUtils.join(environment, Constants.MQ_HOST));
		String mqPort = applicationSettings.getProperty(StringUtils.join(environment, Constants.MQ_PORT));
		String mqChannel = applicationSettings.getProperty(StringUtils.join(environment, Constants.MQ_CHANNEL));
		String mqQMgr = applicationSettings.getProperty(StringUtils.join(environment, Constants.MQ_QUEUEMGRNAME));
		String mqQueue = null;

		switch (queueType) {
		case INPUT:
			mqQueue = StringUtils
					.join(applicationSettings.getProperty(StringUtils.join(environment, Constants.MQ_QUEUENAME)));
			break;
		case BO:
			mqQueue = StringUtils.join(
					applicationSettings.getProperty(StringUtils.join(environment, Constants.MQ_QUEUENAME)),
					Constants.BO);
			break;
		case BAD:
			mqQueue = StringUtils.join(
					applicationSettings.getProperty(StringUtils.join(environment, Constants.MQ_QUEUENAME)),
					Constants.BAD);
			break;
		default:
			throw new Exception("Undefined Queue type");
		}

		MQQueueManager qMgr; // define a queue manager object

		// Set up MQSeries environment
		MQEnvironment.hostname = mqHost;
		MQEnvironment.port = Integer.valueOf(mqPort).intValue();
		MQEnvironment.channel = mqChannel;
		MQEnvironment.properties.put(MQC.TRANSPORT_PROPERTY, MQC.TRANSPORT_MQSERIES);
		qMgr = new MQQueueManager(mqQMgr);
		int openOptions = MQC.MQOO_INQUIRE;
		MQQueue destQueue = qMgr.accessQueue(mqQueue, openOptions);
		Integer depth = destQueue.getCurrentDepth();

		log.info("Depth for Queue : " + mqQueue + " under queue manager : " + mqQMgr + " is : " + depth);

		destQueue.close();
		qMgr.disconnect();
	}

	/**
	 * Enumeration for queue type.
	 * 
	 * @author sksees1
	 *
	 */
	@AllArgsConstructor
	static enum QueueType {
		INPUT(StringUtils.EMPTY), BO("BO"), BAD("BAD");

		@Setter
		@Getter
		String type;
	}
}