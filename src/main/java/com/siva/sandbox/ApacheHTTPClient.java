package com.siva.sandbox;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;

import javax.xml.stream.XMLInputFactory;
import javax.xml.stream.XMLStreamConstants;
import javax.xml.stream.XMLStreamReader;

public class ApacheHTTPClient {
	public static final String URL = "https://www.w3schools.com/xml/simple.xml";

	// public final static void main(String[] args) throws Exception {
	// CloseableHttpClient httpclient = HttpClients.createDefault();
	// try {
	// HttpGet httpget = new HttpGet(URL);
	//
	// System.out.println("Executing request " + httpget.getRequestLine());
	//
	// // Create a custom response handler
	// ResponseHandler<String> responseHandler = new ResponseHandler<String>() {
	//
	// @Override
	// public String handleResponse(final HttpResponse response) throws
	// ClientProtocolException, IOException {
	// int status = response.getStatusLine().getStatusCode();
	// if (status >= 200 && status < 300) {
	// HttpEntity entity = response.getEntity();
	//
	// System.out.println(entity.isStreaming());
	//
	// return entity != null ? EntityUtils.toString(entity) : null;
	// } else {
	// throw new ClientProtocolException("Unexpected response status: " + status);
	// }
	// }
	//
	// };
	// String responseBody = httpclient.execute(httpget, responseHandler);
	// System.out.println("----------------------------------------");
	// System.out.println(responseBody);
	// } finally {
	// httpclient.close();
	// }
	// }

	public final static void main(String[] args) throws Exception {
		URL oracle = new URL(URL);
		URLConnection yc = oracle.openConnection();
		InputStream inputStream = yc.getInputStream();
		BufferedReader in = new BufferedReader(new InputStreamReader(inputStream));

		//
		// String inputLine;
		// while ((inputLine = in.readLine()) != null)
		// System.out.println(inputLine);
		// in.close();

		// CloseableHttpClient httpclient = HttpClients.createDefault();
		// try {
		// HttpGet httpget = new HttpGet(URL);
		//
		// System.out.println("Executing request " + httpget.getRequestLine());
		//
		// // Create a custom response handler
		// ResponseHandler<InputStream> responseHandler = new
		// ResponseHandler<InputStream>() {
		//
		// @Override
		// public InputStream handleResponse(final HttpResponse response)
		// throws ClientProtocolException, IOException {
		// int status = response.getStatusLine().getStatusCode();
		// if (status >= 200 && status < 300) {
		// HttpEntity entity = response.getEntity();
		// // System.out.println(entity.isStreaming());
		// // System.out.println(entity.isRepeatable());
		// // System.out.println(EntityUtils.toString(entity));
		// return entity != null ? entity.getContent() : null;
		// } else {
		// throw new ClientProtocolException("Unexpected response status: " + status);
		// }
		// }
		// };
		// InputStream inputSteam = httpclient.execute(httpget, responseHandler);

		XMLInputFactory factory = XMLInputFactory.newInstance();
		XMLStreamReader reader = factory.createXMLStreamReader(inputStream);
		while (reader.hasNext()) {
			int event = reader.next();
			switch (event) {
			case XMLStreamConstants.START_ELEMENT:
				System.out.println(reader.getLocalName());
				System.out.println(reader.getAttributeCount());
				if (reader.hasText()) {
					System.out.println(reader.getText());
				}
				break;
			}
		}

		// System.out.println("----------------------------------------");
		// } catch (Exception exception) {
		// exception.printStackTrace();
		// } finally {
		// httpclient.close();
		// }
	}
}
