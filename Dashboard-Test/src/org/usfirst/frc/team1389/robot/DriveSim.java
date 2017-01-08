package org.usfirst.frc.team1389.robot;

import org.newdawn.slick.AppGameContainer;
import org.newdawn.slick.BasicGame;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Image;
import org.newdawn.slick.SlickException;

import com.team1389.hardware.inputs.interfaces.BinaryInput;
import com.team1389.hardware.inputs.software.DigitalIn;
import com.team1389.hardware.inputs.software.PercentIn;
import com.team1389.system.SystemManager;
import com.team1389.system.drive.SimBotDriveSystem;
import com.team1389.watch.Watcher;

import input.PCHardware;
import input.SimJoystick;
import motor_sim.SimRobot;
import net.java.games.input.Component.Identifier.Key;

public class DriveSim extends BasicGame {

	public DriveSim(String title) {
		super(title);

	}

	public static void main(String[] args) throws SlickException {
		Tester.initWPILib();
		DriveSim sim = new DriveSim("DriveSim");
		AppGameContainer cont = new AppGameContainer(sim);
		cont.setTargetFrameRate(50);
		cont.setDisplayMode(1265, 622, false);
		cont.start();
	}

	boolean pressed = false;
	SimRobot robot;
	//SimJoystick joy = new SimJoystick(1);
	SimBotDriveSystem drive;
	Watcher dash;
	SystemManager manager=new SystemManager();
	Image map;

	@Override
	public void render(GameContainer container, Graphics g) throws SlickException {
		map.draw(0, 0, 1265, 622);
		robot.render(container, g);
	}

	@Override
	public void init(GameContainer arg0) throws SlickException {
		PCHardware hardware = new PCHardware();
		dash = new Watcher();
		try {
			map = new Image("map.png");
		} catch (SlickException e) {
			e.printStackTrace();
		}
		robot = new SimRobot();
		drive = new SimBotDriveSystem(robot.getDrive(), new PercentIn(() -> 
		hardware.getKey(Key.UP).getLatched().get()? 0.5:
				hardware.getKey(Key.DOWN).getLatched().get()? -0.5: 0.0
		), 
				new PercentIn(() -> hardware.getKey(Key.LEFT).getLatched().get()? 0.5:
					hardware.getKey(Key.RIGHT).getLatched().get()? -.5: 0.0));
		manager.register(drive);
		dash.watch(drive);
		
	}

	@Override
	public void update(GameContainer gc, int delta) throws SlickException {
		manager.update();
		dash.publish(Watcher.DASHBOARD);
		robot.update(gc, delta);
	}

}
