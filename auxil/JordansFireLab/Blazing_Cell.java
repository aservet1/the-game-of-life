
import java.awt.Color;

public class Blazing_Cell extends Cell {

	@Override
	public Cell getNextState() {
		return new Burning_Out_Cell();
	}

	@Override
	public double getFlameIntensity() {
		return Global_Constants.BLAZING;
	}
	
	@Override
	public Color getTileColor() {
		return Color.RED;
	}

}
