/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package horariosadhoc;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map.Entry;

/**
 *
 * @author Victor
 */
public class Utiles {

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
        int mejor_eval = Solucion.evaluar(s, afectados, tiempos);
        boolean parar = false;

        while (!parar) {

            //Obtener el vecindario
            Solucion[] vecinos = Solucion.vecinos(mejor);

            //Evaluar el vecindario y quedarnos con la mejor
            int max_eval = 0;
            Solucion max_v = null;
            for (Solucion v : vecinos) {
                int evaluacion = Solucion.evaluar(v, afectados, tiempos);
                if (evaluacion > max_eval) {
                    max_eval = evaluacion;
                    max_v = v;
                }
            }
            //Después de mirar todos los vecinos paramos si ninguno ha superado al mejor total
            if(max_eval > mejor_eval){
                //Sustituimos al mejor
                mejor = max_v;
                mejor_eval = max_eval;
            }else{
                parar = true;
            }
        }

        return mejor;
    }
}
