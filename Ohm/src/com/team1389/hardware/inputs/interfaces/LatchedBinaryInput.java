package com.team1389.hardware.inputs.interfaces;

import com.team1389.util.LatchedBoolean;

public class LatchedBinaryInput implements BinaryInput {
	LatchedBoolean latchedIn;
	BinaryInput input;

	public LatchedBinaryInput(BinaryInput input) {
		latchedIn = new LatchedBoolean();
		this.input = input;
	}

	@Override
	public Boolean get() {
		return latchedIn.get(input.get());
	}

}
