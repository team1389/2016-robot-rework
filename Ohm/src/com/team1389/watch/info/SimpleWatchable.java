package com.team1389.watch.info;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

import com.team1389.watch.Watchable;

/**
 * an abstract class forcing the ability to return a name(key)
 * 
 * @author Kenneth
 *
 */
public abstract class SimpleWatchable implements Watchable {
	protected String name;

	/**
	 * @param name the name of this info
	 */
	public SimpleWatchable(String name) {
		this.name = name;
	}

	/**
	 * @return a number representation of this info for spreadsheet logs
	 */
	public abstract double getLoggable();

	@Override
	public String getName() {
		return name;
	}

	@Override
	public Map<String, SimpleWatchable> getFlat(Optional<String> parent) {
		Map<String, SimpleWatchable> map = new HashMap<>();
		map.put(parent.map(this::getFullName).orElse(getName()), this);
		return map;
	}
}
