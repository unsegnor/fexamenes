/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package horariosadhoc;

/**
 *
 * @author Victor
 */
class Posicion<T> {
    
    T cosa;
    
    double valor = 0;
    
    public Posicion(T cosa, double valor){
        this.cosa = cosa;
        this.valor = valor;
    }
    
}
