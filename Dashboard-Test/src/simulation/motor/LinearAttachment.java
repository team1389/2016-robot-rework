package simulation.motor;

import simulation.motor.element.Element;

public class LinearAttachment extends Attachment {

	public LinearAttachment(Element e, boolean hasWeight) {
		super(e, hasWeight);
	}

	@Override
	public double getAddedTorque(double theta) {
		if (hasWeight) {
			double torqueGravity = -GRAVITY_ACCEL * element.mass * .05;
			return torqueGravity;
		} else {
			return 0;
		}
	}
}
