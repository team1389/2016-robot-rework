package com.team1389.control;

import com.team1389.hardware.inputs.software.RangeIn;
import com.team1389.hardware.value_types.Speed;

import edu.wpi.first.wpilibj.PIDSource;
import edu.wpi.first.wpilibj.PIDSourceType;

public class PIDSpeedInput implements PIDSource{
	RangeIn<Speed> sensor;
	
	public PIDSpeedInput(RangeIn<Speed> sensor) {
		this.sensor = sensor;
	}

	@Override
	public void setPIDSourceType(PIDSourceType pidSource) {
		if (!pidSource.equals(PIDSourceType.kRate)){
			throw new UnsupportedOperationException("PIDSpeedInput is not able to become anything besides a speed input");
		}
	}

	@Override
	public PIDSourceType getPIDSourceType() {
		return PIDSourceType.kRate;
	}

	@Override
	public double pidGet() {
		return sensor.get();
	}

}
