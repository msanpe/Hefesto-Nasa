
package hefesto.Maps;

/**
 *
 * @author Jose Vicente Lozano Copa
 */
public class PuntoAltitud {
    public enum estado {
        normal, ardiendo, quemado, ignifugo
    }
    
    public estado estatus = estado.normal;  //0 normal, 1 fuego, 2 quemado, 3 no quemable 
    
    public static double metros = 3;
    
    public double x;
    
    public double y;
    
    public double altitud;
    
    public double porcentajeQuemado = 0;
    
    public int tiempo = 0;
    
    public PuntoAltitud(double x_,double y_){
        x= x_;
        y = y_;
    }
    
    
}
