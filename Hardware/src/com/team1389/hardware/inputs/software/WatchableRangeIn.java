package com.team1389.hardware.inputs.software;

import com.team1389.hardware.inputs.interfaces.ScalarInput;
import com.team1389.hardware.valueTypes.Value;
import com.team1389.watch.Info;
import com.team1389.watch.NumberInfo;
import com.team1389.watch.Watchable;

public class WatchableRangeIn<T extends Value> extends RangeIn<T> implements Watchable{
	private String name;
	protected WatchableRangeIn(Class<T> type,ScalarInput<T> val, double min,double max,String name){
		super(type,val,min,max);
		this.name=name;
	}
	protected WatchableRangeIn(Class<T> type,RangeIn<T> in,String name){
		this(type,in.input,in.min,in.max,name);
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
