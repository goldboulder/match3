/*

 */
package Grid;

import java.util.LinkedList;


public class DeleteLog {
    
    private LinkedList<LogEntry> log;
    
    public DeleteLog(){
        log = new LinkedList<>();
    }
    
    public void addMatch(Match3Grid grid, Position startPosition, int streak, Direction direction) {
        DestroyType type = directionToDestroyType(direction);
        
        //System.out.println("Log entry: StartPosition: " + startPosition + " Streak: " + streak + " Direction: " + direction + " Duplicate: " + identicalMatch(startPosition,streak,type));
        
        if (!identicalMatch(startPosition,streak,type) && !(spaceUnderBlocks(grid,startPosition,streak))){
            log.add(new LogEntry(startPosition,streak,type));
        }
    }
    
    //returns true if there is already a match for this entry
    private boolean identicalMatch(Position startPosition, int streak, DestroyType type) {//coloredBlocks assume it checks from left to right**
        //convert right/left to left/right, up,down... if nessesary***
        for (LogEntry entry : log){
            if (entry.startPosition.equals(startPosition) && entry.destroyType == type){
                return true;
            }
        }
        return false;
    }
    
    //determines if there are any empty spaces under a match. Used to prevent matches matching falling blocks.
    private boolean spaceUnderBlocks(Match3Grid grid, Position startPosition, int streak){
        for (int i = 0; i < streak; i++){
            Position currentPositionInStreak = startPosition.positionNextTo(Direction.RIGHT, i);
            if (grid.getBlockWithoutFailsafe(currentPositionInStreak.positionNextTo(Direction.DOWN, 1)) instanceof EmptyBlock){
                return true;
            }
        }
        return false;
    }
    
    private DestroyType directionToDestroyType(Direction d){
            switch (d){
                case UP: case DOWN:
                    return DestroyType.MATCH_VERTICAL;
                case LEFT: case RIGHT:
                    return DestroyType.MATCH_HORIZANTAL;
                default:
                    return DestroyType.OTHER;
            }
    }
    
    public void deleteBlocks(Match3Grid grid) {//gridSpace.deleteBlock()//+ any scoreing arguments... gridSpace.deleteBlock(block)//delete any blocks that need deleting(matches, bombs, ect)
        //if empty and no blocks are busy, check if stuck?***
        
        //block.deleteAction(grid,this)
        // do until empty
        //make sure it handles duplicate entries (blocks being deleted twice)
        while (!log.isEmpty()){
            LogEntry currentEntry = log.poll();
            //System.out.println("hi");
            currentEntry.handleDelete(grid,this);
        }
    }
    
    
    
    
    
    private class LogEntry {
        public Position startPosition;
        public int streak;
        public DestroyType destroyType;
        
        public LogEntry(Position startPosition, int streak, DestroyType destroyType){
            //System.out.println("Added to Log: " + startPosition + " " + streak + " " + destroyType);
            this.startPosition = startPosition;
            this.streak = streak;
            this.destroyType = destroyType;
        }
        
        public void handleDelete(Match3Grid grid, DeleteLog log){
            //addPoints?
            GridSpace[] gridSpaces = getGridSpaces(grid);
            for (GridSpace gridSpace : gridSpaces){
                gridSpace.deleteBlock(grid,log,destroyType);
            }
        }
        
        private GridSpace[] getGridSpaces(Match3Grid grid){
            GridSpace[] gridSpaces = new GridSpace[streak];
            Direction d = null;
            switch (destroyType){
                case MATCH_VERTICAL:
                    d = Direction.DOWN;
                break;
                case MATCH_HORIZANTAL:
                    d = Direction.RIGHT;
                break;
                default:
                    //other will still work for what's below
            }
            
            for (int i = 0; i < streak; i++){
                //System.out.println(startPosition.positionNextTo(d, i));
                gridSpaces[i] = grid.getGridSpace(startPosition.positionNextTo(d, i));
            }
            return gridSpaces;
        }
        
    }
    
}
