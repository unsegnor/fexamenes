/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package horariosadhoc;

/**
 *
 * @author Victor
 */
class Evaluacion {
    double puntos = 0;
    int colisiones = 0;
    
    public double total(){
        double respuesta = 0;
        if(colisiones>0){
            respuesta = -colisiones;
        }
        else{
            respuesta = puntos;
        }
        
          return respuesta; 
    }
}
