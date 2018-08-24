/*

 */
package Grid;

import java.awt.Graphics;
import java.awt.Graphics2D;
import match3.ImagePool;


public class StandardGridSpace extends GridSpace{
    
    
    private int numTimesActivated;

    @Override
    public void doGameTick(Match3Grid grid, Position p, DeleteLog deleteLog) {
        if (isBusy()){
            busyTicks --;
            if(busyTicks == 0){
                block.doGameTick(grid,p,deleteLog);//solving the problem of sometimes missing a double match
            }
        }
        else{
            block.doGameTick(grid,p,deleteLog);
        }
    }
    
    //for drawing, set busy, then actual move, visual move, *

    @Override
    public void justSwapped(Block b, Direction d) {
        //System.out.println(block);
        if (!isBusy()){
            busyTicks = MOVE_DELAY_TICKS;
            busyReason = BusyReason.SWAP;
            swapDirection = d;
        }
        
    }

    @Override
    public void deleteBlock(Match3Grid grid, DeleteLog log, DestroyType type) {
        busyTicks = MOVE_DELAY_TICKS;
        block.deleteAction(grid, log);
        previousBlock = block;
        block = new EmptyBlock();
        numTimesActivated ++;
        
        if (type == DestroyType.MATCH_HORIZANTAL || type == DestroyType.MATCH_VERTICAL){
            busyReason = BusyReason.MATCH;//memory of last block? flash animation?
        }
        else{
            busyReason = BusyReason.DESTROY;
        }
        
        
    }

    @Override
    public void replaced(Block newBlock) {
        this.block = newBlock;
        justSwapped(newBlock,Direction.UP);
    }
    
    @Override
    public String toString(){
        if (isBusy()){
            return "\"" + block.toString() + "\"";
        }
        else{
            return block.toString();
        }
    }

    @Override
    public void clickedOn(Match3Grid aThis, Position clickedPosition) {
        //if (isBusy()){
            //return;
        //}
        
        
        
    }

    @Override
    public void draw(Graphics g, Position p) {
        Graphics2D g2 = (Graphics2D) g;
        //g.drawRect(p.x, p.y, 1, 1);
        
        
        
        //alter position for animations
        //Position truePosition = new Position(p);
        if (isBusy()){
            switch (busyReason){
                case SWAP:
                    switch(swapDirection){
                        case UP:
                            g2.translate(0, -1.0*busyTicks/MOVE_DELAY_TICKS);
                            block.drawNormal(g, p);
                            g2.translate(0, 1.0*busyTicks/MOVE_DELAY_TICKS);
                        break;
                        case DOWN:
                            g2.translate(0, 1.0*busyTicks/MOVE_DELAY_TICKS);
                            block.drawNormal(g, p);
                            g2.translate(0, -1.0*busyTicks/MOVE_DELAY_TICKS);
                        break;
                        case LEFT:
                            g2.translate(-1.0*busyTicks/MOVE_DELAY_TICKS,0);
                            block.drawNormal(g, p);
                            g2.translate(1.0*busyTicks/MOVE_DELAY_TICKS,0);
                        break;
                        case RIGHT:
                            g2.translate(1.0*busyTicks/MOVE_DELAY_TICKS,0);
                            block.drawNormal(g, p);
                            g2.translate(-1.0*busyTicks/MOVE_DELAY_TICKS,0);
                        break;
                        default:
                            System.out.println("Error in drawing switch offset");
                    }
                break;
                case MATCH://flash?
                    
                break;
                case DESTROY://fade?
                    
                break;
                default:
                    System.out.println("Error in drawing block");
            }
        }
        
        else{
            block.drawNormal(g, p);
        }
        
        
        
    }
    
    
}
