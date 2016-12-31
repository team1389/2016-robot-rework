package com.team1389.watch.info;

import java.util.function.Consumer;

public class BooleanInput extends InputWatchable<Boolean> {

	public BooleanInput(String name, double defaultVal, Consumer<Boolean> onChange) {
		super(name, defaultVal, onChange);
	}

}
