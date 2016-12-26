package com.team1389.configuration;

import java.util.function.Consumer;

import com.team1389.watch.CompositeWatchable;
import com.team1389.watch.Watchable;
import com.team1389.watch.info.NumberInput;

public class PIDInput implements CompositeWatchable {
	NumberInput p;
	NumberInput i;
	NumberInput d;
	String name;

	public PIDInput(String name, PIDConstants old, Consumer<PIDConstants> fun) {
		this.name = name;
		this.p = new NumberInput("P", old.p, (Double newP) -> {
			fun.accept(new PIDConstants(newP, i.get(), d.get()));
		});
		this.i = new NumberInput("I", old.i, (Double newI) -> {
			fun.accept(new PIDConstants(p.get(), newI, d.get()));
		});
		this.d = new NumberInput("D", old.d, (Double newD) -> {
			fun.accept(new PIDConstants(p.get(), i.get(), newD));
		});
	}

	@Override
	public String getName() {
		return name;
	}

	@Override
	public Watchable[] getSubWatchables() {
		return new Watchable[] { p, i, d };
	}
}
