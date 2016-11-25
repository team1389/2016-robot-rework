package com.team1389.hardware.outputs.interfaces;

import com.team1389.hardware.util.RangeUtil;

public interface ScalarOutput {
	public void set(double val);
	public static ScalarOutput mapToRange(ScalarOutput out,double inMin,double inMax,double outMin,double outMax){
		return (double val)->{out.set(RangeUtil.map(val, inMin, inMax, outMin, outMax));};
	}
	public static ScalarOutput invert(ScalarOutput out){
		return (double val)->{out.set(-val);};
	}
	public static ScalarOutput applyDeadband(ScalarOutput out,double deadband){
		return (double val)->{out.set(RangeUtil.applyDeadband(val, deadband));};
	}
	public static ScalarOutput limitRange(ScalarOutput out,double limit){
		return (double val) -> {
			out.set(RangeUtil.limit(val, limit));
		};
	}

}
