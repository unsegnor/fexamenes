/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package horariosadhoc;

import static horariosadhoc.Solucion.intercambiar;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map.Entry;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Victor
 */
public class Utiles {

    /*
     * Guardar archivo con el nombre "nombre" y contenido "contenido"
     */
    public static void guardarArchivo(String nombre, String contenido) {
        BufferedWriter bw = null;
        try {
            File archivo = new File(nombre);
            bw = new BufferedWriter(new FileWriter(archivo));

            bw.write(contenido);

            System.out.println("Generado " + nombre);

        } catch (IOException ex) {
            Logger.getLogger(Utiles.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            try {
                bw.close();
            } catch (IOException ex) {
                Logger.getLogger(Utiles.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }

    public static FrequenceGraph montarFG(DatosMatriculas dm) {


        HashMap<Asignatura, Integer> frecuencias = new HashMap<Asignatura, Integer>();

        //Recorrer datos y anotar frecuencias
        for (Matriculacion m : dm.matriculaciones) {
            for (Asignatura a : m.asignaturas) {
                //Aumentamos la frecuencia de la asignatura
                Integer actual = frecuencias.get(a);
                if (actual == null) {
                    actual = new Integer(0);
                }
                actual++;
                frecuencias.put(a, actual);
            }
        }

        //Pasar frecuencias a vector
        ArrayList<Registro<Asignatura>> vector = new ArrayList<Registro<Asignatura>>();
        //Indexamos los registros por asignatura
        HashMap<Asignatura, Registro<Asignatura>> i_registros = new HashMap<Asignatura, Registro<Asignatura>>();

        for (Entry<Asignatura, Integer> e : frecuencias.entrySet()) {
            Registro<Asignatura> r = new Registro<Asignatura>();
            r.objeto = e.getKey();
            r.contador = e.getValue();
            vector.add(r);

            i_registros.put(r.objeto, r); //Añadimos al índice
        }

        //Ordenar asignaturas de mayor a menor frecuencia
        //Este sí es necesario ordenarlo porque después se utilizará
        Collections.sort(vector, new OrdenaRegistrosPorFrecuencia());

        //Obtener vector ranking //AÚN NO SÉ SI ES NECESARIO
        ArrayList<Asignatura> ranking = new ArrayList<Asignatura>();
        for (Registro<Asignatura> r : vector) {
            ranking.add(r.objeto);
        }

        //Imprimir el árbol a ver si está bien ordenado
        for (Registro r : vector) {
            System.out.println(r.contador);
        }

        //Volver a recorrer datos y construir árbol

        //Creamos el nodo padre
        Nodo padre = new Nodo();

        for (Matriculacion m : dm.matriculaciones) {
            //Tratar en orden según el vector de frecuencias ya ordenado
            //Las asignaturas se almacenan como referencias a posiciones de memoria así que podemos utilizar el igual ==

            //Ordenar según las frecuencias obtenidas
            Collections.sort(m.asignaturas, new OrdenarPorFrecuenciasConMap(frecuencias));

            Nodo actual = padre;

            //Para cada asignatura de la matriculación ya ordenadas
            for (Asignatura a : m.asignaturas) {
                //Comprobamos si existe un hijo del nodo actual que la contenga
                int indice = -1;

                for (int i = 0; indice == -1 && i < actual.hijos.size(); i++) {

                    if (((Nodo) (actual.hijos.get(i))).objeto == a) {
                        indice = i;
                    }
                }

                if (indice == -1) {
                    //No existe el hijo -> lo creamos
                    Nodo hijo = new Nodo();
                    //Le asignamos el objeto
                    hijo.objeto = a;
                    //Le asignamos el padre
                    hijo.padre = actual;
                    //Inicializamos la frecuencia
                    hijo.contador = 0;
                    //Lo añadimos a la lista de hijos del actual
                    actual.hijos.add(hijo);

                    //Lo añadimos a las listas del registro
                    //Consultamos el registro que lleva esta asignatura
                    Registro<Asignatura> reg = i_registros.get(hijo.objeto);

                    //Añadimos al registro el nuevo nodo
                    reg.addNodo(hijo);

                    //Lo referenciamos y pasa a ser el nodo actual
                    actual = hijo;
                } else {
                    //Ya existía así que lo referenciamos y pasa a ser el nodo actual
                    actual = (Nodo) actual.hijos.get(indice);
                }

                //En cualquier caso aumentamos su frecuencia, su contador
                actual.contador++;

            }
        }

        //Montamos el FrequenceGraph 
        FrequenceGraph fg = new FrequenceGraph();
        fg.padre = padre;
        fg.registros = vector;
        fg.indice = i_registros;


        //Devolvemos el grafo
        return fg;
    }

    public static String imprimir(FrequenceGraph fg) {
        return imprimir_arbol(fg.padre);
    }

    public static String imprimir_arbol(Nodo padre) {
        return imprimir_arbol(padre, " ", true);
    }

    public static String imprimir_arbol(Nodo padre, String historia, boolean ultimo) {
        StringBuilder sb = new StringBuilder();

        //Imprimimos al padre con la historia de nuestro padre
        //El elemento del nodo


        if (ultimo) {
            int l = historia.length();
            if (l > 0) {
                sb.append(historia.substring(0, l - 1));
            }
            sb.append((char) 9562); //Símbolo que indica que es el último hijo

        } else {
            int l = historia.length();
            if (l > 0) {
                sb.append(historia.substring(0, l - 1));
            }
            sb.append((char) 9568);
        }

        if (padre.hijos.size() > 0) {
            sb.append((char) 9574); //Símbolo que indica que tiene hijos
        } else {
            sb.append((char) 9552); //Símbolo que indica que no tiene hijos
        }

        sb.append(padre.objeto).append(" ").append(padre.contador).append("\n");


        //Guardamos la historia que nos viene dada
        //Y creamos una historia nueva para nuestros hijos
        //String nuevahistoria = historia

        //Para cada hijo añadimos su versión impresa
        ArrayList<Nodo> hijos = padre.hijos;
        for (int i = 0; i < hijos.size(); i++) {

            if (i == hijos.size() - 1) {
                sb.append(imprimir_arbol(hijos.get(i), historia + " ", true));
            } else {
                sb.append(imprimir_arbol(hijos.get(i), historia + (char) 9553, false));
            }
        }
        return sb.toString();
    }

    static HashMap<ParAsig, Integer> dositemsets(FrequenceGraph fg) {

        HashMap<ParAsig, Integer> respuesta = new HashMap<ParAsig, Integer>();

        //Recorrer todos los elementos de la lista de menor a mayor anotando los kitemsets
        for (int i = fg.registros.size() - 1; i >= 0; i--) {
            //Para cada registro
            Registro<Asignatura> reg = fg.registros.get(i);

            //Anotaremos los grupos que genera
            ArrayList<ArrayList<Asignatura>> grupos = new ArrayList<ArrayList<Asignatura>>();

            //Recorrer los nodos de la lista del registro
            Nodo actual = reg.primero;
            while (actual != null) {
                //Tratar el nodo
                //Anotamos su frecuencia
                Asignatura actAsig = (Asignatura) actual.objeto;
                int frecuencia = actual.contador;
                //Trepamos hasta el padre
                //Vamos construyendo los grupos que van a sumar la frecuencia de la hoja
                Nodo actual_trepa = actual.padre;
                while (actual_trepa != null && actual_trepa.objeto != null) { //Hasta que lleguemos al nodo padre (cuyo objeto es "null")
                    //Tratar el padre //TODO De momento lo hacemos para k = 2 sólo, GENERALIZAR
                    //TODO para GENERALIZAR habrá que ir anotando grupos de tamaño k según vayamos trepando por el árbol
                    ParAsig pa = new ParAsig();
                    pa.a = actAsig;
                    pa.b = (Asignatura) actual_trepa.objeto;

                    //Añadimos la pareja al hashmap o aumentamos su frecuencia
                    Integer f_actual = respuesta.get(pa);
                    if (f_actual == null) {
                        f_actual = 0;
                        //Si no existía la creamos
                        //respuesta.put(pa, f_actual);
                    }

                    //Aumentamos la frecuencia
                    f_actual += frecuencia;
                    respuesta.put(pa, f_actual);

                    //Actualizar el padre
                    actual_trepa = actual_trepa.padre;
                }
                //Siguiente nodo
                actual = actual.siguiente;
            }

        }


        return respuesta;
    }

    static public Solucion BL(Solucion s, HashMap<ParAsig, Integer> afectados, int[] tiempos) {
        Solucion mejor = Solucion.copiar(s);
        double mejor_eval = Solucion.evaluar(mejor, afectados, tiempos);
        boolean parar = false;

        while (!parar) {

            //Obtener el vecindario
            Solucion[] vecinos = Solucion.vecinos(mejor);

            //Evaluar el vecindario y quedarnos con la mejor
            double max_eval = -1;
            Solucion max_v = mejor;
            for (Solucion v : vecinos) {
                double evaluacion = Solucion.evaluar(v, afectados, tiempos);
                if (evaluacion > max_eval) {
                    max_eval = evaluacion;
                    max_v = v;
                }
            }
            //Después de mirar todos los vecinos paramos si ninguno ha superado al mejor total
            if (max_eval > mejor_eval) {
                //Sustituimos al mejor
                mejor = max_v;
                mejor_eval = max_eval;
                System.out.println(max_eval + "\t" + mejor);
            } else {
                parar = true;
            }
        }

        return mejor;
    }

    static public Solucion BLprimerMejor(Solucion s, HashMap<ParAsig, Integer> afectados, int[] tiempos) {
        Solucion mejor = Solucion.copiar(s);
        Evaluacion mejor_eval = Solucion.evaluarComp(mejor, afectados, tiempos);
        boolean parar = false;

        while (!parar) {

            //Obtener el vecindario //TODO esta función se come mucha memoria, mejor calcularlos y evaluarlos uno a uno
            //Solucion[] vecinos = Solucion.vecinos(mejor);

            double max_eval = Double.MIN_VALUE;

            int l = mejor.s.length;

            boolean reiniciar = false;
            for (int a = 0; a < l && !reiniciar; a++) {
                for (int b = a + 1; b < l && !reiniciar; b++) {
                    //Intercambiar
                    Solucion vecino = intercambiar(mejor, a, b);
                    //Si hay respuesta comprobamos si es mejor que la actual
                    if (vecino != null) {
                        //Si es mejor que la mejor pasa a ser la mejor y repetimos

                        //Evaluar
                        Evaluacion eval = Solucion.evaluarComp(vecino, afectados, tiempos);
                        //Si la evaluación es mejor pasa a ser la mejor solución
                        if (eval.total() > mejor_eval.total()) {
                            mejor_eval = eval;
                            mejor = vecino;
                            //Hemos terminado con esta posición, volvemos a empezar

                            System.out.println(mejor_eval.colisiones + "\t" + mejor_eval.puntos + "\t" + mejor);
                            reiniciar = true;
                        }
                    }
                }
            }

            //Si llegamos aquí y reiniciar es false es que hemos comprobado todos los vecinos y ninguno es mejor
            //hemos terminado

            if (!reiniciar) {
                parar = true;
            }
        }

        return mejor;
    }

    static Sol ALprimerMejor(Sol s, HashMap<ParAsig, Integer> afectados, int[] huecos, float tdeseado) {
        Sol mejor = s.copia();
        Evaluacion mejor_eval = Sol.evaluarComp(mejor, afectados, huecos, tdeseado);
        boolean parar = false;

        while (!parar) {

            //Obtener el vecindario //TODO esta función se come mucha memoria, mejor calcularlos y evaluarlos uno a uno
            //Solucion[] vecinos = Solucion.vecinos(mejor);

            double max_eval = Double.MIN_VALUE;

            int l = mejor.solucion.size() * 100;

            boolean reiniciar = false;
            for (int a = 0; a < l && !reiniciar; a++) {
                //Modificar
                Sol vecino = Sol.Mutar(mejor);
                //Si hay respuesta comprobamos si es mejor que la actual
                if (vecino != null) {
                    //Si es mejor que la mejor pasa a ser la mejor y repetimos

                    //Evaluar
                    Evaluacion eval = Sol.evaluarComp(vecino, afectados, huecos, tdeseado);
                    //Si la evaluación es mejor pasa a ser la mejor solución
                    if (eval.total() > mejor_eval.total()) {
                        mejor_eval = eval;
                        mejor = vecino;
                        //Hemos terminado con esta posición, volvemos a empezar

                        //System.out.println(mejor_eval.colisiones + "\t" + mejor_eval.puntos + "\t" + mejor);
                        reiniciar = true;
                    }
                }
            }

            //Si llegamos aquí y reiniciar es false es que hemos comprobado todos los vecinos y ninguno es mejor
            //hemos terminado

            if (!reiniciar) {
                parar = true;
            }
        }

        return mejor;
    }

    static Sol ALprimerMejor2(Sol s, HashMap<ParAsig, Integer> afectados, int[] huecos) {
        Sol mejor = s.copia();
        Evaluacion mejor_eval = Sol.evaluarComp(mejor, afectados, huecos);
        boolean parar = false;

        while (!parar) {

            //Obtener el vecindario //TODO esta función se come mucha memoria, mejor calcularlos y evaluarlos uno a uno
            //Solucion[] vecinos = Solucion.vecinos(mejor);

            double max_eval = Double.MIN_VALUE;

            int l = mejor.solucion.size() * 100;

            boolean reiniciar = false;
            for (int a = 0; a < l && !reiniciar; a++) {
                //Modificar
                Sol vecino = Sol.Mutar2(mejor);
                //Si hay respuesta comprobamos si es mejor que la actual
                if (vecino != null) {
                    //Si es mejor que la mejor pasa a ser la mejor y repetimos

                    //Evaluar
                    Evaluacion eval = Sol.evaluarComp(vecino, afectados, huecos);
                    //Si la evaluación es mejor pasa a ser la mejor solución
                    if (eval.total() > mejor_eval.total()) {
                        mejor_eval = eval;
                        mejor = vecino;
                        //Hemos terminado con esta posición, volvemos a empezar

                        //System.out.println(mejor_eval.colisiones + "\t" + mejor_eval.puntos + "\t" + mejor);
                        reiniciar = true;
                    }
                }
            }

            //Si llegamos aquí y reiniciar es false es que hemos comprobado todos los vecinos y ninguno es mejor
            //hemos terminado

            if (!reiniciar) {
                parar = true;
            }
        }

        return mejor;
    }

    static Sol BLprimerMejor(Sol s, HashMap<ParAsig, Integer> afectados, int[] huecos, float tdeseado) {
        Sol mejor = s.copia();
        Evaluacion mejor_eval = Sol.evaluarComp(mejor, afectados, huecos, tdeseado);
        boolean parar = false;

        while (!parar) {

            //Obtener el vecindario //TODO esta función se come mucha memoria, mejor calcularlos y evaluarlos uno a uno
            //Solucion[] vecinos = Solucion.vecinos(mejor);

            double max_eval = Double.MIN_VALUE;

            int l = mejor.solucion.size();
            int nhuecos = mejor.nhuecos;

            boolean reiniciar = false;
            for (int incremento = 1; incremento < nhuecos && !reiniciar; incremento++) {
                for (int elemento = 0; elemento < l && !reiniciar; elemento++) {
                    //Modificar
                    Sol vecino = Sol.cambiar(mejor, elemento, incremento);
                    //Si hay respuesta comprobamos si es mejor que la actual
                    if (vecino != null) {
                        //Si es mejor que la mejor pasa a ser la mejor y repetimos

                        //Evaluar
                        Evaluacion eval = Sol.evaluarComp(vecino, afectados, huecos, tdeseado);
                        //Si la evaluación es mejor pasa a ser la mejor solución
                        if (eval.total() > mejor_eval.total()) {
                            mejor_eval = eval;
                            mejor = vecino;
                            //Hemos terminado con esta posición, volvemos a empezar

                            //System.out.println(mejor_eval.colisiones + "\t" + mejor_eval.puntos + "\t" + mejor);
                            reiniciar = true;
                        }
                    }
                }
            }

            //Si llegamos aquí y reiniciar es false es que hemos comprobado todos los vecinos y ninguno es mejor
            //hemos terminado

            if (!reiniciar) {
                parar = true;
            }
        }

        return mejor;
    }

    /**
     * Los huecos son uno más que los tiempos. El primer hueco contiene un 0 y
     * los siguientes van sumando los tiempos. Por ejemplo si los tiempos son
     * {1,1,1,2} los huecos serán {0,1,2,3,5}
     *
     */
    static int[] calcularHuecos(int[] tiempos) {
        int l = tiempos.length + 1;
        int[] respuesta = new int[l];

        respuesta[0] = 0;

        int actual = 0;

        for (int i = 1; i < l; i++) {

            actual = actual + tiempos[i - 1]; //Vamos acumulando los tiempos

            respuesta[i] = actual;
        }
        return respuesta;
    }
}
