
package hefesto.Maps;

import java.awt.FlowLayout;
import java.awt.image.BufferedImage;
import javax.swing.*;

import static java.lang.Thread.sleep;

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
        this.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        MapCanvas canvas = new MapCanvas(w, h);
        add(canvas);
        map = map_;


        this.setVisible(true);


       new Thread(() -> {
           while(true){
               try {
                   sleep(500);
               } catch (InterruptedException ignored) {}
               paintMaps(canvas);
           }
       }).start();
    }

    public void paintMaps(MapCanvas canvas){
        for (int i = 0; i < 2; i++) {
            for (int j = 0; j < 3; j++) {
                System.out.println("imagen "+i+","+j+ "  ");
                canvas.paint( map.getImagen(i, j), 256*j, 256*i);
            }
        }
        canvas.repaint();
    }

    
    
    
    
}
