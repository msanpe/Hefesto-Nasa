package hefesto.Maps;

/**
 * @author Jose Vicente Lozano Copa
 */
public class Point {
    private double x;
    private double y;
    
    public Point(double x_, double y_){
        x = x_;
        y = y_;
    }
    
    public Point(){
        x = 0; y = 0;
    }
    public void setX(double d){x = d;}
    public void setY(double d){y = d;}
    public double X(){return x;}
    public double Y(){return y;}
}
