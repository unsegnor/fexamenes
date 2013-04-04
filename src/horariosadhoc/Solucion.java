/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package horariosadhoc;

import java.util.ArrayList;
import java.util.Random;

/**
 * Es una solución al problema
 *
 * @author Victor
 */
class Solucion {

    static Random r = new Random();

    private static Solucion copiar(Solucion s) {
        Solucion respuesta = new Solucion();

        respuesta.s = copiar(s.s);

        return respuesta;
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

        for (int i=0; i<espacios; i++) {
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

        Solucion respuesta = null;
        //Comprobar si es posible
        if (asignaturas.size() <= fragmentos.length) { //No estamos teniendo en cuenta los espacios prohibidos

            for (int i = 0; i < asignaturas.size(); i++) {
                //Obtener índice 
                int indice = r.nextInt(fragmentos.length);
                //Asegurar que la posición está vacía
                while (fragmentos[indice].prohibido || fragmentos[indice].contenido != null) {
                    indice = r.nextInt(fragmentos.length);
                }
                //Escribir la asignatura en el índice
                fragmentos[indice].contenido = asignaturas.get(i);
            }

            respuesta = new Solucion();
            respuesta.s = fragmentos;

        }else{
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
