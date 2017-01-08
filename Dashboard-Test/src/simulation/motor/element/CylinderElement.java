package simulation.motor.element;

public class CylinderElement extends Element {

	public CylinderElement(double mass, double radius) {
		super(mass, mass * radius * radius / 2, 0);
	}

	public CylinderElement(Material material, double radius, double length) {
		this(material.getMass(radius * radius * Math.PI * length), radius);
	}

}
