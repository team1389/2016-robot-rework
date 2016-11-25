package com.team1389.hardware.interfaces.inputs;

import com.team1389.hardware.util.RangeUtil;

public interface ScalarInput {
	public double get();
	
	public static ScalarInput mapToRange(ScalarInput in,double inMin,double inMax,double outMin,double outMax){
		return ()->{return RangeUtil.map(in.get(), inMin, inMax, outMin, outMax);};
	}
	public static ScalarInput invert(ScalarInput in){
		return ()->{return(-in.get());};
	}
	public static ScalarInput applyDeadband(ScalarInput in,double deadband){
		return ()->{return RangeUtil.applyDeadband(in.get(), deadband);};
	}
	public static ScalarInput limitRange(ScalarInput in,double limit){
		return () -> {
			return(RangeUtil.limit(in.get(), limit));
		};
	}
}
