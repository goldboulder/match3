/*

 */
package Grid;


public class Position {
    public int y;
    public int x;
    
    public Position(int y, int x){
        this.y = y;
        this.x = x;
    }

    Position(Position p) {
        this.y = p.y;
        this.x = p.x;
    }
    
    public Position positionNextTo(Direction d, int distance){
        switch (d){
            case UP:
                return new Position(y - distance,x);
            case DOWN:
                return new Position(y + distance,x);
            case LEFT:
                return new Position(y,x - distance);
            case RIGHT:
                return new Position(y,x + distance);
            default:
                System.out.println("Something went wrong in swapBlocks");
                return new Position(0,0);
        }
    }
    
    @Override
    public boolean equals(Object o){
        if (o == null){
            return false;
        }
        
        if (!(o instanceof Position)){
            return false;
        }
        
        Position p = (Position) o;
        return (y==p.y && x==p.x);
    }
    
    @Override
    public String toString(){
        return y + ", " + x;
    }
    
}
