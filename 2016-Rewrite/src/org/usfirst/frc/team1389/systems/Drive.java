package org.usfirst.frc.team1389.systems;

import com.team1389.drive.DriveOut;
import com.team1389.hardware.outputs.hardware.CANTalonGroup;
import com.team1389.hardware.value_types.Percent;

public class Drive {
	public static Drive instance = new Drive();

	public static Drive getInstance() {
		return instance;
	}

	CANTalonGroup left;
	CANTalonGroup right;
	DriveOut<Percent> openLoop = new DriveOut<Percent>(left.getVoltageOutput(), right.getVoltageOutput());
	
}
