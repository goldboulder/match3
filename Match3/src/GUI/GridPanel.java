/*

 */
package GUI;

import Grid.BasicColoredBlock;
import Grid.Match3Grid;
import Grid.Position;
import Grid.StandardBlockReplacer;
import java.awt.Graphics;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.geom.Point2D;
import javax.swing.Timer;
import javax.swing.JPanel;


public class GridPanel extends JPanel implements MouseListener, ActionListener{
    
    private PlayFrame frame;
    private Match3Grid grid;
    private Camera camera;
    private Timer drawTimer;
    private Timer tickTimer;
    
    public static final double DRAW_TIME = 0.03;
    public static final double GAME_TICK_TIME = 0.03;
    
    public GridPanel(PlayFrame frame){//grid parameters?
        this.frame = frame;
        grid = new Match3Grid(20,20,new StandardBlockReplacer(new int[]{0,1,2,3,4,5}));
        camera = new Camera(this);
        camera.setSizeAndCenter(grid.width(),grid.height());
        camera.setBounds(0, 0, grid.width(), grid.height());
        drawTimer = new Timer((int)(DRAW_TIME*1000),this);
        tickTimer = new Timer((int)(GAME_TICK_TIME*1000),this);
        drawTimer.start();
        tickTimer.start();
        drawTimer.setActionCommand("draw");
        tickTimer.setActionCommand("tick");
        
        addMouseListener(this);
        /*
        grid.setBlock(new Position(4,4), new BasicColoredBlock(3));
        grid.setBlock(new Position(4,3), new BasicColoredBlock(3));
        grid.setBlock(new Position(4,5), new BasicColoredBlock(3));
        grid.setBlock(new Position(4,6), new BasicColoredBlock(3));
        grid.setBlock(new Position(3,4), new BasicColoredBlock(3));
        grid.setBlock(new Position(2,4), new BasicColoredBlock(3));
*/
    }
    
    @Override
    public void paintComponent(Graphics g){
        camera.draw(g,grid);
    }

    

    @Override
    public void mousePressed(MouseEvent e) {
        Point2D.Double point = camera.screenSpaceToGameSpace(new Point2D.Double(e.getX(),e.getY()));
        //consider grid position in world? now, it's always at 0,0
        grid.mousePressed(point);
        //System.out.println("Sceen: " + e.getX() + ", " + e.getY() + "    World: " + camera.screenSpaceToGameSpace(new Point2D.Double(e.getX(),e.getY())));
    }

    @Override
    public void mouseReleased(MouseEvent e) {
        
    }
    
    @Override
    public void mouseClicked(MouseEvent e) {
        
    }

    @Override
    public void mouseEntered(MouseEvent e) {
        
    }

    @Override
    public void mouseExited(MouseEvent e) {
        
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        switch(e.getActionCommand()){
            case "draw":
                repaint();
            break;
            case "tick":
                grid.doGameTick();
            break;
            default:
                System.out.println("Unknown Action Command in GridPanel");
        }
        repaint();
    }
    
}
