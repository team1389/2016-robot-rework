package com.team1389.util.state;
/**
 * tracks the state of a system, allowing you to call methods on state change and 
 * @author amind
 *
 */
public class StateTracker {
	StateSetup lastState;
	
	public StateTracker(){
		lastState = null;
	}
	
	public State newState(StateSetup setup){
		State state = new TrackedState(setup);
		return state;
	}
	
	private class TrackedState implements State{
		
		StateSetup setup;
		
		public TrackedState(StateSetup setup) {
			this.setup = setup;
		}

		@Override
		public void init() {
			if (lastState != setup){
				if (lastState != null){
					lastState.end();
				}
				lastState = setup;
				setup.setup();
			}
		}
		
	}
}
