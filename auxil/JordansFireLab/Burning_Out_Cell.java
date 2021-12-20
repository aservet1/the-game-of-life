
import java.awt.Color;

public class Burning_Out_Cell extends Cell {

	@Override
	public Cell getNextState() {
		return new Smoldering_Cell();
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
