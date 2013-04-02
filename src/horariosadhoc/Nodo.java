/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package horariosadhoc;

import java.util.ArrayList;

/**
 *
 * @author Victor
 */
class Nodo<T> {
    
    T objeto;
    
    int contador;
    
    Nodo padre;
    
    Nodo siguiente;
    
    //Hijos
    ArrayList<Nodo> hijos = new ArrayList<Nodo>();
    
}
