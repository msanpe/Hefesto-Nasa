
package hefesto.Maps;

import hefesto.Maps.PuntoAltitud.estado;
import hefesto.Utils;
import java.awt.image.BufferedImage;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;


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
    
    protected static final int numPredicciones = 60;
    
    protected static final int umbral = 50; // % de por encima pasa a fuego
    
    private List<PuntoAltitud> estadoActual;
    
    private List[] predicciones;
    
    /**
     * Constructor por defecto
     */
    public Mapa(){
        mapa = new Tile[0][];
        predicciones = new ArrayList[numPredicciones];
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
    
    public List<PuntoAltitud> getFuego(){
       if (estadoActual != null)    
           return estadoActual;
       else
           estadoActual = iniciaFuegoActual();
       return estadoActual;
    }
    
    public List<PuntoAltitud> iniciaFuegoActual() {           
        Random rand = new Random();
        double randCordX = rand.nextDouble()*columnas + mapa[0][0].getX();
        double randCordY = rand.nextDouble()*filas + mapa[0][0].getY();
        List<PuntoAltitud> list = new ArrayList<>();
        PuntoAltitud punto = mapa[Math.round(columnas/2)][Math.round(filas/2)].getPunto(5, 5);
        punto.estatus = estado.ardiendo;
        list.add(punto);
        
        return list;
    }
    
    public void tick() {
        iteraPrediccion();
        // actualizarMapaZonaAfectada
        // t0 = t1

    }
    
    public void iteraPrediccion(){
        List<PuntoAltitud> aux = new ArrayList<>();
        predicciones[0] = new ArrayList<>(estadoActual);

        for (int i = 1; i <= numPredicciones; i++) {
            for (int j = 0; i < predicciones[i].size(); i++) {
                predict((PuntoAltitud)predicciones[i - 1].get(j), aux);
            }
            predicciones[i] = aux;
            aux.clear();
        }
    }


    public double getAltitud(double x, double y){
        if (x < mapa[0][0].getX() || x > mapa[0][columnas-1].getX()+1) return 0;
        if (y < mapa[0][0].getY() || y > mapa[filas-1][0].getY()+1) return 0;
        double xini = mapa[0][0].getX();
        double yini = mapa[0][0].getY();
        double xfin = mapa[0][columnas-1].getX()+1;
        double yfin = mapa[filas-1][0].getY()+1;
        return mapa[(int)Math.round(y - yini)][(int) Math.round(x - xini) ].getAltitud(x, y);
    }
    
    public void predict(PuntoAltitud punto, List<PuntoAltitud> list) {
        double mialtitud = punto.altitud;
        double velViento = Viento.getF();
        double warriba = getAltitud(punto.x, punto.y-0.1) - mialtitud;
        double wabajo  = getAltitud(punto.x, punto.y+0.1) - mialtitud;
        double wderecha = getAltitud(punto.x+0.1, punto.y) - mialtitud;
        double wizquierda  = getAltitud(punto.x-0.1, punto.y) - mialtitud;
        double warrdere  = getAltitud(punto.x+0.1, punto.y-0.1) - mialtitud;
        double warrizq   = getAltitud(punto.x-0.1, punto.y-0.1) - mialtitud;
        double wabader   = getAltitud(punto.x+0.1, punto.y+0.1) - mialtitud;
        double wabizq    = getAltitud(punto.x-0.1, punto.y+0.1) - mialtitud;

        if (warriba > 0)    list.add(new PuntoAltitud(punto.x, punto.y-0.1));
        if (wabajo > 0)    list.add(new PuntoAltitud(punto.x, punto.y+0.1));
        if (wderecha > 0)    list.add(new PuntoAltitud(punto.x+0.1, punto.y));
        if (wizquierda > 0)    list.add(new PuntoAltitud(punto.x-0.1, punto.y));
        if (warrdere > 0)    list.add(new PuntoAltitud(punto.x+0.1, punto.y-0.1));
        if (warrizq > 0)    list.add(new PuntoAltitud(punto.x-0.1, punto.y-0.1));
        if (wabader > 0)    list.add(new PuntoAltitud(punto.x+0.1, punto.y+0.1));
        if (wabizq > 0)    list.add(new PuntoAltitud(punto.x-0.1, punto.y+0.1));

        
        double desY = Viento.getvy()/Viento.getvx() * Viento.getF();
        double desX = Viento.getvx()/Viento.getvy() * Viento.getF();
        if (Viento.getvx()* desX < 0)  desX = desX *-1;
        if (Viento.getvy()* desY < 0)  desY = desY *-1;
        
        
        int distancia = (int)Math.round(velViento + 1);
        
        for (double itX = punto.x - 0.1*distancia; itX < punto.x+0.1*distancia+0.1;itX+=0.1){
            for (double itY = punto.y - 0.1*distancia; itY < punto.y+0.1*distancia+0.1;itY+=0.1){
                double distanciaX = Utils.roundDouble(itX - punto.x, 1) *10;
                double distanciaY = Utils.roundDouble(itY - punto.y, 1) *10;

                //coincidencia de direccion con el vector de viencio
                if (desX * distanciaX >= 0 && desY * distanciaY >= 0){
                    if (Math.abs(desX+1) > Math.abs(distanciaX)  && Math.abs(desY+1) > Math.abs(distanciaY)){
                        list.add(new PuntoAltitud(itX, itY));
                    }
                }
            }
        }
    }
}
