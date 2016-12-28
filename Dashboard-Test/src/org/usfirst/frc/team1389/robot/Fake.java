package org.usfirst.frc.team1389.robot;

import java.util.ArrayList;
import java.util.List;

import edu.wpi.first.wpilibj.util.AllocationException;

public class Fake {
	static List<Integer> used = new ArrayList<Integer>();
	static long start = System.currentTimeMillis();

	public Fake(int port) {
		if (!used.add(port)) {
			throw new AllocationException("port " + port + " in use");
		}
	}

	public double getSystemTime() {
		return (System.currentTimeMillis() - start) / 1000;
	}
}
