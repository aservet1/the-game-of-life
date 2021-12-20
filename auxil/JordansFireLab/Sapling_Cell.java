
import java.awt.Color;

public class Sapling_Cell extends Cell {
	
	private int num_cycles_since_created = 0;
	private final int num_cycles_until_fully_grown = 15;

	@Override
	public Cell getNextState() {

		double random_percent = Global_Constants.RNG.nextDouble();
		
		// saplings are less likely to catch fire than fully grown trees
		boolean cell_catches_fire = random_percent <= chance_of_catching_fire / 2;
		
		if (cell_catches_fire) {
			return new Just_Caught_On_Fire_Cell();
		}
		
		if (num_cycles_since_created > num_cycles_until_fully_grown) {
			return new Tree_Cell();
		}
		
		num_cycles_since_created++;
		chance_of_catching_fire = 0.0;
		return this;
	}
	
	@Override
	public double getFlameIntensity() {
		return Global_Constants.NOT_ON_FIRE;
	}
	
	@Override
	public Color getTileColor() {
		return new Color(0, 128, 0); // medium green
	}

}
