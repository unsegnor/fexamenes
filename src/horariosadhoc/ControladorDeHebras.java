/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package horariosadhoc;

import java.util.ArrayList;
import java.util.Collections;
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
    
    
    ControladorDeHebras(ExecutorService exec){
        this.exec = exec;
    }

    synchronized public void heTerminado(Evaluacion eval, Sol solucion) {
        System.out.println("Una hebra ha terminado con " + eval.colisiones + "," + eval.puntos + "," + solucion);
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
            int actual = 0;
            System.out.println("-------------------"+actual+"--------------------------");
            for(Asignacion a: asignaciones){
                if(a.numero > actual){
                    actual = a.numero;
                    System.out.println("-------------------"+actual+"--------------------------");
                }
                System.out.println(a.asignatura);
            }
            //System.out.println(asignaciones);
        }

    }
}
