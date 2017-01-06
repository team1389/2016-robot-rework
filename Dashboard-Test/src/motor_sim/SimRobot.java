package motor_sim;

import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Image;
import org.newdawn.slick.SlickException;

import com.team1389.hardware.inputs.software.AngleIn;
import com.team1389.hardware.inputs.software.RangeIn;
import com.team1389.hardware.value_types.Percent;
import com.team1389.hardware.value_types.Position;
import com.team1389.system.drive.DriveOut;
import com.team1389.trajectory.Kinematics;
import com.team1389.trajectory.RigidTransform2d;
import com.team1389.trajectory.RobotState;
import com.team1389.trajectory.Rotation2d;
import com.team1389.trajectory.Translation2d;

import edu.wpi.first.wpilibj.Timer;
import motor_sim.Motor.MotorType;
import motor_sim.element.CylinderElement;

public class SimRobot {
	Image robot;
	MotorSystem left = new MotorSystem(new Attachment(new CylinderElement(.51, .097), false), 6,
			new Motor(MotorType.CIM));
	MotorSystem right = new MotorSystem(new Attachment(new CylinderElement(.51, .097), false), 6,
			new Motor(MotorType.CIM));
	RobotState state = new RobotState();
	AngleIn<Position> gyro = new AngleIn<Position>(Position.class,
			() -> state.getLatestFieldToVehicle().getValue().getRotation().getDegrees());
	RangeIn<Position> leftIn = left.getPositionInput().mapToRange(0, 1).scale(Math.PI * 7.65);
	RangeIn<Position> rightIn = right.getPositionInput().mapToRange(0, 1).scale(Math.PI * 7.65);
	double leftDistance = 0;
	double rightDistance = 0;
	double startX = 250;
	double startY = 250;

	public SimRobot() {
		state.reset(Timer.getFPGATimestamp(), new RigidTransform2d(new Translation2d(), new Rotation2d()));
		try {
			robot = new Image("robot.png").getScaledCopy(68, 70);
		} catch (SlickException e) {
			e.printStackTrace();
		}
		robot.setCenterOfRotation(34, 35);

	}

	public void update(GameContainer gc, int delta) {
		left.update();
		right.update();
		state.addFieldToVehicleObservation(Timer.getFPGATimestamp(),
				state.getLatestFieldToVehicle().getValue()
						.transformBy(RigidTransform2d.fromVelocity(new Kinematics(20, 23, 1)
								.forwardKinematics(leftIn.get() - leftDistance, rightIn.get() - rightDistance))));
		leftDistance = leftIn.get();
		rightDistance = rightIn.get();
	}

	public DriveOut<Percent> getDrive() {
		return new DriveOut<Percent>(left.getVoltageOutput(), right.getVoltageOutput());
	}

	public void render(GameContainer container, Graphics g) throws SlickException {
		RigidTransform2d transform = state.getLatestFieldToVehicle().getValue();
		Translation2d trans = transform.getTranslation();
		Rotation2d rot = transform.getRotation();
		float renderX = 2 * (float) (trans.getX() + startX);
		float renderY = 2 * (float) (trans.getY() + startY);
		robot.setRotation((float) rot.getDegrees());
		robot.drawCentered(renderX, renderY);
	}

}
