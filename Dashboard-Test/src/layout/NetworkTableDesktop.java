package layout;
import edu.wpi.first.wpilibj.IterativeRobot;
import edu.wpi.first.wpilibj.networktables.NetworkTable;

public class NetworkTableDesktop extends IterativeRobot{
	
	public static void main(String[] args){
		new NetworkTableDesktop().run("datatable");
	}
	public void run(String key){
		NetworkTable.setClientMode();
		NetworkTable.setIPAddress();//add ip name
		NetworkTable table = NetworkTable.getTable(key);
		while(true){
			Thread.sleep(1000);
		}
		
	}
}
