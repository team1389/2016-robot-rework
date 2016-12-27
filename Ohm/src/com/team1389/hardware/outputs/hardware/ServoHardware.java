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
import com.team1389.util.OptionalUtil;
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
		return new RangeOut<Position>(OptionalUtil.ifPresent(wpiServo, (Servo s, Double pos) -> {
			s.set(pos);
		}), 0, 1);
	}

	@Override
	public Watchable[] getSubWatchables() {
		return new Watchable[] { getAngleInput().getWatchable("angle")};
	}

	public RangeIn<Position> getPositionInput() {
		return new RangeIn<Position>(Position.class, OptionalUtil.<Servo, Double>ifPresent(0d, wpiServo, (Servo s) -> {
			return s.get();
		}), 0, 1);
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
}
