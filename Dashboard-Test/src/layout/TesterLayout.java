package layout;

import com.team1389.hardware.inputs.software.WatchableRangeIn;
import com.team1389.hardware.outputs.software.WatchableRangeOut;
import com.team1389.hardware.value_types.Percent;
import com.team1389.hardware.value_types.Position;
import com.team1389.hardware.value_types.Speed;

public class TesterLayout {
	public WatchableRangeOut<Percent> voltOut1;
	public WatchableRangeOut<Percent> voltOut2;
	
	public WatchableRangeIn<Position> posIn1;
	public WatchableRangeIn<Position> posIn2;
	
	public WatchableRangeIn<Speed> speedIn1;
	public WatchableRangeIn<Speed> speedIn2;

	
}
