package com.team1389.watch;

import java.io.FileWriter;
import java.util.Map;


import com.team1389.util.Optional;

import edu.wpi.first.wpilibj.tables.ITable;

/**
 * represents a stream of values that can be added to ITable
 * 
 * @author Kenneth
 *
 */
public interface Watchable {

	public String getName();
	

	/**
	 * @param name that the value will be stored under
	 * @param table where key+value is published to
	 */
	void publishUnderName(String name, ITable table);
	Map<String,Watchable> getFlat(Optional<String> parent);
	/**
	 * adds parent to the name, then uses <publishUnderName>
	 * 
	 * @param parent additional organizer
	 * @param table where key+value is published to
	 */
	public default void publish(String parent, ITable table) {
		publishUnderName(parent + "." + getName(), table);
	}

	/**
	 * publishing using name provided by object
	 * 
	 * @param table where key+value is published to
	 */
	public default void publish(ITable table) {
		publishUnderName(getName(), table);
	}

	public default String getFullName(String parent) {
		return parent + "." + getName();
	}
	public default double getLoggable(){
		return 0;
	}
		
	

	public String getPrintString();

	
}
