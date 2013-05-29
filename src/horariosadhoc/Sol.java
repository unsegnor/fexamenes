/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package horariosadhoc;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Random;

/**
 *
 * @author Victor
 */
class Sol {

    static Sol Mutar(Sol mejor) {
        Sol respuesta = mejor.copia();
        
        //Alterar una asignación al azar de forma aleatoria
        
        //Seleccionar asignación del vector de la solución
        Asignacion seleccionada = respuesta.solucion.get(r.nextInt(respuesta.solucion.size()));
        
        //Seleccionar un valor entre los posibles para escribir
        int valor = r.nextInt(respuesta.nhuecos);
        
        //Realizar la asignación
        seleccionada.numero = valor;
        
        return respuesta;
    }

    static Sol Mutar2(Sol mejor) {
        Sol respuesta = mejor.copia();
        
        //Alterar una asignación al azar avanzando una posición
        
        //Seleccionar asignación del vector de la solución
        Asignacion seleccionada = respuesta.solucion.get(r.nextInt(respuesta.solucion.size()));
        
        int maxvalor = respuesta.nhuecos;
        
        //Suma 1 al valor sin pasarse del máximo
        int valor = (seleccionada.numero+1)%maxvalor;
        
        //Realizar la asignación
        seleccionada.numero = valor;
        
        return respuesta;
    }

    static Sol cambiar(Sol mejor, int a) {
        Sol respuesta = mejor.copia();
        
        //Avanzar uno en la posición de a
        
        //Seleccionar asignación del vector de la solución
        Asignacion seleccionada = respuesta.solucion.get(a);
        
        int maxvalor = respuesta.nhuecos;
        
        //Suma 1 al valor sin pasarse del máximo
        int valor = (seleccionada.numero+1)%maxvalor;
        
        //Realizar la asignación
        seleccionada.numero = valor;
        
        return respuesta;
    }
    
        //Representación de una solución
    ArrayList<Asignacion> solucion = new ArrayList<Asignacion>();
    
    //Número de huecos permitidos
    int nhuecos;
    
    //Los huecos contienen el instante en el que comienzan
    static Evaluacion evaluarComp(Sol mejor, HashMap<ParAsig, Integer> afectados, int[] huecos) {
        
        Evaluacion eval = new Evaluacion();
        
        double puntos = 0;
        int colisiones = 0;
        boolean valido = true;

        int nasig = mejor.solucion.size();

        for (int a = 0; valido && a < nasig; a++) {
            int tiempo_estudio = 0;
            for (int b = a + 1; valido && b < nasig; b++) {
                
                //Si los dos espacios tienen asignatura
                Asignatura a1 = mejor.solucion.get(a).asignatura;
                Asignatura a2 = mejor.solucion.get(b).asignatura;
                
                //Calculamos el tiempo de estudio
                int hueco1 = mejor.solucion.get(a).numero;
                int hueco2 = mejor.solucion.get(b).numero;
                int t1 = huecos[hueco1];
                int t2 = huecos[hueco2];
                
                tiempo_estudio = Math.abs(t2-t1);
                
                
                if (a1 != null && a2 != null) {
                    //Si hay afectados entonces sumamos algo, sino nada
                    Integer nafectados = afectados.get(new ParAsig(a1, a2));
                    if (nafectados != null) {
                        //Sumamos a la evaluación para cada par de asignaturas de la solución
                        //la multiplicación del tiempo de estudio por el número de afectados
                        //de forma que cuanto más tiempo a más afectados mejor
                        if (tiempo_estudio > 0) {
                            puntos += Math.log(tiempo_estudio) * nafectados;
                        }

                        //No se permiten tiempos iguales a 0
                        /*if(tiempo_estudio==0){
                         valido = false;
                         }*/
                        if(tiempo_estudio==0){
                            colisiones+=nafectados;
                        }
                    }
                }
            }
        }

        //Si no es válido entonces evaluación = 0;
        if (!valido) {
            puntos = 0;
        }
        
        //Rellenamos la evaluación
        eval.puntos= puntos;
        eval.colisiones = colisiones;
        
        return eval;
    }


    
    static Random r = new Random();
    
    //Devolver solución aleatoria, se reparten las asignaturas entre los huecos de forma aleatoria
    static Sol Aleatoria(ArrayList<Asignatura> asignaturas, int nhuecos) {
        Sol respuesta = new Sol();
        
        respuesta.nhuecos = nhuecos;
        
        for(Asignatura a : asignaturas){
            //Asignar a cada asignatura un número entre 0 y nhuecos-1
            Asignacion asignacion = new Asignacion();
            asignacion.asignatura = a;
            asignacion.numero = r.nextInt(nhuecos);
            
            respuesta.solucion.add(asignacion);
        }
        
        return respuesta;
    }

    public Sol copia() {
        Sol respuesta = new Sol();
        
        for(Asignacion a : this.solucion){
            Asignacion b = new Asignacion();
            b.asignatura = a.asignatura;
            b.numero = a.numero;
            
            //Añadir la copia de la asignación
            respuesta.solucion.add(b);
        }
        
        respuesta.nhuecos = this.nhuecos;
        
        return respuesta;
    }
    
    public String toString(){
        StringBuilder sb = new StringBuilder();
        
        sb.append("[");
        for(Asignacion a : solucion){
            
            sb.append("(").append(a.asignatura).append(", ").append(a.numero).append(")").append(",");
        }
        
        sb.append("]");
        
        return sb.toString();
    }
    
}
