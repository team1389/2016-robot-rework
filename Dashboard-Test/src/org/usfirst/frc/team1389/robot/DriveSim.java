package org.usfirst.frc.team1389.robot;

import org.newdawn.slick.AppGameContainer;
import org.newdawn.slick.BasicGame;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Image;
import org.newdawn.slick.SlickException;

import com.team1389.system.SystemManager;
import com.team1389.system.drive.SimBotDriveSystem;
import com.team1389.watch.Watcher;

import input.SimJoystick;
import motor_sim.SimRobot;

public class DriveSim extends BasicGame {

	public DriveSim(String title) {
		super(title);
	}

	public static void main(String[] args) throws SlickException {
		Tester.initWPILib();
		AppGameContainer cont = new AppGameContainer(new DriveSim("Drive Sim"));
		cont.setTargetFrameRate(50);
		cont.setDisplayMode(1265, 622, false);
		cont.start();
	}

	SimRobot robot;
	SimJoystick joy = new SimJoystick(1);
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
		dash = new Watcher();
		try {
			map = new Image("map.png");
		} catch (SlickException e) {
			e.printStackTrace();
		}
		robot = new SimRobot();
		drive = new SimBotDriveSystem(robot.getDrive(), joy.getAxis(0).invert().applyDeadband(.1), joy.getAxis(1).invert().applyDeadband(.1));
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
