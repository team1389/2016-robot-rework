package simulation.motor.element;

public class PrismElement extends Element {
	public PrismElement(double mass, double width, double height, double depth) { // TODO I'm not sure this does what we think it does. If it works as we expect, then remove width
		super(mass, (mass * (depth * depth + height * height) / 12) + mass * depth * depth, depth / 2);
	}
}
