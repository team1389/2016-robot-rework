package com.team1389.hardware.outputs.software;

import com.team1389.hardware.outputs.interfaces.ScalarOutput;
import com.team1389.hardware.valueTypes.Angle;
import com.team1389.hardware.valueTypes.Value;
import com.team1389.watch.Info;
import com.team1389.watch.NumberInfo;
import com.team1389.watch.Watchable;

public class WatchableRangeOut<T extends Value> extends RangeOut<T> implements Watchable {
	private String name;
	double val;

	// TODO could be a bug here when you do some action to a watchable rangeOut
	// it loses its watchableNess so be careful
	public WatchableRangeOut(ScalarOutput<T> output, double min, double max, String name) {
		super(output, min, max);
		this.name = name;
	}

	@Override
	public void set(double val) {
		super.set(val);
		this.val = val;
	}

	public WatchableRangeOut(RangeOut<T> in, String name) {
		this(in.output, in.min, in.max, name);
	}

	@Override
	public String getName() {
		return name;
	}
	public WatchableRangeOut<T> mapToRange(double min, double max) {	
		super.mapToRange(min, max);
		return this;
	}

	public WatchableRangeOut<Angle> mapToAngle() {
		return new WatchableRangeOut<Angle>(super.mapToAngle(), name);
	}

	public WatchableRangeOut<T> invert() {
		super.invert();
		return this;
	}

	public WatchableRangeOut<T> getProfiledOut(double maxChange) {
		super.getProfiledOut(maxChange);
		return this;
	}

	public WatchableRangeOut<T> addFollowers(RangeOut<T> outFollow) {
		super.addFollowers(outFollow);
		return this;
	}

	public WatchableRangeOut<T> scale(double factor) {
		super.scale(factor);
		return this;
	}

	@Override
	public Info[] getInfo() {
		return new Info[] { new NumberInfo(getName(), () -> {
			return val;
		}) };
	}
}
