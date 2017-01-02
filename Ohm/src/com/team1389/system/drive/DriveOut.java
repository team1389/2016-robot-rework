package com.team1389.system.drive;

import com.team1389.hardware.outputs.software.RangeOut;
import com.team1389.hardware.value_types.Value;
import com.team1389.util.AddList;
import com.team1389.util.DriveSignal;
import com.team1389.watch.CompositeWatchable;
import com.team1389.watch.Watchable;

/**
 * A stream combining two Range streams, intended to represent the left and right wheels of a drivetrain
 * 
 * @author amind
 *
 * @param <T> the range type of the wheel streams
 */
public class DriveOut<T extends Value> implements CompositeWatchable {
	RangeOut<T> left;
	RangeOut<T> right;

	/**
	 * @param left the left wheel stream
	 * @param right the right wheel stream
	 */
	public DriveOut(RangeOut<T> left, RangeOut<T> right) {
		this.left = left;
		this.right = right;
	}

	/**
	 * @param sig this values to apply to each stream
	 */
	public void set(DriveSignal sig) {
		this.set(sig.leftMotor, sig.rightMotor);
	}

	/**
	 * @param leftSig the value to apply to the left stream
	 * @param rightSig the value to apply to the right stream
	 */
	public void set(double leftSig, double rightSig) {
		this.left.set(leftSig);
		this.right.set(rightSig);
	}

	/**
	 * sets left and right values to 0
	 */
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
