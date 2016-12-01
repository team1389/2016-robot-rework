package com.team1389.hardware.registry;

import java.util.HashSet;
import java.util.Set;

public class ResourceManager<T>{
	
	private Set<T> used;
	
	public ResourceManager() {
		used = new HashSet<T>();
	}
	
	public boolean isUsed(T t){
		return used.contains(t);
	}
	
	public void setUsed(T t){
		used.add(t);
	}
}
