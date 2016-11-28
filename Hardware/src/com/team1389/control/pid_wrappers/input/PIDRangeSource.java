package com.team1389.control.pid_wrappers.input;

import com.team1389.hardware.inputs.software.RangeIn;
import com.team1389.hardware.valueTypes.Value;

import edu.wpi.first.wpilibj.PIDSource;
import edu.wpi.first.wpilibj.PIDSourceType;

public class PIDRangeSource<T extends Value> implements PIDSource {
	RangeIn<T> input;

	public PIDRangeSource(RangeIn<T> input) {
		this.input = input;
	}

	@Override
	public void setPIDSourceType(PIDSourceType pidSource) {

	}

	@Override
	public PIDSourceType getPIDSourceType() {
		switch (input.type.getName()) {
		case "Position":
		case "Angle":
			return PIDSourceType.kDisplacement;
		case "Speed":
			return PIDSourceType.kRate;
		default:
			return PIDSourceType.kDisplacement;

		}
	}

	@Override
	public double pidGet() {
		return input.get();
	}

}
