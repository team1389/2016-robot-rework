package input;

import java.util.Arrays;
import java.util.Optional;

import com.team1389.hardware.inputs.software.DigitalIn;

import net.java.games.input.Component;
import net.java.games.input.Component.Identifier.Key;
import net.java.games.input.Controller;
import net.java.games.input.Controller.Type;
import net.java.games.input.ControllerEnvironment;
import net.java.games.input.Keyboard;

public class PCHardware {
	public Keyboard keyboard;

	public PCHardware() {
		keyboard = findKeyboard();
		System.out.println(keyboard);
	}

	private static Keyboard findKeyboard() {
		return (Keyboard) findFirst(Controller.Type.KEYBOARD).get();
	}

	public static Optional<Controller> findFirst(Controller.Type controller) {
		Controller[] controllers = ControllerEnvironment.getDefaultEnvironment().getControllers();
		return Arrays.stream(controllers).filter(c -> c.getType() == controller).findFirst();
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

	public static void main(String[] args) {
		findFirst(Type.STICK).ifPresent(PCHardware::init);
	}

	public static void init(Controller stick) {
		MappedController controller=MappedController.mapController(stick);

		while (true) {
			controller.poll();
			System.out.println(controller.getButton(1));
		}
	}
}
