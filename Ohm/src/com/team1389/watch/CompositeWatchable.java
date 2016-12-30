package com.team1389.watch;

import java.io.FileWriter;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.function.UnaryOperator;

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
	public default void log(FileWriter f) {
	
		for(Watchable w: getSubWatchables(stem)){
			
			w.log(f);
			try{
			f.append("\t");
			
			}catch(IOException e){
				System.out.println(e.getMessage());
			}
		}
		}
	@Override
	public default void logKey(FileWriter f) {
		
		for(Watchable w: getSubWatchables(stem)){
			w.logKey(f);
			try{
			f.append("\t");
			}catch(IOException e){
				System.out.println(e.getMessage());
			}
			}
		}
	

	@Override
	public default Map<String, Watchable> getFlat(Optional<String> parent) {
		Map<String, Watchable> map = new HashMap<>();
		getSubWatchables(stem)
				.forEach(w -> map.putAll(w.getFlat(Optional.of(parent.ifPresent(getName(), this::getFullName).get()))));
		return map;
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
