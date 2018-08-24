/*

 */
package Grid;

import java.awt.Graphics;


public abstract class Block {
    public abstract boolean isNull();
    
    public abstract boolean matching(Match3Grid grid, Position p, Direction swapDirection);
    public abstract boolean canMoveManually();
    public abstract void doGameTick(Match3Grid grid, Position p, DeleteLog deleteLog);
    public abstract void deleteAction(Match3Grid grid, DeleteLog deleteLog);//grid.justDeleted(this)***
    
    public void blockGravity(Match3Grid grid, Position p){
        Position otherPosition = p.positionNextTo(Direction.DOWN,1);
        
        if (grid.getBlock(otherPosition) instanceof EmptyBlock){
            //System.out.println("Empty block detected above " + p);
            grid.swapBlocks(p,Direction.DOWN);
        }
        
    }

    public abstract void drawNormal(Graphics g, Position p);
}
