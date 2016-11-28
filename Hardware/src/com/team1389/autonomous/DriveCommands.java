package com.team1389.autonomous;

import com.team1389.commands.Command;
import com.team1389.commands.FollowProfileCommand;
import com.team1389.control.PIDConfiguration;
import com.team1389.control.PIDSystemCreator;
import com.team1389.control.pid_wrappers.input.PIDRangeSource;
import com.team1389.control.pid_wrappers.output.PIDControlledRange;
import com.team1389.hardware.inputs.software.RangeIn;
import com.team1389.hardware.outputs.software.RangeOut;
import com.team1389.hardware.valueTypes.Angle;
import com.team1389.hardware.valueTypes.Position;
import com.team1389.hardware.valueTypes.Speed;
import com.team1389.motion_profile.TrapezoidalMotionProfile;

import edu.wpi.first.wpilibj.PIDController;
import edu.wpi.first.wpilibj.PIDOutput;
import edu.wpi.first.wpilibj.PIDSource;

public class DriveCommands {
	static final double inchesToMeters = .0245;
	double wheelCircumference; // meters
	double maxAcceleration;
	double topSpeed;
	PIDConfiguration turnPID;

	public DriveCommands(double wheelDiameter, PIDConfiguration turnPID) {
		this.wheelCircumference = wheelDiameter * Math.PI * inchesToMeters;
	}

	public Command driveMetersCommand(double meters, RangeOut<Position> bothWheels) {
		RangeOut<Position> encoderTicksToMeters = bothWheels.mapToRange(0, 1).scale(wheelCircumference);
		return new FollowProfileCommand(
				new TrapezoidalMotionProfile(meters, maxAcceleration, maxAcceleration, topSpeed), encoderTicksToMeters);
	}

	public Command turnAngleCommand(double angle, double tolerance, RangeIn<Angle> gyro, RangeOut<Speed> left,
			RangeOut<Speed> right) {
		PIDOutput wheels = PIDSystemCreator.combine(new PIDControlledRange<Speed>(left),
				new PIDControlledRange<Speed>(right.invert()));
		PIDSource gyroSource = new PIDRangeSource<Angle>(gyro);
		PIDController pid = PIDSystemCreator.makeController(turnPID, gyroSource, wheels);
		return new Command() {
			@Override
			public void init() {
				pid.setSetpoint(angle);
				pid.setAbsoluteTolerance(tolerance);
				pid.enable();
			}

			@Override
			public boolean execute() {
				return pid.onTarget();
			}

		};
	}
}
