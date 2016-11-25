package com.team1389.hardware.control;

import com.team1389.hardware.inputs.software.RangeIn;

import edu.wpi.first.wpilibj.PIDSource;
import edu.wpi.first.wpilibj.PIDSourceType;

public class PIDPositionInput implements PIDSource{
	RangeIn sensor;
	
	public PIDPositionInput(RangeIn sensor) {
		this.sensor = sensor;
	}

	@Override
	public void setPIDSourceType(PIDSourceType pidSource) {
		if (!pidSource.equals(PIDSourceType.kDisplacement)){
			throw new UnsupportedOperationException("PIDPosition Input is not able to become anything besides a position input");
		}
	}

	@Override
	public PIDSourceType getPIDSourceType() {
		return PIDSourceType.kDisplacement;
	}

	@Override
	public double pidGet() {
		return sensor.get();
	}

}
