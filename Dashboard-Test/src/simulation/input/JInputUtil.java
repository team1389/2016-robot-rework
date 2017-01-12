package simulation.input;

import java.util.Arrays;
import java.util.Optional;
import java.util.stream.Stream;

import net.java.games.input.Controller;
import net.java.games.input.ControllerEnvironment;

public class JInputUtil {
	public static Controller[] findAll(Controller.Type... controllers) {
		Controller[] all = ControllerEnvironment.getDefaultEnvironment().getControllers();
		return Arrays.stream(all).filter(c -> Arrays.asList(controllers).contains(c.getType()))
				.toArray(Controller[]::new);
	}

	public static Optional<Controller> findFirst(Controller.Type controller) {
		Controller[] controllers = ControllerEnvironment.getDefaultEnvironment().getControllers();
		Stream<Controller> filter = Arrays.stream(controllers).filter(c -> {
			System.out.println(c.getType());
			System.out.println(controller);
			return c.getType() == controller;
		});
		return filter.findFirst();
	}
}
