
import java.awt.Color;

public class Grassy_Cell extends Cell {

	private int num_cycles_since_created = 0;
	private final int num_cycles_until_sapling_can_sprout = 10;

	@Override
	public Cell getNextState() {
		if (num_cycles_since_created > num_cycles_until_sapling_can_sprout) {
			
			double random_percent = Global_Constants.RNG.nextDouble();
			boolean cell_sprouts_into_a_tree = random_percent <= Global_Constants.TREE_REGROWTH_RATE;
			
			if (cell_sprouts_into_a_tree) {
				return new Sapling_Cell();
			}
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
		return new Color(0, 200, 0); // lightish green
	}

}
