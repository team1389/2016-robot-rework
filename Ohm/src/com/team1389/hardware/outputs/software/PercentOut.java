package com.team1389.hardware.outputs.software;

import com.team1389.hardware.outputs.interfaces.ScalarOutput;
import com.team1389.hardware.value_types.Percent;

/**
 * An input that gives a value from -1 to 1
 * 
 * @author Ari Mindell
 */
public class PercentOut extends RangeOut<Percent> {
	public PercentOut(ScalarOutput<Percent> out) {
		super(out, -1, 1);
	}

	public PercentOut(RangeOut<?> out) {
		this(ScalarOutput.mapToPercent(out.output, out.min, out.max));
	}
}
