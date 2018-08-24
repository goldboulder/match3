/*

 */
package Grid;

import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.util.LinkedList;
import match3.ImagePool;


public abstract class ColoredBlock extends Block{
    
    public static int numToMatch = 3;
    protected int colorID;
    
    public ColoredBlock(int colorID){
        this.colorID = colorID;
    }
    
    
    @Override
    public boolean isNull() {
        return false;
    }
    
    public int getColorId(){
        return colorID;
    }
    
    public void setColorID(int newID) {
        colorID = newID;
    }
    

    @Override
    public boolean canMoveManually() {
        return true;
    }
    
    //if block is part of a match, send that information to the delete log
    @Override
    public void doGameTick(Match3Grid grid, Position p, DeleteLog deleteLog) {//only perform match if gridSpace is not busy, add to delete log, don't make busy directly***
        //find horizontal and vertical matches. send to deleteLog. log will only add if it's not already there.
        if (colorID < 0){
            return;
        }
        matchHorizontal(grid,p,deleteLog);
        matchVertical(grid,p,deleteLog);
    }
    
    private void matchHorizontal(Match3Grid grid, Position p, DeleteLog deleteLog) {
        Position startPosition = new Position(p);
        while(getColorIdFromGrid(grid,startPosition.positionNextTo(Direction.LEFT,1)) == colorID){
            startPosition.x --;
        }
        
        int streak = 1;
        
        while(getColorIdFromGrid(grid,startPosition.positionNextTo(Direction.RIGHT,streak)) == colorID){
            streak ++;
        }
        //System.out.println(streak);
        if (streak >= numToMatch){
            deleteLog.addMatch(grid,startPosition,streak,Direction.RIGHT);
        }
        
    }
    
    private void matchVertical(Match3Grid grid, Position p, DeleteLog deleteLog) {
        
        Position startPosition = new Position(p);
        while(getColorIdFromGrid(grid,startPosition.positionNextTo(Direction.UP,1)) == colorID){
            startPosition.y --;
        }
        
        int streak = 1;
        
        while(getColorIdFromGrid(grid,startPosition.positionNextTo(Direction.DOWN,streak)) == colorID){
            streak ++;
        }
        
        if (streak >= numToMatch){
            deleteLog.addMatch(grid,startPosition,streak,Direction.DOWN);
        }
    }
    
    //used to get the color id from neighboring blocks
    private int getColorIdFromGrid(Match3Grid grid, Position p){
        
        Block b = grid.getBlock(p);
        if (b instanceof ColoredBlock){
            ColoredBlock c = (ColoredBlock) b;
            return c.getColorId();
        }
        else{
            return -1;
        }
    }
    
    @Override
    public void deleteAction(Match3Grid grid, DeleteLog deleteLog) {
        
    }
    
    //returns true if the block is part of a match
    @Override
    public boolean matching(Match3Grid grid, Position p, Direction swapDirection){//swap direction: would they match if those two were swapped?
        if (numToMatch == 0 || numToMatch == 1){
            return true;
        }
        if (swapDirection == null){
            return matchingH(grid,p) || matchingV(grid,p);
        }
        if (swapDirection == Direction.LEFT || swapDirection == Direction.RIGHT){
            return matchingHSwapH(grid,p,swapDirection) || matchingVSwapH(grid,p,swapDirection);
        }
        else{
            return matchingHSwapV(grid,p,swapDirection) || matchingVSwapV(grid,p,swapDirection);
        }
        
    }
    
    private boolean matchingH(Match3Grid grid, Position p){//lots of duplicate code here***
        //System.out.println("matchingH for position " + p);
        LinkedList<Integer> list = new LinkedList<>();
        //get list before swapping
        for (int i = 1; i < numToMatch; i++){
            list.addFirst(getColorIdFromGrid(grid, p.positionNextTo(Direction.LEFT, i)));
        }
        list.add(colorID);
        for (int i = 1; i < numToMatch; i++){
            list.add(getColorIdFromGrid(grid, p.positionNextTo(Direction.RIGHT, i)));
        }
        
        return matchingLine(list,colorID);
    }
    
    private boolean matchingV(Match3Grid grid, Position p){
        //System.out.println("matchingV for position " + p);
        LinkedList<Integer> list = new LinkedList<>();
        //get list before swapping
        for (int i = 1; i < numToMatch; i++){
            list.addFirst(getColorIdFromGrid(grid, p.positionNextTo(Direction.UP, i)));
        }
        list.add(colorID);
        for (int i = 1; i < numToMatch; i++){
            list.add(getColorIdFromGrid(grid, p.positionNextTo(Direction.DOWN, i)));
        }
        
        return matchingLine(list,colorID);
    }
    
