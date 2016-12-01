package com.team1389.hardware.value_types;

import edu.wpi.first.wpilibj.PIDSourceType;

public abstract class PIDTunableValue extends Value{
	public abstract PIDSourceType getValueType();
}
