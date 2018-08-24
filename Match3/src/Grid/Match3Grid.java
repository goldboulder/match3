/*

 */
package Grid;


import GUI.Drawable;
import java.awt.Graphics;
import java.awt.geom.Point2D;
import java.util.ArrayList;
import match3.ImagePool;


public class Match3Grid implements Drawable{
    
    private ArrayList<ArrayList<GridSpace>> gridSpaces;
    private BlockReplacer blockReplacer;
    private Position selectedGridPosition;
    private DeleteLog deleteLog;
    
    public Match3Grid(int height, int width, BlockReplacer blockReplacer){
        this.blockReplacer = blockReplacer;
        this.deleteLog = new DeleteLog();
        gridSpaces = new ArrayList<>();
        
        for (int y = 0; y < height; y ++){
            ArrayList<GridSpace> row = new ArrayList<>();
            for (int x = 0; x < width; x ++){
                row.add(new StandardGridSpace());
            }
            gridSpaces.add(row);
        }
        
        this.blockReplacer.initialSetUp(this);
        
    }
    
    public GridSpace getGridSpace(Position p){
        try{
            return gridSpaces.get(p.y).get(p.x);
        }
        catch(IndexOutOfBoundsException e){
            return new InvalidGridSpace();
        }
    }
    
    public Block getBlock(Position p){
        try{
            return gridSpaces.get(p.y).get(p.x).getBlock();
        }
        catch(IndexOutOfBoundsException e){
            return new InvalidBlock();
        }
    }
    
    public Block getBlockWithoutFailsafe(Position p){
        try{
            return gridSpaces.get(p.y).get(p.x).getBlockWithoutFailsafe();
        }
        catch(IndexOutOfBoundsException e){
            return new InvalidBlock();
        }
    }
    
    public void setBlock(Position p, Block block){
        if (inBounds(p)){
            gridSpaces.get(p.y).get(p.x).setBlock(block);
        }
        else{
            System.out.println("Error: set block out of bounds");
        }
    }
    
    public int width(){
        return gridSpaces.get(0).size();
    }
    
    public int height(){
        return gridSpaces.size();
    }
            
    public void doGameTick(){
        
        if (selectedGridPosition != null && getGridSpace(selectedGridPosition).isBusy()){
            selectedGridPosition = null;
        }
        
        makeBlocksFall();//remember what order these are processed in
        doBlockGameTicks();
        deleteLog.deleteBlocks(this);//+ any scoreing arguments... gridSpace.deleteBlock(block)//delete any blocks that need deleting(matches, bombs, ect)
        blockReplacer.replaceBlocks(this);//give this field to blockReplacer?
    }
    
    public void makeBlocksFall(){
        for (int y = height()-1; y >= 0; y --){//change order for different gravity direction?
            for (int x = 0; x < width(); x ++){
                Position p = new Position(y,x);
                if (!getGridSpace(p).isBusy()){
                    getBlock(p).blockGravity(this,p);
                }
            }
        }
    }
    
    
    private void doBlockGameTicks(){
        for (int y = 0; y < height(); y ++){
            for (int x = 0; x < width(); x ++){
                Position p = new Position(y,x);
                getGridSpace(p).doGameTick(this, p, deleteLog);
            }
        }
    }
    
    
    
    
    public void swapBlocks(Position blockPosition, Direction d){
        Position otherBlockPosition = blockPosition.positionNextTo(d,1);
        
        //test for out of bounds block
        if(!inBounds(otherBlockPosition) || getGridSpace(otherBlockPosition).isBusy() || !inBounds(blockPosition) || getGridSpace(blockPosition).isBusy()){
            return;
        }
        if (getBlock(blockPosition) instanceof EmptyBlock){
            return;
        }
        
        Block tempBlock = getBlock(blockPosition);
        setBlock(blockPosition, getBlock(otherBlockPosition));
        setBlock(otherBlockPosition, tempBlock);
        
        getGridSpace(blockPosition).justSwapped(getBlock(blockPosition),d);
        getGridSpace(otherBlockPosition).justSwapped(getBlock(otherBlockPosition),opposite(d));
        
    }
    
