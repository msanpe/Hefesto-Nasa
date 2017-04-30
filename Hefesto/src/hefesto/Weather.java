package hefesto;

import com.google.gson.Gson;

/**
 * Created by pavel on 30/04/2017.
 */
public class Weather {
    private int wind_kph;
    private String relative_humidity;

    private int wind_degrees;

    private String wind_dir;
    public static Weather getGson() throws Exception {
        Gson gson = new Gson();
        Weather weather = gson.fromJson(Utils.readJSON(), Weather.class);
        return weather;
    }

    @Override
    public String toString() {
        return "Weather{" +
                "wind_kph=" + wind_kph +
                ", relative_humidity='" + relative_humidity + '\'' +
                ", wind_degrees=" + wind_degrees +
                ", wind_dir='" + wind_dir + '\'' +
                '}';
    }

    public int getWind_degrees() {
        return wind_degrees;
    }

    public String getWind_dir() {
        return wind_dir;
    }
}
