package hefesto.Maps;

/**
 *
 * @author Jose Vicente Lozano Copa
 */
public class RealPoint {
    
    
    private double longitud = 0;
    
    private double latitud = 0;
    
    public double Longitud(){return longitud;}
    
    public double Latitud(){return latitud;}

    public RealPoint(){}
    
    public RealPoint(double lat, double lon){
        longitud = lon;
        latitud = lat;
    }
    
    public void setLongitud(double d){longitud = d;}
    
    public void setLatitud(double d){latitud = d;}
    
    
    
}
