
package hefesto.Maps;

import java.awt.FlowLayout;
import java.awt.image.BufferedImage;
import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JLabel;

/**
 * @author Jose Vicente Lozano Copa
 */
public class MapJFrame extends JFrame{
    
    private Mapa map;
    
    int offsetX = 0;
    
    int offsetY = 0;
    
    JLabel l1;
    
    public MapJFrame(int w, int h, Mapa map_){
        super();
        this.setSize(w, h);
        this.setDefaultCloseOperation( JFrame.EXIT_ON_CLOSE );
        MapCanvas canvas = new MapCanvas(w, h);
        add(canvas);
        map = map_;
        BufferedImage img = map.getImagen(0, 0);
        canvas.paint(img, 0, 0);
        this.setVisible(true);
    }
    
    
    
    
}
