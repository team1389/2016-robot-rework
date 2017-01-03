package com.team1389.watch.input;

import java.util.function.Consumer;
import java.util.function.Supplier;

import com.team1389.watch.info.SimpleWatchable;

import edu.wpi.first.wpilibj.tables.ITable;

/**
 * rather than <em>periodically</em> publishing itself, an inputWatchable publishes its default value <em>once</em> upon initialization, then tracks changes in the value from elsewhere in the network.
 * 
 * @author amind
 *
 * @param <T> the type of value to watch in the table
 */
public abstract class InputWatchable<T> extends SimpleWatchable implements Supplier<T> {
	boolean init;
	Consumer<T> onChange;

	/**
	 * @param name the name of the input
	 * @param defaultVal the default value to apply to the network table
	 * @param onChange an action to perfom when the table value changes
	 */
	public InputWatchable(String name, T defaultVal, Consumer<T> onChange) {
		super(name);
		this.onChange = onChange;
		this.val = defaultVal;
	}

	T val;

	/**
	 * @return the current value in the table
	 */
	@Override
	public T get() {
		return val;
	}

	@SuppressWarnings("unchecked")
	@Override
	public void publishUnderName(String name, ITable table) {
		if (!table.containsKey(name)) {
			put(table, name, val);

		} else {
			val = get(table, name, val);
		}
		table.addTableListener(name, (ITable t, String s, Object val, boolean changed) -> {
			onChange.accept((T) val);
		}, true);
	}

	protected abstract void put(ITable table, String name, T val);

	protected abstract T get(ITable table, String name, T defaultVal);

	@Override
	public String getPrintString() {
		return name + ": " + val;
	}
}
