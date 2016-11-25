package example;

import java.util.List;

import com.team1389.hardware.inputs.hardware.SwitchHardware;
import com.team1389.hardware.outputs.hardware.CANTalonHardware;
import com.team1389.hardware.outputs.hardware.ServoHardware;
import com.team1389.hardware.outputs.hardware.VictorHardware;
import com.team1389.hardware.registry.Registry;
import com.team1389.hardware.watch.Watchable;

public class Example {
	public static void main(String[] args) {
		// registering hardware
		Registry registry = new Registry();

		CANTalonHardware talon = new CANTalonHardware(0, registry);
		VictorHardware victor = new VictorHardware(0, registry);
		ServoHardware servo = new ServoHardware(1, registry);
		SwitchHardware limitSwitch = new SwitchHardware(0, registry);

		// thus it is not possible to instantiate talons or any hardware without
		// the registry

		// getting info about all registered hardware
		List<Watchable> allHardware = registry.getHardwareInfo();
		for (Watchable watchable : allHardware) {// loop through watchables
			System.out.println(watchable.getName());

		}
		/*
		 * will print out something like this:
		 * 
		 * CANTalon 0 speed: 1.2 position: 34534.23 voltage out: 4.5 mode:
		 * Position Victor 0 last set output: 0.67 Servo 1 position: .4 Switch 0
		 * state: true
		 */
	}
}
