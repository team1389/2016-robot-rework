package com.team1389.util.state;

/**
 * represents a state that can be initialized
 * 
 * @author Jacob Prinz
 *
 */
public interface State extends Runnable, StateSetup {
	@Override
	public default void run() {
		init();
	}

	@Override
	public default void setup() {
		init();
	}

	/**
	 * initializes the state
	 */
	public void init();
}
