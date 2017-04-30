
package hefesto.Maps;

import com.google.gson.annotations.SerializedName;

/**
 * @author Jose Vicente Lozano Copa
 */
public class FirePointForJSON {
    @SerializedName("longitud")
    public double longitud;
    
    @SerializedName("latitud")
    public double latitud;
    
    @SerializedName("minutos")
    public int minutos;
    
    public FirePointForJSON(double lon, double lat, int min_){
        longitud = lon;
        latitud = lat;
        minutos = min_;
    }
}
