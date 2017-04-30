
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
import java.util.logging.Level;
import java.util.logging.Logger;
import static hefesto.Maps.googleMapsCoonector.staticMapsKey;
import java.awt.image.BufferedImage;
import javax.imageio.ImageIO;

/**
 * @author Jose Vicente Lozano Copa
 */
public class Tile {
  
    private int resolucionAltitud = 10;
    
    private RealPoint centro;
    
    private int zoom;
    
    private PuntoAltitud[][] altitud;
    
    private int x = 0;
    
    private int y = 0;
    
    private BufferedImage imagen = null;
    
    private static final int MaxDownloadThread = 20;
    
    private static int downloadThread = 0;
    
    public RealPoint center(){return centro;}
    
    /**
     * Constructor de Tile
     * @param x_    X cuadricula del mundo
     * @param y_    Y cuadricula del mundo
     * @param zoom_ Nivel de zoom (1-20)
     * @param res   numero de subdivisiones del tile para la altitud  resXres puntos
     */
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
    
    /**
     * @return Retorna el valor de resolucion de altitudes, dimesion de la matriz de altitudes
     */
    public int getResolucionAltitud(){
        return resolucionAltitud;
    }

    /**
     * Obtiene las coordenadas geograficas del subpunto i,j
     * @param i Elemento i
     * @param j Elemento j
     * @return Coordenadas geograficas
     */
    public RealPoint getRealPoint(int i, int j){
        return calcLonLatFromTile(altitud[i][j].x, altitud[i][j].y, zoom);
    }

    /**
     * Carga en memoria la imagen de la zona
     * Primero la imagen se busca en la cache de imagenes
     * Si no existiese, se descarga de internet
     */
    private void refreshImage(){
        imagen = new BufferedImage(1, 1, BufferedImage.TYPE_INT_RGB);
        imagen = getStaticMap(x, y, zoom, 256);
    }

    /**
     * establece el valro de altitud para un subelemento del Tile
     * @param x_    Desplazamiento en el eje X
     * @param y_    Desplazamiento en el eje Y
     * @param d     Valor de la altitud
     */
    public void setAltitud(int x_, int y_, double d){
        altitud[y_][x_].altitud = d;
    }
    
    /**
     * OBtiene la altitud de un subelemento del Tile
     * @param x_    Elemento en X
     * @param y_    Elemento en Y
     * @return      Valor de altitud 
     */
    public double getAltitud(int x_, int y_){
        return altitud[y_][x_].altitud;
    }
    
    /**
     * Retorna el subelemento del Tile
     * @param i Fila
     * @param j Columna
     * @return 
     */
    public PuntoAltitud getPunto(int i, int j){
        return altitud[i][j];
    }

    /**
     * Establece el array de bytes de la imagen del Tile
     * @param map_  Array de bytes de la imagen
     */
    //public void setImagen(byte[] map_){
    //    if (map_ != null)    imagen = map_;
    //}
    
    /**
     * Retorna la imagen del Tile, si no se tubiese, se buscaria en cache y de no estar se descargaria de internet
     * @return Imagen del Tile
     */
    public BufferedImage getImagen(){
        if (imagen == null) refreshImage();
        return imagen;
    }

    /**
     * Escribe el Tile y sus subelementos a un buferedWriter para guardar el mapa en un fichero de texto
     * @param writer BufferedWriter de destino
     * @throws IOException No se puede escribir en el fichero
     */
    protected void toBufferedWriter(BufferedWriter writer) throws IOException {
        DecimalFormat formatter = new DecimalFormat("#0.00");     
        writer.write(""+x + "\n");
        writer.write(""+y + "\n");
        writer.write(""+resolucionAltitud+"\n");
        for (int i=0;i<resolucionAltitud;i++)
            for (int j=0;j<resolucionAltitud;j++){
                writer.write( (String)(""+formatter.format(altitud[j][i].x) + "\n").replace(",", "."));
                writer.write( (String)(""+formatter.format(altitud[j][i].y) + "\n").replace(",", "."));
                writer.write( (String)(""+formatter.format(altitud[j][i].altitud) + "\n").replace(",", "."));
            }
    }
    
