package simulation;

import java.util.ArrayList;
import java.util.Arrays;

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
import org.newdawn.slick.geom.Vector2f;

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
	private boolean drawLines = false;
	
	public SimulationRobot(ArrayList<Line> boundries, boolean drawLines){
		this(boundries);
		this.drawLines = drawLines;
	}

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

		boolean intersects = false;
		for(Line l : boundries){

			if(findDistanceToTranslate(l) != null){
				intersects = true;
				break;
			}
		}

	}

	Line[] someLines = null;
	Line toPrint = null;
	Point tp = null;
	private Vector2f findDistanceToTranslate(Line l) {
		float[] points = getBoundingBox().getPoints();
		Line[] lines = new Line[points.length / 2];
		for(int i = 0; i < points.length; i+=2){
			lines[i / 2] = new Line(getX(), getY(), points[i], points[i + 1]);
			Vector2f poi = lines[i / 2].intersect(l, true);
			if(poi != null){
				float dx = points[i] - poi.x;
				float dy = points[i + 1] - poi.y;
				tp = new Point(poi.x, poi.y);
				toPrint = new Line(poi.x, poi.y, poi.x + dx, poi.y + dy);
				return new Vector2f(dx, dy);
			}

		}
		someLines = lines;
		return null;

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

	private float getX(){
		Translation2d trans = getTransform().getTranslation();
		return 2 * (float) (trans.getX() + startX);
	}


	private float getY(){
		Translation2d trans = getTransform().getTranslation();
		return 2 * (float) (trans.getY() + startY);
	}

	private RigidTransform2d getTransform(){
		return  state.getLatestFieldToVehicle().getValue();
	}


	public void render(GameContainer container, Graphics g) throws SlickException {

		Rotation2d rot = getTransform().getRotation();
		float renderX = getX();
		float renderY = getY();
		robot.setRotation((float) rot.getDegrees());
		robot.setCenterOfRotation(34, 35);
		robot.drawCentered(renderX, renderY);

		if(drawLines){
			g.setLineWidth(2);
			g.setColor(Color.orange);
			for(Line l : boundries){
				g.draw(l);
			}


			g.draw(getBoundingBox());
			if(someLines != null){
				g.setColor(Color.green);
				for(Line l : someLines){
					if(l != null)
						g.draw(l);
				}
				someLines = null;
			}

		}



		g.setColor(Color.white);
		g.rotate(renderX, renderY, (float) rot.getDegrees());
		g.fillOval(renderX - 5, renderY - 5, 10, 10);
		g.setLineWidth(1);
		g.setColor(Color.pink);



		if(drawLines){
			g.rotate(renderX, renderY, (float) -rot.getDegrees());
			g.setLineWidth(4);
			if(toPrint != null){
				g.setColor(Color.magenta);
				g.draw(toPrint);
				g.fillOval(tp.getX() - 5, tp.getY() - 5, 10, 10);
			}
			toPrint = null;
		}



	}

	private Polygon getBoundingBox(){
		Rotation2d rot = getTransform().getRotation();
		float renderX = getX();
		float renderY = getY();
		Polygon r = new Polygon();
		r.addPoint(renderX - robot.getWidth() / 2, renderY - robot.getWidth() / 2);
		r.addPoint(renderX + robot.getWidth() / 2, renderY - robot.getWidth() / 2);
		r.addPoint(renderX + robot.getWidth() / 2, renderY + robot.getWidth() / 2);
		r.addPoint(renderX - robot.getWidth() / 2, renderY + robot.getWidth() / 2);

		r = (Polygon) r.transform(Transform.createRotateTransform((float) rot.getRadians(), renderX, renderY));
		return r;
	}
}
