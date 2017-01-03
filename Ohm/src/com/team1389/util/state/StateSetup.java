package com.team1389.util.state;

/**
 * a state that can recieve notification when it is exited
 * 
 * @author Jacob Prinz
 *
 */
@FunctionalInterface

public interface StateSetup {
	/**
	 * initializes this state
	 */
	void setup();

	/**
	 * can be called on state end
	 */
	default void end() {
	}
}
