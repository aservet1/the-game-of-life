
import java.awt.Color;

public class Extinguished_Cell extends Cell {
	
	private int num_cycles_since_created = 0;
	private final int num_cycles_until_fertile_again = 15;

	@Override
	public Cell getNextState() {
		if (num_cycles_since_created > num_cycles_until_fertile_again) {
			return new Fertile_Cell();
		}
		num_cycles_since_created++;
		return this;
	}

	@Override
	public double getFlameIntensity() {
		return Global_Constants.NOT_ON_FIRE;
	}

	@Override
	public Color getTileColor() {
		return Color.LIGHT_GRAY;
	}
	
}
