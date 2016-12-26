package com.team1389.util;

import java.util.Optional;
import java.util.function.Function;

public class OptionalUtil {
	public static <T, V> Function<Optional<T>, V> ifPresent(V defaultVal, Function<T, V> fun) {
		return (Optional<T> in) -> {
			if (in.isPresent()) {
				return fun.apply(in.get());
			} else {
				return defaultVal;
			}
		};
	}
}
