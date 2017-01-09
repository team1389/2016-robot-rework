package simulation.input;

import java.util.Optional;

import com.team1389.hardware.inputs.interfaces.BinaryInput;
import com.team1389.hardware.inputs.software.DigitalIn;
import com.team1389.hardware.inputs.software.DigitalIn.InputFilter;
import com.team1389.hardware.inputs.software.PercentIn;

import net.java.games.input.Controller;
import net.java.games.input.Controller.Type;

public class SimJoystick {
	Optional<MappedController> controller;

	public SimJoystick(int port) {
		this(port, Type.STICK, Type.GAMEPAD);
	}

	public SimJoystick(int port, Controller.Type... types) {
		Controller[] controllers = JInputUtil.findAll(types);
		System.out.println(controllers.length+" "+port);
		if (port < controllers.length) {
			this.controller = Optional.of(MappedController.mapController(controllers[port]));
		} else {
			this.controller = Optional.empty();
		}
	}

	/**
	 * @param button
	 *            the button port to check
	 * @return a boolean stream that tracks the current state of the button
	 */
	public DigitalIn getButton(int button) {
		return new DigitalIn(getRawButton(button));
	}

	/**
	 * @param button
	 *            the button port to check
	 * @param filter
	 *            a boolean filter to apply to the button stream
	 * @return a boolean stream that tracks the current state of the button
	 */
	public DigitalIn getButton(int button, InputFilter filter) {
		return new DigitalIn(getRawButton(button), filter);
	}

	/**
	 * @param button
	 *            the button port to check
	 * @return a boolean stream of the button data
	 */
	public BinaryInput getRawButton(int button) {
		return () -> {
			return controller.map(c -> c.getButton(button)).orElse(false);
		};
	}

	/**
	 * 
	 * @param axis
	 *            the axis to track
	 * @return a percent stream that tracks the value of the axis
	 */
	public PercentIn getAxis(int axis) {
		return new PercentIn(() -> {
			return controller.map(c -> c.getAxis(axis)).orElse(0.0);
		});
	}

}
