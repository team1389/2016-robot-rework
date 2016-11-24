package com.team1389.hardware.inputs;

import java.util.HashMap;
import java.util.Map;

import com.team1389.hardware.inputs.DigitalInput.InputStyle;
import com.team1389.hardware.interfaces.inputs.BooleanSource;
import com.team1389.hardware.registry.Constructor;
import com.team1389.hardware.registry.DIOPort;
import com.team1389.hardware.watch.BooleanInfo;
import com.team1389.hardware.watch.Info;
import com.team1389.hardware.watch.Watchable;


public class SwitchHardware implements Watchable{
	boolean inverted;
	public static final Constructor<DIOPort, SwitchHardware> constructor = (DIOPort port) -> {
		return new SwitchHardware(port);
	};
	
	edu.wpi.first.wpilibj.DigitalInput wpiSwitch;
	
	private SwitchHardware(DIOPort port) {
		wpiSwitch = new edu.wpi.first.wpilibj.DigitalInput(port.number);
		inverted=false;
	}
	private boolean get(){
		return inverted?!wpiSwitch.get():wpiSwitch.get();
	}
	public BooleanSource getRawSwitch(){
		return ()->{
			return get();
		};
	}
	public static BooleanSource combineSwitchAND(SwitchHardware... switches){
		return ()->{
		boolean stillTrue=true;
		for(SwitchHardware switchHardware:switches){
			stillTrue=stillTrue&&switchHardware.get();
		}
		return stillTrue;
		};
	}
	public static BooleanSource combineSwitchOR(SwitchHardware... switches){
		return ()->{
		boolean stillTrue=false;
		for(SwitchHardware switchHardware:switches){
			stillTrue=stillTrue||switchHardware.get();
		}
		return stillTrue;
		};
	}
	public DigitalInput getSwitchAsInput(InputStyle style){
		return DigitalInput.createInput(getRawSwitch(),style);
	}
	
	@Override
	public String getName() {
		return wpiSwitch.getSmartDashboardType() + wpiSwitch.getChannel();
	}
	public void invert(boolean inverted){
		this.inverted=inverted;
	}
	@Override
	public Info[] getInfo() {
		return new Info[]{
				new BooleanInfo(getName(),()->{return wpiSwitch.get();})
			};
	}

}
