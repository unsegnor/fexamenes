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
    private static float penalizacion_bajo_media = 10;
    private static float penalizacion_sobre_media = 1;

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

    static Sol cambiar(Sol mejor, int a, int sum) {
        Sol respuesta = mejor.copia();
        
        //Avanzar uno en la posición de a
        
        //Seleccionar asignación del vector de la solución
        Asignacion seleccionada = respuesta.solucion.get(a);
        
        int maxvalor = respuesta.nhuecos;
        
        //Suma 1 al valor sin pasarse del máximo
        int valor = (seleccionada.numero+sum)%maxvalor;
        
        //Realizar la asignación
        seleccionada.numero = valor;
        
        return respuesta;
    }

    static HashMap<Integer, Integer> calcularHistograma(Sol solucion, HashMap<ParAsig, Integer> afectados, int[] huecos) {
        HashMap<Integer, Integer> respuesta = new HashMap<Integer, Integer>();
        
        boolean valido = true;

        int nasig = solucion.solucion.size();

        for (int a = 0; valido && a < nasig; a++) {
            int tiempo_estudio = 0;
            for (int b = a + 1; valido && b < nasig; b++) {
                
                //Si los dos espacios tienen asignatura
                Asignatura a1 = solucion.solucion.get(a).asignatura;
                Asignatura a2 = solucion.solucion.get(b).asignatura;
                
                //Calculamos el tiempo de estudio
                int hueco1 = solucion.solucion.get(a).numero;
                int hueco2 = solucion.solucion.get(b).numero;
                int t1 = huecos[hueco1];
                int t2 = huecos[hueco2];
                
                tiempo_estudio = Math.abs(t2-t1);
                
                
                if (a1 != null && a2 != null) {
                    //Si hay afectados entonces sumamos algo, sino nada
                    Integer nafectados = afectados.get(new ParAsig(a1, a2));
                    if (nafectados != null) {
                       //Añadimos datos al histograma
                       //Comprobar si existe la entrada (tiempo de estudio)
                       Integer actual = respuesta.get(tiempo_estudio);
                       
                       if(actual==null){
                           //Si no existe actual es 0, se crea cuando lo introduzcamos la primera vez
                           actual = 0;
                       }
                       
                       //Sumamos los afectados
                       actual+=nafectados;
                       
                       //Lo actualizamos
                       respuesta.put(tiempo_estudio, actual);
                    }
                }
            }
        }
        
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
    
    //En esta función damos mayor puntuación al que más se aproxima al tiempo de estudio deseado
    static Evaluacion evaluarComp(Sol mejor, HashMap<ParAsig, Integer> afectados, int[] huecos, float tdeseado) {
        
        Evaluacion eval = new Evaluacion();
        
        double puntos = 0;
        int colisiones = 0;
        int bajo_media = 0;
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
                
                //Comprobamos la diferencia entre el tiempo de estudio real y el deseado
                float difftestudio = tiempo_estudio - tdeseado;
                float absdiff = Math.abs(difftestudio);
                
                //TODO Criterios que debería seguir 
                //Primero que no haya colisiones
                //Segundo que haya la menor gente posible por debajo de la media
                //Tercero que la mayor gente posible tenga el mayor tiempo posible
                
                if (a1 != null && a2 != null) {
                    //Si hay afectados entonces sumamos algo, sino nada
                    Integer nafectados = afectados.get(new ParAsig(a1, a2));
                    if (nafectados != null) {
                        //Tenemos que intentar reducir la diferencia entre los tiempos así que lo restaremos
                        if (tiempo_estudio > 0) { // > 0
                            //Si estamos por debajo de la media penalizamos más que si estamos por encima
                            if(tiempo_estudio < tdeseado){
                            //Cuanto más abajo más penalizamos
                            //usaremos una función exponencial que comienza en el infinito para x=0 hasta 0 para x=tdeseado
                                //puntos -= (Math.pow(2, tdeseado/tiempo_estudio)-2) * nafectados;
                                puntos -= Math.pow(2, absdiff) * nafectados;
                            }else if (difftestudio > 0){
                                //puntos -= Math.log(absdiff) * nafectados;
                            }
                                
                        }

                        //No se permiten tiempos iguales a 0
                        /*if(tiempo_estudio==0){
                         valido = false;
                         }*/
                        if(tiempo_estudio == 0){ // == 0
                            colisiones+=nafectados;
                        }
                        
                        if(tiempo_estudio < tdeseado){
                            //bajo_media += nafectados;
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
        eval.bajo_minimos = bajo_media;
        eval.colisiones = colisiones;
        
        return eval;
    }
    
    
    //En esta función damos mayor puntuación al que más se aproxima al tiempo de estudio deseado
    static Evaluacion evaluarComp(Sol mejor, HashMap<ParAsig, Integer> afectados, int[] huecos, DatosDelProblema dp) {
        
        Evaluacion eval = new Evaluacion();
        
        double puntos = 0;
        int colisiones = 0;
        int bajo_minimos = 0;
        boolean valido = true;
        float tdeseado = dp.tdeseado;
        float tmin = 12;//dp.tmin;

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
                
                //Comprobamos la diferencia entre el tiempo de estudio real y el deseado
                float difftestudio = tiempo_estudio - tdeseado;
                float absdiff = Math.abs(difftestudio);
                
                //TODO Criterios que debería seguir 
                //Primero que no haya colisiones
                //Segundo que haya la menor gente posible por debajo de la media
                //Tercero que la mayor gente posible tenga el mayor tiempo posible
                
                if (a1 != null && a2 != null) {
                    //Si hay afectados entonces sumamos algo, sino nada
                    Integer nafectados = afectados.get(new ParAsig(a1, a2));
                    if (nafectados != null) {
                        //Tenemos que intentar reducir la diferencia entre los tiempos así que lo restaremos
                        if (tiempo_estudio > 0) { // > 0
                            //Si estamos por debajo de la media penalizamos más que si estamos por encima
                            if(tiempo_estudio < tdeseado){
                            //Cuanto más abajo más penalizamos
                            //usaremos una función exponencial que comienza en el infinito para x=0 hasta 0 para x=tdeseado
                                //puntos -= (Math.pow(2, tdeseado/tiempo_estudio)-2) * nafectados;
                                puntos -= Math.pow(2, absdiff) * nafectados;
                            }else if (difftestudio > 0){
                                //puntos -= Math.log(absdiff) * nafectados;
                            }
                                
                        }

                        //No se permiten tiempos iguales a 0
                        /*if(tiempo_estudio==0){
                         valido = false;
                         }*/
                        if(tiempo_estudio == 0){ // == 0
                            colisiones+=nafectados;
                        }
                        
                        if(tiempo_estudio < tmin){
                            bajo_minimos += nafectados;
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
        eval.bajo_minimos = bajo_minimos;
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
            
            sb.append(a).append(",");
        }
        
        sb.append("]");
        
        return sb.toString();
    }

    String bonita() {
        StringBuilder sb = new StringBuilder();
        
        
        
        return sb.toString();
    }
    
}
