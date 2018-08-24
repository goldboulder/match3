/*

 */
package Grid;

import java.awt.Graphics;

/**
 *
 * @author Nathan
 */
public abstract class GridSpace {

    

    

    
    public enum BusyReason{SWAP,MATCH,DESTROY}
    
    protected static final int MOVE_DELAY_TICKS = 8;//for manual swapping, falling, and deleting
    protected Block block;
    protected Block previousBlock;
    protected BusyReason busyReason;
    protected Direction swapDirection;
    protected int busyTicks;
    
    public abstract void doGameTick(Match3Grid grid, Position p, DeleteLog deleteLog);
    public abstract void justSwapped(Block b, Direction d);
    public abstract void deleteBlock(Match3Grid grid, DeleteLog log, DestroyType type);
    public abstract void clickedOn(Match3Grid aThis, Position clickedPosition);
    public abstract void draw(Graphics g, Position p);
    
    public GridSpace(){
        block = new EmptyBlock();
        previousBlock = new EmptyBlock();
    }
    
    public Block getBlock() {
        if (!isBusy()){
            return block;
        }
        return new InvalidBlock();
    }
    
    public Block getBlockWithoutFailsafe(){
        return block;
    }

    public void setBlock(Block block) {
        this.block = block;
    }
    
    public boolean isBusy() {
        return busyTicks > 0;
    }
    
    public abstract void replaced(Block newBlock);
    
}