    private boolean matchingHSwapV(Match3Grid grid, Position p, Direction swapDirection) {
        System.out.println("matchingHSwapV for position " + p);
        LinkedList<Integer> list = new LinkedList<>();
        Position positionWhenSwapped = p.positionNextTo(swapDirection, 1);
        
        for (int i = 1; i < numToMatch; i++){
            list.addFirst(getColorIdFromGrid(grid, positionWhenSwapped.positionNextTo(Direction.LEFT, i)));
        }
        list.add(colorID);
        for (int i = 1; i < numToMatch; i++){
            list.add(getColorIdFromGrid(grid, positionWhenSwapped.positionNextTo(Direction.RIGHT, i)));
        }
        printList(list);
        return matchingLine(list,colorID);
    }
    
    private boolean matchingVSwapH(Match3Grid grid, Position p, Direction swapDirection) {
        System.out.println("matchingVSwapH for position " + p);
        LinkedList<Integer> list = new LinkedList<>();
        Position positionWhenSwapped = p.positionNextTo(swapDirection, 1);
        
        for (int i = 1; i < numToMatch; i++){
            list.addFirst(getColorIdFromGrid(grid, positionWhenSwapped.positionNextTo(Direction.UP, i)));
        }
        list.add(colorID);
        for (int i = 1; i < numToMatch; i++){
            list.add(getColorIdFromGrid(grid, positionWhenSwapped.positionNextTo(Direction.DOWN, i)));
        }
        printList(list);
        return matchingLine(list,colorID);
    }
    
    private boolean matchingHSwapH(Match3Grid grid, Position p, Direction swapDirection) {// only left and right are inputted here
        System.out.println("matchingHSwapH for position " + p + " direction: " + swapDirection);
        LinkedList<Integer> list = new LinkedList<>();
        //Position newPosition = p.positionNextTo(swapDirection, 1);
        //get list before swapping
        for (int i = 1; i < numToMatch+1; i++){
            list.addFirst(getColorIdFromGrid(grid, p.positionNextTo(Direction.LEFT, i)));
            System.out.println(p.positionNextTo(Direction.LEFT, i));
        }
        list.add(colorID);
        for (int i = 1; i < numToMatch+1; i++){
            list.add(getColorIdFromGrid(grid, p.positionNextTo(Direction.RIGHT, i)));
            System.out.println(p.positionNextTo(Direction.RIGHT, i));
        }
        System.out.print("Before swapping: ");
        printList(list);
        //swap elements in list
        int centerIndex = numToMatch;
        if (swapDirection == Direction.LEFT){
            list.set(centerIndex,list.get(centerIndex-1));
            list.set(centerIndex-1, colorID);
            
        }
        else{
            list.set(centerIndex,list.get(centerIndex+1));
            list.set(centerIndex+1, colorID);
            
        }
        System.out.print("After Swapping: ");
        printList(list);
        return matchingLine(list,colorID);
    }
    
    private boolean matchingVSwapV(Match3Grid grid, Position p, Direction swapDirection) {// only left and right are inputted here
        System.out.println("matchingVSwapV for position " + p);
        LinkedList<Integer> list = new LinkedList<>();
        //Position newPosition = p.positionNextTo(swapDirection, 1);
        //get list before swapping
        for (int i = 1; i < numToMatch+1; i++){//add one because blocks moved
            list.addFirst(getColorIdFromGrid(grid, p.positionNextTo(Direction.UP, i)));
        }
        list.add(colorID);
        for (int i = 1; i < numToMatch+1; i++){
            list.add(getColorIdFromGrid(grid, p.positionNextTo(Direction.DOWN, i)));
        }
        System.out.print("Before swapping: ");
        printList(list);
        //swap elements in list
        int centerIndex = numToMatch;
        if (swapDirection == Direction.UP){
            list.set(centerIndex,list.get(centerIndex-1));
            list.set(centerIndex-1, colorID);
        }
        else{
            list.set(centerIndex,list.get(centerIndex+1));
            list.set(centerIndex+1, colorID);
        }
        
        return matchingLine(list,colorID);
    }
    
    //for debugging
    private void printList(LinkedList<Integer> list){
        for (Integer i : list){
            System.out.print(i + ",");
        }
        System.out.println();
    }

