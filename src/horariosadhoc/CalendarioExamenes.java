/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package horariosadhoc;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.GregorianCalendar;

/**
 *
 * @author hcarreras
 */
public class CalendarioExamenes {

    /**
     * @param args the command line arguments
     */
    
    public static ArrayList<Integer> horas(Calendar fechaInicio, Calendar fechaFinal){
        ArrayList<Integer> horasDiferencias = new ArrayList<Integer>();
        //Calendar fechaInicio = new GregorianCalendar(2013, Calendar.JUNE, 13);
        //Calendar fechaFinal = new GregorianCalendar(2013, Calendar.JULY, 7);
        //Se inicializa a fechaInicio, como no hay constructor a partir de otro GragorianCalendar, lo inicializo asi
        Calendar auxiliar = (Calendar) fechaInicio.clone();

        
        //System.out.println(auxiliar.getTime());
        //System.out.println(auxiliar.get(Calendar.DAY_OF_MONTH));        
        
        horasDiferencias.add(7); //Del primer día
        auxiliar.add(Calendar.DAY_OF_WEEK, 1);
        //Para comparar tengo que hacerlo en milisegundos
        while(auxiliar.getTimeInMillis() < fechaFinal.getTimeInMillis()){
            //Hay que tener en cuenta que Domingo es el 1
            if(auxiliar.get(Calendar.DAY_OF_WEEK) != Calendar.SATURDAY && auxiliar.get(Calendar.DAY_OF_WEEK) != Calendar.SUNDAY){  
                //Cualquier otro día es 17 hasta primera y 7 hasta segunda
                //Mañana
                horasDiferencias.add(17);      
                //Tarde
                horasDiferencias.add(7);
                //Incremento un dia
           //     System.out.println("Antes: " + auxiliar.get(Calendar.DAY_OF_WEEK));
                auxiliar.add(Calendar.DAY_OF_WEEK, 1);
                //System.out.println("Despues: " + auxiliar.get(Calendar.DAY_OF_WEEK)); 
              //  Thread.sleep(3000);
            }else{    
                
                if(auxiliar.get(Calendar.DAY_OF_WEEK) == Calendar.SATURDAY){
                //Esto es el lunes después del finde 65 primera 7 segunda
                horasDiferencias.add(65);
                horasDiferencias.add(7);
                auxiliar.add(Calendar.DAY_OF_WEEK, 3);
                }else if(auxiliar.get(Calendar.DAY_OF_WEEK) != Calendar.SUNDAY){
                    System.out.println("ERRORRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRR");
                }
               
            }

            
        }
       return horasDiferencias;    
    }
}