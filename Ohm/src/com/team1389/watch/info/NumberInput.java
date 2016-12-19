package com.team1389.watch.info;

public class NumberInput extends InputWatchable<Double> {

	public NumberInput(String name, double defaultVal, ChangeHandler<Double> onChange) {
		super(name, defaultVal, onChange);
	}

}
