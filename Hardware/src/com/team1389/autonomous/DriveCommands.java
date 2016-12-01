package com.team1389.autonomous;

import com.team1389.commands.CommandUtil;
import com.team1389.commands.FollowProfileCommand;
import com.team1389.commands.command_base.Command;
import com.team1389.control.PIDConfiguration;
import com.team1389.control.PIDSystemCreator;
import com.team1389.control.pid_wrappers.input.PIDRangeSource;
import com.team1389.control.pid_wrappers.output.PIDControlledRange;
import com.team1389.hardware.inputs.software.RangeIn;
import com.team1389.hardware.outputs.software.RangeOut;
import com.team1389.hardware.value_types.Angle;
import com.team1389.hardware.value_types.Position;
import com.team1389.hardware.value_types.Speed;
import com.team1389.motion_profile.TrapezoidalMotionProfile;

import edu.wpi.first.wpilibj.PIDController;
import edu.wpi.first.wpilibj.PIDOutput;
import edu.wpi.first.wpilibj.PIDSource;

public class DriveCommands {
	static final double inchesToMeters = .0245;
	double wheelCircumference; // meters
	double maxAcceleration;
	double topSpeed;

	public DriveCommands(double wheelDiameter, PIDConfiguration turnPID, double maxAcceleration, double topSpeed) {
		this.wheelCircumference = wheelDiameter * Math.PI * inchesToMeters;
		this.topSpeed = topSpeed;
		this.maxAcceleration = maxAcceleration;
	}

	public Command driveMetersCommand(double meters, RangeOut<Position> left, RangeOut<Position> right,
			RangeIn<Position> leftIn, RangeIn<Position> rightIn) {

		left.mapToRange(0, 1).scale(wheelCircumference);
		right.mapToRange(0, 1).scale(wheelCircumference);
		leftIn.mapToRange(0, 1).scale(wheelCircumference);
		rightIn.mapToRange(0, 1).scale(wheelCircumference);
		TrapezoidalMotionProfile profile = new TrapezoidalMotionProfile(meters, maxAcceleration, maxAcceleration,
				topSpeed);
		return CommandUtil.combineSimultaneous(new FollowProfileCommand(profile, left, leftIn),
				new FollowProfileCommand(profile, right, rightIn));
	}

	public Command turnAngleCommand(double angle, double tolerance, RangeIn<Angle> gyro, RangeOut<Speed> left,
			RangeOut<Speed> right, PIDConfiguration turnPID) {
		PIDOutput wheels = PIDSystemCreator.combine(new PIDControlledRange<Speed>(left),
				new PIDControlledRange<Speed>(right.invert()));
		PIDSource gyroSource = new PIDRangeSource<Angle>(gyro);
		PIDController pid = PIDSystemCreator.makeController(turnPID, gyroSource, wheels);
		return new Command() {
			@Override
			public void initialize() {
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