    /*
    private boolean matchingHorizantal(Match3Grid grid, Position p, Direction swapDirection) {
        System.out.println(p + " Matching horizontal: " + grid.getBlock(p.positionNextTo(Direction.LEFT, 2)) + " " + grid.getBlock(p.positionNextTo(Direction.LEFT, 1)) + " " + colorID + " " + grid.getBlock(p.positionNextTo(Direction.RIGHT, 1)) + " " + grid.getBlock(p.positionNextTo(Direction.RIGHT, 2)));
        LinkedList<Integer> list = new LinkedList<>();
        
        for (int i = 1; i < numToMatch; i++){
            Block b = grid.getBlock(p.positionNextTo(Direction.LEFT, i));
            if (b instanceof ColoredBlock){
                ColoredBlock c = (ColoredBlock) b;
                list.addLast(c.getColorId());
            }
            else{
                list.addLast(-1);
            }
        }
        
        list.add(colorID);
        
        for (int i = 1; i < numToMatch; i++){
            Block b = grid.getBlock(p.positionNextTo(Direction.RIGHT, i));
            if (b instanceof ColoredBlock){
                ColoredBlock c = (ColoredBlock) b;
                list.add(c.getColorId());
            }
            else{
                list.add(-1);
            }
        }
        
        //handle swapping
        horizantalListSwap(list,grid,p,swapDirection);
        
        return matchingLine(list,colorID);
    }

    private boolean matchingVertical(Match3Grid grid, Position p, Direction swapDirection) {
        System.out.println(p + " Matching vertical: " + grid.getBlock(p.positionNextTo(Direction.UP, 2)) + " " + grid.getBlock(p.positionNextTo(Direction.UP, 1)) + " " + colorID + " " + grid.getBlock(p.positionNextTo(Direction.DOWN, 1)) + " " + grid.getBlock(p.positionNextTo(Direction.DOWN, 2)));
        LinkedList<Integer> list = new LinkedList<>();
        
        for (int i = 1; i < numToMatch; i++){
            Block b = grid.getBlock(p.positionNextTo(Direction.UP, i));
            if (b instanceof ColoredBlock){
                ColoredBlock c = (ColoredBlock) b;
                list.addLast(c.getColorId());
            }
            else{
                list.addLast(-1);
            }
        }
        
        list.add(colorID);
        
        for (int i = 1; i < numToMatch; i++){
            Block b = grid.getBlock(p.positionNextTo(Direction.DOWN, i));
            if (b instanceof ColoredBlock){
                ColoredBlock c = (ColoredBlock) b;
                list.add(c.getColorId());
            }
            else{
                list.add(-1);
            }
        }
        
        verticalListSwap(list,grid,p,swapDirection);
        
        return matchingLine(list,colorID);
    }
    
    private void horizantalListSwap(LinkedList<Integer> list, Match3Grid grid, Position p, Direction d){
        if (d == null){
            return;
        }
        int index = numToMatch - 1;
        switch(d){
            case UP: case DOWN:
                list.set(index, getColorIDOfBlock(grid.getBlock(p.positionNextTo(d, 1))));
            break;
            case LEFT:
                list.set(index, getColorIDOfBlock(grid.getBlock(p.positionNextTo(d, 1))));
                list.set(index-1, colorID);
            break;
            case RIGHT:
                list.set(index, getColorIDOfBlock(grid.getBlock(p.positionNextTo(d, 1))));
                list.set(index+1, colorID);
            break;
            default:
                System.out.println("Unknown direction in horizantalListSwap");
            
        }
        
        
    }
    
    private void verticalListSwap(LinkedList<Integer> list, Match3Grid grid, Position p, Direction d){
        if (d == null){
            return;
        }
        int index = numToMatch - 1;
        switch(d){
            case RIGHT: case LEFT:
                list.set(index, getColorIDOfBlock(grid.getBlock(p.positionNextTo(d, 1))));
            break;
            case UP:
                list.set(index, getColorIDOfBlock(grid.getBlock(p.positionNextTo(d, 1))));
                list.set(index-1, colorID);
            break;
            case DOWN:
                list.set(index, getColorIDOfBlock(grid.getBlock(p.positionNextTo(d, 1))));
                list.set(index+1, colorID);
            break;
            default:
                System.out.println("Unknown direction in horizantalListSwap");
            
        }
    }
*/
    
    
    
    private boolean matchingLine(LinkedList<Integer> list, int colorID){
        //printList(list);
        int streak = 0;
        for (Integer i : list){
            if (i == colorID){
                streak ++;
            }
            else{
                streak = 0;
            }
            if (streak == numToMatch){
                return true;
            }
        }
        return false;
    }

    
    @Override
    public String toString(){
        return Integer.toString(colorID);
    }
    
    @Override
    public void drawNormal(Graphics g, Position p) {
        g.drawImage(getBlockImage(),p.x,p.y,1,1,null);
    }
    
    private BufferedImage getBlockImage(){
        switch(colorID){
            case 0: return ImagePool.getPicture("Block/Red Block");
            case 1: return ImagePool.getPicture("Block/Blue Block");
            case 2: return ImagePool.getPicture("Block/Yellow Block");
            case 3: return ImagePool.getPicture("Block/Green Block");
            case 4: return ImagePool.getPicture("Block/Orange Block");
            case 5: return ImagePool.getPicture("Block/Purple Block");
            case 6: return ImagePool.getPicture("Block/Brown Block");
            case 7: return ImagePool.getPicture("Block/Pink Block");
            case 8: return ImagePool.getPicture("Block/Lime Block");
            case 9: return ImagePool.getPicture("Block/Light Blue Block");
            case 10: return ImagePool.getPicture("Block/Lavender Block");
            case 11: return ImagePool.getPicture("Block/Tan Block");
        }
        return ImagePool.getPicture("Default Image");
    }
    
    
}
