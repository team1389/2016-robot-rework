package simulation;

import java.util.ArrayList;

import org.newdawn.slick.AppGameContainer;
import org.newdawn.slick.BasicGame;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Image;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.geom.Line;

import com.team1389.hardware.inputs.software.DigitalIn;
import com.team1389.system.SystemManager;
import com.team1389.system.drive.SimboticsDriveSystem;
import com.team1389.watch.Watcher;

import net.java.games.input.Component.Identifier.Key;
import simulation.input.Axis;
import simulation.input.KeyboardHardware;

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
	// SimJoystick joy = new SimJoystick(1);
	SimboticsDriveSystem drive;

	Watcher dash;
	SystemManager manager = new SystemManager();
	Image map;

	@Override
	public void render(GameContainer container, Graphics g) throws SlickException {
		map.draw(0, 0, 1265, 622);
		robot.render(container, g);
	}

	DigitalIn undo;

	@Override
	public void init(GameContainer arg0) throws SlickException {
		KeyboardHardware hardware = new KeyboardHardware();
		undo = hardware.getKey(Key.LCONTROL).combineAND(hardware.getKey(Key.Z).getLatched());
		dash = new Watcher();
		try {
			map = new Image("map.png");
		} catch (SlickException e) {
			e.printStackTrace();
		}
		ArrayList<Line> lines = new ArrayList<Line>();
		int buffer = 0;
		lines.add(new Line(buffer, buffer, buffer, 622 - buffer));
		lines.add(new Line(buffer, 622 - buffer, 1265 - buffer, 622 - buffer));
		lines.add(new Line(1265 - buffer, 622 - buffer, 1265 - buffer, buffer));
		lines.add(new Line(1265 - buffer, buffer, buffer, buffer));
		lines.add(new Line(150, 150, 0, 0));

		robot = new SimulationRobot(lines, true);
		drive = new SimboticsDriveSystem(robot.getDrive(), Axis.make(hardware, Key.UP, Key.DOWN, 0.5),
				Axis.make(hardware, Key.LEFT, Key.RIGHT, 0.5));
		manager.register(drive);
		dash.watch(drive);

	}

	@Override
	public void update(GameContainer gc, int delta) throws SlickException {
		manager.update();
		dash.publish(Watcher.DASHBOARD);
		robot.update();

	}

}
