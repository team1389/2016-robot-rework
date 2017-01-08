package input;

import com.team1389.hardware.inputs.software.DigitalIn;

import net.java.games.input.Component.Identifier.Key;
import net.java.games.input.Controller;
import net.java.games.input.Keyboard;
	
public class KeyboardHardware {
	public Keyboard keyboard;

	public KeyboardHardware() {
		keyboard = findKeyboard();
		System.out.println(keyboard);
	}

	private static Keyboard findKeyboard() {
		return (Keyboard) JInputUtil.findFirst(Controller.Type.KEYBOARD).get();
	}

	public DigitalIn getKey(Key k) {
		return new DigitalIn(() -> keyboard.isKeyDown(k));
	}
}
