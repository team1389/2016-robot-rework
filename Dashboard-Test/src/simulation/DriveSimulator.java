package simulation;

import java.util.ArrayList;
import java.util.List;

import org.newdawn.slick.AppGameContainer;
import org.newdawn.slick.BasicGame;
import org.newdawn.slick.Color;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Image;
import org.newdawn.slick.Input;
import org.newdawn.slick.SlickException;

import com.team1389.hardware.inputs.software.DigitalIn;
import com.team1389.hardware.value_types.Speed;
import com.team1389.system.SystemManager;
import com.team1389.system.drive.DriveOut;
import com.team1389.system.drive.PathFollowingSystem;
import com.team1389.system.drive.SimboticsDriveSystem;
import com.team1389.trajectory.Path;
import com.team1389.trajectory.Path.Waypoint;
import com.team1389.trajectory.RobotStateEstimator;
import com.team1389.trajectory.Translation2d;
import com.team1389.util.RangeUtil;
import com.team1389.watch.Watcher;

import simulation.input.SimJoystick;

public class DriveSimulator extends BasicGame {

	public DriveSimulator(String title) {
		super(title);

	}

	public static void main(String[] args) throws SlickException {
		Simulator.initWPILib();
		DriveSimulator sim = new DriveSimulator("DriveSim");
		AppGameContainer cont = new AppGameContainer(sim);
		cont.setTargetFrameRate(50);
		cont.setDisplayMode(1265, 622, false);
		cont.start();
	}

	boolean pressed = false;
	SimulationRobot robot;
	SimJoystick joy = new SimJoystick(0);
	SimboticsDriveSystem drive;

	Watcher dash;
	SystemManager manager = new SystemManager();
	Image map;

	@Override
	public void render(GameContainer container, Graphics g) throws SlickException {
		map.draw(0, 0, 1265, 622);
		robot.render(container, g);
		g.setColor(Color.black);
		g.fillOval((float) state.get().getTranslation().getX() - 5, (float) state.get().getTranslation().getY() - 5, 10, 10);
		points.forEach(p -> g.fillOval((float) p.position.getX() - 5, (float) p.position.getY() - 5, 10, 10));
	}

	DigitalIn trigger;
	DigitalIn pointer;

	@Override
	public void init(GameContainer arg0) throws SlickException {
		input = new Input(0);
		points = new ArrayList<>();
		trigger = new DigitalIn(() -> input.isKeyDown(Input.KEY_SPACE)).getLatched();
		pointer = new DigitalIn(() -> input.isMouseButtonDown(Input.MOUSE_LEFT_BUTTON)).getLatched();
		dash = new Watcher();
		try {
			map = new Image("map.png");
		} catch (SlickException e) {
			e.printStackTrace();
		}
		robot = new SimulationRobot();
		state = new RobotStateEstimator(robot.state, robot.leftIn, robot.rightIn, robot.left.getSpeedInput(),
				robot.right.getSpeedInput(), robot.gyro, robot.kinematics);
		apps = new PathFollowingSystem(new DriveOut<Speed>(robot.leftSpeed, robot.rightSpeed),
				state,
				10000, 500000);
		drive = new SimboticsDriveSystem(robot.getDrive(), joy.getAxis(0).applyDeadband(0.1).invert(),
				joy.getAxis(1).applyDeadband(0.1).invert());
		manager.register(apps);
		dash.watch(apps);

	}
	RobotStateEstimator state;
	Input input;
	List<Waypoint> points;
	PathFollowingSystem apps;

	@Override
	public void update(GameContainer gc, int delta) throws SlickException {
		manager.update();
		if (pointer.get()) {
			points.add(new Waypoint(new Translation2d(input.getMouseX(),
					RangeUtil.map(-input.getMouseY(), 4, gc.getHeight(), gc.getHeight(), 4)), 0));
		}
		if (trigger.get()) {
			apps.followPath(new Path(points), false);
		}
		dash.publish(Watcher.DASHBOARD);
		robot.update();
	}

}
