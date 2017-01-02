package com.team1389.system.drive;

import com.team1389.control.SynchronousPID;
import com.team1389.hardware.inputs.software.AngleIn;
import com.team1389.hardware.value_types.Speed;
import com.team1389.trajectory.Rotation2d;

/**
 * a driveSystem that directs the robot towards a {@link VelocityHeadingSetpoint}
 * 
 * @author amind
 */
public class VelocityHeadingSystem {
	// TODO figure out how to use this drive algorithm
	protected DriveOut<Speed> drive;
	private AngleIn gyro;

	private double mLastHeadingErrorDegrees;
	private VelocityHeadingSetpoint velocityHeadingSetpoint_;
	private SynchronousPID velocityHeadingPid_;

	/**
	 * @param drive a speed controlled drive stream
	 * @param gyro a stream of robot heading
	 */
	public VelocityHeadingSystem(DriveOut<Speed> drive, AngleIn gyro) {
		this.drive = drive;
		this.gyro = gyro;
	}

	/**
	 * 
	 */
	public void update() {
		Rotation2d actualGyroAngle = Rotation2d.fromDegrees(gyro.get());

		mLastHeadingErrorDegrees = velocityHeadingSetpoint_.getHeading().rotateBy(actualGyroAngle.inverse())
				.getDegrees();

		double deltaSpeed = velocityHeadingPid_.calculate(mLastHeadingErrorDegrees);
		drive.set(velocityHeadingSetpoint_.getLeftSpeed() + deltaSpeed / 2,
				velocityHeadingSetpoint_.getRightSpeed() - deltaSpeed / 2);
	}

	/**
	 * sets the {@link VelocityHeadingSetpoint} of this driveSystem. assumes that the setpoint desires straight line motion
	 * 
	 * @param forward_inches_per_sec the desired wheel speed (applied to both wheels)
	 * @param headingSetpoint the desired heading
	 */
	public void setVelocityHeadingSetpoint(double forward_inches_per_sec, Rotation2d headingSetpoint) {
		velocityHeadingPid_.reset();
		velocityHeadingSetpoint_ = new VelocityHeadingSetpoint(forward_inches_per_sec, forward_inches_per_sec,
				headingSetpoint);
	}
}
