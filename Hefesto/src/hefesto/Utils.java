
package hefesto;

import hefesto.Maps.Mapa;
import hefesto.Maps.PuntoAltitud;
import hefesto.Maps.PuntoAltitud.estado;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.URL;
import java.text.DecimalFormat;
import java.util.List;
import java.util.Random;

/**
 * @author Jose Vicente Lozano Copa
 */
public class Utils {

    public static double roundDouble(double valor, int decimales) {
        String formato = "#.";
        for (int i = 0; i < decimales; i++)
            formato += "#";
        DecimalFormat df = new DecimalFormat(formato);

        return Double.parseDouble(df.format(valor).replace(",", "."));
    }

    public static void addNotExists(List<PuntoAltitud> lista, PuntoAltitud obj, Mapa map) {
        if (!lista.contains(obj))
            if (map.getPuntoAltitud(obj.x, obj.y).estatus != estado.quemado) {
                Random r = new Random();
                if (r.nextDouble() <= 0.5)
                    lista.add(obj);

            }
    }

    public static String readJSON() throws Exception {
        String json = readUrl("http://192.168.170.183:3000/weather/39.452076/-03.43173");
        return json;
    }

    private static String readUrl(String urlString) throws Exception {
        BufferedReader reader = null;
        try {
            URL url = new URL(urlString);
            reader = new BufferedReader(new InputStreamReader(url.openStream()));
            StringBuffer buffer = new StringBuffer();
            int read;
            char[] chars = new char[1024];
            while ((read = reader.read(chars)) != -1)
                buffer.append(chars, 0, read);

            return buffer.toString();
        } finally {
            if (reader != null)
                reader.close();
        }
    }
}
