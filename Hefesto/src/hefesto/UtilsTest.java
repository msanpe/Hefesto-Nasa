package hefesto;



public class UtilsTest {
   
    public void readJSON() throws Exception {
        Weather w = Weather.getGson();
        System.out.println(w);
    }

}