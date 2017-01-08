package simulation.motor;

import simulation.motor.element.CylinderElement;
import simulation.motor.element.Element;

public class Attachment {
	public final static double GRAVITY_ACCEL = 9.8; // acceleration of gravity (m/s^2)

	static final Element FREE = new CylinderElement(8,.025);

	final boolean hasWeight;
	Element element;

	public Attachment(Element e, boolean hasWeight) {
		this.element = e;
		this.hasWeight = hasWeight;
	}

	public double getAddedTorque(double theta) {
		if (hasWeight) {
			double torqueGravity = -GRAVITY_ACCEL * element.mass * Math.cos(theta) * element.centerOfMass;
			return torqueGravity;
		} else {
			return 0;
		}
	}

	public double getMoment() {
		return element.moment;
	}

}