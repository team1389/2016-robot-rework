package com.team1389.hardware.outputs.hardware;

import java.util.Optional;

import com.team1389.hardware.DoubleHardware;
import com.team1389.hardware.registry.Registry;
import com.team1389.hardware.registry.port_types.PCM;
import com.team1389.hardware.registry.port_types.PortInstance;
import com.team1389.util.AddList;
import com.team1389.watch.Watchable;
import edu.wpi.first.wpilibj.DoubleSolenoid;
public class DoubleSolenoidHardware extends DoubleHardware <PCM>{
	Optional<DoubleSolenoid> doubleSolenoid1, doubleSolenoid2;
	
	public DoubleSolenoidHardware(PCM requestedPort1, PCM requestedPort2, Registry registry) {
		super(requestedPort1, requestedPort2, registry);
	}

	@Override
	public AddList<Watchable> getSubWatchables(AddList<Watchable> stem) {
		
		return super.getSubWatchables(stem).put(getDigitalOut().getWatchable("state"));
	}

	@Override
	public void failInit() {
		// TODO Auto-generated method stub
		
	}

	@Override
	protected String getHardwareIdentifier() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void init(PCM port1, PCM port2) {
		// TODO Auto-generated method stub
		
	}

}
