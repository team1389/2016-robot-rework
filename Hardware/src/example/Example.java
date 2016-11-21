package example;

import java.util.List;

import com.team1389.hardware.inputs.SwitchHardware;
import com.team1389.hardware.outputs.CANTalonHardware;
import com.team1389.hardware.outputs.ServoHardware;
import com.team1389.hardware.outputs.VictorHardware;
import com.team1389.hardware.registry.Registry;
import com.team1389.hardware.watch.Watchable;

public class Example {
	public static void main(String[] args){
		//registering hardware
		Registry registry = new Registry();

		CANTalonHardware talon = registry.registerCANHardware(0, CANTalonHardware.constructor);
		VictorHardware victor = registry.registerPWMHardware(0, VictorHardware.constructor);
		ServoHardware servo = registry.registerPWMHardware(1, ServoHardware.constructor);
		SwitchHardware limitSwitch = registry.registerDIOHardware(0, SwitchHardware.constructor);
		
		//thus it is not possible to instantiate talons or any hardware without the registry
		
		
		//getting info about all registered hardware
		List<Watchable> allHardware = registry.getHardwareInfo();
		for (Watchable watchable : allHardware){//loop through watchables
			System.out.println(watchable.getName());
			watchable.getInfo().forEach((String name, String value) -> {//loop through info
				System.out.println("\t" + name + ":\t" + value);
			});
		}
		/*
		 * will print out something like this:
		 * 
		 * CANTalon 0
		 *  	speed: 	1.2
		 *   	position: 	34534.23
		 *  	voltage out: 4.5
		 *  	mode: Position
		 * Victor 0
		 *  	last set output: 	0.67
		 * Servo 1
		 *  	position: 	.4
		 * Switch 0
		 *  	state: 	true
		 */
	}
}
