/*

 */
package GUI;

import java.awt.Color;
import javax.swing.JPanel;


public class ScorePanel extends JPanel{
    
    private PlayFrame frame;
    
    public ScorePanel(PlayFrame frame){
        this.frame = frame;
        setBackground(Color.RED);
    }
}
