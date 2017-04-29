
package hefesto;

import hefesto.Maps.MapJFrame;
import hefesto.Maps.Mapa;

import java.io.File;
import java.io.PrintWriter;

/**
 * @author Jose Vicente
 */
public class Hefesto {

    
    public static void main(String[] args) {
        MyMapsTest();
    }
    
    public static void mapsBrowserTest(){
    }

    public static void MyMapsTest(){
        try{
            //Mapa m = new Mapa(39.792520, -0.538822, 39.751799, -0.451287, 19);
            //m.refreshFromGoogle();
            //m.saveToFile("mimapa.map");

            Mapa m2 = Mapa.loadFromFile("mapaMarines.map");
            //new MapJFrame(768, 512, m2);

        }
            catch(Exception e){
            System.out.println("Error: "+e.toString());}
    }
    
    
}
