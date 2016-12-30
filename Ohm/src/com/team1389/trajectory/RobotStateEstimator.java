package com.team1389.trajectory;

import java.util.function.Supplier;

import com.team1389.hardware.inputs.software.AngleIn;
import com.team1389.hardware.inputs.software.RangeIn;
import com.team1389.hardware.value_types.Position;
import com.team1389.hardware.value_types.Speed;

import edu.wpi.first.wpilibj.Timer;

public class RobotStateEstimator implements Supplier<RigidTransform2d> {

	private RangeIn<Position> left;
	private RangeIn<Position> right;
	private RangeIn<Speed> leftVel;
	private RangeIn<Speed> rightVel;
	private AngleIn gyro;
	Kinematics kinematics;
	double left_encoder_prev_distance_ = 0;
	double right_encoder_prev_distance_ = 0;
	RobotState state;

	public RobotStateEstimator(double trackWidth, double trackLength, double scrub) {
		this.kinematics = new Kinematics(trackLength, trackWidth, scrub);
	}

	@Override
	public RigidTransform2d get() {
		double time = Timer.getFPGATimestamp();
		double left_distance = left.get();
		double right_distance = right.get();
		Rotation2d gyro_angle = Rotation2d.fromDegrees(gyro.get());
		RigidTransform2d odometry = generateOdometryFromSensors(left_distance - left_encoder_prev_distance_,
				right_distance - right_encoder_prev_distance_, gyro_angle);
		RigidTransform2d.Delta velocity = kinematics.forwardKinematics(leftVel.get(), rightVel.get());
		state.addObservations(time, odometry, velocity);
		left_encoder_prev_distance_ = left_distance;
		right_encoder_prev_distance_ = right_distance;
		return state.getLatestFieldToVehicle().getValue();
	}

	public RigidTransform2d generateOdometryFromSensors(double left_encoder_delta_distance,
			double right_encoder_delta_distance, Rotation2d current_gyro_angle) {
		RigidTransform2d last_measurement = state.getLatestFieldToVehicle().getValue();
		return kinematics.integrateForwardKinematics(last_measurement, left_encoder_delta_distance,
				right_encoder_delta_distance, current_gyro_angle);
	}

}
