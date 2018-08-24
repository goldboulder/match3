/*

 */
package Grid;

import java.util.ArrayList;

/**
 *
 * @author Nathan
 */
public class StandardBlockReplacer implements BlockReplacer{
    private int[] colors;
    
    public StandardBlockReplacer(int[] colors){
        this.colors = colors;
    }
    
    public void initialSetUp(Match3Grid grid){
        if (colors.length <= 0){
            //fill all grids with null block
            for (int y = 0; y < grid.height(); y ++){
                for (int x = 0; x < grid.width(); x ++){
                    grid.setBlock(new Position(y,x),new EmptyBlock());
                }
            }
        }
        else if (colors.length == 1){
            //fill all grids with same block
            for (int y = 0; y < grid.height(); y ++){
                for (int x = 0; x < grid.width(); x ++){
                    grid.setBlock(new Position(y,x),new BasicColoredBlock(colors[0]));
                }
            }
        }
        else if (colors.length == 2){
            placeWithoutMatching2Colors(grid);
        }
        else{
            for (int y = 0; y < grid.height(); y ++){
                for (int x = 0; x < grid.width(); x ++){
                    placeWithoutMatching3Colors(grid,new Position(y,x));
                }
            }
        }
        
    }
    
    //places a random block, if it makes a match, try again
    private void placeWithoutMatching3Colors(Match3Grid grid, Position p){
        while (true){
            Block b = getNextBlock();
            if (b instanceof ColoredBlock){
                ColoredBlock c = (ColoredBlock) b;
                grid.setBlock(p,c);
                if (!c.matching(grid, p,null)){
                    return;
                }
            }
            //other blocks?
            //System.out.println("match at " + y + " " + x + ": " + grid.getBlock(y, x).getColorId());
        }
    }
    
    private void placeWithoutMatching2Colors(Match3Grid grid) {
        //initially, fill up the board randomly
        for (int y = 0; y < grid.height(); y ++){
            for (int x = 0; x < grid.width(); x ++){
                grid.setBlock(new Position(y,x),new BasicColoredBlock(colors[(int)(Math.random()*2)]));
            }
        }
        
        boolean clear = false;
        
        while (!clear){//always does 2*2 tiles
            clear = true;
            for (int y = 0; y < grid.height(); y ++){
                for (int x = 0; x < grid.width(); x ++){
                    Position p = new Position(y,x);
                    ColoredBlock c = (ColoredBlock) grid.getBlock(p);
                    if (c.matching(grid, p,null)){
                        clear = false;
                        if (Math.random() < 0.5){
                            changeBlock2Color(c);
                        }
                    }
                }
            }
        }
        
    }
    
    private void changeBlock2Color(ColoredBlock c){
        if (c.getColorId() == colors[0]){
            c.setColorID(colors[1]);
        }
        else{
            c.setColorID(colors[0]);
        }
    }
    
    private Block getNextBlock(){
        return new BasicColoredBlock(colors[(int)(Math.random()*colors.length)]);
    }

    @Override
    public void replaceBlocks(Match3Grid grid) {//gravity?
        for (int x = 0; x < grid.width(); x++){//replace from top (gravity)
            Position p = new Position(0,x);
            GridSpace g = grid.getGridSpace(p);
            if (g.getBlock() instanceof EmptyBlock){
                
                g.replaced(getNextBlock());
            }
        }
    }

    
    
    
    
}
