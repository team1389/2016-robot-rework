package motor_sim.element;

public class PrismElement extends Element {
	public PrismElement(double mass, double width, double height, double depth) {
		super(mass, (mass * (depth * depth + height * height) / 12) + mass * depth * depth, depth / 2);
	}
}
