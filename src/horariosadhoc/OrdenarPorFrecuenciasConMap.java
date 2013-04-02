/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package horariosadhoc;

import java.util.Comparator;
import java.util.HashMap;

/**
 *
 * @author Victor
 */
class OrdenarPorFrecuenciasConMap implements Comparator<Asignatura> {
    private final HashMap<Asignatura, Integer> map;

    public OrdenarPorFrecuenciasConMap(HashMap<Asignatura, Integer> map) {
        this.map = map;
    }

    @Override
    public int compare(Asignatura t, Asignatura t1) {
        //Antes cuanto mayor sea la frecuencia que devuelve el map
        return (map.get(t1))-(map.get(t));
    }
    
}
