
package firesim;

import java.util.stream.IntStream;

public class Grid {

	private static final int DEFAULT_NUM_ROWS = 25;
	private static final int DEFAULT_NUM_COLUMNS = 25;
	
	private int num_rows;
	private int num_columns;
	
	private Cell[][] cells;
	
	public Grid() {
		this(DEFAULT_NUM_ROWS, DEFAULT_NUM_COLUMNS);
	}
	
	public Grid(int num_rows, int num_columns) {
		cells = new Cell[num_rows][num_columns];
		this.num_rows = num_rows;
		this.num_columns = num_columns;
		
		fillAllWithTrees();
		addRandomFires();
		fillBorderWithBarrenCells();
	}
	
	private void fillAllWithTrees() {
		for (int i = 0; i < num_rows; i++) {
			for (int j = 0; j < num_columns; j++) {
				cells[i][j] = new Tree_Cell();
			}
		}
	}
	
	private void addRandomFires() {
		IntStream.range(0, Global_Constants.NUM_FIRES_AT_START)
		.forEach(i -> {
			int x_coord = Global_Constants.RNG.nextInt(num_rows);
			int y_coord = Global_Constants.RNG.nextInt(num_columns);
			
			cells[x_coord][y_coord] = new Just_Caught_On_Fire_Cell();
		});
	}
	
	private void fillBorderWithBarrenCells() {
		// fill in top and bottom rows
		for (int i = 0; i < num_columns; i++) {
			cells[0][i] = new Barren_Cell();
			cells[num_rows - 1][i] = new Barren_Cell();
		}
		
		// fill in left and right columns
		for (int i = 0; i < num_rows; i++) {
			cells[i][0] = new Barren_Cell();
			cells[i][num_columns - 1] = new Barren_Cell();
		}
	}
	
	public int getNumRows() {
		return num_rows;
	}
	
	public int getNumColumns() {
		return num_columns;
	}
	
	public Cell[][] getCells() {	
		return cells; // privacy leak in the name of efficiency
	}
	
	public void tick() {
		
		// ignore the row of barren cells around the outside so
		// we don't need to check for i and j out of bounds
		for (int i = 1; i < cells.length - 1; i++) {
			for (int j = 1; j < cells[0].length - 1; j++) { 
				
				// if a cell is burning, spread sparks to all
				// of its neighbors
				if (cells[i][j].getFlameIntensity() > 0.0) {
					
					for (int k = -1; k <= 1; k++) {
						for (int l = -1; l <= 1; l++) {
							cells[i][j].applyBuringTo(cells[i + k][j + l]);
						}
					}
				}					
			}
		}
		
		// go through all the cells again and update their states 
		// after any sparks have been applied
		for (int i = 1; i < cells.length - 1; i++) {
			for (int j = 1; j < cells[0].length - 1; j++) { 
				cells[i][j] = cells[i][j].getNextState();
			}
		}
	}
}
