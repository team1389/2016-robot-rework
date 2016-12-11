package layout;

import com.team1389.hardware.inputs.software.RangeIn;
import com.team1389.hardware.outputs.software.PercentOut;
import com.team1389.hardware.value_types.Position;
import com.team1389.hardware.value_types.Speed;

public class TesterDefaultHardware extends TesterLayout {
	static final double V_TO_RPM_CONSTANT = 5.52;
	double val1;
	double val2;
	double pos1;
	double pos2;
	double speed1;
	double speed2;

	public TesterDefaultHardware() {
		voltOut1 = new PercentOut((double val) -> {
			val1 = val;
		});
		voltOut2 = new PercentOut((double val) -> {
			val2 = val;
		});

		speedIn1 = new RangeIn<Speed>(Speed.class, () -> {
			return speed1;
		}, 0, 1);
		speedIn2 = new RangeIn<Speed>(Speed.class, () -> {
			return speed2;
		}, 0, 8192);

		posIn1 = new RangeIn<Position>(Position.class, () -> {
			return pos1;
		}, 0, 1);
		posIn2 = new RangeIn<Position>(Position.class, () -> {
			return pos2;
		}, 0, 8192);
		new Thread(() -> {
			double lastLoop = System.currentTimeMillis();
			while (true) {
				double dt = System.currentTimeMillis() - lastLoop;
				lastLoop = System.currentTimeMillis();
				dt /= 1000;
				speed1 = V_TO_RPM_CONSTANT * val1;
				speed2 = V_TO_RPM_CONSTANT * val2;
				pos1 += speed1 * dt;
				pos2 += speed2 * dt;
				try {
					Thread.sleep(1 / 50);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}
		}).start();
	}
}
