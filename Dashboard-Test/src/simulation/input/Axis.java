package simulation.input;

import com.team1389.hardware.inputs.interfaces.ScalarInput;
import com.team1389.hardware.inputs.software.DigitalIn;
import com.team1389.hardware.inputs.software.PercentIn;
import com.team1389.hardware.value_types.Percent;

import net.java.games.input.Component.Identifier.Key;

public class Axis implements ScalarInput<Percent> {
	double scale;
	DigitalIn up;
	DigitalIn down;

	public Axis(Key up, Key down, double scale) {
		this(new KeyboardHardware(), up, down, scale);
	}

	public Axis(KeyboardHardware keyboard, Key up, Key down, double scale) {
		this.up = keyboard.getKey(up);
		this.down = keyboard.getKey(down);
		this.scale = scale;
	}

	public Double get() {
		return scale * (up.get() ? 1 : down.get() ? -1 : 0);
	}

	public static PercentIn make(Key up, Key down, double scale) {
		return new PercentIn(new Axis(up, down, scale)::get);
	}

	public static PercentIn make(KeyboardHardware keyboard, Key up, Key down, double scale) {
		return new PercentIn(new Axis(keyboard, up, down, scale)::get);
	}
}
