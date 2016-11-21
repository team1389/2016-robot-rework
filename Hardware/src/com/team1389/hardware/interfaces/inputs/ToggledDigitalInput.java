package com.team1389.hardware.interfaces.inputs;

import com.team1389.hardware.util.ToggleBoolean;

public class ToggledDigitalInput extends DigitalInput {
	ToggleBoolean ToggleIn;
	public ToggledDigitalInput(BooleanSource in) {
		super(in);
		ToggleIn=new ToggleBoolean();
	}

	@Override
	public boolean get() {
		return ToggleIn.get(in.get());
	}

}
