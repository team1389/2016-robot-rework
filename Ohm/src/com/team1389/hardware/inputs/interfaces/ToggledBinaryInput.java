package com.team1389.hardware.inputs.interfaces;

import com.team1389.util.ToggleBoolean;

public class ToggledBinaryInput implements BinaryInput {
	ToggleBoolean toggleIn;
	BinaryInput input;

	public ToggledBinaryInput(BinaryInput input) {
		toggleIn = new ToggleBoolean();
		this.input = input;
	}

	@Override
	public boolean get() {
		return toggleIn.get(input.get());
	}

}
