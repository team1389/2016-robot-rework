package com.team1389.hardware.inputs.software;

import com.team1389.hardware.inputs.interfaces.ScalarInput;
import com.team1389.hardware.valueTypes.Value;

public class RangeIn<T extends Value> {
	protected ScalarInput<T> input;
	protected double max,min;
	public RangeIn(ScalarInput<T> val,double min,double max){
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

	public RangeIn<T> mapToRange(double min, double max) {
		input=ScalarInput.mapToRange(input, this.min, this.max, min, max);
		this.min=min;
		this.max=max;
		return this;
	}
	public RangeIn<T> addChangeListener(Runnable onChange){
		input=ScalarInput.getListeningInput(input, onChange);
		return this;
	}
	public PercentIn mapToPercentIn(){
		return new PercentIn(this);
	}
	public WatchableRangeIn<T> getWatchable(String name){
		return new WatchableRangeIn<T>(this,name);
	}

	public RangeIn<T> invert() {
		input=ScalarInput.invert(input); 
		return this;
	}

}
