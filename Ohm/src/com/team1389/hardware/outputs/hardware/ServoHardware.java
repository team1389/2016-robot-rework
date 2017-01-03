package com.team1389.hardware.outputs.hardware;

import java.util.Optional;

import com.team1389.hardware.Hardware;
import com.team1389.hardware.inputs.software.AngleIn;
import com.team1389.hardware.inputs.software.RangeIn;
import com.team1389.hardware.outputs.software.AngleOut;
import com.team1389.hardware.outputs.software.RangeOut;
import com.team1389.hardware.registry.Registry;
import com.team1389.hardware.registry.port_types.PWM;
import com.team1389.hardware.value_types.Position;
import com.team1389.util.AddList;
import com.team1389.watch.Watchable;

import edu.wpi.first.wpilibj.Servo;

/**
 * A Servo motor
 * 
 * @author Jacob Prinz
 */
public class ServoHardware extends Hardware<PWM> {
	public ServoHardware(PWM requestedPort, Registry registry) {
		super(requestedPort, registry);
	}

	Optional<Servo> wpiServo;

	public RangeOut<Position> getPositionOutput() {
		return new RangeOut<Position>(pos -> wpiServo.ifPresent(s -> s.set(pos)), 0, 1);
	}

	@Override
	public AddList<Watchable> getSubWatchables(AddList<Watchable> stem) {
		return super.getSubWatchables(stem).put(getAngleInput().getWatchable("angle"));
	}

	public RangeIn<Position> getPositionInput() {
		return new RangeIn<Position>(Position.class, () -> wpiServo.map(servo -> servo.get()).orElse(0.0), 0, 1);
	}

	public AngleIn getAngleInput() {
		return getPositionInput().mapToRange(0, 180).setRange(0, 360).mapToAngle();
	}

	public AngleOut getAngleOutput() {
		return getPositionOutput().mapToRange(0, 180).setRange(0, 360).mapToAngle();
	}

	@Override
	protected String getHardwareIdentifier() {
		return "Servo";
	}

	@Override
	public void init(PWM port) {
		wpiServo = Optional.of(new Servo(port.index()));
	}

	@Override
	public void failInit() {
		wpiServo = Optional.empty();
	}
}
