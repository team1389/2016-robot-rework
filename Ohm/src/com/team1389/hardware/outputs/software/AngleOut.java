package com.team1389.hardware.outputs.software;

import com.team1389.hardware.outputs.interfaces.ScalarOutput;
import com.team1389.hardware.value_types.Angle;

public class AngleOut extends RangeOut<Angle> {

	public AngleOut(ScalarOutput<Angle> out) {
		super(out, 0, 360);
	}

	public AngleOut(RangeOut<?> out) {
		this(ScalarOutput.mapToAngle(out.output, out.min, out.max));
	}

}
