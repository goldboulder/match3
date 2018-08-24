/*

 */
package Grid;

import java.awt.Graphics;

/**
 *
 * @author Nathan
 */
public class InvalidGridSpace extends GridSpace{

    @Override
    public void doGameTick(Match3Grid grid, Position p, DeleteLog deleteLog) {
        
    }

    @Override
    public void justSwapped(Block b, Direction d) {
        
    }

    @Override
    public void deleteBlock(Match3Grid grid, DeleteLog log, DestroyType type) {
        
    }

    @Override
    public boolean isBusy() {
        return true;
    }

    @Override
    public void replaced(Block newBlock) {
        
    }
    
    @Override
    public Block getBlock(){
        return new InvalidBlock();
    }
    
    @Override
    public void clickedOn(Match3Grid aThis, Position clickedPosition) {
        if (isBusy()){
            return;
        }
        
        
        
    }
    
    @Override
    public void draw(Graphics g, Position p) {
        
    }
    
}
