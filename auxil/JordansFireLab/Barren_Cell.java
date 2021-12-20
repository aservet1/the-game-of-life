
import java.awt.Color;

public class Barren_Cell extends Cell {

	@Override
	public Cell getNextState() {
		return this;
	}

	@Override
	public double getFlameIntensity() {
		return Global_Constants.NOT_ON_FIRE;
	}
	
	@Override
	public Color getTileColor() {
		return Color.GRAY;
	}
	
}
