package com.siva.sandbox;

import java.util.LinkedHashMap;
import java.util.Map;

public class DataGenerator {
	// private static final int ORDER_RANGE_MIN = 200000;
	// private static final int ORDER_RANGE_MAX = 300000;
	// private static final int ORDER_COUNT = 15000;
	//
	// public static void main(String[] args) {
	// SortedSet<Long> orderNumberSet = new TreeSet<>();
	// while (orderNumberSet.size() < ORDER_COUNT) {
	// Long data = (long) (Math.random() * ORDER_RANGE_MAX);
	// if (data > ORDER_RANGE_MIN) {
	// orderNumberSet.add(data);
	// }
	// }
	// System.out.println(orderNumberSet);
	// }

	private static final int DATA_COUNT = 20;
	private static final int DATA_RANGE_MIN = 20000000;
	private static final int DATA_RANGE_MAX = 30000000;

	public static void main(String[] args) {
		Map<Integer, Long> finalData = new LinkedHashMap<>();

		int count = 1;

		do {
			Long data = (long) (Math.random() * DATA_RANGE_MAX);
			if (data > DATA_RANGE_MIN) {
				finalData.put(count, data);
				count = count + 1;
			}
		} while (finalData.size() < DATA_COUNT);

		System.out.println(finalData);
	}
}