package com.team1389.control;

import com.team1389.command_framework.CommandUtil;
import com.team1389.command_framework.command_base.Command;
import com.team1389.hardware.inputs.hardware.Timer;
import com.team1389.hardware.inputs.interfaces.BinaryInput;
import com.team1389.hardware.inputs.software.RangeIn;
import com.team1389.hardware.outputs.software.RangeOut;
import com.team1389.hardware.value_types.Percent;
import com.team1389.hardware.value_types.Position;
import com.team1389.hardware.value_types.Speed;
import com.team1389.motion_profile.MotionProfile;

public class MotionProfileController extends SynchronousPID {
	MotionProfile following;
	double startPos;
	RangeIn<Speed> vel;
	RangeIn<Position> pos;
	RangeOut<Percent> out;
	Timer timer;

	public MotionProfileController(double kP, double kI, double kD, RangeIn<Position> source, RangeIn<Speed> vel,
			RangeOut<Percent> output) {
		super(kP, kI, kD);
		this.vel = vel;
		this.out = output;
		this.pos = source;
		startPos = 0;
		timer = new Timer();
	}

	public void followProfile(MotionProfile prof) {
		this.following = prof;
		startPos = pos.get();
		timer.zero();
	}

	public void update() {
		double time = timer.get();
		if (following != null && !following.isFinished(time)) {
			super.setSetpoint(following.getPosition(time) + startPos);
		}
		double calculate = calculate(pos.get());
		out.set(calculate);
	}

	public Command getPIDDoCommand() {
		return getPIDDoCommand(() -> {
			return false;
		});
	}

	public Command getPIDDoCommand(BinaryInput exitCondition) {
		return CommandUtil.createCommand(() -> {
			update();
			return exitCondition.get();
		});
	}

}
