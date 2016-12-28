package com.team1389.watch;

import java.util.function.UnaryOperator;

import com.team1389.util.AddList;

import edu.wpi.first.wpilibj.tables.ITable;

public interface CompositeWatchable extends Watchable {
	static AddList<Watchable> stem = new AddList<Watchable>();

	public AddList<Watchable> getSubWatchables(AddList<Watchable> stem);

	@Override
	default void publishUnderName(String name, ITable table) {
		getSubWatchables(stem).forEach(w -> w.publish(name, table));
	}

	// TODO fix these
	@Override
	public default String getPrintString() {
		String s = "";
		return s;
	}

	@Override
	public default double getLoggable() {
		return 0;
	}

	public static CompositeWatchable of(String name, UnaryOperator<AddList<Watchable>> subWatchables) {
		return new CompositeWatchable() {

			@Override
			public String getName() {
				return name;
			}

			@Override
			public AddList<Watchable> getSubWatchables(AddList<Watchable> stem) {
				return subWatchables.apply(stem);
			}

		};
	}
}
