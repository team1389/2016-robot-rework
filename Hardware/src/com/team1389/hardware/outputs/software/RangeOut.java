package com.team1389.hardware.outputs.software;

import com.team1389.hardware.outputs.interfaces.ScalarOutput;
import com.team1389.hardware.valueTypes.Value;

public class RangeOut<T extends Value> {
	protected ScalarOutput<T> output;
	protected double min,max;
	public RangeOut(ScalarOutput<T> out,double min,double max){
		this.output=out;
		this.min=min;
		this.max=max;
	}
	public void set(double val){
		output.set(val);
	}

	public double min(){
		return min;
	}

	public double max(){
		return max;
	}

	public RangeOut<T> mapToRange(double min, double max) {
		this.output=ScalarOutput.mapToRange(output,min,max,this.min,this.max);
		this.min=min;
		this.max=max;
		return this;
	}
	public WatchableRangeOut<T> getWatchable(String name){
		return new WatchableRangeOut<T>(this,name);
	}

	public PercentOut mapToPercentOut() {
		return new PercentOut(this);
	}
	public RangeOut<T> invert(){
		this.output=ScalarOutput.invert(output);
		return this;
	}
	public RangeOut<T> getProfiledOut(double maxChange){
		output=new ProfiledRangeOut<T>(output,min,max,maxChange);
		return this;
	}
}
