package com.team1389.util;

import java.util.Optional;
import java.util.function.BiConsumer;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Supplier;

public class OptionalUtil {
	public static <T, V> Supplier<V> ifPresent(V defaultVal, Optional<T> in, Function<T, V> fun) {
		if (in.isPresent()) {
			return () -> {
				return fun.apply(in.get());
			};
		} else {
			return () -> defaultVal;
		}
	}

	public static <T, V> Consumer<V> ifPresent(Optional<T> in, BiConsumer<T, V> fun) {
		if (in.isPresent()) {
			return (V val) -> {
				fun.accept(in.get(), val);
			};
		} else {
			return (V val) -> {
			};
		}
	}
}
