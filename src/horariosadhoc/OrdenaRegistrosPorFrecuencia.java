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
class OrdenaRegistrosPorFrecuencia implements Comparator<Registro> {

    public OrdenaRegistrosPorFrecuencia() {
    }

    @Override
    public int compare(Registro t, Registro t1) {
        //Primero los más frecuentes
        return t1.contador - t.contador;
    }
    
}
