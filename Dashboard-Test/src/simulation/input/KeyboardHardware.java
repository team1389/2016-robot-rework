package simulation.input;

import com.team1389.hardware.inputs.software.DigitalIn;

import net.java.games.input.Component.Identifier.Key;
import net.java.games.input.Controller;
import net.java.games.input.Keyboard;

public class KeyboardHardware {
	public Keyboard keyboard;

	public KeyboardHardware() {
		keyboard = findKeyboard();
	}

	private static Keyboard findKeyboard() {
		return (Keyboard) JInputUtil.findFirst(Controller.Type.KEYBOARD).get();
	}

	public DigitalIn getKey(Key k) {
		return new DigitalIn(() -> {
			keyboard.poll();
			return keyboard.isKeyDown(k);
		});
	}
}
