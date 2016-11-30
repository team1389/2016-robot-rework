package com.team1389.hardware.inputs.software;

import com.team1389.hardware.inputs.interfaces.BooleanSource;
import com.team1389.util.ToggleBoolean;

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
