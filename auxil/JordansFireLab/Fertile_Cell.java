
import java.awt.Color;

public class Fertile_Cell extends Cell {
	
	private int num_cycles_since_created = 0;
	private final int num_cycles_until_grass_grows = 10;

	@Override
	public Cell getNextState() {
		if (num_cycles_since_created > num_cycles_until_grass_grows) {
			return new Grassy_Cell();
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
		return new Color(0, 255, 0); // light green
	}

}
