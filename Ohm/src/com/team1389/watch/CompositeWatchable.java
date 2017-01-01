package com.team1389.watch;

import java.io.IOException;
import java.io.Writer;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.team1389.util.AddList;
import com.team1389.util.Optional;
import com.team1389.watch.info.SimpleWatchable;

import edu.wpi.first.wpilibj.tables.ITable;

/**
 * implements the <em>Composite Pattern</em>: <br>
 * represents a group of {@link Watchable} objects, but also implements {@link Watchable} itself
 * 
 * @author amind
 * @see <a href="https://www.javacodegeeks.com/2015/09/composite-design-pattern.html">Composite pattern</a>
 * @see SimpleWatchable
 * @see Watchable
 */
public interface CompositeWatchable extends Watchable {

	/**
	 * a blank list used to simplify the process of building a subWatchables list
	 * 
	 * @see CompositeWatchable#getSubWatchables(AddList stem)
	 */
	static AddList<Watchable> stem = new AddList<Watchable>();

	/**
	 * @param stem a supplied empty list to build the subWatchables list from
	 * @return a list of watchables tracked by this composite
	 * @see CompositeWatchable#stem
	 */
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
	public default void log(Writer f) {

		for (Watchable w : getSubWatchables(stem)) {

			w.log(f);
			try {
				f.append("\t");

			} catch (IOException e) {
				System.out.println(e.getMessage());
			}
		}
	}

	@Override
	public default void logKey(Writer f) {
		for (Watchable w : getSubWatchables(stem)) {
			w.logKey(f);
			try {
				f.append("\t");
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}

	@Override
	public default Map<String, SimpleWatchable> getFlat(Optional<String> parent) {
		Map<String, SimpleWatchable> map = new HashMap<>();
		getSubWatchables(stem)
				.forEach(w -> map.putAll(w.getFlat(Optional.of(parent.ifPresent(getName(), this::getFullName).get()))));
		return map;
	}

	/**
	 * generates a composite watchable
	 * 
	 * @param name the name of the composite watchable
	 * @param subWatchables the watchables to combine
	 * @return a composite watchable containing the given subWatchables
	 */
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

	/**
	 * generates a composite watchable
	 * 
	 * @param name the name of the composite watchable
	 * @param subWatchables the list of watchables to combine
	 * @return a composite watchable containing the given subWatchables
	 */
	public static CompositeWatchable of(String name, List<Watchable> subWatchables) {
		return of(name, subWatchables.toArray(new Watchable[0]));
	}
}
