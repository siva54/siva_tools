package com.siva.sandbox;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import org.springframework.util.CollectionUtils;

import com.fasterxml.jackson.core.JsonFactory;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonToken;

public class StreamJSONParser {
	public static void main(String[] args) {
		try {
			BufferedReader br = new BufferedReader(
					new InputStreamReader(new FileInputStream(new File(
							"src/main/resources/data/sample/json/gir_item_restriction.json"))));

			String currentElement = null;
			String currentUPC = null;
			String metadataKey = null;
			String metadataValue = null;
			String currentRestriction = null;

			Set<String> applicableRestrictions = new HashSet<>();
			Map<String, String> metaDataMap = new HashMap<>();

			JsonFactory jsonFactory = new JsonFactory();
			JsonParser jsonParser = jsonFactory.createParser(br);
			while (true) {
				JsonToken token = jsonParser.nextToken();
				currentElement = jsonParser.getCurrentName();
				
				System.out.println(currentElement);
				
				if ("upc".equals(currentElement)) {
					jsonParser.nextToken();
					currentUPC = jsonParser.getText();
				} else if ("ActionType".equals(currentElement)) {
					jsonParser.nextToken();
					currentRestriction = jsonParser.getText();

					// For items with no restrictions, The data will be sent as
					// null
					if (!"null".equals(currentRestriction)) {
						applicableRestrictions.add(currentRestriction);
					}
				} else if ("name".equals(currentElement)) {
					jsonParser.nextToken();
					metadataKey = jsonParser.getText();
				} else if ("value".equals(currentElement)) {
					jsonParser.nextToken();
					metadataValue = jsonParser.getText();
					metaDataMap.put(metadataKey, metadataValue);
				} else if ("decision".equals(currentElement)) {
					// TODO : Restrictions can be calculated here.
					if (!CollectionUtils.isEmpty(applicableRestrictions)) {
//						System.out.println("################");
//						System.out.println("UPC : " + currentUPC);
//						System.out.println("Applicable Restrictions : "
//								+ applicableRestrictions);
//						System.out.println("Metadata : " + metaDataMap);

						currentUPC = null;
						applicableRestrictions.clear();
						metaDataMap.clear();
					}
				}
				if (token == null) {
					break;
				}
			}

			jsonParser.close();

			// JsonReader reader = new JsonReader(br);
			// reader.beginObject();
			//
			// JsonToken token = null;
			// String currentElement = null;
			// String currentUPC = null;
			// String metadataKey = null;
			// String metadataValue = null;
			// String currentRestriction = null;

			// Set<String> applicableRestrictions = new HashSet<>();
			// Map<String, String> metaDataMap = new HashMap<>();
			//
			// while (reader.hasNext()) {
			// token = reader.peek();
			// if (token != JsonToken.NULL) {
			// if (token == JsonToken.NAME) {
			// currentElement = reader.nextName();
			// // If its a line item, parse through it
			// if (currentElement.equalsIgnoreCase("lineItem")) {
			// reader.beginArray();
			// while (reader.hasNext()) {
			// token = reader.peek();
			// if (token == JsonToken.BEGIN_OBJECT) {
			// reader.beginObject();
			// while (reader.hasNext()) {
			// if (token == JsonToken.NAME) {
			// currentElement = reader.nextName();
			// // Retrieve current UPC
			// if (currentElement
			// .equalsIgnoreCase("upc")) {
			// currentUPC = reader
			// .nextString();
			// } else if (currentElement
			// .equalsIgnoreCase(
			// "restrictionAction")) {
			// reader.beginArray();
			// while (reader.hasNext()) {
			// token = reader.peek();
			// if (token == JsonToken.BEGIN_OBJECT) {
			// reader.beginObject();
			// while (reader
			// .hasNext()) {
			// token = reader
			// .peek();
			// if (token == JsonToken.NAME) {
			// currentElement = reader
			// .nextName();
			// if (currentElement
			// .equalsIgnoreCase(
			// "ActionType")) {
			// currentRestriction = reader
			// .nextString();
			// }
			// }
			// }
			// }
			// }
			// }
			// }
			// }
			// }
			// }
			// }
			// }
			// }
			// }

			// while (reader.hasNext()) {
			// JsonToken token = reader.peek();
			// if (token != JsonToken.NULL) {
			// String name = reader.nextName();
			// if (name.equalsIgnoreCase("lineItem")) {
			// reader.beginArray();
			// while (reader.hasNext()) {
			// JsonToken token1 = reader.peek();
			//
			// if (token1 == JsonToken.BEGIN_OBJECT) {
			// reader.beginObject();
			// while (reader.hasNext()) {
			// name = reader.nextName();
			// if (name.equalsIgnoreCase("upc")) {
			// System.out.println(reader.nextString());
			// } else {
			// token1 = reader.peek();
			// if (token1 == JsonToken.BEGIN_ARRAY
			// || token1 == JsonToken.END_ARRAY
			// || token1 == JsonToken.NULL) {
			// reader.skipValue();
			// } else {
			// reader.nextName();
			// }
			// }
			// }
			// reader.endObject();
			// }
			// }
			// reader.endArray();
			// }
			// } else {
			// reader.skipValue();
			// }
			// }

			// reader.endObject();
			// reader.close();

		} catch (Exception exception) {
			exception.printStackTrace();
		}
	}
}