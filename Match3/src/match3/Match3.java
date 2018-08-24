/*

 */
package match3;

import GUI.PlayFrame;
import Grid.Match3Grid;
import Grid.StandardBlockReplacer;


public class Match3 {

    
    
    public static void main(String[] args) {
        new PlayFrame();
        //test();
    }
    
    public static void test(){
        Match3Grid grid = new Match3Grid(7,7,new StandardBlockReplacer(new int[]{0,1}));
        
        System.out.println(grid);
        /*
        grid.setBlock(new Position(0,0), new BasicColoredBlock(0));
        grid.setBlock(new Position(1,0), new BasicColoredBlock(0));
        grid.setBlock(new Position(2,0), new BasicColoredBlock(0));
        System.out.println(grid);
        System.out.println("Now doing game tick");
        grid.doGameTick();
        for (int t = 0; t < 30; t++){
            for (int i = 0; i < 1; i++){
                grid.doGameTick();
            }
            System.out.println(grid);
        }
*/
        
    }
    
}
