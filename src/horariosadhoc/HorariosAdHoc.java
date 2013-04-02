/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package horariosadhoc;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map.Entry;

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
        for(Entry<ParAsig, Integer> e : dositemsets.entrySet()){
            System.out.println(e.getKey().a + "\t" + e.getKey().b + "\t->\t" + e.getValue());
        }
        
        //Componer sistema para consultas, transformar en restricciones con peso
        ArrayList<Restriccion> restricciones = new ArrayList<Restriccion>();
        for(Entry<ParAsig, Integer> e : dositemsets.entrySet()){
            Restriccion r = new Restriccion();
            r.a = e.getKey().a;
            r.b = e.getKey().b;
            r.peso = e.getValue();
            restricciones.add(r);
        }
        
        
        //Aquí ya tenemos las restricciones del problema ponderadas por número de personas a las que afecta
        Problema p = new Problema();
        
        //Necesitamos soluciones completas ya que todas son consistentes -> metaheurísticas
        
        //Forma de la solución -> fragmentos de 12 horas, días, ... cada asignatura en uno de ellos.
        Solucion s = new Solucion((ArrayList<Asignatura>) Asignatura.existentes.values(),20); //También hay espacios vacíos. 
        
        //Generación de nueva solución, intercambio de asignaturas entre ellas o con espacios vacíos
        
        
        
        
        //Fitness -> cuanto más alejadas estén las asignaturas con altas restricciones mejor.
        
    }
}
