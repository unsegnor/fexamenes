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
    private float tdeseado;

    HebraEjecutora(ControladorDeHebras control, ArrayList<Asignatura> asignaturas, int nhuecos, HashMap<ParAsig, Integer> afectados, int[] huecos, float tdeseado) {
        this.control = control;
        this.asignaturas = asignaturas;
        this.nhuecos = nhuecos;
        this.afectados = afectados;
        this.huecos = huecos;
        this.tdeseado = tdeseado;
        
    }

    @Override
    public void run() {

            //Crear solución aleatoria
            Sol solucion = Sol.Aleatoria(asignaturas, nhuecos);

            //Mejorar
            //System.out.println("--------------------------AL1-----------------------");
            Sol sBL1 = Utiles.ALprimerMejor(solucion, afectados, huecos, tdeseado); //Primero mejoramos de forma aleatoria
            //System.out.println("--------------------------AL2-----------------------");
            //Sol sBL2 = Utiles.ALprimerMejor2(sBL1, dositemsets, huecos); //Después afinamos un poco
            //System.out.println("--------------------------BL-----------------------");
            Sol sBL = Utiles.BLprimerMejor(sBL1, afectados, huecos, tdeseado); //Después hacemos BL
            Evaluacion eval = Sol.evaluarComp(sBL, afectados, huecos, tdeseado);


            //System.out.println(mejor_eval);
            //System.out.println(eval.colisiones + "\t"+ eval.puntos +"\t" + sBL);
            control.heTerminado(eval, sBL);
    }
    
}
