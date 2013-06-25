/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package horariosadhoc;

import java.util.ArrayList;
import java.util.HashMap;

/**
 *
 * @author Victor
 */
public class HebraEjecutora implements Runnable {
    private final ArrayList<Asignatura> asignaturas;
    private final int nhuecos;
    private final HashMap<ParAsig, Integer> afectados;
    private final int[] huecos;
    private final ControladorDeHebras control;
    private final DatosDelProblema dp;

    HebraEjecutora(ControladorDeHebras control, ArrayList<Asignatura> asignaturas, int nhuecos, HashMap<ParAsig, Integer> afectados, int[] huecos, DatosDelProblema dp) {
        this.control = control;
        this.asignaturas = asignaturas;
        this.nhuecos = nhuecos;
        this.afectados = afectados;
        this.huecos = huecos;
        this.dp = dp;
        
    }

    @Override
    public void run() {

            //Crear solución aleatoria
            Sol solucion = Sol.Aleatoria(asignaturas, nhuecos);

            //Mejorar
            //System.out.println("--------------------------AL1-----------------------");
            Sol sBL1 = Utiles.ALprimerMejor(solucion, afectados, huecos, dp); //Primero mejoramos de forma aleatoria
            //System.out.println("--------------------------AL2-----------------------");
            //Sol sBL2 = Utiles.ALprimerMejor2(sBL1, dositemsets, huecos); //Después afinamos un poco
            //System.out.println("--------------------------BL-----------------------");
            Sol sBL = Utiles.BLprimerMejor(sBL1, afectados, huecos, dp); //Después hacemos BL
            Evaluacion eval = Sol.evaluarComp(sBL, afectados, huecos, dp);


            //System.out.println(mejor_eval);
            //System.out.println(eval.colisiones + "\t"+ eval.puntos +"\t" + sBL);
            control.heTerminado(eval, sBL);
    }
    
}
