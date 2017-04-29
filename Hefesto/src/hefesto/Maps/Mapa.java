
package hefesto.Maps;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;

/**
 *
 * @author user
 */
public class Mapa {

    protected Tile[][] mapa;
    
    private int filas = 0;
    
    private int columnas = 0;

    private int mapZoom = 19;
    
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
    
    public Tile getCoordinate(int row, int col){
        if (row < 0 || row >= filas)    return null;
        if (col < 0 || col >= columnas) return null;
        return mapa[row][col];
    }
    
    public void refreshFromGoogle(){
        googleMapsCoonector.getElevation(this);
    }

    public double getAltitud(int row, int col){
        if (row < 0 || row >= filas)    return 0;
        if (col < 0 || col >= columnas) return 0;
        return mapa[row][col].getAltitud(5, 5);
    }
    public byte[] getImagen(int row, int col){
        if (row < 0 || row >= filas)    return null;
        if (col < 0 || col >= columnas) return null;
        return mapa[row][col].getImagen();
    }
    
    public double getLongitud(int row, int col){
        if (row < 0 || row >= filas)    return 0;
        if (col < 0 || col >= columnas) return 0;
        return mapa[row][col].center().Longitud();
    }

    public double getLatitud(int row, int col){
        if (row < 0 || row >= filas)    return 0;
        if (col < 0 || col >= columnas) return 0;
        return mapa[row][col].center().Latitud();
    }
    
    public int getFilas(){return filas;}
    
    public int getColumnas(){return columnas;}
    
    public void print(){
        for (int i=0;i<filas;i++){
            System.out.println("Fila");
            for (int j=0;j<columnas;j++){
                System.out.println(mapa[i][j].toString());
            }
        }
    }
}
