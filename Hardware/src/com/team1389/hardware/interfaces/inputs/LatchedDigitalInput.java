package com.team1389.hardware.interfaces.inputs;

import com.team1389.hardware.util.LatchedBoolean;

public class LatchedDigitalInput extends DigitalInput{
	LatchedBoolean latchedIn;
	public LatchedDigitalInput(BooleanSource in) {
		super(in);
		latchedIn=new LatchedBoolean();
	}

	@Override
	public boolean get() {
		return latchedIn.get(in.get());
	}
	
}
