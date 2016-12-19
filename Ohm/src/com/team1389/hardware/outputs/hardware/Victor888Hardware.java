package com.team1389.hardware.outputs.hardware;

import com.team1389.hardware.Hardware;
import com.team1389.hardware.outputs.software.PercentOut;
import com.team1389.hardware.registry.port_types.PWM;
import com.team1389.watch.Watchable;

import edu.wpi.first.wpilibj.Victor;

public class Victor888Hardware extends Hardware<PWM> {

	boolean inverted;
	Victor victor;
	
	public Victor888Hardware(boolean inverted)
	{
		this.inverted = inverted;
	}
	
	@Override
	public Watchable[] getSubWatchables() {
		return new Watchable[] { getVoltageOutput().getWatchable("voltage") };
	}
	public PercentOut getVoltageOutput()
	{
		return new PercentOut((double volt) -> {victor.set(volt);});
	}

	@Override
	public void init(int port) {
		victor = new Victor(port);
		victor.setInverted(inverted);
		
	}

	@Override
	protected String getHardwareIdentifier() {
		return "Victor888";
	}
	

}
