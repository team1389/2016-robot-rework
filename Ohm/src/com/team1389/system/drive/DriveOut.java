package com.team1389.system.drive;

import com.team1389.hardware.outputs.software.RangeOut;
import com.team1389.hardware.value_types.Value;
import com.team1389.util.AddList;
import com.team1389.util.DriveSignal;
import com.team1389.watch.CompositeWatchable;
import com.team1389.watch.Watchable;

public class DriveOut<T extends Value> implements CompositeWatchable {
	RangeOut<T> left;
	RangeOut<T> right;

	public DriveOut(RangeOut<T> left, RangeOut<T> right) {
		this.left = left;
		this.right = right;
	}

	public void set(DriveSignal sig) {
		this.set(sig.leftMotor, sig.rightMotor);
	}

	public void set(double leftSig, double rightSig) {
		this.left.set(leftSig);
		this.right.set(rightSig);
	}

	public void neutral() {
		this.set(0, 0);
	}

	@Override
	public String getName() {
		return "drive";
	}

	@Override
	public AddList<Watchable> getSubWatchables(AddList<Watchable> stem) {
		return stem.put(left.getWatchable("left"), right.getWatchable("right"));
	}
}
