package com.team1389.drive;

import com.team1389.hardware.outputs.software.RangeOut;
import com.team1389.hardware.value_types.Value;
import com.team1389.util.DriveSignal;

public class DriveOut<T extends Value> {
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
}
