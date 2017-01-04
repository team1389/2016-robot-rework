package input;
import com.team1389.hardware.inputs.hardware.Timer;
import com.team1389.hardware.inputs.software.DigitalIn;
import com.team1389.hardware.inputs.software.PercentIn;

public class Axis {
	static final double kChangeRate = .05;
	Timer timer;
	double val;
	DigitalIn up;
	DigitalIn down;

	public Axis(DigitalIn up, DigitalIn down) {
		this.up = up;
		this.down = down;
		timer = new Timer();
	}

	public PercentIn getVal() {
		return new PercentIn(() -> {
			if (up.get())
				val += kChangeRate * timer.get();
			if (down.get())
				val -= kChangeRate * timer.get();
			timer.zero();
			return val;
		}).clamp();
	}
}
