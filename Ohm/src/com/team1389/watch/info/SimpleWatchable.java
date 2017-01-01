package com.team1389.watch.info;

import java.io.IOException;
import java.io.Writer;
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

	public abstract double getLoggable();

	@Override
	public void log(Writer f) {
		try {
			f.append(Double.toString(this.getLoggable()));
			f.append("\t");
		} catch (IOException e) {
			System.out.println(e.getMessage());
		}
	}

	@Override
	public void logKey(Writer f) {
		try {
			f.append(this.getName());
			f.append("\t");
		} catch (IOException e) {
			System.out.println(e.getMessage());
		}
	}

	@Override
	public String getName() {
		return name;
	}

	@Override
	public Map<String, SimpleWatchable> getFlat(Optional<String> parent) {
		Map<String, SimpleWatchable> map = new HashMap<>();
		map.put(parent.ifPresent(getName(), this::getFullName).get(), this);
		return map;
	}
}
