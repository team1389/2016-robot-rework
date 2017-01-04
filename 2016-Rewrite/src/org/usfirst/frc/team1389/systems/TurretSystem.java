package org.usfirst.frc.team1389.systems;

import com.team1389.auto.command.TurnAngleCommand;
import com.team1389.configuration.PIDConstants;
import com.team1389.hardware.inputs.software.AngleIn;
import com.team1389.hardware.inputs.software.DigitalIn;
import com.team1389.hardware.inputs.software.PercentIn;
import com.team1389.hardware.outputs.software.PercentOut;
import com.team1389.hardware.value_types.Percent;
import com.team1389.hardware.value_types.Position;
import com.team1389.system.Subsystem;
import com.team1389.util.AddList;
import com.team1389.watch.Watchable;

public class TurretSystem extends Subsystem {
	PercentOut voltRange;

	PercentIn joystick;
	public double joyVal;

	AngleIn<Position> turretAngle;

	DigitalIn zeroButton;
	boolean toZero;

	public TurretSystem(PercentOut voltageSystem, AngleIn<Position> turretAngle, PercentIn joystickIn,
			DigitalIn zeroButton) {
		this.voltRange = voltageSystem;
		this.joystick = joystickIn.scale(.2);
		this.turretAngle = turretAngle;
		this.zeroButton = zeroButton;
	}

	@Override
	public String getName() {
		return "Turret subsystem";
	}

	@Override
	public void init() {
		voltRange.addChangeListener(COMMAND_CANCEL);
	}

	public void zero() {
		schedule(new TurnAngleCommand<Percent>(0, true, 2, turretAngle, voltRange, new PIDConstants(.07, 0, .15)));
	}

	@Override
	public void update() {
		joyVal = joystick.get();
		toZero = zeroButton.get();
		voltRange.set(joyVal);
		if (toZero) {
			zero();
		}
	}

	@Override
	public AddList<Watchable> getSubWatchables(AddList<Watchable> stem) {
		return stem.put(turretAngle.getWatchable("turret Angle"));
	}

}
