package com.team1389.watch.info;

import java.util.function.Consumer;

public class NumberInput extends InputWatchable<Double> {

	public NumberInput(String name, double defaultVal, Consumer<Double> onChange) {
		super(name, defaultVal, onChange);
	}

}
