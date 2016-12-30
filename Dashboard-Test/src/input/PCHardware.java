package input;

import com.team1389.hardware.inputs.software.DigitalIn;

import net.java.games.input.Component;
import net.java.games.input.Component.Identifier.Key;
import net.java.games.input.Controller;
import net.java.games.input.ControllerEnvironment;
import net.java.games.input.Keyboard;

public class PCHardware {
	public Keyboard keyboard;

	public PCHardware() {
		keyboard = findKeyboard();
		System.out.println(keyboard);
	}

	private static Keyboard findKeyboard() {
		Controller[] controllers = ControllerEnvironment.getDefaultEnvironment().getControllers();
		for (int i = 0; i < controllers.length; i++)
			if (controllers[i].getType() == Controller.Type.KEYBOARD)
				return (Keyboard) controllers[i];
		return null;
	}

	public boolean[] pollKeyboard() {
		keyboard.poll();
		Component[] c = keyboard.getComponents();
		boolean[] b = new boolean[c.length];
		for (int i = 0; i < c.length; i++) {
			if (c[i].getPollData() != 0) {
				b[i] = true;
			} else
				b[i] = false;
		}

		return b;
	}

	public DigitalIn getKey(Key k) {
		return new DigitalIn(() -> keyboard.isKeyDown(k));
	}
	public static void main(String[] args){
		while(true){
			
		}
	}
}
