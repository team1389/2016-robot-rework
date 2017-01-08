package simulation.motor.element;

public abstract class Element {
	public final double mass, moment, centerOfMass;

	public Element(double mass, double moment, double centerOfMass) {
		this.mass = mass;
		this.moment = moment;
		this.centerOfMass = centerOfMass;
	}

	public enum Material {
		ALUMINUM(2700);
		/**
		 * the density of the material (kg/m^3)
		 */
		public final double density;

		Material(double density) {
			this.density = density;
		}

		/**
		 * @param volume the volume of the object (m^3)
		 * @return the mass of the object kg
		 */
		public double getMass(double volume) {
			return density * volume;
		}
	}
}
