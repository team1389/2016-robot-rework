package com.team1389.hardware.inputs.interfaces;

import java.util.function.BooleanSupplier;

import com.team1389.util.ToggleBoolean;

public class ToggledBooleanSupplier implements BooleanSupplier {
	ToggleBoolean toggleIn;
	BooleanSupplier input;

	public ToggledBooleanSupplier(BooleanSupplier input) {
		toggleIn = new ToggleBoolean();
		this.input = input;
	}

	@Override
	public boolean getAsBoolean() {
		return toggleIn.get(input.getAsBoolean());
	}

}
