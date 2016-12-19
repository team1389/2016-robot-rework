package com.team1389.system;

import java.util.ArrayList;
import java.util.Arrays;

public class SystemManager {
	ArrayList<System> systems;
	boolean hasInited;

	public SystemManager(System... systems) {
		this.systems = new ArrayList<>();
		this.systems.addAll(Arrays.asList(systems));
		hasInited = false;
	}

	public void register(System... systems) {
		if (hasInited) {
			for (System system : systems) {
				system.init();
			}
		}
		this.systems.addAll(Arrays.asList(systems));
	}

	public void init() {
		hasInited = true;
		for (System system : systems) {
			system.init();
		}
	}

	public void update() {
		for (System system : systems) {
			system.thisUpdate();
		}
	}
}
