
package hefesto;

import java.text.DecimalFormat;

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
    
}
