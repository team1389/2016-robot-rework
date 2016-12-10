package com.team1389.hardware.inputs.hardware;

import com.team1389.hardware.inputs.interfaces.BinaryInput;
import com.team1389.hardware.registry.Registry;
import com.team1389.watch.BooleanInfo;
import com.team1389.watch.Info;
import com.team1389.watch.Watchable;

public class SwitchHardware implements Watchable {
	boolean inverted;

	edu.wpi.first.wpilibj.DigitalInput wpiSwitch;

	public SwitchHardware(int dioPort, Registry registry) {
		registry.claimDIOPort(dioPort);
		registry.registerWatcher(this);
		wpiSwitch = new edu.wpi.first.wpilibj.DigitalInput(dioPort);
	}

	private boolean get() {
		return inverted ? !wpiSwitch.get() : wpiSwitch.get();
	}

	public BinaryInput getRawSwitch() {
		return () -> {
			return get();
		};
	}

	@Override
	public String getName() {
		return wpiSwitch.getSmartDashboardType() + wpiSwitch.getChannel();
	}

	public void invert(boolean inverted) {
		this.inverted = inverted;
	}

	@Override
	public Info[] getInfo() {
		return new Info[] { new BooleanInfo(getName(), () -> {
			return wpiSwitch.get();
		}) };
	}

}
