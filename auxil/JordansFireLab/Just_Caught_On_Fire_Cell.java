
import java.awt.Color;

public class Just_Caught_On_Fire_Cell extends Cell {

	@Override
	public Cell getNextState() {
		return new Blazing_Cell();
	}

	@Override
	public double getFlameIntensity() {
		return Global_Constants.BURNING;
	}
	
	@Override
	public Color getTileColor() {
		return Color.ORANGE;
	}

}
