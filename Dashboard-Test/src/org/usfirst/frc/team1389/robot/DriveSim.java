package org.usfirst.frc.team1389.robot;

import org.newdawn.slick.AppGameContainer;
import org.newdawn.slick.BasicGame;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.geom.Rectangle;
import org.newdawn.slick.geom.Shape;

import com.team1389.hardware.inputs.software.RangeIn;
import com.team1389.hardware.value_types.Percent;
import com.team1389.hardware.value_types.Position;
import com.team1389.system.drive.DriveOut;
import com.team1389.system.drive.TankDriveSystem;
import com.team1389.trajectory.Kinematics;
import com.team1389.trajectory.RigidTransform2d;
import com.team1389.trajectory.RobotState;
import com.team1389.trajectory.Rotation2d;
import com.team1389.trajectory.Translation2d;
import com.team1389.watch.Watcher;

import edu.wpi.first.wpilibj.Timer;
import input.SimJoystick;
import motor_sim.Attachment;
import motor_sim.Motor;
import motor_sim.Motor.MotorType;
import motor_sim.MotorSystem;
import motor_sim.element.CylinderElement;

public class DriveSim extends BasicGame {

	public DriveSim(String title) {
		super(title);
	}

	public static void main(String[] args) throws SlickException {
		Tester.initWPILib();
		AppGameContainer cont = new AppGameContainer(new DriveSim("Drive Sim"));
		cont.setTargetFrameRate(50);
		cont.setDisplayMode(500, 500, false);
		cont.start();
	}

	SimJoystick joy = new SimJoystick(1);
	MotorSystem left = new MotorSystem(new Attachment(new CylinderElement(.51, .097), false), 6,
			new Motor(MotorType.CIM));
	MotorSystem right = new MotorSystem(new Attachment(new CylinderElement(.51, .097), false), 6,
			new Motor(MotorType.CIM));
	TankDriveSystem drive = new TankDriveSystem(
			new DriveOut<Percent>(left.getVoltageOutput(), right.getVoltageOutput()), joy.getAxis(0), joy.getAxis(1).invert());
	RobotState state = new RobotState();
	double leftDistance = 0;
	double rightDistance = 0;
	RangeIn<Position> leftIn = left.getPositionInput().mapToRange(0, 1).scale(Math.PI * 7.65);
	RangeIn<Position> rightIn = right.getPositionInput().mapToRange(0, 1).scale(Math.PI * 7.65);
	Watcher dash;

	@Override
	public void render(GameContainer container, Graphics g) throws SlickException {
		RigidTransform2d transform = state.getLatestFieldToVehicle().getValue();
		Translation2d trans = transform.getTranslation();
		Rotation2d rot = transform.getRotation();
		float x = (float) trans.getX();
		float y = (float) trans.getY();
		Shape robot = new Rectangle(0, 0, 22, 25);
		g.draw(robot);
		g.fillOval(x - 5, y - 5, 10, 10);
		g.drawLine(x, y, x + 10f * (float) Math.cos(rot.getRadians()), y + 10f * (float) Math.sin(rot.getRadians()));
	}

	@Override
	public void init(GameContainer arg0) throws SlickException {
		state.reset(Timer.getFPGATimestamp(), new RigidTransform2d(new Translation2d(), new Rotation2d()));
		dash = new Watcher();
		dash.watch(drive, leftIn.getWatchable("left position"), rightIn.getWatchable("right position"));
	}

	@Override
	public void update(GameContainer arg0, int arg1) throws SlickException {
		drive.update();
		left.update();
		right.update();
		state.addFieldToVehicleObservation(Timer.getFPGATimestamp(),
				state.getLatestFieldToVehicle().getValue()
						.transformBy(RigidTransform2d.fromVelocity(new Kinematics(20, 23, 1)
								.forwardKinematics(leftIn.get() - leftDistance, rightIn.get() - rightDistance))));
		leftDistance = leftIn.get();
		rightDistance = rightIn.get();
		dash.publish(Watcher.DASHBOARD);
		System.out.println(state.getLatestFieldToVehicle().getValue().getRotation().getDegrees());
	}

}
