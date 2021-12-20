
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
