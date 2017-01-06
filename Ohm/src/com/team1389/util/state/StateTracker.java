package com.team1389.util.state;

/**
 * tracks the state of a system, allowing you to call methods on state change and
 * 
 * @author Jacob Prinz
 *
 */
public class StateTracker {
	StateSetup lastState;

	/**
	 * initializes the stateTracker
	 */
	public StateTracker() {
		lastState = null;
	}
	
	/**
	 * creates a state and tracks it
	 * @param setup the state to track
	 * @return the tracked version of the state
	 */
	public State newState(StateSetup setup) {
		State state = new TrackedState(setup);
		return state;
	}

	private class TrackedState implements State {

		StateSetup setup;

		public TrackedState(StateSetup setup) {
			this.setup = setup;
		}

		@Override
		public void init() {
			if (lastState != setup) {
				if (lastState != null) {
					lastState.end();
				}
				lastState = setup;
				setup.setup();
			}
		}

	}
}
