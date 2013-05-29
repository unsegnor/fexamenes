/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package horariosadhoc;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map.Entry;
import java.util.Set;

/**
 *
 * @author Victor
 */
public class HorariosAdHoc {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        // TODO code application logic here

        //Obtener datos
        DatosMatriculas da = Cargador.leer_matriculas("AsignaturasPrueba.txt");

        //Preprocesar datos
        FrequenceGraph fg = Utiles.montarFG(da);

        //Imprimir el árbol
        System.out.println(Utiles.imprimir(fg));

        //Extraer 2-itemsets
        HashMap<ParAsig, Integer> dositemsets = Utiles.dositemsets(fg);

        //Imprimir los itemsets con sus frecuencias
        for (Entry<ParAsig, Integer> e : dositemsets.entrySet()) {
            System.out.println(e.getKey().a + "\t" + e.getKey().b + "\t->\t" + e.getValue());
        }

        //Guardar los itemsets con sus frecuencias en un archivo
        StringBuilder sb = new StringBuilder();
        for (Entry<ParAsig, Integer> e : dositemsets.entrySet()) {
            sb.append(e.getKey().a.ID).append(",").append(e.getKey().b.ID).append(",").append(e.getValue().toString()).append("\n");
        }

        Utiles.guardarArchivo("frecuencias2.txt", sb.toString());

        //Componer sistema para consultas, transformar en restricciones con peso
        ArrayList<Restriccion> array_restricciones = new ArrayList<Restriccion>();
        for (Entry<ParAsig, Integer> e : dositemsets.entrySet()) {
            Restriccion r = new Restriccion();
            r.a = e.getKey().a;
            r.b = e.getKey().b;
            r.peso = e.getValue();
            array_restricciones.add(r);
        }
        //Añadir también los grupos inversos
        Set<Entry<ParAsig, Integer>> conjunto = new HashSet<Entry<ParAsig, Integer>>(dositemsets.entrySet());
        for (Entry<ParAsig, Integer> e : conjunto) {
            ParAsig inverso = new ParAsig();
            inverso.a = e.getKey().b;
            inverso.b = e.getKey().a;

            dositemsets.put(inverso, e.getValue());
        }


        //Crear matriz de restricciones
        /*for (Entry<ParAsig, Integer> e : dositemsets.entrySet()) {
         Restriccion r = new Restriccion();
         r.a = e.getKey().a;
         r.b = e.getKey().b;
         r.peso = e.getValue();
         }*/

        //Aquí ya tenemos las restricciones del problema ponderadas por número de personas a las que afecta





        int ndias = 28;


        //Lo modificamos como queramos, por ejemplo, cada 7 añadimos 2 prohibidos

        //La lista de asignaturas posibles
        ArrayList<Asignatura> asignaturas = new ArrayList<Asignatura>(Asignatura.existentes.values());

        //Creamos una solución aleatoria
        //Solucion s = Solucion.Aleatoria(asignaturas, fragmentos);

        //Definimos los tiempos que separan los fragmentos
        //int[] tiempos = new int[nfragmentos - 1];
        //Arrays.fill(tiempos, 1);

        /*
         *         int[] tiempos = {
         0,0,0,0,0,0,0,0,0,0,0,0,0,0,7,0,0,0,0,0,0,0,0,0,0,0,0,0,0,17,0,0,0,0,0,0,0,0,0,0,0,0,0,0,7,0,0,0,0,0,0,0,0,0,0,0,0,0,0,17,0,0,0,0,0,0,0,0,0,0,0,0,0,0,7,0,0,0,0,0,0,0,0,0,0,0,0,0,0,17,0,0,0,0,0,0,0,0,0,0,0,0,0,0,7,0,0,0,0,0,0,0,0,0,0,0,0,0,0,17,0,0,0,0,0,0,0,0,0,0,0,0,0,0,7,0,0,0,0,0,0,0,0,0,0,0,0,0,0,65
         ,0,0,0,0,0,0,0,0,0,0,0,0,0,0,7,0,0,0,0,0,0,0,0,0,0,0,0,0,0,17,0,0,0,0,0,0,0,0,0,0,0,0,0,0,7,0,0,0,0,0,0,0,0,0,0,0,0,0,0,17,0,0,0,0,0,0,0,0,0,0,0,0,0,0,7,0,0,0,0,0,0,0,0,0,0,0,0,0,0,17,0,0,0,0,0,0,0,0,0,0,0,0,0,0,7,0,0,0,0,0,0,0,0,0,0,0,0,0,0,17,0,0,0,0,0,0,0,0,0,0,0,0,0,0,7,0,0,0,0,0,0,0,0,0,0,0,0,0,0,65
         ,0,0,0,0,0,0,0,0,0,0,0,0,0,0,7,0,0,0,0,0,0,0,0,0,0,0,0,0,0,17,0,0,0,0,0,0,0,0,0,0,0,0,0,0,7,0,0,0,0,0,0,0,0,0,0,0,0,0,0,17,0,0,0,0,0,0,0,0,0,0,0,0,0,0,7,0,0,0,0,0,0,0,0,0,0,0,0,0,0,17,0,0,0,0,0,0,0,0,0,0,0,0,0,0,7,0,0,0,0,0,0,0,0,0,0,0,0,0,0,17,0,0,0,0,0,0,0,0,0,0,0,0,0,0,7,0,0,0,0,0,0,0,0,0,0,0,0,0,0,65
         ,0,0,0,0,0,0,0,0,0,0,0,0,0,0,7,0,0,0,0,0,0,0,0,0,0,0,0,0,0,17,0,0,0,0,0,0,0,0,0,0,0,0,0,0,7,0,0,0,0,0,0,0,0,0,0,0,0,0,0,17,0,0,0,0,0,0,0,0,0,0,0,0,0,0,7,0,0,0,0,0,0,0,0,0,0,0,0,0,0,17,0,0,0,0,0,0,0,0,0,0,0,0,0,0,7,0,0,0,0,0,0,0,0,0,0,0,0,0,0,17,0,0,0,0,0,0,0,0,0,0,0,0,0,0,7,0,0,0,0,0,0,0,0,0,0,0,0,0,0,65
         };
         */

        
        int[] tiempos = {
            7, 17, 7, 17, 7, 17, 7, 17, 7, 65,
            7, 17, 7, 17, 7, 17, 7, 17, 7, 65,
            7, 17, 7, 17, 7, 17, 7, 17, 7, 65,
            7, 17, 7, 17, 7, 17, 7, 17, 7, 65
        };
        
