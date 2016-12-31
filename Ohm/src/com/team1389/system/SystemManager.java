package com.team1389.system;

import java.util.ArrayList;
import java.util.Arrays;

public class SystemManager {
	ArrayList<Subsystem> systems;
	boolean hasInited;

	public SystemManager(Subsystem... systems) {
		this.systems = new ArrayList<>();
		this.systems.addAll(Arrays.asList(systems));
		hasInited = false;
	}

	public void register(Subsystem... systems) {
		if (hasInited) {
			for (Subsystem system : systems) {
				system.init();
			}
		}
		this.systems.addAll(Arrays.asList(systems));
	}

	public void init() {
		hasInited = true;
		for (Subsystem system : systems) {
			system.init();
		}
	}

	public void update() {
		for (Subsystem system : systems) {
			system.thisUpdate();
		}
	}
}
