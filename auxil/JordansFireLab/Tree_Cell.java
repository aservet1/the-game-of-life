
import java.awt.Color;

public class Tree_Cell extends Cell {
	
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
