package com.team1389.hardware.outputs;

import java.util.ArrayList;
import java.util.List;

import com.team1389.hardware.control.PIDConfiguration;
import com.team1389.hardware.interfaces.outputs.CANTalonFollower;
import com.team1389.hardware.interfaces.outputs.OpenRangeOutput;
import com.team1389.hardware.interfaces.outputs.PercentRangeOutput;
import com.team1389.hardware.watch.Info;
import com.team1389.hardware.watch.Watchable;

public class CANTalonGroup implements Watchable{
	
	private final CANTalonHardware main;
	private final List<CANTalonFollower> followers;

	public CANTalonGroup(CANTalonHardware main, CANTalonHardware ... followers) {
		this.main = main;
		
		this.followers = new ArrayList<CANTalonFollower>();
		for (CANTalonHardware talon : followers){
			this.followers.add(talon.getFollower(main));
		}
	}
	
	private void setFollowers(){
		for (CANTalonFollower follower : followers){
			follower.follow();
		}
	}

	public PercentRangeOutput getVoltageOutput(){
		PercentRangeOutput mainOutput = main.getVoltageOutput();
		return (double voltage) -> {
			setFollowers();
			mainOutput.set(voltage);
		};
	}
	
	public OpenRangeOutput getPositionOutput(PIDConfiguration config){
		OpenRangeOutput mainOutput = main.getPositionOutput(config);
		return new OpenRangeOutput() {

			@Override
			public void set(double val) {
				setFollowers();
				mainOutput.set(val);
			}

			@Override
			public double min() {
				return mainOutput.min();
			}

			@Override
			public double max() {
				return mainOutput.max();
			}
		};
	}

	public OpenRangeOutput getSpeedOutput(PIDConfiguration config){
		OpenRangeOutput mainOutput = main.getSpeedOutput(config);
		return new OpenRangeOutput() {

			@Override
			public void set(double val) {
				setFollowers();
				mainOutput.set(val);
			}

			@Override
			public double min() {
				// TODO Auto-generated method stub
				return 0;
			}

			@Override
			public double max() {
				// TODO Auto-generated method stub
				return 0;
			}
		};
	}
	public CANTalonHardware getLeader(){
		return main;
	}
	@Override
	public String getName() {
		return "CANTalon Group: "+main.getName()+" ";
		//TODO add followers
	}

	@Override
	public Info[] getInfo() {
		// TODO Auto-generated method stub
		return null;
	}
}
