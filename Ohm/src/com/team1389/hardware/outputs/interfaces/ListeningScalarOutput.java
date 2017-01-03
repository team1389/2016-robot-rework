package com.team1389.hardware.outputs.interfaces;

import com.team1389.hardware.value_types.Value;
/**
 * stream of doubles with a ChangeListener
 * @author Kenneth
 *
 * @param <T> type of value that the double is representing
 */
public class ListeningScalarOutput<T extends Value> implements ScalarOutput<T> {
	private ScalarOutput<T> out;
	private Runnable onChange;
	private boolean check;
	double oldVal;

	protected ListeningScalarOutput(ScalarOutput<T> out, Runnable onChange) {
		this.onChange = onChange;
		this.out = out;
		}
	
	@Override
	public void set(double val) {
		out.set(val);
		if(!check){
			oldVal = val;
		}
		if(val!=oldVal){
		onChange.run();
		}
		oldVal = val;
	}
}
