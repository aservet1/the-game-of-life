
import java.awt.Color;

public class Smoldering_Cell extends Cell {

	@Override
	public Cell getNextState() {
		return new Extinguished_Cell();
	}

	@Override
	public double getFlameIntensity() {
		return Global_Constants.SMOLDERING;
	}

	@Override
	public Color getTileColor() {
		return Color.YELLOW;
	}
	
}
