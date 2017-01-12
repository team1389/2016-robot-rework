package simulation;

import java.util.ArrayList;

import org.newdawn.slick.Color;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Image;
import org.newdawn.slick.Input;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.geom.Line;
import org.newdawn.slick.geom.Point;
import org.newdawn.slick.geom.Polygon;
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
import simulation.motor.Motor.MotorType;
import simulation.motor.MotorSystem;
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
	private boolean collision = false;

	public SimulationRobot(ArrayList<Line> boundries, boolean collision){
		this(boundries);
		this.collision = collision;
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

	private Vector2f extraTranslate = null;
	public void update() {

		left.update();
		right.update();
		state.addFieldToVehicleObservation(Timer.getFPGATimestamp(),
				state.getLatestFieldToVehicle().getValue()
				.transformBy(RigidTransform2d.fromVelocity(new Kinematics(10, 23, .8)
						.forwardKinematics(leftIn.get() - leftDistance, rightIn.get() - rightDistance))));
		leftDistance = leftIn.get();
		rightDistance = rightIn.get();


		if(collision){
			for(Line l : boundries){
				//System.out.println(checkCollision(l));
				Vector2f translate = findDistanceToTranslate(l);
				if(translate != null){
					extraTranslate = translate.scale(-1).add(extraTranslate != null? extraTranslate: new Vector2f(0,0));
				}
				/*while(checkCollision(l)){
					double distanceFromLine = l.distance(new Vector2f(getX(), getY()));
					Vector2f possibleTranslate = new Vector2f(getWheelLines()[0].getX(), getWheelLines()[0].getY()).scale((float) .01);
					double secondDistance = l.distance(new Vector2f(getX(), getY()).add(possibleTranslate));
					double thirdDistance = l.distance(new Vector2f(getX(), getY()).add(possibleTranslate.scale(-1)));
					System.out.println(distanceFromLine + " " + secondDistance + " " + thirdDistance);
					//System.out.println(new Vector2f(getX(), getY()).add(possibleTranslate));
					if(distanceFromLine < secondDistance){
						possibleTranslate.scale(-1);
					}
					//System.out.print(distanceFromLine);
					//System.out.println(" " + secondDistance);
					//System.out.println(possibleTranslate);
					extraTranslate = possibleTranslate.add(extraTranslate != null? extraTranslate: new Vector2f(0,0));
					//System.out.println(extraTranslate);
				}*/
			}
		}


	}

	Line someLine = null;
	Line toPrint = null;
	Point tp = null;
	private Vector2f findDistanceToTranslate(Line l) {
		for(Line wheelLine : getWheelLines()){
			Vector2f poi = wheelLine.intersect(l, true);
			if(poi != null){
				Vector2f one = new Vector2f(wheelLine.getX1(), wheelLine.getY1());
				Vector2f two = new Vector2f(wheelLine.getX2(), wheelLine.getY2());
				Vector2f opperating;
				if(l.intersect(new Line(one, new Vector2f(getX(), getY())), true) != null){
					opperating = one;
				}
				else{
					opperating = two;
				}

				float dx = opperating.x - poi.x;
				float dy = opperating.y - poi.y;
				tp = new Point(poi.x, poi.y);
				toPrint = new Line(poi.x, poi.y, poi.x + dx, poi.y + dy);
				someLine = new Line(opperating, new Vector2f(getX(), getY()));
				return new Vector2f(dx, dy);
			}

		}
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
		return 2 * (float) (trans.getX() + startX)  + (extraTranslate != null? extraTranslate.x : 0);
	}

	private float getY(){
		Translation2d trans = getTransform().getTranslation();
		return 2 * (float) (trans.getY() + startY) + (extraTranslate != null? extraTranslate.y : 0);
	}

	private RigidTransform2d getTransform(){
		return  state.getLatestFieldToVehicle().getValue();
	}

	private ArrayList<Point> points = new ArrayList<Point>();
	public void render(GameContainer container, Graphics g) throws SlickException {
		Input input = container.getInput();
		int xpos = input.getMouseX();
		int ypos = input.getMouseY();

		if(input.isMousePressed(0)){
			points.add(new Point(xpos, ypos));
		}
		if(input.isMousePressed(1)){
			points.add(null);
		}

		if(points.size() > 1){
			Point point1 = points.get(points.size() - 1);
			Point point2 = points.get(points.size() - 2);
			if(point1 != null && point2 != null){
				boundries.add(new Line(point1.getX(), point1.getY(), point2.getX(), point2.getY()));
			}
		}

		Rotation2d rot = getTransform().getRotation();
		float renderX = getX();
		float renderY = getY();
		robot.setRotation((float) rot.getDegrees());
		robot.setCenterOfRotation(34, 35);
		robot.drawCentered(renderX, renderY);
		g.setColor(Color.white);
		g.fillOval(renderX - 5, renderY - 5, 10, 10);

		if(collision){
			g.setLineWidth(2);
			g.setColor(Color.orange);
			for(Line l : boundries){
				g.draw(l);
			}


			g.draw(getBoundingBox());
			if(someLine != null){
				g.setColor(Color.green);
				g.draw(someLine);
				someLine = null;
			}

		}

		if(collision){
			g.setLineWidth(4);
			if(toPrint != null){
				g.setColor(Color.magenta);
				g.draw(toPrint);
				g.fillOval(tp.getX() - 5, tp.getY() - 5, 10, 10);
			}
			toPrint = null;

			for(Line l : getWheelLines()){
				g.setColor(Color.cyan);
				g.draw(l);
			}
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

	private Line[] getWheelLines(){
		Rotation2d rot = getTransform().getRotation();
		float renderX = getX();
		float renderY = getY();
		Line one = new Line(renderX + robot.getWidth() / 2, renderY + robot.getHeight() / 2, 
				renderX - robot.getWidth() / 2, renderY + robot.getHeight() / 2);
		Line two = new Line(renderX + robot.getWidth() / 2, renderY - robot.getHeight() / 2, 
				renderX - robot.getWidth() / 2, renderY - robot.getHeight() / 2);
		one = (Line) one.transform(Transform.createRotateTransform((float) rot.getRadians(), renderX , renderY));
		two = (Line) two.transform(Transform.createRotateTransform((float) rot.getRadians(), renderX, renderY));
		return new Line[]{one, two};
	}
}
