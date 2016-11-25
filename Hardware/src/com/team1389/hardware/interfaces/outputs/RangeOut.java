package com.team1389.hardware.interfaces.outputs;

import com.team1389.hardware.interfaces.inputs.WatchableRangeIn;

public class RangeOut {
	protected ScalarOutput output;
	protected double min,max;
	public RangeOut(ScalarOutput out,double min,double max){
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

	public RangeOut mapToRange(double min, double max) {
		this.output=ScalarOutput.mapToRange(output,min,max,this.min,this.max);
		this.min=min;
		this.max=max;
		return this;
	}
	public WatchableRangeOut getWatchable(String name){
		return new WatchableRangeOut(this,name);
	}

	public PercentOut mapToPercentOut() {
		return new PercentOut(this);
	}
	public RangeOut invert(){
		this.output=ScalarOutput.invert(output);
		return this;
	}
	public RangeOut getProfiledOut(double maxChange){
		output=new ProfiledRangeOut(output,min,max,maxChange);
		return this;
	}
	
	public static void main(String[] args){
		RangeOut orig=new RangeOut((double set)->{System.out.println(set);},0,8192);
		
		orig.invert().mapToRange(0d,360d).set(45);
	}

}
