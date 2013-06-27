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
    int bajo_minimos;

    /*public double total(){
     double respuesta = 0;
     if(colisiones>0){
     respuesta = -colisiones;
     }
     else{
     respuesta = puntos;
     }
        
     return respuesta; 
     }
     * */
    /**
     * Devuelve true si esta evaluación es mejor que mejor_eval
     *
     * @param mejor_eval
     * @return
     */
    boolean es_mejor_que(Evaluacion mejor_eval) {
        boolean respuesta = false;
        //Miramos primero las colisiones
        if (this.colisiones < mejor_eval.colisiones) {
            respuesta = true;
        } else if (this.colisiones > mejor_eval.colisiones) {
            respuesta = false;
        } else {
            //Después los que están por debajo de la media en total
            if (this.bajo_minimos < mejor_eval.bajo_minimos) {
                respuesta = true;
            } else if (this.bajo_minimos > mejor_eval.bajo_minimos) {
                respuesta = false;
            } else {
                //Después cómo están repartidos
                if (this.puntos > mejor_eval.puntos) {
                    respuesta = true;
                } else {
                    respuesta = false;
                }
            }
        }
        return respuesta;
    }
}
