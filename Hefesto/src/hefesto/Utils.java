
package hefesto;

import hefesto.Maps.Mapa;
import hefesto.Maps.PuntoAltitud;
import hefesto.Maps.PuntoAltitud.estado;
import java.text.DecimalFormat;
import java.util.List;

/**
 *
 * @author Jose Vicente Lozano Copa
 */
public class Utils {

public static double roundDouble(double valor, int decimales){
    String formato = "#.";
    for (int i = 0;i<decimales;i++)
         formato+="#";
    DecimalFormat df = new DecimalFormat(formato);    
    
    return Double.parseDouble(df.format(valor).replace(",", "."));
}
 
public static void addNotExists(List<PuntoAltitud> lista, PuntoAltitud obj, Mapa map){
        if (!lista.contains(obj)) 
            if (map.getPuntoAltitud(obj.x, obj.y).estatus != estado.quemado)
                lista.add(obj);
    }
}
