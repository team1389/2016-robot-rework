package com.team1389.hardware.watch;

public abstract class Info {
	String name;
	public Info(String name){
		this.name=name;
	}
	public String getName(){
		return name;
	}
	public abstract void display();
	public abstract String toString();
	public abstract double loggable();
}
