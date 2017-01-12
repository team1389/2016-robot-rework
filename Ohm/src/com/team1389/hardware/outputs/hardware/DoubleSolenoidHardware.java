package com.team1389.hardware.outputs.hardware;

import java.util.Optional;
import java.util.function.Consumer;

import com.team1389.hardware.DoubleHardware;
import com.team1389.hardware.outputs.software.DigitalOut;
import com.team1389.hardware.registry.Registry;
import com.team1389.hardware.registry.port_types.PCM;
import com.team1389.util.AddList;
import com.team1389.watch.Watchable;

import edu.wpi.first.wpilibj.DoubleSolenoid;

public class DoubleSolenoidHardware extends DoubleHardware<PCM> {
	Optional<DoubleSolenoid> doubleSolenoid;

	public DoubleSolenoidHardware(PCM requestedPort1, PCM requestedPort2, Registry registry) {
		super(requestedPort1, requestedPort2, registry);
	}
	
	//public  Consumer<DoubleSolenoid.Value> getSolenoidValOut(){
		//return v->doubleSolenoid.ifPresent(w->w.set(v));
	//}
	public DigitalOut getDigitalOut(){
		return new DigitalOut(val -> doubleSolenoid.ifPresent(w-> w.set((val==true)? DoubleSolenoid.Value.kForward: DoubleSolenoid.Value.kOff)));
				}
	@Override
	public AddList<Watchable> getSubWatchables(AddList<Watchable> stem) {
	return super.getSubWatchables(stem).put(getDigitalOut().getWatchable("state"));
	}

	@Override
	public void failInit() {
		doubleSolenoid = Optional.empty();
	}

	@Override
	protected String getHardwareIdentifier() {
		return "Double Solenoid";
	}

	@Override
	public void init(PCM port1, PCM port2) {
		doubleSolenoid = Optional.of(new DoubleSolenoid(port1.index(), port2.index()));
	}

}
