package com.team1389.hardware.inputs.software;

import com.team1389.hardware.inputs.interfaces.ScalarInput;
import com.team1389.hardware.value_types.Value;
import com.team1389.watch.Info;
import com.team1389.watch.NumberInfo;
import com.team1389.watch.Watchable;

public class WatchableRangeIn<T extends Value> extends RangeIn<T> implements Watchable {
	private String name;
	double val;

	protected WatchableRangeIn(Class<T> type, ScalarInput<T> input, double min, double max, String name) {
		super(type, null, min, max);
		this.input = () -> {
			this.val = input.get();
			return input.get();
		};
		this.name = name;
	}

	protected WatchableRangeIn(Class<T> type, RangeIn<T> in, String name) {
		this(type, in.input, in.min, in.max, name);
	}
	public WatchableRangeIn<T> mapToRange(double min, double max) {
		super.mapToRange(min, max);
		return this;
	}
	public RangeIn<T> addChangeListener(Runnable onChange) {
		super.addChangeListener(onChange);
		return this;
	}

	public WatchableRangeIn<T> invert() {
		super.invert();
		return this;
	}

	public RangeIn<T> scale(double factor) {
		super.scale(factor);
		return this;
	}

	@Override
	public String getName() {
		return name;
	}

	@Override
	public Info[] getInfo() {
		return new Info[] { new NumberInfo(getName(), () -> {
			return val;
		}) };
	}
}
