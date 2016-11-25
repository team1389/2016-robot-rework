package com.team1389.hardware.inputs.software;

import com.team1389.hardware.inputs.interfaces.BooleanSource;
import com.team1389.hardware.util.LatchedBoolean;

public class LatchedDigitalInput extends DigitalInput{
	LatchedBoolean latchedIn;
	public LatchedDigitalInput(BooleanSource in) {
		this(in,null);
	}

	public LatchedDigitalInput(BooleanSource in, String name) {
		super(in,name);
		latchedIn=new LatchedBoolean();
	}

	@Override
	public boolean get() {
		return latchedIn.get(in.get());
	}
	
}
