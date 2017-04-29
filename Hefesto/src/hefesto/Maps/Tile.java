
package hefesto.Maps;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.ByteArrayOutputStream;
import java.io.DataInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import static java.lang.Thread.sleep;
import java.net.URL;
import java.text.DecimalFormat;
import java.util.Base64;
import java.util.logging.Level;
import java.util.logging.Logger;
import static hefesto.Maps.googleMapsCoonector.staticMapsKey;

/**
 *
 * @author user
 */
public class Tile {
  
    private int resolucionAltitud = 10;
    
    private RealPoint centro;
    
    private int zoom;
    
    private PuntoAltitud[][] altitud;
    
    private int x = 0;
    
    private int y = 0;
    
    private byte[] imagen = null;
    
    private static final int MaxDownloadThread = 20;
    
    private static int downloadThread = 0;
    
    
    
    public RealPoint center(){return centro;}
    
    
    
    public Tile(int x_, int y_, int zoom_, int res){
        resolucionAltitud = res;
        centro = calcLonLatFromTile(0.5+x, 0.5+y, zoom_);
        zoom = zoom_;
        x = x_;
        y = y_;
        
        altitud = new PuntoAltitud[resolucionAltitud][];
        double factor = 1.0/resolucionAltitud;
        for (int i=0;i<resolucionAltitud;i++){
            altitud[i] = new PuntoAltitud[resolucionAltitud];
            for (int j=0;j<resolucionAltitud;j++){
                altitud[i][j] = new PuntoAltitud(x+j*factor, y+i*factor);
            }
        }
    }
    
    public int getResolucionAltitud(){
        return resolucionAltitud;
    }
    
    public RealPoint getRealPoint(int i, int j){
        return calcLonLatFromTile(altitud[i][j].x, altitud[i][j].y, zoom);
    }

    private void refreshImage(){
        imagen = new byte[0];
        while(downloadThread >= MaxDownloadThread){
            try {
                sleep(1);
            } catch (InterruptedException ex) {
                Logger.getLogger(Tile.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        Thread T = new Thread(){
            public void run(){
                imagen = getStaticMap(x, y, zoom, 256);
            }
        };
        downloadThread++;
        T.start();        
    }

    
    public void setAltitud(int x_, int y_, double d){
        altitud[y_][x_].altitud = d;
    }
    
    public double getAltitud(int x_, int y_){
        return altitud[y_][x_].altitud;
    }
    
    public PuntoAltitud getPunto(int i, int j){
        return altitud[i][j];
    }

    public void setImagen(byte[] map_){
        if (map_ != null)    imagen = map_;
    }
    
    public byte[] getImagen(){
        if (imagen == null) refreshImage();
        return imagen;
    }

    void toBufferedWriter(BufferedWriter writer) throws IOException {
        DecimalFormat formatter = new DecimalFormat("#0.00");     
        writer.write(""+x + "\n");
        writer.write(""+y + "\n");
        writer.write(""+resolucionAltitud+"\n");
        for (int i=0;i<resolucionAltitud;i++)
            for (int j=0;j<resolucionAltitud;j++){
                writer.write(""+formatter.format(altitud[j][i].x) + "\n");
                writer.write(""+formatter.format(altitud[j][i].y) + "\n");
                writer.write(""+formatter.format(altitud[j][i].altitud) + "\n");
            }
                
    }
    
    public static Tile fromBufferedReader(BufferedReader reader, int zoom) throws IOException{
        int X = Integer.parseInt(reader.readLine());
        int Y = Integer.parseInt(reader.readLine());
        int res = Integer.parseInt(reader.readLine());
        
        //double altitud = Double.parseDouble(reader.readLine());
        
        Tile salida = new Tile(X, Y, zoom, res);

        for (int i=0;i<res;i++)
            for (int j=0;j<res;j++){
                salida.getPunto(i, j).x = Double.parseDouble(reader.readLine());
                salida.getPunto(i, j).y = Double.parseDouble(reader.readLine());
                salida.getPunto(i, j).altitud = Double.parseDouble(reader.readLine());
            }
        
        return salida;
    }
    
    
    
    public static Point calcTileFromLonLat(RealPoint punto, int zoom){
        double lat_rad = punto.Latitud()/180 * Math.PI;
        double n = Math.pow(2, zoom);
        double x =  (punto.Longitud() + 180.0) / 360.0 * n;
        double y = (1.0 - Math.log(Math.tan(lat_rad) + (1 / Math.cos(lat_rad))) / Math.PI) / 2.0 * n;
        return new Point(x, y);
    }
    
    public static RealPoint calcLonLatFromTile(double xtile, double ytile, int zoom){
        double n = Math.pow(2, zoom);
        double lon_deg = xtile / n * 360.0 - 180.0;
        double lat_rad = Math.atan(Math.sinh(Math.PI * (1 - 2 * ytile / n)));
        double lat_deg = 180.0 * (lat_rad / Math.PI);
        return new RealPoint(lat_deg, lon_deg);
    }
    
    
    public static byte[] getStaticMap(int X, int Y, int Zoom, int size){
        String url = "";
        try{
            String Fname = "imagen"+X+"_"+Y+"_"+Zoom+".png";
            File f = new File(Fname);
            if (f.exists()){
                FileInputStream fis = new FileInputStream(f);
                ByteArrayOutputStream buffer = new ByteArrayOutputStream();

                int nRead;
                byte[] data = new byte[16384];

                while ((nRead = fis.read(data, 0, data.length)) != -1) {
                    buffer.write(data, 0, nRead);
                }
                buffer.flush();
                fis.close();
                downloadThread--;
                return buffer.toByteArray();                
            }else{
                url = "http://d.maptile.maps.svc.ovi.com/maptiler/v2/maptile/newest/satellite.day/"+Zoom+"/"+X+"/"+Y+"/"+size+"/png8";
                URL u = new URL(url);
                InputStream is = u.openStream();
                DataInputStream dis = new DataInputStream(is);
                ByteArrayOutputStream out = new ByteArrayOutputStream();

                int tambufer = 2048;
                byte[] buffer = new byte[tambufer];
                int n=0;
                while (-1!=(n=dis.read(buffer))) {
                    out.write(buffer, 0, n);
                }
                is.close();
                out.close();
                byte[] salida = out.toByteArray();

                FileOutputStream fo = new FileOutputStream(Fname);
                fo.write(salida);
                fo.close();
                System.out.println("Descargada "+Fname);
                downloadThread--;
                return salida;
            }
        }catch(Exception e){
            System.out.println("Error descargando imagen " + url);
            System.out.println(e.toString());
            downloadThread--;
            return new byte[0];
        }
    }
    
    
}
