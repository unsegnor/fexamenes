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
        int nhebras = 8;
        int nbusquedas = 32;
        String archivo_matriculaciones = "AsignaturasPrueba.txt";
        String archivo_codigos = "RelacionAsignaturasGrados.csv";
        String archivo_asignaturas_validas = "RelacionAsignaturasGrados.csv";

        int dia_i = 2; //13/5/2013
        int mes_i = 7;
        int anio_i = 2013;

        int dia_f = 20; //11/6/2013
        int mes_f = 8;
        int anio_f = 2013;

        //Obtener datos

        //Lista de asignaturas a tener en cuenta
        ArrayList<Asignatura> asignaturas_validas = Cargador.leer_asignaturas(archivo_asignaturas_validas, 0);

        for (Asignatura a : asignaturas_validas) {
            System.out.println(a + " cuatrimestre: " + a.cuatrimestre);
        }


        DatosMatriculas da = Cargador.leer_y_comprobar_matriculas(asignaturas_validas, archivo_matriculaciones);




        //DatosMatriculas da = Cargador.leer_matriculas(archivo_matriculaciones);

        //Leemos las asignaturas matriculadas por cada matriculación para sacar el número medio
        int n_asignaturas = 0;
        for (Matriculacion mat : da.matriculaciones) {
            n_asignaturas += mat.asignaturas.size();
        }
        //Calculamos la media de asignaturas matriculadas por estudiante
        float media_asignaturas = (float) n_asignaturas / (float) da.matriculaciones.size();

        //Leemos la relación de códigos con nombre de las asignaturas
        //HashMap<Asignatura, String> codigos = Cargador.leer_codigos(archivo_codigos);

        System.out.println("Los estudiantes se han matriculado de una media de " + media_asignaturas + " asignaturas");
        System.out.println("Se han contado " + Asignatura.existentes.size() + " asignaturas diferentes.");


        //Fechas de inicio y fin de la temporada de exámenes
        Calendar fecha_de_inicio = new GregorianCalendar(anio_i, mes_i, dia_i, 9, 00);
        Calendar fecha_de_fin = new GregorianCalendar(anio_f, mes_f, dia_f, 23, 59);



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

        //Guardar las colisiones en una tabla
        //generar la tabla
        HashMap< Asignatura, HashMap< Asignatura, Integer>> tabla_colisiones = new HashMap< Asignatura, HashMap< Asignatura, Integer>>();

        //Rellenar la tabla
        for (Entry<ParAsig, Integer> e : dositemsets.entrySet()) {
            //Si existía el hashmap para esta... vamos al grano
        }

        //Definir un orden de asignaturas
        HashMap< Asignatura, Integer> a2i = new HashMap< Asignatura, Integer>();
        ArrayList< Asignatura> i2a = new ArrayList<Asignatura>();

        //Recorrer la lista de asignaturas existentes para establecer el orden
        for (Asignatura a : Asignatura.existentes.values()) {

            i2a.add(a);
            a2i.put(a, i2a.size() - 1);

        }

        int na = i2a.size(); // Número de asignaturas

        //Generamos la matriz de colisiones
        int[][] m_colisiones = new int[na][na];

        boolean[] activas = new boolean[na];
        Arrays.fill(activas, false);

        //Ahora podemos recorrer el hashmap de colisiones rellenando la matriz de colisiones
        for (Entry<ParAsig, Integer> e : dositemsets.entrySet()) {
            int indiceI = a2i.get(e.getKey().a);
            int indiceJ = a2i.get(e.getKey().b);
            int valor = e.getValue();

            //Almacenamos el valor
            m_colisiones[indiceI][indiceJ] = valor;
            //Y el contrario
            m_colisiones[indiceJ][indiceI] = valor;

            //Anotamos la fila y la columna como activas
            activas[indiceI] = true;
            activas[indiceJ] = true;

        }

        //Ahora podemos imprimir la matriz de colisiones
        StringBuilder sb2 = new StringBuilder();

        //La cabecera
        sb2.append("Asignaturas");

        for (int i = 0; i < na; i++) {
            if (activas[i]) {
                sb2.append(";").append(i2a.get(i));
            }
        }

        //El contenido (sólo de las activas)
        for (int i = 0; i < na; i++) {
            if (activas[i]) {
                sb2.append("\n").append(i2a.get(i));
                for (int j = 0; j < na; j++) {
                    if (activas[j]) {
                        sb2.append(";").append(m_colisiones[i][j]);
                    }
                }
            }
        }

        System.out.println(sb2);
        Utiles.guardarArchivo("TablaColisiones.csv", sb2.toString());

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

        int c = 0;
        for (Integer hora : Horas) {
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
        int nhuecos = tiempos.length + 1;

        //Calculamos los huecos
        int[] huecos = Utiles.calcularHuecos(tiempos);

        //Sumar tiempo total de la temporada de exámenes es el tiempo hasta el último hueco
        int ttotal = huecos[huecos.length - 1];

        System.out.println("La temporada de exámenes dura en total " + ttotal + " unidades de tiempo.");

        //Calculamos el tiempo de estudio objetivo

        float testudio_deseado = (float) ttotal / media_asignaturas;

        System.out.println("El tiempo de estudio deseado es de " + testudio_deseado + " unidades de tiempo.");

        //Tiempo mínimo de separación entre exámenes
        int nasig = Asignatura.existentes.size();
        float tmin = (float) ttotal / (float) nasig;

        System.out.println("El tiempo mínimo de separación entre exámenes será de " + tmin + " unidades de tiempo.");

        DatosDelProblema dp = new DatosDelProblema();
        dp.tdeseado = testudio_deseado;
        dp.tmin = tmin;

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

        for (int i = 0; i < N; i++) {
            executor.execute(new HebraEjecutora(control, asignaturas, nhuecos, dositemsets, huecos, dp));
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
