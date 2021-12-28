
package firesim;

import java.awt.EventQueue;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

import javax.swing.JFrame;
import javax.swing.Timer;

/**
 * animates simple 2d color graphics of forest fire cell automata
 * 
 * a number of fires are set when the program starts running
 * these fires then may spread to adjacent trees
 * 
 *  There are 4 levels of fire:
 *  -> just caught on fire (orange)
 *  -> blazing (red)
 *  -> burning out (orange)
 *  -> smoldering (yellow)
 *  
 *  each fire level has a "fire intensity" value that
 *  effects how likely an adjacent tree is to catch
 *  on fire
 *  
 *  the more on-fire cells there are adjacent to a tree,
 *  the more likely it is to catch on fire
 *  
 *  after a tree has burned down, the area eventually
 *  becomes fertile again and a sapling may eventually 
 *  sprout, eventually growing into a tree
 *  
 *  the cycle of a tree is:
 *  -> tree (dark green)
 *  -> burned out (grey)
 *  -> fertile ground (light green)
 *  -> grassy (light-ish green)
 *  -> sapling (medium green)
 *  -> tree (dark green)
 *  
 *  saplings are less likely to catch on fire than 
 *  fully grown trees
 *  
 *  the trees grow back at a silly and unrealistic rate, but
 *  it makes it look cool
 *  
 *  all types of cells inherit from base abstract class "Cell"
 *  
 *  new behavior can easily be achieved by writing new cell subclasses
 *  with the desired state change behavior
 *  
 *  some iterations burn out quickly and settle into a stable state, 
 *  while others seem to go on forever, or at least a long time
 *  
 *  graphics code is shameless copy paste from http://zetcode.com/gfx/java2d/
 *  
 *  to alter properties go into Global_Constants class and tweak variables
 *  
 *  it is recommended to consume any drugs you may have before running program
 */

public class Driver extends JFrame {
	
	private static final Grid grid = new Grid(Global_Constants.GRID_WIDTH, Global_Constants.GRID_HEIGHT);
	
	private static final long serialVersionUID = 1L;
	
    public Driver() {
        initUI();
    }
    
    public static void main(String[] args) {

        EventQueue.invokeLater(new Runnable() {
            @Override
            public void run() {
            	
            	Driver fire = new Driver();
            	fire.setVisible(true);
            }
        });
    }

    private void initUI() {

        final Simple_Graphics surface = new Simple_Graphics(grid);
        add(surface);

        addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                Timer timer = surface.getTimer();
                timer.stop();
            }
        });

        setTitle("Denethor's program");
        setSize(Global_Constants.GRID_WIDTH * Global_Constants.CELL_WIDTH,
        		Global_Constants.GRID_HEIGHT * Global_Constants.CELL_HEIGHT);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    }

}

