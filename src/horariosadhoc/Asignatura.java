/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package horariosadhoc;

import java.util.HashMap;

/**
 * Identifica a una asignatura
 * @author Victor
 */
class Asignatura {
    
    public static HashMap<String, Asignatura> existentes = new HashMap<String, Asignatura>();
    
    String ID;
    String texto;
    int cuatrimestre;
    
    public int matriculados = 0;
    
    @Override
    public String toString(){
        return "(" + ID + ") " + texto; 
    }
    
    public void addMatriculado(){
        matriculados++;
    }
    
}
