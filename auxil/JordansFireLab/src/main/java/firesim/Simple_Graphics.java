
package firesim;

import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JPanel;
import javax.swing.Timer;

/**
 * much of this is shameless copy paste from http://zetcode.com/gfx/java2d/
 */

public class Simple_Graphics extends JPanel implements ActionListener {

	private static final long serialVersionUID = 1L;
	
	private final int DELAY = Global_Constants.REFRESH_DELAY;
    private Timer timer;
    
    private Grid grid;

    public Simple_Graphics(Grid grid) {
    	this.grid = grid;
        initTimer();
    }

    private void initTimer() {
        timer = new Timer(DELAY, this);
        timer.start();
    }
    
    public Timer getTimer() {
        return timer;
    }

    private void doDrawing(Graphics g) {
    	
    	Graphics2D g2d = (Graphics2D) g;

        int total_width = getWidth();
        int width_of_cell = total_width / grid.getNumColumns();

        int total_height = getHeight();
        int height_of_cell = total_height / grid.getNumRows();
        
		for (int i = 0; i < grid.getNumRows(); i++) {
			for (int j = 0; j < grid.getNumColumns(); j++) { 

		        g2d.setPaint(grid.getCells()[i][j].getTileColor());
		        
		        g2d.fillRect(i * width_of_cell, j * height_of_cell, width_of_cell, height_of_cell);
			}
		}
		
		grid.tick();
    }

    @Override
    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        doDrawing(g);
    }
    
	@Override
	public void actionPerformed(ActionEvent arg0) {
		repaint();
	}

}

