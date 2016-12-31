package com.team1389.watch;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.team1389.util.AddList;
import com.team1389.util.Optional;

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

	@Override
	public default Map<String, Watchable> getFlat(Optional<String> parent) {
		Map<String, Watchable> map = new HashMap<>();
		getSubWatchables(stem)
				.forEach(w -> map.putAll(w.getFlat(Optional.of(parent.ifPresent(getName(), this::getFullName).get()))));
		return map;
	}

	public static CompositeWatchable of(String name, Watchable... subWatchables) {
		return new CompositeWatchable() {

			@Override
			public String getName() {
				return name;
			}

			@Override
			public AddList<Watchable> getSubWatchables(AddList<Watchable> stem) {
				return stem.put(subWatchables);
			}

		};
	}

	public static CompositeWatchable of(String name, List<Watchable> watchables) {
		return of(name, watchables.toArray(new Watchable[0]));
	}
}
