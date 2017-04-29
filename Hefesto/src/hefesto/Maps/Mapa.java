
package hefesto.Maps;

import java.awt.image.BufferedImage;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;

/**
 *
 * @author user
 */
public class Mapa {

    //Matriz de porciones del mapa
    protected Tile[][] mapa;
    
    //Numero de filas
    private int filas = 0;
    
    //Numero de columnas
    private int columnas = 0;

    //Zoom del mapa
    private int mapZoom = 19;
    
    /**
     * Constructor por defecto
     */
    public Mapa(){
        mapa = new Tile[0][];
    }
    
    
    public Mapa(int fil, int col){
        filas = fil; columnas = col; ;
        mapa = new Tile[filas][];
        for (int i=0;i<filas;i++){
            mapa[i] = new Tile[columnas];
            for (int j=0;j<columnas;j++){
                mapa[i][j] = new Tile( 0, 0, 0, 10);
            }
        }
    }
    
    /**
     * Contructor del mapa, genera un mapa del rectangulo entre dos puntos
     * @param latini    Latitud del punto inicial
     * @param lonini    Longitud del punto inicial
     * @param latfin    Latitud del punto final
     * @param lonfin    Longitud del punto final
     * @param zoom      Nivel de zoom (1 toda la tierra, 20 zoom maximo)
     */
    public Mapa(double latini, double lonini, double latfin, double lonfin, int zoom){
        mapZoom = zoom;
        Point pini = Tile.calcTileFromLonLat(new RealPoint(latini, lonini), zoom);
        Point pfin = Tile.calcTileFromLonLat(new RealPoint(latfin, lonfin), zoom);
        
        Double f =   pfin.Y() - pini.Y();
        Double c =   pfin.X() - pini.X();
        
        if (c<0)c=c*-1;
        if (f<0)f=f*-1;
        filas = f.intValue()+1;
        columnas = c.intValue()+1;
        mapa = new Tile[filas][];

        for (int i=0;i<filas;i++){
            mapa[i] = new Tile[columnas];
            for (int j=0;j<columnas;j++){
                mapa[i][j] = new Tile( ((Double) pini.X()).intValue()+j,  ((Double) pini.Y()).intValue()+i, zoom, 10);
            }
        }
        
        System.out.println("Creado mapa de "+filas+" filas y "+columnas+" columnas");
    }

    /**
     * Guarda el mapa formateado a un fichero de texto
     * @param f Nombre del fichero
     * @return true si se guardo correctamente
     */
    public boolean saveToFile(String f){
        try{
            BufferedWriter writer = new BufferedWriter(new FileWriter(f));
            writer.write(""+filas + "\n");
            writer.write(""+columnas + "\n");
            writer.write(""+mapZoom + "\n");
            
            for (int i=0;i<filas;i++)
                for (int j=0;j<columnas;j++)
                    mapa[i][j].toBufferedWriter(writer);
            
            writer.close();
            
        }catch(Exception e){
            return false;
        }
        
        return true;
    }
    
    /**
     * Retorna el mapa cargado del fichero pasado por parametro
     * @param f Nombre de fichero
     * @return Mapa contenido en el fichero, si no fuese correcto se retorna un mapa vacio
     */
    public static Mapa loadFromFile(String f){
        try{
            BufferedReader reader = new BufferedReader(new FileReader(f));
            
            int fil = Integer.parseInt(reader.readLine());
            int col = Integer.parseInt(reader.readLine());
            int zoom = Integer.parseInt(reader.readLine());
            Mapa map = new Mapa(fil, col);
            
            for (int i=0;i<fil;i++)
                for (int j=0;j<col;j++){
                    map.mapa[i][j] = Tile.fromBufferedReader(reader, zoom);
                }
            reader.close();
            return map;
        }catch(Exception e){
            System.out.println("Error: "+ e.toString());
            return new Mapa();
        }
    }
    
    /**
     * Retorna la porcion del mapa correspondiente a la fila columna indicada
     * @param row   Fila
     * @param col   Columna
     * @return  Porcion del mapa
     */
    public Tile getCoordinate(int row, int col){
        if (row < 0 || row >= filas)    return null;
        if (col < 0 || col >= columnas) return null;
        return mapa[row][col];
    }
    
    /**
     * El mapa actualiza la informacion de altitud desde los servidores de google
     */
    public void refreshFromGoogle(){
        googleMapsCoonector.getElevation(this);
    }

   /* public double getAltitud(int row, int col){
        if (row < 0 || row >= filas)    return 0;
        if (col < 0 || col >= columnas) return 0;
        return mapa[row][col].getAltitud(5, 5);
    }*/
    
    public BufferedImage getImagen(int row, int col){
        if (row < 0 || row >= filas)    return null;
        if (col < 0 || col >= columnas) return null;
        return mapa[row][col].getImagen();
    }
    
    /**
     * Obtiene la Longitud geografica del punto central de la cuadricula i,j indicada
     * @param row   numero de Fila
     * @param col   Numero de columna
     * @return  Longitud
     */
    public double getLongitud(int row, int col){
        if (row < 0 || row >= filas)    return 0;
        if (col < 0 || col >= columnas) return 0;
        return mapa[row][col].center().Longitud();
    }

    /**
     * Obtiene la Laitud geografica del punto central de la cuadricula i,j indicada
     * @param row   numero de Fila
     * @param col   Numero de columna
     * @return  Latitud
     */
    
    public double getLatitud(int row, int col){
        if (row < 0 || row >= filas)    return 0;
        if (col < 0 || col >= columnas) return 0;
        return mapa[row][col].center().Latitud();
    }
    
    /**
     * @return Retorna el numero de filas de la matriz
     */
    public int getFilas(){return filas;}
    
    /**
     * @return Retorna el numero de columnas de la matriz
     */
    public int getColumnas(){return columnas;}
}
