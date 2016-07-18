package com.siva.tools.tool;

import java.io.File;

import org.apache.commons.io.FileUtils;

public class CreateDummyFile {
	public static void main(String[] args) {

		try {
			for (int i = 0; i < 100000; i++) {
				FileUtils.write(new File("C:\\Test\\input\\file_" + i + ".txt"), "Count " + i);
				System.out.println("Created File : " + i);
			}
		} catch (Exception exception) {
			exception.printStackTrace();
		}
	}
}
