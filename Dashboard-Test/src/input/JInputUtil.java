package input;

import java.util.Arrays;
import java.util.Optional;

import net.java.games.input.Controller;
import net.java.games.input.ControllerEnvironment;

public class JInputUtil {
	public static Controller[] findAll(Controller.Type... controllers) {
		Controller[] all = ControllerEnvironment.getDefaultEnvironment().getControllers();
		return Arrays.stream(all).filter(c -> Arrays.asList(controllers).contains(c)).toArray(Controller[]::new);
	}

	public static Optional<Controller> findFirst(Controller.Type controller) {
		Controller[] controllers = ControllerEnvironment.getDefaultEnvironment().getControllers();
		return Arrays.stream(controllers).filter(c -> c.getType() == controller).findFirst();
	}
}
