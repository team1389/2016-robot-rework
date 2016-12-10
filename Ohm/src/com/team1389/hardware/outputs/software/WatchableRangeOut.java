package com.team1389.hardware.outputs.software;

import com.team1389.hardware.outputs.interfaces.ScalarOutput;
import com.team1389.hardware.value_types.Value;
import com.team1389.watch.Info;
import com.team1389.watch.NumberInfo;
import com.team1389.watch.Watchable;

public class WatchableRangeOut<T extends Value> extends RangeOut<T> implements Watchable {
	private String name;
	double val;

	public WatchableRangeOut(ScalarOutput<T> output, double min, double max, String name) {
		super(null, min, max);
		this.output = (double val) -> {
			this.val = val;
			output.set(val);
		};
		this.name = name;
	}

	public WatchableRangeOut(RangeOut<T> in, String name) {
		this(in.output, in.min, in.max, name);
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
