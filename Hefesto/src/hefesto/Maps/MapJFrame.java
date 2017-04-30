
package hefesto.Maps;

import hefesto.Weather;

import java.util.List;
import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;

/**
 * @author Jose Vicente Lozano Copa
 */
public class MapJFrame extends JFrame {
    private static final Color COLOR_01 = new Color(250, 23, 0, 188);
    private static final Color COLOR_02 = new Color(255, 87, 34, 214);
    private static final Color COLOR_03 = new Color(255, 152, 0, 197);
    private static final Color COLOR_04 = new Color(255, 213, 79, 192);
    private static final Color COLOR_05 = new Color(255, 241, 118, 221);

    private Mapa map;
    private int x_offset, y_offset;

    public MapJFrame(int w, int h, Mapa map_) {
        super();
        this.setSize(w, h);
        this.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        x_offset = y_offset = 0;
        MapCanvas canvas = new MapCanvas(w, h);
        add(canvas);
        map = map_;
        this.setVisible(true);
        KeyboardFocusManager.getCurrentKeyboardFocusManager()
                .addKeyEventDispatcher(e -> {
                    if (e.getID() != KeyEvent.KEY_PRESSED) return false;
                    switch (e.getKeyCode()) {
                        case KeyEvent.VK_UP:
                            y_offset = Math.max(0, y_offset-1);
                            break;
                        case KeyEvent.VK_DOWN:
                            y_offset = Math.min(76, y_offset + 1);
                            break;
                        case KeyEvent.VK_LEFT:
                            x_offset = Math.max(0, x_offset - 1);
                            break;
                        case KeyEvent.VK_RIGHT:
                            x_offset = Math.min(115, x_offset + 1);
                            break;
                    }
                    paintMaps(canvas);
                    return false;
                });
        
        new Thread() {
            public void run(){
                while(true){
                    try{
                        sleep(3000);
                    }catch(Exception e){}
                    paintMaps(canvas);
                }
            }
        }.start();
    }

    private void paintMaps(MapCanvas canvas) {
        for (int i = 0; i < 4; i++) {
            for (int j = 0; j < 7; j++) {
                //System.out.println("imagen " + i + "," + j + "  ");
                canvas.paint(map.getImagen(i + y_offset, j + x_offset), 256 * j, 256 * i);
            }
        }
        paintSpeed(canvas);
        canvas.repaint();

        List<PuntoAltitud> qem = map.getQuemado();
        for (int i=0;i<qem.size();i++)
            drawPoint(canvas, qem.get(i).x, qem.get(i).y, Color.BLACK);

        List<PuntoAltitud> mas4 = map.getPrediccion(5);
        for (int i=0;i<mas4.size();i++){
            drawPoint(canvas, mas4.get(i).x, mas4.get(i).y, COLOR_05);
        }
        List<PuntoAltitud> mas3 = map.getPrediccion(4);
        for (int i=0;i<mas3.size();i++){
            drawPoint(canvas, mas3.get(i).x, mas3.get(i).y, COLOR_04);
        }
        List<PuntoAltitud> mas2 = map.getPrediccion(3);
        for (int i=0;i<mas2.size();i++){
            drawPoint(canvas, mas2.get(i).x, mas2.get(i).y, COLOR_03);
        }
        List<PuntoAltitud> mas1 = map.getPrediccion(2);
        for (int i=0;i<mas1.size();i++){
            drawPoint(canvas, mas1.get(i).x, mas1.get(i).y, COLOR_02);
        }
        for (int i=0;i<map.getEstadoActual().size();i++){
            drawPoint(canvas, map.getEstadoActual().get(i).x, map.getEstadoActual().get(i).y, COLOR_01);
        }
        
    }

    private void paintSpeed(MapCanvas canvas) {
        int w = canvas.imagen.getWidth();
        int h = canvas.imagen.getHeight();
        BufferedImage bimage = new BufferedImage(w, h, BufferedImage.TYPE_INT_ARGB);
        Graphics2D g2d = bimage.createGraphics();
        g2d.drawImage(canvas.imagen, 0, 0, null);
        g2d.setPaint(Color.WHITE);
        g2d.setFont(new Font("Serif", Font.BOLD, 40));
        String s = "Tiempo x " + 60/(Mapa.Tiempo/1000f) + "\n";
        s = getWindDegrees(s);
        FontMetrics fm = g2d.getFontMetrics();
        int x = 15, y = 0;
        for (String line : s.split("\n"))
            g2d.drawString(line, x, y += g2d.getFontMetrics().getHeight());
        g2d.dispose();
        canvas.paint(bimage, 0, 0);
        canvas.repaint();
        canvas.repaint();
    }

    private String getWindDegrees(String s) {
        try {
            Weather weather = Weather.getGson();
            s += "Dirección viento: " + /*weather.getWind_dir()*/ "SE";
        } catch (Exception e) {
            System.out.println(e);
        }
        return s;
    }

    private void drawPoint(MapCanvas canvas, double x_, double y_, Color c) {
        int size = 7;

        int x = (int)((x_ - map.mapa[0][0].getX())*64);
        int y = (int)((y_ - map.mapa[0][0].getY())*64);
  
        BufferedImage bimage = new BufferedImage(size, size, BufferedImage.TYPE_INT_ARGB);
        Graphics2D g2d = bimage.createGraphics();
        g2d.setColor(c);
        g2d.fill(new Rectangle2D.Float(0, 0, size, size));
        g2d.dispose();
        int rel_x = x - x_offset * 256,
                rel_y = y - y_offset * 256;
        if (rel_x >= 0 && rel_x <= 1792 && rel_y >= 0 && rel_y <= 1024) {
            canvas.paint(bimage, Math.max(0, rel_x-size), Math.max(0, rel_y-size));
            canvas.repaint();
        } else {
            //System.out.println("No pinto");
        }
    }


}
