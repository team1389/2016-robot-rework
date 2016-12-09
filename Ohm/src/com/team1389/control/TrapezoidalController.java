package com.team1389.control;

import com.team1389.command_framework.CommandUtil;
import com.team1389.command_framework.command_base.Command;
import com.team1389.hardware.inputs.hardware.Timer;
import com.team1389.hardware.inputs.interfaces.BooleanSource;
import com.team1389.hardware.inputs.software.RangeIn;
import com.team1389.hardware.outputs.software.RangeOut;
import com.team1389.hardware.value_types.Percent;
import com.team1389.hardware.value_types.Position;
import com.team1389.hardware.value_types.Speed;
import com.team1389.motion_profile.MotionProfile;
import com.team1389.motion_profile.TrapezoidalMotionProfile;

public class TrapezoidalController extends SynchronousPID {
	MotionProfile following;
	double maxAcc;
	double maxDec;
	double maxVel;
	RangeIn<Speed> vel;
	RangeIn<Position> pos;
	RangeOut<Percent> out;
	Timer timer;
	
	public TrapezoidalController(double kP, double kI, double kD, double maxAcc, double maxDec, double maxVel,
			RangeIn<Position> source, RangeIn<Speed> vel, RangeOut<Percent> output) {
		super(kP, kI, kD);
		this.maxAcc = maxAcc;
		this.maxDec = maxDec;
		this.maxVel = maxVel;
		this.vel = vel;
		this.out=output;
		this.pos=source;
		timer=new Timer();
	}

	@Override
	public void setSetpoint(double val) {
			following = new TrapezoidalMotionProfile(vel.get(), val - pos.get(), maxAcc, maxDec, maxVel);
			timer.zero();
	}
	public void update(){
		double time = timer.get();
		if(following!=null&&time<following.getDuration()){
			super.setSetpoint(following.getPosition(time));
		}
		double calculate = calculate(pos.get());
		out.set(calculate);
	}
	public Command getPIDDoCommand() {
		return getPIDDoCommand(() -> {
			return false;
		});
	}
	public Command getPIDDoCommand(BooleanSource exitCondition) {
		return CommandUtil.createCommand(() -> {
			update();
			return exitCondition.get();
		});
	}
}
