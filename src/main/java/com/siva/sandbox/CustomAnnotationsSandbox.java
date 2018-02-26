package com.siva.sandbox;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import java.util.Set;

import org.reflections.Reflections;

public class CustomAnnotationsSandbox {

	public static void main(String[] args) {
		Class<?> clazz = DingClass.class;

		System.out.println(clazz.getAnnotations());
		System.out.println(clazz.isAnnotationPresent(Ding.class));

		Reflections reflections = new Reflections("com.siva.sandbox");

		Set<Class<? extends Object>> allClasses = reflections
				.getSubTypesOf(Object.class);
		
		System.out.println(allClasses.size());

		for (Class<? extends Object> class1 : allClasses) {

			System.out.println(class1.getCanonicalName());
			
			if (class1.isAnnotationPresent(Ding.class)) {
				System.out.println(class1.getCanonicalName());
			}
		}

	}

	@Ding
	public class DingClass {
		@DingMethod
		public void doSomething() {
			System.out.println("Hello from DingClass");
		}
	}

	@Retention(RetentionPolicy.RUNTIME)
	@Target(ElementType.TYPE)
	public @interface Ding {
	}

	@Retention(RetentionPolicy.RUNTIME)
	@Target(ElementType.METHOD)
	public @interface DingMethod {

	}
}
