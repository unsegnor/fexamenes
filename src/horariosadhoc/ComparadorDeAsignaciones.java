/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package horariosadhoc;

import java.util.Comparator;

/**
 *
 * @author Victor
 */
class ComparadorDeAsignaciones implements Comparator<Asignacion> {

    @Override
    public int compare(Asignacion t, Asignacion t1) {
        return t.numero - t1.numero;
    }
    
}
