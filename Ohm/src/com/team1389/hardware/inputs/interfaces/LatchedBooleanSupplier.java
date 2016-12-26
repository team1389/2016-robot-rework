package com.team1389.hardware.inputs.interfaces;

import java.util.function.BooleanSupplier;

import com.team1389.util.LatchedBoolean;

public class LatchedBooleanSupplier implements BooleanSupplier {
	LatchedBoolean latchedIn;
	BooleanSupplier input;

	public LatchedBooleanSupplier(BooleanSupplier input) {
		latchedIn = new LatchedBoolean();
		this.input = input;
	}

	@Override
	public boolean getAsBoolean() {
		return latchedIn.get(input.getAsBoolean());
	}

}
