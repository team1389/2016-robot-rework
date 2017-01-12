package com.team1389.system.drive;

import com.team1389.hardware.inputs.software.PercentIn;
import com.team1389.hardware.value_types.Percent;
import com.team1389.system.DriveSystem;
import com.team1389.util.AddList;
import com.team1389.watch.CompositeWatchable;
import com.team1389.watch.Watchable;

/**
 * implements a tank drive system that uses bezier curves to improve joystick
 * responsiveness. Developed by team 1114, Simbotics
 * 
 * @author amind
 *
 */
public class SimboticsDriveSystem extends DriveSystem {
	private DriveOut<Percent> output;
	private PercentIn throttle;
	private PercentIn wheel;

	private SimBezierCurve SimCurveXH;
	private SimBezierCurve SimCurveYH;

	/**
	 * 
	 * @param output
	 *            a percent controlled driveStream (can be speed or voltage)
	 * @param throttle
	 *            percent of desired speed (forward/back)
	 * @param wheel
	 *            percent of desired turning to (l/r)
	 */
	public SimboticsDriveSystem(DriveOut<Percent> output, PercentIn throttle, PercentIn wheel) {
		this.output = output;
		this.throttle = throttle;
		this.wheel = wheel;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void init() {
		SimPoint zero = new SimPoint(0, 0);
		SimPoint xP1H = new SimPoint(0.0, 0.30);
		SimPoint xP2H = new SimPoint(0.45, -0.1);

		SimPoint yP1H = new SimPoint(0.0, 0.54);
		SimPoint yP2H = new SimPoint(0.45, -0.07);
		SimPoint one = new SimPoint(1, 1);
		this.SimCurveXH = new SimBezierCurve(zero, xP1H, xP2H, one);
		this.SimCurveYH = new SimBezierCurve(zero, yP1H, yP2H, one);
	}

	/**
	 * update wheel and throttle values, then update output using those values
	 */
	@Override
	public void update() {
		double x = wheel.get();
		double y = throttle.get();
		x = this.SimCurveXH.getPoint(x).getY();
		y = this.SimCurveYH.getPoint(y).getY();
		output.set(new DriveSignal(y + x, y - x));
	}

	/**
	 * return key
	 */
	@Override
	public String getName() {
		return "Simbot Drive";
	}

	/**
	 * add no watchables to stem
	 */
	@Override
	public AddList<Watchable> getSubWatchables(AddList<Watchable> stem) {
		return stem.put(wheel.getWatchable("wheel"), throttle.getWatchable("throttle"))
				.put(output.getSubWatchables(CompositeWatchable.stem));
	}

}
