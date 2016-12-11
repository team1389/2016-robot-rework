package layout;

import com.team1389.hardware.inputs.software.RangeIn;
import com.team1389.hardware.outputs.software.PercentOut;
import com.team1389.hardware.value_types.Position;
import com.team1389.hardware.value_types.Speed;

public class TesterLayout {
	public PercentOut voltOut1;
	public PercentOut voltOut2;
	
	public RangeIn<Position> posIn1;
	public RangeIn<Position> posIn2;
	
	public RangeIn<Speed> speedIn1;
	public RangeIn<Speed> speedIn2;

	
}
