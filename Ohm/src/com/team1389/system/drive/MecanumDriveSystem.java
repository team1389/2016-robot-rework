package com.team1389.system.drive;

import com.team1389.hardware.inputs.software.PercentIn;
import com.team1389.hardware.value_types.Percent;
import com.team1389.system.DriveSystem;
import com.team1389.util.AddList;
import com.team1389.watch.Watchable;

import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

/**
 * Very simple mecanum drive class
 * 
 * @author Raffi
 *
 */

public class MecanumDriveSystem extends DriveSystem {
	private FourWheelSignal wheelValues;
	private PercentIn xThrottle;
	private PercentIn yThrottle;
	private PercentIn twist;
	private DriveOut<Percent> front;
	private DriveOut<Percent> back;

	public MecanumDriveSystem(PercentIn xThrottle, PercentIn yThrottle, PercentIn twist, DriveOut<Percent> front,
			DriveOut<Percent> back) {
		this.xThrottle = xThrottle;
		this.yThrottle = yThrottle;
		this.twist = twist;
		this.front = front;
		this.back = back;
	}

	public FourWheelSignal mecanumCalc(double xThrottle, double yThrottle, double twist) {
		double forward = -yThrottle; // push joystick forward to go forward
		double right = xThrottle; // push joystick to the right to strafe right
		double clockwise = twist;
		SmartDashboard.putNumber("strafe", right);
		SmartDashboard.putNumber("forward", forward);
		SmartDashboard.putNumber("turn", clockwise);

		double front_left = forward + clockwise + right;
		double front_right = forward - clockwise - right;
		double rear_left = forward + clockwise - right;
		double rear_right = forward - clockwise + right;
		// Finally, normalize the wheel speed commands
		// so that no wheel speed command exceeds magnitude of 1:
		double max = Math.abs(front_left);
		if (Math.abs(front_right) > max)
			max = Math.abs(front_right);
		if (Math.abs(rear_left) > max)
			max = Math.abs(rear_left);
		if (Math.abs(rear_right) > max)
			max = Math.abs(rear_right);
		if (max > 1) {
			front_left /= max;
			front_right /= max;
			rear_left /= max;
			rear_right /= max;
		}
		SmartDashboard.putNumber("topLeft", front_left);
		SmartDashboard.putNumber("topRight", front_right);
		SmartDashboard.putNumber("botLeft", rear_left);
		SmartDashboard.putNumber("botRight", rear_right);

		return new FourWheelSignal(front_left, front_right, rear_left, rear_right);
	}

	public void update() {
		wheelValues = mecanumCalc(xThrottle.get(), yThrottle.get(), twist.get());
		front.set(wheelValues.getTopWheels());
		back.set(wheelValues.getBottomWheels());

	}

	@Override
	public AddList<Watchable> getSubWatchables(AddList<Watchable> arg0) {
		return arg0.put(xThrottle.getWatchable("x"), yThrottle.getWatchable("y"));
	}

	@Override
	public String getName() {
		return "Mecanum";
	}

	@Override
	public void init() {

	}

}
