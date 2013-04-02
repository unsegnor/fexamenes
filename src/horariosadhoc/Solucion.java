/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package horariosadhoc;

import java.util.ArrayList;

/**
 * Es una solución al problema
 * @author Victor
 */
class Solucion {
    
    //Fragmentos de 12 horas
    Asignatura[] s;
    
    public Solucion(ArrayList<Asignatura> asignaturas, int espacios){
        s = new Asignatura[espacios];
        
        for(int i=0; i<asignaturas.size(); i++){
           s[i] = asignaturas.get(i);
        }
    }
    
}
