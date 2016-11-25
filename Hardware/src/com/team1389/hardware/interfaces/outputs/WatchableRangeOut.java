package com.team1389.hardware.interfaces.outputs;

import com.team1389.hardware.watch.Info;
import com.team1389.hardware.watch.NumberInfo;
import com.team1389.hardware.watch.Watchable;

public class WatchableRangeOut extends RangeOut implements Watchable{
	private String name;
	double val;
	//TODO could be a bug here when you do some action to a watchable rangeOut it loses its watchableNess so be careful
	public WatchableRangeOut(ScalarOutput output, double min,double max,String name){
		super(output,min,max);
		this.name=name;
	}
	@Override
	public void set(double val){
		super.set(val);
		this.val=val;
	}
	public WatchableRangeOut(RangeOut in,String name){
		this(in.output,in.min,in.max,name);
	}
	@Override
	public String getName() {
		return name;
	}

	@Override
	public Info[] getInfo() {
		return new Info[]{
				new NumberInfo(getName(),()->{return val;})
		};
	}
}
