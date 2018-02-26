package com.siva.sandbox;

import java.io.BufferedReader;
import java.io.File;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.xml.stream.XMLInputFactory;
import javax.xml.stream.XMLStreamConstants;
import javax.xml.stream.XMLStreamReader;

import org.apache.commons.httpclient.methods.PostMethod;
import org.apache.commons.io.FileUtils;
import org.apache.commons.lang3.StringUtils;

public class StreamingHTTPResponse {

	public static final String GIR_URL = "https://girservice.qa.walmart.com/"
			+ "girservice/globalitemrules/locationrules/v1/us/Sams";
	public static final String LOCAL_URL = "http://localhost:8080/"
			+ "girservice/globalitemrules/locationrules/v1/us/Sams";
	public static final String URL = "https://www.w3schools.com/xml/simple.xml";
	public static final String URL_AGE = "http://girtest.getsandbox.com/age";

	public static final String NDM_CAPABILITY_SERVICE = "http://stage-glintful-disp-node.wal-mart.com/fulfillment-node-dispensing-manager/resource/capabilities";

	/**
	 * Stream with SAX parser implementation.
	 */
	public static void main(String[] args) {

		PostMethod post = null;
		PostMethod post1 = null;
		try {
			long startTime = System.currentTimeMillis();

			// /**
			// * HTTP Post implementation using Apache Commons HTTPClient.
			// */
			// post = new PostMethod(LOCAL_URL);
			// RequestEntity entity = new FileRequestEntity(
			// new File("C:\\temp_dir\\huge_request.xml"),
			// "application/xml; charset=ISO-8859-1");
			// post.setRequestEntity(entity);
			//
			// /**
			// * HTTP Post implementation using Apache Commons HTTPClient.
			// */
			// post1 = new PostMethod(LOCAL_URL);
			// post1.setRequestEntity(entity);
			//
			// HttpClient httpclient = new HttpClient();
			// int result = httpclient.executeMethod(post);
			// // Display status code
			// System.out.println("Response status code: " + result);
			// // Display response
			// System.out.println("Response body: ");
			// // System.out.println(post.getResponseBodyAsString());
			//
			// InputStream inputStream = post.getResponseBodyAsStream();
			// System.out.println("First request:"
			// + (System.currentTimeMillis() - startTime));
			//
			// result = httpclient.executeMethod(post1);
			//
			// inputStream = post1.getResponseBodyAsStream();
			// System.out.println("Second request: "
			// + (System.currentTimeMillis() - startTime));
			//
			// byte[] data = new byte[1024];

			// IOUtils.read(inputStream, data);
			// System.out.println("DIng:"+
			// StringUtils.toString(data, Charset.defaultCharset().toString()));

			URL url = new URL(GIR_URL);
			HttpURLConnection urlConnection = (HttpURLConnection) url
					.openConnection();
			urlConnection.setDoInput(true);
			urlConnection.setDoOutput(true);
			urlConnection.setUseCaches(false);
			urlConnection.setRequestProperty("Connection", "close");
			urlConnection.setRequestMethod("POST");
			urlConnection.setRequestProperty("Content-Type", "application/xml");
			System.setProperty("http.keepAlive", "false");
			OutputStream outputStream = urlConnection.getOutputStream();
			outputStream.write(FileUtils.readFileToByteArray(
					new File("C:\\temp_dir\\huge_request.xml")));
			InputStream inputStream = urlConnection.getInputStream();

			System.out.println("*********************************************");
			// System.out.println(
			// "Status Response : " + urlConnection.getResponseCode());
			System.out.println("*********************************************");

			// /**
			// * Simply print data from stream.
			// */
			// System.out.println(IOUtils.readLines(inputStream,
			// Charset.defaultCharset()));

			// InputStream inputStream = new FileInputStream(new
			// File("C:\\temp_dir\\mixed.xml"));

			BufferedReader br = new BufferedReader(
					new InputStreamReader(inputStream), 512);
			XMLInputFactory factory = XMLInputFactory.newInstance();
			XMLStreamReader reader = factory.createXMLStreamReader(br);

			String[] applicableElements = new String[]{"upc", "ActionType",
					"name", "value"};
			List<String> applicableElementsList = Arrays
					.asList(applicableElements);

			String currentElement = null;
			String currentUPC = null;
			String metadataKey = null;
			String metadataValue = null;
			String currentRestriction = null;

			Set<String> applicableRestrictions = new HashSet<>();
			Map<String, Object> metaDataMap = new HashMap<>();

			while (reader.hasNext()) {
				int event = reader.next();
				switch (event) {
					case XMLStreamConstants.START_ELEMENT :
						// System.out.println(reader.getLocalName());
						currentElement = reader.getLocalName();
						break;
					case XMLStreamConstants.CHARACTERS :
						if (applicableElementsList.contains(currentElement)) {
							if (currentElement.equals("upc")) {
								currentUPC = reader.getText();
							}

							if (currentElement.equals("ActionType")) {
								currentRestriction = reader.getText();
								applicableRestrictions.add(currentRestriction);
							}

							if (currentElement.equals("name")) {
								metadataKey = reader.getText();
							}

							if (currentElement.equals("value")) {
								metadataValue = StringUtils
										.trim(reader.getText());
								metaDataMap.put(metadataKey, metadataValue);
							}

							// System.out.println(
							// "This is some data : " +
							// StringUtils.trim(reader.getText()) + " : End of
							// Data");
						}
						break;
					case XMLStreamConstants.END_ELEMENT :
						// System.out.println("/" + reader.getLocalName());
						if (reader.getLocalName().equals("lineItem")) {
							if (currentUPC != null && !metaDataMap.isEmpty()) {
								// Dump the data of the previous UPC element
								// TODO : Instead take the Map and process it to
								// DTO.
								System.out.println("################");
								System.out.println("UPC : " + currentUPC);
								System.out.println("Applicable Restrictions : "
										+ applicableRestrictions);
								System.out.println("Metadata : " + metaDataMap);

								currentUPC = null;
								applicableRestrictions.clear();
								metaDataMap.clear();
							}
						}
						currentElement = null;
						break;
				}
			}

			System.out.println("*********************************************");
			long endTime = System.currentTimeMillis();
			System.out.println("Total time taken : " + (endTime - startTime));
		} catch (Exception exception) {
			exception.printStackTrace();
		} finally {
			// post.releaseConnection();
		}
	}

	// /**
	// * Stream with Buffer and DOM Parser implementation
	// */
	// public static void main(String[] args) {
	// try {
	// URL oracle = new URL(URL_AGE);
	// URLConnection yc = oracle.openConnection();
	// InputStream inputStream = yc.getInputStream();
	//
	// // byte[] data = new byte[128];
	//
	// // BufferedInputStream bis = new BufferedInputStream(inputStream);
	//
	// BufferedReader br = new BufferedReader(new
	// InputStreamReader(inputStream));
	//
	// char[] data = new char[32];
	//
	// while (true) {
	// int result = br.read(data);
	// System.out.println("***********************************************************************");
	// System.out.println(data);
	// System.out.println("***********************************************************************");
	// if (result == -1) {
	// break;
	// }
	// }
	//
	// } catch (Exception exception) {
	// exception.printStackTrace();
	// }
	// }
}