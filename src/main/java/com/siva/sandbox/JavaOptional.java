package com.siva.sandbox;

import java.util.Optional;

public class JavaOptional {
	public static void main(String[] args) {
		Optional<String> data = Optional.of(null);
		data.ifPresent(element -> System.out.println(element));
	}
}