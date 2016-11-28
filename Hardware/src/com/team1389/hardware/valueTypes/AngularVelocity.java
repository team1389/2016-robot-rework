package com.team1389.hardware.valueTypes;

import edu.wpi.first.wpilibj.PIDSourceType;

public class AngularVelocity extends PIDTunableValue{

	@Override
	public PIDSourceType getValueType() {
		return PIDSourceType.kRate;
	}
	
}