    /**
     * Recibe un bufferedReader y un zoom para crear un Tile, a continuacion carga del buferedreader
     * todos los subelementos de la altitud
     * @param reader    BuferedREader
     * @param zoom      Zoom para generar el tile
     * @return          Retorna el Tile ya geenrado
     * @throws IOException  Error, no se puede leer el fichero
     */
    protected static Tile fromBufferedReader(BufferedReader reader, int zoom) throws IOException{
        int X = Integer.parseInt(reader.readLine());
        int Y = Integer.parseInt(reader.readLine());
        int res = Integer.parseInt(reader.readLine());
        Tile salida = new Tile(X, Y, zoom, res);
        for (int i=0;i<res;i++)
            for (int j=0;j<res;j++){
                salida.getPunto(j, i).x = Double.parseDouble(reader.readLine().replace(",", "."));
                salida.getPunto(j, i).y = Double.parseDouble(reader.readLine().replace(",", "."));
                salida.getPunto(j, i).altitud = Double.parseDouble(reader.readLine().replace(",", "."));
            }
        return salida;
    }

    /**
     * Obtiene las posiciones X,Y del Tile al que pertenece una coordenada geografica en base a un Zoom
     * @param punto Coordenadas geograficas 
     * @param zoom  Nivel de zoom
     * @return Punto X,Y al que pertenece
     */
    public static Point calcTileFromLonLat(RealPoint punto, int zoom){
        double lat_rad = punto.Latitud()/180 * Math.PI;
        double n = Math.pow(2, zoom);
        double x =  (punto.Longitud() + 180.0) / 360.0 * n;
        double y = (1.0 - Math.log(Math.tan(lat_rad) + (1 / Math.cos(lat_rad))) / Math.PI) / 2.0 * n;
        return new Point(x, y);
    }
    
    /**
     * Recibe un punto X,Y de cuadricula y retorna las coordenadas geograficas en el globo
     * @param xtile X
     * @param ytile Y
     * @param zoom  Zoom 
     * @return Coordenada geografica
     */
    public static RealPoint calcLonLatFromTile(double xtile, double ytile, int zoom){
        double n = Math.pow(2, zoom);
        double lon_deg = xtile / n * 360.0 - 180.0;
        double lat_rad = Math.atan(Math.sinh(Math.PI * (1 - 2 * ytile / n)));
        double lat_deg = 180.0 * (lat_rad / Math.PI);
        return new RealPoint(lat_deg, lon_deg);
    }
    

    /**
     * Retorna la imagen en cache de un Tile X,Y con Zoom Z a tamaño Size.
     * Si dicha imagen no esta en cache, la descargara de internet y la almacenara en cache, a continuacion la retornara
     * @param X TileX
     * @param Y TileY
     * @param Zoom Zoom Z
     * @param size  Tamaño de la imagen (128, 256, 512)
     * @return Imagen de la Tile
     */
    public static BufferedImage getStaticMap(int X, int Y, int Zoom, int size){
        String url = "";
        try{
            String Fname = "imagen"+X+"_"+Y+"_"+Zoom+".png";
            File f = new File(Fname);
            if (f.exists()){
                downloadThread--;
                return ImageIO.read(f);
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
                return getStaticMap(X, Y, Zoom, size);
            }
        }catch(Exception e){
            System.out.println("Error descargando imagen " + url);
            System.out.println(e.toString());
            downloadThread--;
            return new BufferedImage(1, 1, 1);
        }
    }
    
    public int getX() {
        return x;
    }
    
    public int getY() {
        return y;
    }

    public double getAltitud(double x, double y) {
        if (x < this.x || x >= this.x +1 ) return 0; 
        if (y < this.y || y >= this.y +1 ) return 0; 
        
        double decx = (x - (int)x);
        double decy = (y - (int)y);
        int col = (int)Math.round(resolucionAltitud * decx);
        int row = (int)Math.round(resolucionAltitud * decy);
        return altitud[row][col].altitud;
    }

    PuntoAltitud getPuntoAltitud(double x, double y) {
        if (x < this.x || x >= this.x +1 ) return new PuntoAltitud(x, y); 
        if (y < this.y || y >= this.y +1 ) return new PuntoAltitud(x, y); 

        double decx = (x - (int)x);
        double decy = (y - (int)y);
        int col = (int)Math.round(resolucionAltitud * decx);
        int row = (int)Math.round(resolucionAltitud * decy);
        return altitud[row][col];
    }
}
