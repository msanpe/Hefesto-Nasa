
package hefesto.Maps;

/**
 * @author Jose Vicente Lozano Copa
 */
public class Viento {
    
    //Velocidad del viento en metros/s
    
    private static double vx = -0.5;
    
    private static double vy = 0.8;
    
    public static double getvx(){
        return vx;
    }
    
    public static double getvy(){
        return vy;
    }
    
    public static double getF(){
        return Math.sqrt(vx*vx + vy*vy);
    }
    
}
