//package com.siva.functions;
//
//import java.util.Objects;
//
//@FunctionalInterface
//public interface TriFunction<T, R, K, Z> {
//
//	Z apply(T t, R r, K k, Z z);
//
//	default <V> TriFunction<V, R, Z, T> compose(
//			TriFunction<? super V, ? super R, ? super Z, ? extends T> before) {
//		Objects.requireNonNull(before);
//		return (V v) -> apply(before.apply(v));
//	}
//
//	default <V> TriFunction<V, R, Z, T> andThen(
//			TriFunction<? super R, ? extends V> after) {
//		Objects.requireNonNull(after);
//		return (T t) -> after.apply(apply(t));
//	}
//
//	static <T> TriFunction<V, R, Z, T> identity() {
//		return t -> t;
//	}
//}