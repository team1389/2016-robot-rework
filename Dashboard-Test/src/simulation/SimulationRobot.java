package simulation;

import java.util.ArrayList;

import org.newdawn.slick.Color;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Image;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.geom.Line;
import org.newdawn.slick.geom.Point;
import org.newdawn.slick.geom.Polygon;
import org.newdawn.slick.geom.Rectangle;
import org.newdawn.slick.geom.Transform;

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
import simulation.motor.Attachment;
import simulation.motor.Motor;
import simulation.motor.MotorSystem;
import simulation.motor.Motor.MotorType;
import simulation.motor.element.CylinderElement;

public class SimulationRobot {
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
	ArrayList<Line> boundries;

	public SimulationRobot(ArrayList<Line> boundries) {
		state.reset(Timer.getFPGATimestamp(), new RigidTransform2d(new Translation2d(), new Rotation2d()));
		try {
			robot = new Image("robot.png").getScaledCopy(68, 70);
		} catch (SlickException e) {
			e.printStackTrace();
		}
		this.boundries = boundries;

	}
	
	public SimulationRobot(){
		this(new ArrayList<Line>());
	}

	public void update() {
		left.update();
		right.update();
		state.addFieldToVehicleObservation(Timer.getFPGATimestamp(),
				state.getLatestFieldToVehicle().getValue()
				.transformBy(RigidTransform2d.fromVelocity(new Kinematics(10, 23, .8)
						.forwardKinematics(leftIn.get() - leftDistance, rightIn.get() - rightDistance))));
		leftDistance = leftIn.get();
		rightDistance = rightIn.get();
	}

	public boolean checkCollision(Line l){
		return getBoundingBox().intersects(l);
	}

	public DriveOut<Percent> getDrive() {
		return new DriveOut<Percent>(left.getVoltageOutput(), right.getVoltageOutput());
	}

	public AngleIn<Position> getHeadingIn() {

		return new AngleIn<Position>(Position.class,
				() -> state.getLatestFieldToVehicle().getValue().getRotation().getDegrees());
	}

	public void render(GameContainer container, Graphics g) throws SlickException {

		RigidTransform2d transform = state.getLatestFieldToVehicle().getValue();
		Translation2d trans = transform.getTranslation();
		Rotation2d rot = transform.getRotation();
		float renderX = 2 * (float) (trans.getX() + startX);
		float renderY = 2 * (float) (trans.getY() + startY);
		robot.setRotation((float) rot.getDegrees());
		robot.setCenterOfRotation(34, 35);
		robot.drawCentered(renderX, renderY);

		g.setColor(Color.white);
		g.rotate(renderX, renderY, (float) rot.getDegrees());
		g.fillOval(renderX - 5, renderY - 5, 10, 10);
		g.setColor(Color.pink);
		g.setLineWidth(4);
		g.drawRect(renderX - robot.getWidth() / 2, renderY - robot.getWidth() / 2, robot.getWidth(), robot.getHeight());
		g.setColor(Color.blue);
		g.draw(getBoundingBox());
	}

	private Polygon getBoundingBox(){
		RigidTransform2d transform = state.getLatestFieldToVehicle().getValue();
		Translation2d trans = transform.getTranslation();
		Rotation2d rot = transform.getRotation();
		float renderX = 2 * (float) (trans.getX() + startX);
		float renderY = 2 * (float) (trans.getY() + startY);
		Polygon r = new Polygon();
		r.addPoint(renderX - robot.getWidth() / 2, renderY - robot.getWidth() / 2);
		r.addPoint(renderX + robot.getWidth() / 2, renderY - robot.getWidth() / 2);
		r.addPoint(renderX + robot.getWidth() / 2, renderY + robot.getWidth() / 2);
		r.addPoint(renderX - robot.getWidth() / 2, renderY + robot.getWidth() / 2);

		r.transform(Transform.createRotateTransform((float) rot.getRadians()));
		return r;
	}
}
