package com.team1389.hardware.interfaces.inputs;

import com.team1389.hardware.interfaces.ScalarValue;
import com.team1389.hardware.util.RangeUtil;

/**
 * An input that gives a value from -1 to 1
 * 
 * @author Jacob Prinz
 */
public class PercentIn extends RangeIn{
	public PercentIn(ScalarValue val){
		super(val,-1,1);
	}

	public PercentIn applyDeadband(double deadband) {
		return new PercentIn(() -> {
			return RangeUtil.applyDeadband(this.val.get(), deadband);
		});
	}

	public PercentIn limitRange(double limit) {
		return new PercentIn(() -> {
			return RangeUtil.limit(this.val.get(), limit);
		});
	}
}
