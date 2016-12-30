package com.team1389.watch.info;

import java.io.FileWriter;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import com.team1389.util.Optional;
import com.team1389.watch.Watchable;

/**
 * an abstract class forcing the ability to return a name(key)
 * 
 * @author Kenneth
 *
 */
public abstract class SimpleWatchable implements Watchable {
	protected String name;

	public SimpleWatchable(String name) {
		this.name = name;
	}
	public double getLoggable(){
		return 0;
	}
	public void log(FileWriter f) {
		try{
		f.append(Double.toString(this.getLoggable()));
		f.append("\t");
		}catch(IOException e){
			System.out.println(e.getMessage());
		}
	}
	public  void logKey(FileWriter f){
		try{
			f.append(this.getName());
			f.append("t");
		}catch(IOException e){
			System.out.println(e.getMessage());
		}
	}

	@Override
	public String getName() {
		return name;
	}

	@Override
	public Map<String, Watchable> getFlat(Optional<String> parent) {
		Map<String, Watchable> map = new HashMap<>();
		map.put(parent.ifPresent(getName(), this::getFullName).get(), this);
		return map;
	}
}
