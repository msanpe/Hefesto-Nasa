
package hefesto.Maps;

import java.awt.Canvas;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.image.BufferedImage;

/**
 *
 * @author Jose Vicente Lozano Copa
 */
public class MapCanvas extends Canvas{
    
    public int WIDTH;
    public int HEIGHT;

    BufferedImage imagen;
    
    public MapCanvas(int w, int h){
        WIDTH = w;
        HEIGHT = h;
        Dimension size = new Dimension (WIDTH, HEIGHT);
        setPreferredSize(size);
        imagen = new BufferedImage(w, h, BufferedImage.TYPE_INT_RGB);
    }
    
    public void paint(BufferedImage img, int offx, int offy){
        System.out.println("" + img.getWidth() + " " + img.getHeight());
        for (int x = 0;x<img.getWidth();x++)
            for (int y=0;y<img.getHeight();y++){
                imagen.setRGB(offx+x, offy+y, img.getRGB(x, y));
               
            }
    }
    
    @Override
    public void paint(Graphics g){
        g.drawImage(imagen, 0, 0, null);
        
       // g.setColor(Color.BLUE);
       // g.drawRect(10, 10, 100, 100);
    }
}
