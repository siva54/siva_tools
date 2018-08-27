package com.siva.sandbox;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class ListHandlingJava8Sandbox {
	public static void main(String[] args) {
		List<Demon> demonList = new ArrayList<Demon>();
		demonList.add(new Demon("fire", "Diablo", 10));
		demonList.add(new Demon("fire", "Diablo", 10));
		demonList.add(new Demon("cold", "Baal", 10));

		System.out.println(demonList.parallelStream().collect(Collectors.groupingBy(demon -> ((Demon) demon).getType(),
				Collectors.mapping(Demon::getName, Collectors.toSet()))));

		List<Map<String, Object>> listData = new ArrayList<>();
		Map<String, Object> data1 = new HashMap<>();
		data1.put("1", "abc");
		data1.put("2", 1);
		listData.add(data1);

		Map<String, Object> data2 = new HashMap<>();
		data2.put("1", "abc");
		data2.put("2", 2);
		listData.add(data2);

		System.out.println(
				listData.stream().collect(Collectors.groupingBy(element -> ((Map<String, Object>) element).get("1"),
						Collectors.mapping(element -> ((Map<String, Object>) element).get("2"), Collectors.toSet()))));
	}

	static class Demon {
		private String type;
		private String name;
		private int power;

		public Demon(String type, String name, int power) {
			super();
			this.type = type;
			this.name = name;
			this.power = power;
		}

		@Override
		public String toString() {
			return getType() + "|" + getName() + "|" + getPower();
		}

		/**
		 * @return the type
		 */
		public String getType() {
			return type;
		}

		/**
		 * @param type
		 *            the type to set
		 */
		public void setType(String type) {
			this.type = type;
		}

		/**
		 * @return the name
		 */
		public String getName() {
			return name;
		}

		/**
		 * @param name
		 *            the name to set
		 */
		public void setName(String name) {
			this.name = name;
		}

		/**
		 * @return the power
		 */
		public int getPower() {
			return power;
		}

		/**
		 * @param power
		 *            the power to set
		 */
		public void setPower(int power) {
			this.power = power;
		}
	}
}
