package com.siva.sandbox;

import java.io.File;
import java.io.IOException;

import org.apache.commons.io.FileUtils;

public class Generator {
	public static void main(String[] args) {
		try {
			doProcess1();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	//
	// private static void doProcess2() throws IOException {
	//
	// String data = FileUtils.readFileToString(new File(
	// "src/main/resources/data/templates/template_uods.xml"));
	//
	// for (int i = 0; i < 1000; i++) {
	// FileUtils.write(new File(
	// "src/main/resources/data/datasource/output/super.txt"),
	// data + "\n", true);
	// System.out.println("Counter : " + i);
	// }
	// }

	private static void doProcess1() throws IOException {
		for (int i = 10000000; i < 20000000; i++) {
			FileUtils.write(new File("src/main/resources/data/datasource/data.txt"), i + "\n", true);
		}
	}
}
