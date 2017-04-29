
package hefesto.Maps;
import java.awt.Image;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.ByteArrayOutputStream;
import java.io.DataInputStream;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.Queue;

import org.json.simple.JSONArray;
import org.json.simple.parser.JSONParser;
import org.json.simple.JSONObject;
import org.json.simple.parser.ParseException;


/**
 *
 * @author Jose Vicente Lozano Copa
 */
public class googleMapsCoonector {

    public static int maxPeticionesPorSolicitud = 256;

    private static int zoom = 19;
    
    public static final String apiKey = "AIzaSyDO4hof2_8NE1DLYv_FwBcn16QCOOj5bP8";

    public static final String staticMapsKey = "AIzaSyCCQq02KcCDgflg9CCihV4rjdTG_Mkjstk";

    
    Queue<String> keys;
    
    
    //public static final String elevationKey1 = "AIzaSyCFMWdxs3sWgufu6iBPY2DOl7QLuanheK8";
    
    public static final String[] elkey ={
        "AIzaSyCfVddvps7qLfAUGIyCdmq1T_XHIRm57TQ", 
        "AIzaSyBRLcPynM6GVR_5--DDqr1ZEd3UuUFFrJ0",
        "AIzaSyCEEpct0NB303mS5vaoVV7Az7AHuL1WvIg",
        "AIzaSyCsx-b8O5a_sSX7_nFHnsKZLaItHNDYd4E",
        "AIzaSyABi0b8FMYn-FMvcG2ML_fIO-8K7Z6xFgM",
        "AIzaSyAG32N3I3fpw23YSfGTvik3-BLKpuA3-iU",
        "AIzaSyCkTgLSeFnl5JN091CwdirpCcTosg3YQB4",
        "AIzaSyDNtPs6tpJDTgALOuvPHAvksyNSBgGFous"
    }; 
    
  

    

    
    public static void getElevation(Mapa map){
        for (int i=0;i<map.getFilas();i++)
            for (int j=0;j<map.getColumnas();j++){
                
                System.out.println( "" + (i*map.getColumnas() + j) + " de " + (map.getFilas()*map.getColumnas())  );

                getElevation(map.getCoordinate(i, j), (i*map.getColumnas()+j) % elkey.length  );
            }
    }

    public static void getElevation(Tile tile, int key){
        String url = "https://maps.googleapis.com/maps/api/elevation/json?locations=";
        for (int i=0;i<tile.getResolucionAltitud();i++)
            for (int j=0;j<tile.getResolucionAltitud();j++){
                RealPoint rp = tile.getRealPoint(i, j);
                url+= formatCoordinate(rp.Latitud()) + "," + formatCoordinate(rp.Longitud());
                if (i < tile.getResolucionAltitud() -1  ||  j < tile.getResolucionAltitud() -1 )    url+="|";
            }
        url += "&key="+elkey[key];
        //System.out.println(elkey[key]);

        try{
            JSONObject obj = getJSON(url);
            if (   ((String)obj.get("status")).equals("OK")){
                JSONArray v = (JSONArray)obj.get("results");
                System.out.println("Resultados: " + v.size());
                for (int i=0;i<v.size();i++){
                    JSONObject e = (JSONObject)v.get(i);
                    if (e.get("elevation").getClass() == Long.class){
                     tile.getPunto( Math.round(i/tile.getResolucionAltitud()) , Math.round(i%tile.getResolucionAltitud()) ).altitud = Double.valueOf(new String(""+e.get("elevation"))) ;
                    }else{
                     tile.getPunto( Math.round(i/tile.getResolucionAltitud()) , Math.round(i%tile.getResolucionAltitud()) ).altitud = (double)e.get("elevation") ;
                    }
                }
            }
        }catch(Exception e){}
        
    }
    
    
    
   /* public static void getElevation(Tile[] puntos){
        String url = "https://maps.googleapis.com/maps/api/elevation/json?locations=";
        for (int i=0;i<puntos.length && i<maxPeticionesPorSolicitud;i++){
                url+= formatCoordinate(puntos[i].center().Latitud())+","+formatCoordinate(puntos[i].center().Longitud());
                if (i < puntos.length -1 && i < maxPeticionesPorSolicitud-1 )    url+="|";
        }
        url += "&key="+elevationKey1;
        try{
            JSONObject obj = getJSON(url);
            if (   ((String)obj.get("status")).equals("OK")){
                JSONArray v = (JSONArray)obj.get("results");
                System.out.println("Resultados: " + v.size());
                for (int i=0;i<v.size();i++){
                    JSONObject e = (JSONObject)v.get(i);
                    if (e.get("elevation").getClass() == Long.class){
                       // puntos[i].setAltitud(  Double.valueOf(new String(""+e.get("elevation")))   );
                    }else{
                       // puntos[i].setAltitud((double)e.get("elevation"));
                    }
                }
            }

        }catch(Exception e){
        System.out.println("Error: "+e.toString());}
    }*/
    
    public static double getElevation(double latitud, double longitud){
        String url = "https://maps.googleapis.com/maps/api/elevation/json?locations=";
        url+= formatCoordinate(latitud) + "," + formatCoordinate(longitud);
        url += "&key="+elkey[0];
        try{
            JSONObject obj = getJSON(url);
            if (   ((String)obj.get("status")).equals("OK")){
                JSONArray v = (JSONArray)obj.get("results");
                for (int i=0;i<v.size();i++){
                    JSONObject e = (JSONObject)v.get(i);
                    return (double)e.get("elevation");
                }
            }
            return 0;
        }catch(Exception e){
            System.out.println("Error: "+e.toString());
            return 0;
        }
    }
    
    private static String formatCoordinate(double d){
        NumberFormat formatter = new DecimalFormat("#0.0000000");     
        String salida = formatter.format(d);
        salida = salida.replace(",", ".");
        return salida;
                
    }

    public static JSONObject getTestJSON(String ur_) throws MalformedURLException, IOException, ParseException {
      System.out.println("JSON de pruebas");
      JSONObject js;
      BufferedReader reader = new BufferedReader(new FileReader("salida.json"));
      String line;
      String salida = "";
      while ((line = reader.readLine()) != null) {
         salida+=line;
      }
      reader.close();
      JSONParser parser = new JSONParser();
      js = (JSONObject) parser.parse(salida);
      return js;
        
    }
    
   public static JSONObject getJSON(String ur_) throws MalformedURLException, IOException, ParseException {
      String salida = "";
      JSONObject js;
      URL url = new URL(ur_);
      BufferedReader reader = new BufferedReader(new InputStreamReader(url.openStream()));
      BufferedWriter writer = new BufferedWriter(new FileWriter("salida.json"));
      String line;
      while ((line = reader.readLine()) != null) {
         salida+=line;
         writer.write(line);
      }
      reader.close();
      writer.close();
      JSONParser parser = new JSONParser();
      js = (JSONObject) parser.parse(salida);
      return js;
   }
}
