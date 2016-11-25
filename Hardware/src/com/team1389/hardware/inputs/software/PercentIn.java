package com.team1389.hardware.inputs.software;

import com.team1389.hardware.inputs.interfaces.ScalarInput;

/**
 * An input that gives a value from -1 to 1
 * 
 * @author Jacob Prinz
 */
public class PercentIn extends RangeIn{
	public PercentIn(ScalarInput val){
		super(val,-1,1);
	}
	public PercentIn(RangeIn in){
		this(ScalarInput.mapToRange(in.input, -1d, 1d, in.min, in.max));
	}
	public PercentIn applyDeadband(double deadband) {
		input=ScalarInput.applyDeadband(input, deadband);
		return this;
	}

	public PercentIn limitRange(double limit) {
		input=ScalarInput.limitRange(input, limit);
		return this;
	}
}
