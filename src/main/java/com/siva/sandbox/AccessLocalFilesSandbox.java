package com.siva.sandbox;

import org.apache.commons.io.IOUtils;

public class AccessLocalFilesSandbox {
	public static void main(String[] args) {
		AccessLocalFilesSandbox clazz = new AccessLocalFilesSandbox();
		clazz.doSomething();
	}

	public void doSomething() {
		try {
			ClassLoader classLoader = getClass().getClassLoader();
			System.out.println(IOUtils.toString(classLoader.getResourceAsStream("data/datasource/data.txt")));
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}