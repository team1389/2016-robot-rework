package input;

import java.util.Arrays;
import java.util.Optional;

import com.team1389.hardware.inputs.interfaces.BinaryInput;
import com.team1389.hardware.inputs.software.DigitalIn;
import com.team1389.hardware.inputs.software.DigitalIn.InputFilter;
import com.team1389.hardware.inputs.software.PercentIn;

import net.java.games.input.Controller;
import net.java.games.input.Controller.Type;
import net.java.games.input.ControllerEnvironment;

public class SimJoystick {
	static Controller c = findFirst(Type.STICK).get();
	Optional<MappedController> controller;

	public SimJoystick(int port) {
		//Controller c = port < controllers.length ? controllers[port] : null;
		controller = Optional.ofNullable(c == null ? null : MappedController.mapController(c));
	}

	/**
	 * @param button the button port to check
	 * @return a boolean stream that tracks the current state of the button
	 */
	public DigitalIn getButton(int button) {
		return new DigitalIn(getRawButton(button));
	}

	/**
	 * @param button the button port to check
	 * @param filter a boolean filter to apply to the button stream
	 * @return a boolean stream that tracks the current state of the button
	 */
	public DigitalIn getButton(int button, InputFilter filter) {
		return new DigitalIn(getRawButton(button), filter);
	}

	/**
	 * @param button the button port to check
	 * @return a boolean stream of the button data
	 */
	public BinaryInput getRawButton(int button) {
		return () -> {
			return controller.map(c->c.getButton(button)).orElse(false);
		};
	}

	/**
	 * 
	 * @param axis the axis to track
	 * @return a percent stream that tracks the value of the axis
	 */
	public PercentIn getAxis(int axis) {
		return new PercentIn(() -> {
			return controller.map(c -> c.getAxis(axis)).orElse(0.0);
		});
	}

	public static Controller[] findAll(Controller.Type... controllers) {
		Controller[] all = ControllerEnvironment.getDefaultEnvironment().getControllers();
		return Arrays.stream(all).filter(c -> Arrays.asList(controllers).contains(c)).toArray(Controller[]::new);
	}
	public static Optional<Controller> findFirst(Controller.Type controller) {
		Controller[] controllers = ControllerEnvironment.getDefaultEnvironment().getControllers();
		return Arrays.stream(controllers).filter(c -> c.getType() == controller).findFirst();
	}


}
