package com.team1389.hardware.inputs.software;

import com.team1389.hardware.inputs.interfaces.ScalarInput;
import com.team1389.hardware.value_types.Angle;

public class AngleIn extends RangeIn<Angle> {

	public AngleIn(ScalarInput<Angle> val) {
		super(Angle.class, val, 0, 360);
	}

	public AngleIn(RangeIn<?> in) {
		this(ScalarInput.mapToAngle(in.input, in.min, in.max));
	}
}
