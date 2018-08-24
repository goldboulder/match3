/*

 */
package GUI;

import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.geom.Point2D;
import java.awt.geom.Rectangle2D;
import javax.swing.JPanel;


public class Camera {
    
    private JPanel panel;
    
    public static final double DEFAULT_ZOOM_SCALE = 1.05;
    public static final double DEFAULT_SCROLL_PERCENT = 0.01;
    public static final double DEFAULT_MIN_SIZE = 1;
    public static final double DEFAULT_MAX_SIZE = 1000;
    
    private Point2D.Double position;//top left corner
    private Point2D.Double size;
    
    private double zoomScale = DEFAULT_ZOOM_SCALE;
    private double scrollPercent = DEFAULT_SCROLL_PERCENT;
    private double minSize = DEFAULT_MIN_SIZE;
    private double maxSize = DEFAULT_MAX_SIZE;
    
    public Camera(JPanel panel){
        this.panel = panel;
        position = new Point2D.Double(0,0);
        size = new Point2D.Double(10,10);
    }
    
    public Camera(JPanel panel, double xPosition, double yPosition, double xSize, double ySize){
        this.panel = panel;
        position = new Point2D.Double(xPosition - xSize/2,yPosition - ySize/2);
        size = new Point2D.Double(xSize,ySize);
    }
    public Camera(JPanel panel, Point2D.Double position, Point2D.Double size){
        this.panel = panel;
        this.position = new Point2D.Double(position.x - size.x/2, position.y - size.y/2);
        this.size = new Point2D.Double(size.x,size.y);
    }
    
    public void draw(Graphics g, Drawable d){
        //transform,draw, transform back? copy of g?
        Graphics2D g2 = (Graphics2D) g;
        
        g2.scale(panel.getWidth()/size.x, panel.getHeight()/size.y);
        g2.translate(-position.x, -position.y);
        
        d.draw(g);
        
        g2.translate(position.x, position.y);
        g2.scale(size.x/panel.getWidth(), size.y/panel.getHeight());
    }
    
    public Point2D.Double gameSpaceToScreenSpace(Point2D.Double gamePoint){
        double xPercent = (double)(gamePoint.x - position.x)/size.x;
        double yPercent = (double)(gamePoint.y - position.y)/size.y;
        double x = panel.getWidth() * xPercent;
        double y = panel.getHeight() * (1-yPercent);
        
        return new Point2D.Double(x,y);
    }
    
    public Point2D.Double screenSpaceToGameSpace(Point2D.Double screenPoint){
        double xPercent = (double)(screenPoint.x)/panel.getWidth();
        double yPercent = (double)(panel.getHeight()-screenPoint.y)/panel.getHeight();
        double x = position.x + size.x * xPercent;
        double y = position.y + size.y * yPercent;
        
        return new Point2D.Double(x,y);
    }
    
    
    
    public double getXMin(){
        return position.x;
    }
    
    public double getXMax(){
        return position.x + size.x;
    }
    
    public double getYMin(){
        return position.y + size.y;
    }
    
    public double getYMax(){
        return position.y;
    }
    
    public double getXSize(){
        return size.x;
    }
    
    public double getYSize(){
        return size.y;
    }
    
    //returns the game point at the center of the screen
    public Point2D.Double getCenter(){
        return new Point2D.Double(position.x + size.x/2, position.y + size.y/2);
    }
    
    public void setSize(Point2D.Double newSize){
        setSizeAndCenter(newSize.x,newSize.y);
    }
    
    public double getMaxSize() {
        return maxSize;
    }
    
    public void setMaxSize(double maxSize) {
        this.maxSize = maxSize;
    }

    public double getMinSize() {
        return minSize;
    }

    public void setMinSize(double minSize) {
        this.minSize = minSize;
    }
    
    public double getZoomScale() {
        return zoomScale;
    }
    
    public void setZoomScale(double zoomScale) {
        this.zoomScale = zoomScale;
    }

    public double getScrollPercent() {
        return scrollPercent;
    }
    
    public void setScrollPercent(double scrollPercent) {
        this.scrollPercent = scrollPercent;
    }
    
    // multiplies x and y sizes by amount specified
    // x and y position change so center is still at same point
    public void zoom(double scale){
        setSizeAndCenter(size.x / scale, size.y / scale);
    }
    
    // zooms so that x and y sizes are those specified.
    // x and y position change so center is still at same point
    public void setSizeAndCenter(double xSize, double ySize){
        if (xSize < maxSize && ySize < maxSize && xSize > minSize && ySize > minSize){
            translate(size.x/2, size.y/2);
            size.x = xSize;
            size.y = ySize;
            translate(-size.x/2, -size.y/2);
        }
    }
    
    public void setBounds(double xMin, double yMin, double xMax, double yMax){
        
        if (xMax < xMin){
            setBounds(xMax,yMin,xMin,yMax);
            return;
        }
        if (yMax < yMin){
            setBounds(xMin,yMax,xMax,yMin);
            return;
        }
        double xSize = xMax - xMin;
        double ySize = yMax - yMin;
        if (xSize < maxSize && ySize < maxSize && xSize > minSize && ySize > minSize){
            size.x = xSize;
            size.y = ySize;
        }
        position.x = xMin;
        position.y = yMin;
        System.out.println("Set bounds results: Position: " + position + ", size: " + size);
    }
    
    public void translate(double dx, double dy){
        position.x += dx;
        position.y += dy;
    }
    
    public void translatePercent(double dx, double dy){// here dx = 1 means move the screen over by one screen
        position.x += size.x * dx;
        position.y += size.y * dy;
    }
    
    public void translate(Point2D.Double ds){
        translate(ds.x,ds.y);
    }
    
    //moves the camera so that the center of the screen is the specified point
    public void setCenter(double x, double y){
        position.x = x - size.x/2;
        position.y = y - size.y/2;
    }
    
    public void setCenter(Point2D.Double position){
        setCenter(position.x,position.y);
    }

    
    
    /*
    private double percentOnScreenX(double x) {//decouple?
        return (x - position.x)/size.x;
    }

    private double percentOnScreenY(double y) {
        return (y - position.y)/size.y;
    }
*/
    
    public boolean onScreen(Point2D.Double point){
        //return onScreen(point.x,point.y);
        return point.x > position.x && (point.x < position.x + size.x) && point.y > position.y && (point.y < position.y + size.y);
    }
    
    
    public boolean onScreen(Rectangle2D.Double rect){
        Rectangle2D.Double screenRect = new Rectangle2D.Double(position.x,position.y,size.x,size.y);
        return screenRect.intersects(rect);
    }
    
    /*
    public void transform(Graphics2D g2, int screenWidth, int screenHeight){
        g2.scale(screenWidth/size.x, screenHeight/size.y);
        g2.translate(-position.x, -position.y);
    }
    
    public void transformBack(Graphics2D g2, int screenWidth, int screenHeight){
        g2.translate(position.x, position.y);
        g2.scale(size.x/screenWidth, size.y/screenHeight);
    }
*/
    
}
