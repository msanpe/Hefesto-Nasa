/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package hefesto.Maps;

/**
 *
 * @author Miguel
 */
class Pair {
    private int tiempo = 0;
    private PuntoAltitud punto;
    
    public Pair(int t, PuntoAltitud punto) {
        this.tiempo = t;
        this.punto = punto;
    }
    
    public int getTiempo() {
        return tiempo;
    }
    
    public PuntoAltitud getPunto() {
        return  punto;
    }
}
