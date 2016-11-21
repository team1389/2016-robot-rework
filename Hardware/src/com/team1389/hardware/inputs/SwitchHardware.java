package com.team1389.hardware.inputs;

import java.util.HashMap;
import java.util.Map;

import com.team1389.hardware.interfaces.inputs.BooleanSource;
import com.team1389.hardware.interfaces.inputs.DigitalInput;
import com.team1389.hardware.interfaces.inputs.DigitalInput.InputStyle;
import com.team1389.hardware.registry.Constructor;
import com.team1389.hardware.registry.DIOPort;
import com.team1389.hardware.watch.Watchable;


public class SwitchHardware implements Watchable{
	public static final Constructor<DIOPort, SwitchHardware> constructor = (DIOPort port) -> {
		return new SwitchHardware(port);
	};
	
	edu.wpi.first.wpilibj.DigitalInput wpiSwitch;
	
	private SwitchHardware(DIOPort port) {
		wpiSwitch = new edu.wpi.first.wpilibj.DigitalInput(port.number);
	}
	
	public BooleanSource getRawSwitch(){
		return ()->{
			return wpiSwitch.get();
		};
	}
	public DigitalInput getSwitchAsInput(InputStyle style){
		return DigitalInput.createInput(getRawSwitch(),style);
	}
	@Override
	public String getName() {
		return "Switch" + wpiSwitch.getChannel();
	}

	@Override
	public Map<String, String> getInfo() {
		Map<String, String> info = new HashMap<>();
		
		info.put("state", "" + wpiSwitch.get());
		
		return info;
	}

}
