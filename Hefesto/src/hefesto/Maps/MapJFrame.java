
package hefesto.Maps;

import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;

/**
 * @author Jose Vicente Lozano Copa
 */
public class MapJFrame extends JFrame {
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

        new Thread(() -> paintMaps(canvas)).start();
    }

    private void paintMaps(MapCanvas canvas) {
        int x = 768;
        int y = 900;
        for (int i = 0; i < 2; i++) {
            for (int j = 0; j < 3; j++) {
                System.out.println("imagen " + i + "," + j + "  ");
                canvas.paint(map.getImagen(i + y_offset, j + x_offset), 256 * j, 256 * i);
            }
        }
        canvas.repaint();
        drawPoint(canvas, x_offset, y_offset, x, y);
    }

    private void drawPoint(MapCanvas canvas, int x_offset, int y_offset, int x, int y) {
        int size = 5;
        BufferedImage bimage = new BufferedImage(size, size, BufferedImage.TYPE_INT_ARGB);
        Graphics2D g2d = bimage.createGraphics();
        g2d.setColor(Color.red);
        g2d.fill(new Rectangle2D.Float(0, 0, size, size));
        g2d.dispose();
        int rel_x = x - x_offset * 256,
                rel_y = y - y_offset * 256;
        if (rel_x >= 0 && rel_x <= 768 && rel_y >= 0 && rel_y <= 512) {
            canvas.paint(bimage, rel_y, rel_x);
        } else {
            System.out.println("No pinto");
        }
    }


}
