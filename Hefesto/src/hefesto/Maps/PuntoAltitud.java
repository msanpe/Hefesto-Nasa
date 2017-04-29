
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

    @Override
    public int hashCode() {
        int hash = 3;
        hash = 59 * hash + (int) (Double.doubleToLongBits(this.x) ^ (Double.doubleToLongBits(this.x) >>> 32));
        hash = 59 * hash + (int) (Double.doubleToLongBits(this.y) ^ (Double.doubleToLongBits(this.y) >>> 32));
        return hash;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final PuntoAltitud other = (PuntoAltitud) obj;
        if (Double.doubleToLongBits(this.x) != Double.doubleToLongBits(other.x)) {
            return false;
        }
        if (Double.doubleToLongBits(this.y) != Double.doubleToLongBits(other.y)) {
            return false;
        }
        return true;
    }
    

    
}
