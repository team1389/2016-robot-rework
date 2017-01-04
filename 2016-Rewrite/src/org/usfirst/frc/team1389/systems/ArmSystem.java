package org.usfirst.frc.team1389.systems;

import com.team1389.command_framework.command_base.Command;
import com.team1389.configuration.PIDConstants;
import com.team1389.control.SynchronousPIDController;
import com.team1389.hardware.inputs.software.AngleIn;
import com.team1389.hardware.outputs.software.AngleOut;
import com.team1389.hardware.outputs.software.PercentOut;
import com.team1389.hardware.value_types.Percent;
import com.team1389.hardware.value_types.Position;
import com.team1389.system.Subsystem;
import com.team1389.util.AddList;
import com.team1389.util.ButtonEnumMap;
import com.team1389.watch.Watchable;

/**
 * a system to control the arm on Maelstrom using buttons
 * 
 * @author amind
 *
 */
public class ArmSystem extends Subsystem {

	AngleOut<Position> elevator;
	SynchronousPIDController<Percent, Position> elevatorPID;
	ButtonEnumMap<ArmLocation> buttons;
	AngleIn<Position> armVal;
	Command profileMover;
	double inputAngle;

	/**
	 * @param elevator a voltage stream for the elevator motors
	 * @param map the button map for controlling the arm
	 * @param armPosition an angle stream for the arm
	 */
	public ArmSystem(PercentOut elevator, ButtonEnumMap<ArmLocation> map, AngleIn<Position> armPosition) {
		this.buttons = map;
		this.armVal = armPosition;
		elevatorPID = new SynchronousPIDController<Percent, Position>(new PIDConstants(.03, 0, 0), armPosition,
				elevator.offset(.147));
		this.elevator = elevatorPID.getSetpointSetter().getProfiledOut(30, 0);
		this.inputAngle = 0;
	}

	@Override
	public void init() {
		elevator.set(inputAngle);
		buttons.addChangeListener(elevatorPID::resetIntegrator);
	}

	@Override
	public void update() {
		inputAngle = buttons.getVal().angle;
		elevator.set(inputAngle);
		elevatorPID.update();
	}
	
	public enum ArmLocation {
		DOWN(0), DEFENSE(45), LOW_GOAL(25), VERTICAL(90);

		ArmLocation(double angle) {
			this.angle = angle;
		}

		public ArmLocation next() {
			if (ordinal() < values().length - 1) {
				return values()[ordinal() + 1];
			}
			return this;
		}

		public ArmLocation previous() {
			if (ordinal() > 0) {
				return values()[ordinal() - 1];
			}
			return this;
		}

		public final double angle;
	}

	@Override
	public String getName() {
		return "Arm System";
	}

	@Override
	public AddList<Watchable> getSubWatchables(AddList<Watchable> stem) {
		return stem.put(buttons.getWatchable("target location"), armVal.getWatchable("arm position"),
				elevatorPID.getSetpointSetter().getWatchable("arm setpoint"),
				elevatorPID.getOutput().getWatchable("arm vOut"), elevatorPID.getPIDTuner("arm controller"));
	}
}