package input;

import com.team1389.hardware.inputs.interfaces.ScalarInput;
import com.team1389.hardware.inputs.software.DigitalIn;
import com.team1389.hardware.value_types.Percent;

import net.java.games.input.Component.Identifier.Key;

public class Axis implements ScalarInput<Percent> {
	double scale;
	private KeyboardHardware keyboard;
	DigitalIn up;
	DigitalIn down;

	public Axis(Key up, Key down, double scale) {
		this.keyboard = new KeyboardHardware();
		this.up = keyboard.getKey(up).getLatched();
		this.down = keyboard.getKey(down).getLatched();
		this.scale = scale;
	}

	public Double get() {
		return scale * (up.get() ? 1 : down.get() ? -1 : 0);
	}
}
