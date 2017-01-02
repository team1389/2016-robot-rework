package motor_sim;

public class Attachment {
	public final static double GRAVITY_ACCEL = 9.8; // acceleration of gravity (m/s^2)

	static final Attachment FREE = new Attachment(1, .05, .008, 1.6, false);

	public final double moment, rCenterMass, mass;
	private final boolean hasWeight;

	public Attachment(double m, double r, double rCenterMass, double I, boolean hasWeight) {
		this.mass = m;
		this.rCenterMass = rCenterMass;
		this.moment = I;
		this.hasWeight = hasWeight;
	}

	public Attachment(double m, double r, double rCenterMass, boolean hasWeight) {
		this(m, r, rCenterMass, m * rCenterMass * rCenterMass + m * r * r / 12, // calculate moment of inertia of the arm
				hasWeight);
	}

	public Attachment(double m, double r, boolean hasWeight) {
		this(m, r, r / 2, hasWeight);
	}

	public double getAddedTorque(double theta) {
		if (hasWeight) {
			double torqueGravity = GRAVITY_ACCEL * mass * Math.cos(theta) * rCenterMass;
			return torqueGravity;
		} else {
			return 0;
		}
	}

}