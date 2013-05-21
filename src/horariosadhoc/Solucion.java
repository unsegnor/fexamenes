/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package horariosadhoc;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Random;

/**
 * Es una solución al problema
 *
 * @author Victor
 */
class Solucion {

    static Random r = new Random();

    public static Solucion copiar(Solucion s) {
        Solucion respuesta = new Solucion();

        respuesta.s = copiar(s.s);

        return respuesta;
    }

    
    /**
     * Esta función devuelve el valor de evaluación total y el de cada par de asignaturas que componen la solución.
     * También recibe los pares para consultarlos por si no han cambiado. Los que han cambiado se vuelven a calcular.
     * @param pares_calculados
     * @param s
     * @param afectados
     * @param tiempos
     * @return 
     */
    static double evaluar(ParAsig cambiado, HashMap<ParAsig,Double> pares_calculados, Solucion s, HashMap<ParAsig, Integer> afectados, int[] tiempos){
        
        
    return 2;
    }
    
    
    static double evaluar(Solucion s, HashMap<ParAsig, Integer> afectados, int[] tiempos) {
        double evaluacion = 0;
        boolean valido = true;

        Fragmento[] f = s.s;

        int l = f.length;

        for (int a = 0; valido && a < l; a++) {
            int tiempo_estudio = 0;
            for (int b = a + 1; valido && b < l; b++) {
                tiempo_estudio += tiempos[b - 1];
                //Si los dos espacios tienen asignatura
                Asignatura a1 = f[a].contenido;
                Asignatura a2 = f[b].contenido;
                if (a1 != null && a2 != null) {
                    //Si hay afectados entonces sumamos algo, sino nada
                    Integer nafectados = afectados.get(new ParAsig(a1, a2));
                    if (nafectados != null) {
                        //Sumamos a la evaluación para cada par de asignaturas de la solución
                        //la multiplicación del tiempo de estudio por el número de afectados
                        //de forma que cuanto más tiempo a más afectados mejor
                        if (tiempo_estudio > 0) {
                            evaluacion += Math.log(tiempo_estudio) * nafectados;
                        }

                        //No se permiten tiempos iguales a 0
                        /*if(tiempo_estudio==0){
                         valido = false;
                         }*/
                    }
                }
            }
        }

        //Si no es válido entonces evaluación = 0;
        if (!valido) {
            evaluacion = 0;
        }
        return evaluacion;

    }
    //Fragmentos de 12 horas
    Fragmento[] s;

    public Solucion() {
    }

    public Solucion(ArrayList<Asignatura> asignaturas, int espacios) {
        s = new Fragmento[espacios];

        for (int i = 0; i < asignaturas.size(); i++) {
            s[i].contenido = asignaturas.get(i);
        }
    }

    static public Fragmento[] nuevoFragmento(int espacios) {
        Fragmento[] f = new Fragmento[espacios];

        for (int i = 0; i < espacios; i++) {
            f[i] = new Fragmento();
        }

        return f;
    }

    static public Fragmento[] copiar(Fragmento[] v) {
        Fragmento[] respuesta = nuevoFragmento(v.length);

        for (int i = 0; i < v.length; i++) {
            respuesta[i].contenido = v[i].contenido;
            respuesta[i].prohibido = v[i].prohibido;
        }

        return respuesta;
    }

    static public Solucion Aleatoria(ArrayList<Asignatura> asignaturas, Fragmento[] fragmentos) {

        Fragmento[] f = copiar(fragmentos);


        Solucion respuesta = null;
        //Comprobar si es posible
        if (asignaturas.size() <= f.length) { //No estamos teniendo en cuenta los espacios prohibidos

            for (int i = 0; i < asignaturas.size(); i++) {
                //Obtener índice 
                int indice = r.nextInt(f.length);
                //Asegurar que la posición está vacía
                while (f[indice].prohibido || f[indice].contenido != null) {
                    indice = r.nextInt(f.length);
                }
                //Escribir la asignatura en el índice
                f[indice].contenido = asignaturas.get(i);
            }

            respuesta = new Solucion();
            respuesta.s = f;

        } else {
            System.err.println("No existe solución. Demasiadas asignaturas.");
        }
        return respuesta;
    }

    static public Solucion[] vecinos(Solucion s) {
        ArrayList<Solucion> soluciones = new ArrayList<Solucion>();

        int l = s.s.length;

        for (int a = 0; a < l; a++) {
            for (int b = a + 1; b < l; b++) {
                //Intercambiar
                Solucion s2 = intercambiar(s, a, b);
                //Si hay respuesta la añadimos al conjunto de vecinos
                if (s2 != null) {
                    soluciones.add(s2);
                }
            }
        }

        return soluciones.toArray(new Solucion[soluciones.size()]);
    }

    //Devuelve la solución que intercambia dos posiciones o null si no es posible
    static public Solucion intercambiar(Solucion s, int p1, int p2) {
        Solucion respuesta = null;
        if (!s.s[p1].prohibido && !s.s[p2].prohibido) {
            respuesta = copiar(s);
            respuesta.s[p1] = s.s[p2];
            respuesta.s[p2] = s.s[p1];
        }
        return respuesta;
    }

    static public Solucion Aleatoria(ArrayList<Asignatura> asignaturas, int espacios) {

        Fragmento[] f = nuevoFragmento(espacios);

        return Aleatoria(asignaturas, f);
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();

        for (Fragmento f : s) {
            if (f.prohibido) {
                sb.append("X");
            } else {
                if (f.contenido != null) {
                    sb.append(f.contenido);
                } else {
                    sb.append("-");
                }
            }
            sb.append("||");
        }

        return sb.toString();
    }
}
