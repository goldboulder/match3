/*

 */
package Grid;

import java.awt.Graphics;


public class EmptyBlock extends Block{//needed?

    @Override
    public boolean isNull() {
        return true;
    }
    
    public int getColorId(){
        return -1;
    }
    
    public void setColorID(int newID) {
        
    }
    
    @Override
    public void deleteAction(Match3Grid grid, DeleteLog deleteLog) {
        
    }

    @Override
    public void doGameTick(Match3Grid grid, Position p, DeleteLog deleteLog) {
        
    }
    
    @Override
    public String toString(){
        return "_";
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
    public void blockGravity(Match3Grid grid, Position p){
        //System.out.println("no grav");
    }

    @Override
    public void drawNormal(Graphics g, Position p) {
        
    }
    
    
    
}
