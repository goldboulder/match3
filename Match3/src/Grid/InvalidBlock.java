/*

 */
package Grid;

import java.awt.Graphics;

/**
 *
 * @author Nathan
 */
public class InvalidBlock extends Block{

    @Override
    public boolean isNull() {
        return true;
    }

    @Override
    public void deleteAction(Match3Grid grid, DeleteLog deleteLog) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void doGameTick(Match3Grid grid, Position p, DeleteLog deleteLog) {
        
    }

    @Override
    public boolean matching(Match3Grid grid, Position p, Direction swapDirection) {
        return false;
    }

    @Override
    public boolean canMoveManually() {
        return false;
    }
    
    @Override
    public String toString(){
        return "X";
    }

    @Override
    public void drawNormal(Graphics g, Position p) {
        
    }
    
}
