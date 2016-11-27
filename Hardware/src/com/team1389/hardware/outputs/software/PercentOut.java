package com.team1389.hardware.outputs.software;

import com.team1389.hardware.outputs.interfaces.ScalarOutput;

/**
 * An input that gives a value from -1 to 1
 * 
 * @author Ari Mindell
 */
public class PercentOut extends RangeOut{
	public PercentOut(ScalarOutput out){
		super(out,-1,1);
	}
	public PercentOut(RangeOut out){
		this(ScalarOutput.mapToRange(out.output, -1d, 1d, out.min, out.max));
	}
	public PercentOut applyDeadband(double deadband) {
		output=ScalarOutput.applyDeadband(output, deadband);
		return this;
	}

	public PercentOut limitRange(double limit) {
		output=ScalarOutput.limitRange(output,limit);
		return this;
	}
}
