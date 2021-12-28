
package firesim;

import java.awt.Color;

public abstract class Cell {
	
	protected double chance_of_catching_fire = 0;
	
	public void applyBuringTo(Cell other) {
		other.chance_of_catching_fire += this.getFlameIntensity();
	}
	
	public abstract Cell getNextState();

	public abstract double getFlameIntensity();
	
	public abstract Color getTileColor();
	
}

class Tree_Cell extends Cell {
	
	@Override
	public Cell getNextState() {

		double random_percent = Global_Constants.RNG.nextDouble();
		boolean cell_catches_fire = random_percent <= chance_of_catching_fire;
		
		if (cell_catches_fire) {
			return new Just_Caught_On_Fire_Cell();
		}
		
		chance_of_catching_fire = 0.0;
		return this;
	}
	
	@Override
	public double getFlameIntensity() {
		return Global_Constants.NOT_ON_FIRE;
	}
	
	@Override
	public Color getTileColor() {
		return new Color(0, 64, 0); // dark green
	}

}

class Grassy_Cell extends Cell {

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

class Sapling_Cell extends Cell {
	
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

class Barren_Cell extends Cell {

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

class Blazing_Cell extends Cell {

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

class Burning_Out_Cell extends Cell {

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

class Fertile_Cell extends Cell {
	
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

class Extinguished_Cell extends Cell {
	
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

class Just_Caught_On_Fire_Cell extends Cell {

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

class Smoldering_Cell extends Cell {

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

