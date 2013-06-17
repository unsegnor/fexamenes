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
class OrdenarPosiciones implements Comparator<Posicion> {

    public OrdenarPosiciones() {
    }

    @Override
    public int compare(Posicion t, Posicion t1) {
        return Double.compare(t.valor, t1.valor);
    }
    
}
