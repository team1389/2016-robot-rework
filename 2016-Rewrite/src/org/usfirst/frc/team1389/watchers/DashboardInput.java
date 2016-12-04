package org.usfirst.frc.team1389.watchers;

import org.usfirst.frc.team1389.autonomous.AutonModeSelector;
import org.usfirst.frc.team1389.autonomous.AutonOption;

import com.team1389.auto.AutoModeBase;

import edu.wpi.first.wpilibj.smartdashboard.SendableChooser;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

public class DashboardInput {
	private static DashboardInput instance = new DashboardInput();
	public static DashboardInput getInstance() {
		return instance;
	}
	public DashboardInput(){
		init();
	}
	private SendableChooser autonSelector;
	public void init(){
		autonSelector=new SendableChooser();
        for (AutonOption autonOption : AutonOption.values()) {
    		autonSelector.addObject(autonOption.name, autonOption);
        }

		SmartDashboard.putData(SELECTED_AUTO_MODE,autonSelector);
	}

	private static final String SELECTED_AUTO_MODE = "selected_auto_mode";
	private static final AutonOption DEFAULT_MODE = AutonOption.DRIVE_STRAIGHT;
	/**
	 * dashboard must be initialized before this can be called
	 * @return
	 */
	public AutoModeBase getSelectedAutonMode() {
		String autoModeString = SmartDashboard.getString(SELECTED_AUTO_MODE, DEFAULT_MODE.name);
		AutonOption selectedOption = DEFAULT_MODE;
		for (AutonOption autonOption : AutonOption.values()) {
			if (autonOption.name.equals(autoModeString)) {
				selectedOption = autonOption;
				break;
			}
		}
		selectedOption=(AutonOption) autonSelector.getSelected();
		return AutonModeSelector.createAutoMode(selectedOption);
	}

}
