package operation;
import com.team1389.hardware.inputs.software.RangeIn;
import com.team1389.hardware.outputs.software.RangeOut;
import com.team1389.hardware.value_types.Position;
import com.team1389.hardware.value_types.Speed;
public class MotionProfileController {
	RangeIn<Position> pos;
	RangeIn<Speed> speed;
	RangeOut<Position> setter;
}
