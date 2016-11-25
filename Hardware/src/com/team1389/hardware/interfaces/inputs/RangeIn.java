package com.team1389.hardware.interfaces.inputs;

import com.team1389.hardware.interfaces.ScalarValue;
import com.team1389.hardware.util.RangeUtil;

public class RangeIn {
	protected ScalarValue val;
	protected double max,min;
	public RangeIn(ScalarValue val,double min,double max){
		this.val=val;
		this.min=min;
		this.max=max;
	}
	public RangeIn(double min,double max){
		this(()->{return 0.0;},min,max);
	}
	public double get(){
		return val.get();
	}

	public double min(){
		return min;
	}

	public double max(){
		return max;
	}

	public RangeIn mapToRange(double min, double max) {
		return new RangeIn(()->{return RangeUtil.map(this.val.get(), this.min(), this.max(), min, max);},min,max);
	}
	public PercentIn mapToPercentIn(){
		RangeIn percentRange=this.mapToRange(-1,1);
		return new PercentIn(()->{
			return percentRange.val.get();
		});
	}

	public RangeIn invert() {
		return new RangeIn(()->{return -val.get();},min,max); 
	}

}
