package com.team1389.watch;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Set;

import edu.wpi.first.wpilibj.networktables.NetworkTable;
import edu.wpi.first.wpilibj.tables.ITable;
/** 
 * publishes all watchers to SmartDashboard, manipulates it's contents
 * @author Kenneth
 *
 */
public class Watcher {
	public static ITable DASHBOARD = NetworkTable.getTable("SmartDashboard");

	protected List<Watchable> watchables;

	public Watcher() {
		watchables = new ArrayList<>();
	}
	/**
	 * used if individual watchables are passed in
	 * @param watchables added to list
	 * @return list of watchables
	 */
	public Watcher watch(Watchable... watchables) {
		this.watchables.addAll(Arrays.asList(watchables));
		return this;
	}
	/**
	 * used if list of watchables is passed in
	 * @param watchables list of watchables added to original list
	 * @return original list
	 */
	public Watcher watch(List<Watchable> watchables) {
		this.watchables.addAll(watchables);
		return this;
	}

	public List<Watchable> getWatchables() {
		return watchables;
	}
	/**
	 * 
	 * @param table that all watchables publish keys+values to 
	 */
	public void publish(ITable table) {
		for (Watchable info : watchables) {
			info.publish(table);
		}
	}
	/**
	 * 
	 * @return printable values of watchables
	 */
	public String getPrintString() {
		String s = "";
		for (Watchable w : watchables) {
			s = String.join(s, w.getPrintString() + "\n");
		}
		return s;
	}

	/*
	 * public Set<String> getInfoKeys() { Set<String> keys = new HashSet<>(); for (Watchable w : watchables) { for (Info info : w.getInfo()) { keys.add(info.getName()); } } return keys; }
	 */
	/**
	 * reset list
	 */
	public void clearWatchers() {
		watchables = new ArrayList<>();
	}
	/**
	 * remove all keys
	 * @param table containing keys 
	 * @param keys every kind of key contained in table
	 */
	public static void clearKeys(ITable table, Set<String> keys) {
		for (String key : keys) {
			table.delete(key);
		}
	}
	/***
	 * reset NetworkTable
	 */
	public static void clear(){
		NetworkTable.globalDeleteAll();
	}
}
