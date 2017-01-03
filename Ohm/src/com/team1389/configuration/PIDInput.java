package com.team1389.configuration;

import java.util.function.Consumer;

import com.team1389.util.AddList;
import com.team1389.watch.CompositeWatchable;
import com.team1389.watch.Watchable;
import com.team1389.watch.input.NumberInput;

/**
 * represents a set of PID constants that can be tuned on the smart dashboard
 * 
 * @author amind
 *
 */
public class PIDInput implements CompositeWatchable {
	NumberInput p;
	NumberInput i;
	NumberInput d;
	String name;

	/**
	 * @param name the string identifier of this tuner
	 * @param defaultVal the default PIDconstants
	 * @param fun an action to perform when any one of the tuner values changes
	 */
	public PIDInput(String name, PIDConstants defaultVal, Consumer<PIDConstants> fun) {
		this.name = name;
		this.p = new NumberInput("p", defaultVal.p, (Double newP) -> {
			fun.accept(new PIDConstants(newP, i.get(), d.get()));
		});
		this.i = new NumberInput("i", defaultVal.i, (Double newI) -> {
			fun.accept(new PIDConstants(p.get(), newI, d.get()));
		});
		this.d = new NumberInput("d", defaultVal.d, (Double newD) -> {
			fun.accept(new PIDConstants(p.get(), i.get(), newD));
		});
	}

	@Override
	public String getName() {
		return name;
	}

	@Override
	public AddList<Watchable> getSubWatchables(AddList<Watchable> stem) {
		return stem.put(p, i, d);
	}
}
