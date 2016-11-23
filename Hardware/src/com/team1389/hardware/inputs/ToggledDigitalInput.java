package com.team1389.hardware.inputs;

import com.team1389.hardware.interfaces.inputs.BooleanSource;
import com.team1389.hardware.util.ToggleBoolean;

public class ToggledDigitalInput extends DigitalInput {
	ToggleBoolean toggleIn;
	public ToggledDigitalInput(BooleanSource in) {
		this(in,null);
	}

	public ToggledDigitalInput(BooleanSource in, String name) {
		super(in,name);
		toggleIn=new ToggleBoolean();
	}
	@Override
	public boolean get() {
		return toggleIn.get(in.get());
	}

}
