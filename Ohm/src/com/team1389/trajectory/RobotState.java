package com.team1389.trajectory;

import java.util.Map;

import com.team1389.util.interpolable.InterpolatingDouble;
import com.team1389.util.interpolable.InterpolatingTreeMap;

public class RobotState {
	public static final int kObservationBufferSize = 100;
	protected InterpolatingTreeMap<InterpolatingDouble, RigidTransform2d> fieldToVehicle;
	protected RigidTransform2d.Delta vehicleVel;

	protected RobotState() {
		reset(0, new RigidTransform2d());
	}

	public synchronized void reset(double start_time, RigidTransform2d initial_field_to_vehicle) {
		fieldToVehicle = new InterpolatingTreeMap<>(kObservationBufferSize);
		fieldToVehicle.put(new InterpolatingDouble(start_time), initial_field_to_vehicle);
		vehicleVel = new RigidTransform2d.Delta(0, 0, 0);
	}

	public synchronized RigidTransform2d getFieldToVehicle(double timestamp) {
		return fieldToVehicle.getInterpolated(new InterpolatingDouble(timestamp));
	}

	public synchronized Map.Entry<InterpolatingDouble, RigidTransform2d> getLatestFieldToVehicle() {
		return fieldToVehicle.lastEntry();
	}

	public synchronized RigidTransform2d getPredictedFieldToVehicle(double lookahead_time) {
		return getLatestFieldToVehicle().getValue()
				.transformBy(RigidTransform2d.fromVelocity(new RigidTransform2d.Delta(vehicleVel.dx * lookahead_time,
						vehicleVel.dy * lookahead_time, vehicleVel.dtheta * lookahead_time)));
	}

	public synchronized void addFieldToVehicleObservation(double timestamp, RigidTransform2d observation) {
		fieldToVehicle.put(new InterpolatingDouble(timestamp), observation);
	}

	public synchronized void addObservations(double timestamp, RigidTransform2d field_to_vehicle,
			RigidTransform2d.Delta velocity) {
		addFieldToVehicleObservation(timestamp, field_to_vehicle);
		vehicleVel = velocity;
	}
}
