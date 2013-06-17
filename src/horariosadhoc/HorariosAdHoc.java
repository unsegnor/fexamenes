/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package horariosadhoc;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Collections;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map.Entry;
import java.util.Set;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

/**
 *
 * @author Victor
 */
public class HorariosAdHoc {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) throws InterruptedException {
        // TODO code application logic here
        
        //Parámetros
        int nhebras = 10;
        int nbusquedas = 100;
        String archivo_matriculaciones = "AsignaturasPrueba.txt";
        String archivo_codigos = "RelacionAsignaturasGrados.csv";
        
        int dia_i = 2; //13/5/2013
        int mes_i = 8;
        int anio_i = 2013;
        
        int dia_f = 20; //11/6/2013
        int mes_f = 8;
        int anio_f = 2013;
        
        //Obtener datos
        
        //Lista de asignaturas a tener en cuenta
        //ArrayList<Asignatura> asignaturas_validas = Cargador.leer_asignaturas(archivo_asignaturas_validas);
        
        //DatosMatriculas da = Cargador.leer_y_comprobar_matriculas(asignaturas_validas, archivo_matriculaciones);
        
        //HashMap<Asignatura, String> codigos = Cargador.leer_codigos(archivo_codigos);
        
        DatosMatriculas da = Cargador.leer_matriculas(archivo_matriculaciones);
        
        //Fechas de inicio y fin de la temporada de exámenes
        Calendar fecha_de_inicio = new GregorianCalendar(anio_i, mes_i, dia_i, 9,00);
        Calendar fecha_de_fin = new GregorianCalendar(anio_f, mes_f, dia_f, 23,59);
        


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

        //Definición del problema
        
        //Fecha de inicio
        Calendar fini = fecha_de_inicio;
        Calendar ffin = fecha_de_fin;
        
        //Tiempos
        ArrayList<Integer> Horas = CalendarioExamenes.horas(fini, ffin);
        
        int[] tiempos = new int[Horas.size()];
        
        int c=0;
        for(Integer hora : Horas){
            tiempos[c] = hora.intValue();
            c++;
        }
        
        
        //int[] tiempos = 
        /*
        {
            7, 17, 7, 17, 7, 17, 7, 17, 7, 65,
            7, 17, 7, 17, 7, 17, 7, 17, 7, 65,
            7, 17, 7, 17, 7, 17, 7, 17, 7, 65,
            7, 17, 7, 17, 7, 17, 7, 17, 7, 65
        };
        */
        //Los huecos serán lo que hay enmedio
        int nhuecos = tiempos.length+1;
      
        //Calculamos los huecos
        int[] huecos = Utiles.calcularHuecos(tiempos);
        
        
        
        //Creamos las herramientas para paralelizar
        ExecutorService executor = Executors.newFixedThreadPool(nhebras);
        
        ControladorDeHebras control = new ControladorDeHebras(executor);
        control.fini = fini;
        control.tiempos = tiempos;
        control.huecos = huecos;
        control.afectados = dositemsets;
        
        
        
        //HebraEjecutora h = new HebraEjecutora(asignaturas, nhuecos, dositemsets, huecos);

        //Realizar N búsquedas locales y quedarnos con la mejor
        int N = nbusquedas;
        control.max_ejecuciones = N;
        
        for(int i=0; i<N; i++){
            executor.execute(new HebraEjecutora(control, asignaturas, nhuecos, dositemsets, huecos));
        }
        
        //executor.awaitTermination(1, TimeUnit.DAYS);
        //Ordenar la mejor solución según el hueco
        
        //ArrayList<Asignacion> asignaciones = new ArrayList<Asignacion>(mejor.solucion);
        
        //Collections.sort(asignaciones, new ComparadorDeAsignaciones());
        
        //System.out.println(asignaciones);
        
        
        
        //Utiles.guardarArchivo("mejorSolucion.txt", "La mejor solución es" + mejor_eval.colisiones + "\t" + mejor_eval.puntos + "\t" + mejor + "\n" + asignaciones);


        //Necesitamos soluciones completas ya que todas son consistentes -> metaheurísticas

        //Forma de la solución -> fragmentos de 12 horas, días, ... cada asignatura en uno de ellos.


        //Generación de nueva solución, intercambio de asignaturas entre ellas o con espacios vacíos




        //Fitness -> cuanto más alejadas estén las asignaturas con altas restricciones mejor.

    }
    
    
}
