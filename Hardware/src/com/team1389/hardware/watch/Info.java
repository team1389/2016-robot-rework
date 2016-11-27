package com.team1389.hardware.watch;

import edu.wpi.first.wpilibj.tables.ITable;

public abstract class Info {
	String name;
	public Info(String name){
		this.name=name;
	}
	public String getName(){
		return name;
	}
	public abstract void publish(ITable table);
	public abstract String toString();
	public abstract double loggable();
}
