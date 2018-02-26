package com.siva.sandbox;

import java.util.ArrayList;
import java.util.List;
import java.util.function.BiFunction;
import java.util.stream.Collectors;

public class Java8 {
	public static void main(String[] args) {
		BiFunction<Integer, String, Demon> demonSupplier = new BiFunction<Integer, String, Demon>() {
			@Override
			public Demon apply(Integer t, String u) {
				return new Demon(t, u);
			}
		};
		
		List<Demon> demonList = new ArrayList<>();
		demonList.add(demonSupplier.apply(10, "Goblin"));
		demonList.add(demonSupplier.apply(20, "Orc"));
		demonList.add(demonSupplier.apply(30, "Naga"));
		demonList.add(demonSupplier.apply(40, "Berserker"));
		demonList.add(demonSupplier.apply(40, "Excutioner"));

		List<String> strongestDemons = demonList.parallelStream()
				.filter(p -> p.getStrength() > 20).map(Demon::getName)
				.collect(Collectors.toList());

		System.out.println(strongestDemons);

		demonList.parallelStream().filter(p -> p.getStrength() > 20)
				.map(demon -> demon.getName().toUpperCase())
				.forEach(System.out::println);

		demonList.parallelStream().filter(p -> p.name.equals("Orc1"))
				.findFirst().map(Demon::getStrength)
				.ifPresent(System.out::println);

		demonList.parallelStream().min((p1, p2) -> p1.strength - p2.strength)
				.map(Demon::getName).ifPresent(System.out::println);

		System.out.println(demonList.parallelStream()
				.allMatch(demon -> demon.getStrength() > 100));

	}

	static class Demon {
		private int strength;
		private String name;
		private String element;

		public Demon(int strength, String name) {
			super();
			this.strength = strength;
			this.name = name;
			this.element = "Normal";
		}

		public Demon(int strength, String name, String element) {
			super();
			this.strength = strength;
			this.name = name;
			this.element = element;
		}

		/**
		 * @return the strength
		 */
		public int getStrength() {
			return strength;
		}

		/**
		 * @param strength
		 *            the strength to set
		 */
		public void setStrength(int strength) {
			this.strength = strength;
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
		 * @return the element
		 */
		public String getElement() {
			return element;
		}

		/**
		 * @param element
		 *            the element to set
		 */
		public void setElement(String element) {
			this.element = element;
		}
	}
}