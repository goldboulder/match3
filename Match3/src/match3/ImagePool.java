/*

 */
package match3;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.imageio.ImageIO;


public class ImagePool {
    
    private static ImagePool instance;
    private static HashMap<String,BufferedImage> imagePool;
    private static BufferedImage defaultImage;
    
    private ImagePool(){
        imagePool = new HashMap<>();
        try {
            defaultImage = ImageIO.read(new File("pictures/Default Image.png"));
        } catch (IOException ex) {
            Logger.getLogger(ImagePool.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    public  static BufferedImage getPicture(String address){//address should not have spaces
        if (instance == null){
            instance = new ImagePool();
        }
        
        if (address.startsWith("pictures/")){
            address = address.substring(9,address.length());//9 = length of "pictures/"
        }
        if (address.endsWith(".png")){
            address = address.substring(0, address.length()-4);// 4 = length of ".png"
        }
        try{
            BufferedImage img = imagePool.get(address);
            if (img == null){
                img = ImageIO.read(new File("pictures/" + address + ".png"));
                
                imagePool.put(address, img);
                return img;
            }
            return img;
        }
        catch(IOException ex){
            //System.out.println("Could not find image: " + address);
            return defaultImage;
        }
    }
    
    public  static BufferedImage[] getPictures(String address){// animation pool? return that if it exists, else, do method. could improve performance by not checking for thumbnails.
        if (instance == null){
            instance = new ImagePool();
        }
        
        String[] directory = new File("pictures/" + address).list();
        directory = removeThumb(directory);
        
        
        BufferedImage[] images = new BufferedImage[directory.length];
        for (int i = 0; i < images.length; i++){
            String noPngSubString = directory[i].substring(0, directory[i].length() - 4);// remove duplicate .png
            
            images[i] = getPicture(address + "/" + noPngSubString);
            
        }
        return images;
    }
    
    private static String[] removeThumb(String[] address){//weird thumb file in some folders
        ArrayList<String> arrayAddress = new ArrayList<>();
        for (int i = 0; i < address.length; i++){
            if (!address[i].contains("Thumb")){
                arrayAddress.add(address[i]);
            }  
            
        }
        return (String[])arrayAddress.toArray(new String[address.length -1]);
    }
    
    
    
    public static int numImages(){
       if (instance == null){
            instance = new ImagePool();
        } 
        return imagePool.size();// don't count default image
        
    }
    
}