        //Los huecos serán lo que hay enmedio
        int nhuecos = tiempos.length;

        //Creamos los fragmentos iniciales
        Fragmento[] fragmentos = Solucion.nuevoFragmento(nhuecos);
        
        
        //Calculamos los huecos
        int[] huecos = Utiles.calcularHuecos(tiempos);
        
        
        /*
         int evaluacion = Solucion.evaluar(s, dositemsets, tiempos);

         System.out.println(s);
         System.out.println(evaluacion);

         Solucion mejorBL = Utiles.BL(s, dositemsets, tiempos);
         int evaluacion2 = Solucion.evaluar(mejorBL, dositemsets, tiempos);

         System.out.println(mejorBL);
         System.out.println(evaluacion2);

         */

        //Realizar N búsquedas locales y quedarnos con la mejor
        int N = 10;
        Sol mejor = Sol.Aleatoria(asignaturas, nhuecos);
        mejor = Utiles.BLprimerMejor(mejor, dositemsets, huecos);
        Evaluacion mejor_eval = Sol.evaluarComp(mejor, dositemsets, huecos);

        System.out.println(mejor_eval.colisiones + "\t" + mejor_eval.puntos + "\t" + mejor);

        for (int i = 0; i < N; i++) {
            //Crear solución aleatoria
            Sol solucion = Sol.Aleatoria(asignaturas, nhuecos);

            //Búsqueda local
            System.out.println("--------------------------AL1-----------------------");
            Sol sBL1 = Utiles.ALprimerMejor(solucion, dositemsets, huecos); //Primero mejoramos de forma aleatoria
            //System.out.println("--------------------------AL2-----------------------");
            //Sol sBL2 = Utiles.ALprimerMejor2(sBL1, dositemsets, huecos); //Después afinamos un poco
            System.out.println("--------------------------BL-----------------------");
            Sol sBL = Utiles.BLprimerMejor(sBL1, dositemsets, huecos); //Después hacemos BL
            Evaluacion seval = Sol.evaluarComp(sBL, dositemsets, huecos);

            //System.out.println(solucion + " -> " + sBL);

            if (seval.total() > mejor_eval.total()) {
                mejor_eval = seval;
                mejor = sBL;
                System.out.println(mejor_eval.colisiones + "\t" + mejor_eval.puntos + "\t" + mejor);
            }

            //System.out.println(mejor_eval);
            System.out.println(seval.colisiones + "\t"+ seval.puntos +"\t" + sBL);
        }

        System.out.println("La mejor solución es" + mejor_eval.colisiones + "\t" + mejor_eval.puntos + "\t" + mejor);
        
        
        //Ordenar la mejor solución según el hueco
        
        ArrayList<Asignacion> asignaciones = new ArrayList<Asignacion>(mejor.solucion);
        
        Collections.sort(asignaciones, new ComparadorDeAsignaciones());
        
        System.out.println(asignaciones);
        
        
        
        Utiles.guardarArchivo("mejorSolucion.txt", "La mejor solución es" + mejor_eval.colisiones + "\t" + mejor_eval.puntos + "\t" + mejor + "\n" + asignaciones);


        //Necesitamos soluciones completas ya que todas son consistentes -> metaheurísticas

        //Forma de la solución -> fragmentos de 12 horas, días, ... cada asignatura en uno de ellos.


        //Generación de nueva solución, intercambio de asignaturas entre ellas o con espacios vacíos




        //Fitness -> cuanto más alejadas estén las asignaturas con altas restricciones mejor.

    }
}
