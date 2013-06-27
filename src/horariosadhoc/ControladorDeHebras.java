/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package horariosadhoc;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map.Entry;
import java.util.concurrent.ExecutorService;

/**
 *
 * @author Victor
 */
public class ControladorDeHebras {

    int mejor_colisiones = Integer.MAX_VALUE;
    double mejor_puntuacion = 0;
    double mejor_total = Double.MIN_VALUE;
    private Sol mejor_solucion;
    public int contador = 0;
    public int max_ejecuciones = 0;
    private final ExecutorService exec;
    int[] tiempos;
    Calendar fini;
    int[] huecos;
    HashMap<ParAsig, Integer> afectados;
    
    
    ControladorDeHebras(ExecutorService exec){
        this.exec = exec;
    }

    synchronized public void heTerminado(Evaluacion eval, Sol solucion) {
        System.out.println("Una hebra ha terminado con " + eval.colisiones + " colisiones, "+ eval.bajo_minimos + " estudiantes bajo el tiempo mínimo, " + eval.puntos + " puntos, " + solucion);
        //Sumamos una ejecución más
        contador++;

        boolean mejora = false;

        if (eval.colisiones < mejor_colisiones) {
            mejor_colisiones = eval.colisiones;
            mejor_puntuacion = eval.puntos;
            mejor_solucion = solucion;
            mejora = true;
        } else {
            if (eval.colisiones == mejor_colisiones) {
                if (eval.puntos > mejor_puntuacion) {
                    mejor_colisiones = eval.colisiones;
                    mejor_puntuacion = eval.puntos;
                    mejor_solucion = solucion;
                    mejora = true;
                }
            }
        }

        if (mejora && contador > 10) {
            //Si hemos mejorado entonces guardamos la solución
            //Utiles.guardarArchivo("Solucion" + contador, mejor_colisiones + "," + mejor_puntuacion + "\n" + mejor_solucion);
        }

        if (contador >= max_ejecuciones) {
            //Hemos terminado
            exec.shutdown();
            //Transformamos y escribimos la mejor solución encontrada

            ArrayList<Asignacion> asignaciones = new ArrayList<Asignacion>(mejor_solucion.solucion);
            //Ordenar la solución
            Collections.sort(asignaciones, new ComparadorDeAsignaciones());
            //Pasar a calendario
            //Imprimir guay
            
            Calendar fecha = this.fini;
            
            int actual = 0;
            System.out.println("Exámenes de incidencia previstos: " + mejor_colisiones);
            System.out.println("Puntuación del calendario: " + mejor_puntuacion);
            System.out.println("---------------------------------------------");
            System.out.println(fecha.get(Calendar.DAY_OF_MONTH) +"/"+ (fecha.get(Calendar.MONTH)+1) +"/"+ fecha.get(Calendar.YEAR) +" - "+ fecha.get(Calendar.HOUR_OF_DAY) +":"+ fecha.get(Calendar.MINUTE));
            System.out.println("---------------------------------------------");
            for(Asignacion a: asignaciones){
                if(a.numero > actual){
                    while(actual<a.numero){
                    fecha.add(Calendar.HOUR, tiempos[actual]);
                    actual++;
                    }
                    System.out.println("---------------------------------------------");
                    System.out.println(fecha.get(Calendar.DAY_OF_MONTH) +"/"+ (fecha.get(Calendar.MONTH)+1) +"/"+ fecha.get(Calendar.YEAR) +" - "+ fecha.get(Calendar.HOUR_OF_DAY) +":"+ fecha.get(Calendar.MINUTE));
                    System.out.println("---------------------------------------------");
                }
                System.out.println(a.asignatura + "\t(" + a.asignatura.matriculados + " estudiantes)");
            }
            //System.out.println(asignaciones);
            
            //Calcular histograma de la solución
            HashMap<Integer, Integer> histograma = Sol.calcularHistograma(mejor_solucion, afectados, huecos);
            
            Evaluacion evalu = Sol.evaluarComp(mejor_solucion, afectados, huecos);
            
            //Vector en el que lo vamos a almacenar
            ArrayList<Posicion<Integer> > vhistograma = new ArrayList<Posicion<Integer> >();
            
            //Pasar histograma a vector
            for(Entry<Integer,Integer> e : histograma.entrySet()){
                Posicion<Integer> p = new Posicion<Integer>(e.getValue(), e.getKey());
                vhistograma.add(p);
            }
            
            //Ordenar vector
            Collections.sort(vhistograma, new OrdenarPosiciones());
            
            //Imprimir histograma
            for(Posicion p : vhistograma){
                System.out.println(p.valor + "," + (Integer)p.cosa);
            }
        }

    }
}
