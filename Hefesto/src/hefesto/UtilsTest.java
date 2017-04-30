package hefesto;

import org.junit.Test;

import static org.junit.Assert.*;

public class UtilsTest {
    @Test
    public void readJSON() throws Exception {
        Weather w = Weather.getGson();
        System.out.println(w);
    }

}