package com.team1389.hardware.watch;

public interface Watchable {
	public String getName();
	public Info[] getInfo();
	public default void display(){
		for(Info e:getInfo()){
			e.display();
		}
	}
	public default String getPrintString(){
		String s="";
		for(Info e:getInfo()){
			s=String.join(s,e.toString()+" ");
		}
		return s;
	}
	public default String loggable(){
		String s="";
		for(Info e:getInfo()){
			s=String.join(s,e.loggable()+"\t");
		}
		return s;
	}
}
