package com.team1389.hardware.interfaces.inputs;

import com.team1389.hardware.interfaces.ScalarValue;
import com.team1389.hardware.watch.Info;
import com.team1389.hardware.watch.NumberInfo;
import com.team1389.hardware.watch.Watchable;

public class WatchableRangeIn extends RangeIn implements Watchable{
	private String name;
	public WatchableRangeIn(ScalarValue val, double min,double max,String name){
		super(val,min,max);
		this.name=name;
	}
	@Override
	public String getName() {
		return name;
	}

	@Override
	public Info[] getInfo() {
		return new Info[] { new NumberInfo(getName(), () -> {
			return get();
		}) };
	}
}
