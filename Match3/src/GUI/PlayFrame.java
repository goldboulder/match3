/*

 */
package GUI;

import java.awt.Dimension;
import javax.swing.BoxLayout;
import javax.swing.JFrame;

/**
 *
 * @author Nathan
 */
public class PlayFrame extends JFrame{
    
    private GridPanel gridPanel;
    private ScorePanel scorePanel;
    
    public static final int WIDTH = 1000;
    public static final int HEIGHT = 800;
    public static final int SCORE_WIDTH = 200;
    
    public PlayFrame(){
        gridPanel = new GridPanel(this);
        scorePanel = new ScorePanel(this);
        
        add(gridPanel);
        add(scorePanel);
        
        setLayout(new BoxLayout(this.getContentPane(),BoxLayout.X_AXIS));
        getContentPane().setPreferredSize(new Dimension(WIDTH,HEIGHT));
        gridPanel.setPreferredSize(new Dimension(WIDTH-SCORE_WIDTH,HEIGHT));
        scorePanel.setPreferredSize(new Dimension(SCORE_WIDTH,HEIGHT));
        gridPanel.setMinimumSize(new Dimension(WIDTH-SCORE_WIDTH,HEIGHT));
        scorePanel.setMinimumSize(new Dimension(SCORE_WIDTH,HEIGHT));
        gridPanel.setMaximumSize(new Dimension(WIDTH-SCORE_WIDTH,HEIGHT));
        scorePanel.setMaximumSize(new Dimension(SCORE_WIDTH,HEIGHT));
        
        setTitle("Match 3");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        pack();
        setLocationRelativeTo(null);
        setVisible(true);
    }
    
    
}
