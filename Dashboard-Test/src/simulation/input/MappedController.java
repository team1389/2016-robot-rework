package simulation.input;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import net.java.games.input.Component;
import net.java.games.input.Component.Identifier;
import net.java.games.input.Controller;

public class MappedController {
	public final List<Mapping> axes, buttons;
	private Controller controller;

	private MappedController(Controller controller, List<Mapping> axes, List<Mapping> buttons) {
		this.axes = axes;
		this.buttons = buttons;
		this.controller = controller;
	}

	public void poll() {
		controller.poll();
	}

	public double getAxis(int axis) {
		poll();
		if (axis < axes.size()) {
			return axes.get(axis).y.getPollData();
		}
		return 0;
	}

	public boolean getButton(int button) {
		poll();
		if (button < buttons.size()) {
			return buttons.get(button).y.getPollData() > 0;
		}
		return false;
	}

	public static MappedController mapController(Controller c) {
		Component[] axes = Arrays.stream(c.getComponents()).filter(r -> r.isAnalog()).toArray(Component[]::new);
		List<Mapping> mappedAxes = IntStream.range(0, axes.length).mapToObj(i -> new Mapping(i, axes[i]))
				.collect(Collectors.toList());
		Component[] btns = Arrays.stream(c.getComponents())
				.filter(r -> !(r.isAnalog() || r.getIdentifier() == Identifier.Axis.POV)).toArray(Component[]::new);
		List<Mapping> mappedBtns = IntStream.range(0, btns.length).mapToObj(i -> new Mapping(i, btns[i]))
				.collect(Collectors.toList());
		return new MappedController(c, mappedAxes, mappedBtns);

	}

	static class Mapping extends Tuple<Integer, Component> {
		Mapping(Integer x, Component y) {
			super(x, y);
		}

	}

}