    public static Direction opposite(Direction d){
        switch (d){
                case UP:
                    return Direction.DOWN;
                case DOWN:
                    return Direction.UP;
                case LEFT:
                    return Direction.RIGHT;
                case RIGHT:
                    return Direction.LEFT;
                default:
                    System.out.println("Error in Match3.oppositeDirection");
                    return Direction.UP;
            }
    }
    
    private boolean inBounds(Position p){
        return (p.y >= 0) && (p.x >= 0) && (p.y < height()) && (p.x < width());
    }
    
    @Override
    public void draw(Graphics g) {
        g.drawImage(ImagePool.getPicture("Background/Stone"), 0, 0, width(), height(), null);
        for (int y = 0; y < height(); y ++){
            for (int x = 0; x < width(); x ++){
                Position p = new Position(y,x);
                getGridSpace(p).draw(g,p);
            }
        }
        
        if (selectedGridPosition != null){
            g.drawImage(ImagePool.getPicture("Other/Selection Highlight"),selectedGridPosition.x,selectedGridPosition.y,1,1,null);
        }
        
    }
    
    public void mousePressed(Point2D.Double point) {
        Position clickedPosition = new Position((int)(height()-point.y),(int)point.x);//grids are one unit in length in the game world
        GridSpace gridSpace = getGridSpace(clickedPosition);
        gridSpace.clickedOn(this,clickedPosition);
        
        if (selectedGridPosition == null){
            selectedGridPosition = clickedPosition;
        }
        else if (selectedGridPosition.x == clickedPosition.x && selectedGridPosition.y == clickedPosition.y){
            selectedGridPosition = null;
        }
        else{//second click
            manualMoveBlocks(clickedPosition);
        }
    }
    
    private void manualMoveBlocks(Position otherPosition){
        Direction d = getDirectionIfNextTo(otherPosition);
        //System.out.println("Now attempting to swap " + selectedGridPosition + " and " + otherPosition);
        
        //if positions not next to each other, stop
        if (d == null){
            selectedGridPosition = otherPosition;
            return;
        }
        //if you can't move the blocks/grids, stop
        if (!(getBlock(selectedGridPosition).canMoveManually() && getBlock(otherPosition).canMoveManually())){
            return;
        }
        //if there is no match, stop
        if (!(getBlock(selectedGridPosition).matching(this, selectedGridPosition, d) || getBlock(otherPosition).matching(this, otherPosition, opposite(d)))){
            return;//failed move animation?
        }
        
        swapBlocks(selectedGridPosition,d);
        selectedGridPosition = null;
    }
    
    private Direction getDirectionIfNextTo(Position otherPosition){
        if (selectedGridPosition.x == otherPosition.x){
            if (selectedGridPosition.y == otherPosition.y + 1){
                return Direction.UP;
            }
            if (selectedGridPosition.y == otherPosition.y - 1){
                return Direction.DOWN;
            }
        }
        if (selectedGridPosition.y == otherPosition.y){
            if (selectedGridPosition.x == otherPosition.x + 1){
                return Direction.LEFT;
            }
            if (selectedGridPosition.x == otherPosition.x - 1){
                return Direction.RIGHT;
            }
        }
        return null;
    }
            
    @Override
    public String toString(){
        StringBuilder b = new StringBuilder();
        
        b.append("____________________________________________________________\n");
        
        for (int y = 0; y < height(); y++){
            for (int x = 0; x < width(); x ++){
                b.append(gridSpaces.get(y).get(x)).append("\t");
            }
            b.append("\n");
        }
        b.append("____________________________________________________________");
        return b.toString();
    }  

    

    

    
            
}
