/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package horariosadhoc;

/**
 *
 * @author Victor
 */
class Registro<T> {
    
    T objeto = null;
    
    int contador = 0;
    
    Nodo primero = null;
    
    Nodo ultimo = null;

    /*
     * Añade un nodo a la lista enlazada
     */
    void addNodo(Nodo nodo) {
        //Si no hay nodo añadimos el primero
        if(primero == null){
            primero = nodo;
            ultimo = nodo;
        }else{
            //Si ya había 
            
            //Enlazamos el último con el que nos pasan 
            ultimo.siguiente = nodo;
            
            //y actualizamos el último
            ultimo = nodo;
        }
    }
    
}
