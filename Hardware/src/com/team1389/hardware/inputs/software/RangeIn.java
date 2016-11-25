package com.team1389.hardware.inputs.software;

import com.team1389.hardware.inputs.interfaces.ScalarInput;

public class RangeIn {
	protected ScalarInput input;
	protected double max,min;
	public RangeIn(ScalarInput val,double min,double max){
		this.input=val;
		this.min=min;
		this.max=max;
	}
	public RangeIn(double min,double max){
		this(()->{return 0.0;},min,max);
	}
	
	public double get(){
		return input.get();
	}

	public double min(){
		return min;
	}

	public double max(){
		return max;
	}

	public RangeIn mapToRange(double min, double max) {
		input=ScalarInput.mapToRange(input, this.min, this.max, min, max);
		this.min=min;
		this.max=max;
		return this;
	}
	public PercentIn mapToPercentIn(){
		return new PercentIn(this);
	}
	public WatchableRangeIn getWatchable(String name){
		return new WatchableRangeIn(this,name);
	}

	public RangeIn invert() {
		input=ScalarInput.invert(input); 
		return this;
	}

}
