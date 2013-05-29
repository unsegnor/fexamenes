/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package horariosadhoc;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Victor
 */
public class Cargador {

    /*
     * Actualmente lee por cada línea una matriculación con las asignaturas separadas por comas
     */
    public static DatosMatriculas leer_matriculas(String archivo) {
        FileReader fr = null;
        BufferedReader br = null;
        DatosMatriculas da = null;
        try {
            fr = new FileReader(archivo);
            br = new BufferedReader(fr);
            da = new DatosMatriculas();

            //------------------------------------------------------------------------------------------------------


            ArrayList<Matriculacion> matriculas = new ArrayList<Matriculacion>();

            String linea = br.readLine();

            while (linea != null) {

                //tratar la línea
                //Cada línea es una matriculación
                ArrayList<Asignatura> asignaturas = new ArrayList<Asignatura>();

                //dividir por el separador
                String[] ld = linea.split(Global.cadena_separacion_asignaturas);

                //para cada cadena anotar la asignatura
                for (String texto_asignatura : ld) {

                    //Hacemos el trim a la cadena
                    texto_asignatura = texto_asignatura.trim();

                    //Si el texto es igual a la cadena vacía lo obviamos
                    if (!"".equals(texto_asignatura)) {



                        //comprobamos si existe
                        Asignatura asignatura = Asignatura.existentes.get(texto_asignatura);
                        //Si no existe la creamos
                        if (asignatura == null) {

                            asignatura = new Asignatura();
                            asignatura.ID = texto_asignatura;
                            //y la añadimos a la lista
                            Asignatura.existentes.put(texto_asignatura, asignatura);
                        }
                        //Cuando está creada o existe apuntamos a la asignatura que ya existe
                        //Añadimos la asognatura a la lista para la matriculación
                        asignaturas.add(asignatura);
                    }
                }

                //Aquí están todas las asignaturas añadidas y enlazadas
                Matriculacion matriculacion = new Matriculacion();
                //Montamos la matriculación
                matriculacion.asignaturas = asignaturas;
                //Y la añadimos a la lista
                matriculas.add(matriculacion);

                linea = br.readLine();
            }

            //Aquí tenemos todas las matriculaciones en la lista "matriculas"
            //montamos el objeto de datos de respuesta
            da.matriculaciones = matriculas;



            //------------------------------------------------------------------------------------------------------

        } catch (FileNotFoundException ex) {
            Logger.getLogger(Cargador.class.getName()).log(Level.SEVERE, null, ex);

        } catch (IOException ex) {
            Logger.getLogger(Cargador.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            try {
                fr.close();
            } catch (IOException ex) {
                Logger.getLogger(Cargador.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        return da;
    }

    static ArrayList<Asignatura> leer_asignaturas(String archivo) {
        FileReader fr = null;
        BufferedReader br = null;
        ArrayList<Asignatura> da = null;
        try {
            fr = new FileReader(archivo);
            br = new BufferedReader(fr);
            da = new ArrayList<Asignatura>();

            //------------------------------------------------------------------------------------------------------


            String linea = br.readLine();

            while (linea != null) {

                //tratar la línea
                //Cada línea es una asignatura
                //Hacemos el trim a la cadena
                String texto_asignatura = linea.trim();
                //Si el texto es igual a la cadena vacía lo obviamos
                if (!"".equals(texto_asignatura)) {
                    Asignatura asignatura = new Asignatura();
                    asignatura.ID = texto_asignatura;
                    //y la añadimos a la lista

                    da.add(asignatura);
                }


                linea = br.readLine();
            }



            //------------------------------------------------------------------------------------------------------

        } catch (FileNotFoundException ex) {
            Logger.getLogger(Cargador.class.getName()).log(Level.SEVERE, null, ex);

        } catch (IOException ex) {
            Logger.getLogger(Cargador.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            try {
                fr.close();
            } catch (IOException ex) {
                Logger.getLogger(Cargador.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        return da;
    }

    static DatosMatriculas leer_y_comprobar_matriculas(ArrayList<Asignatura> asignaturas_validas, String archivo) {
        FileReader fr = null;
        BufferedReader br = null;
        DatosMatriculas da = null;
        try {
            fr = new FileReader(archivo);
            br = new BufferedReader(fr);
            da = new DatosMatriculas();

            //--------------------------------------------------

            //Añadir las asignaturas válidas a la lista de asignaturas existentes
            for (Asignatura asig : asignaturas_validas) {
                Asignatura.existentes.put(asig.ID, asig);
            }



            //------------------------------------------------------------------------------------------------------


            ArrayList<Matriculacion> matriculas = new ArrayList<Matriculacion>();

            String linea = br.readLine();

            while (linea != null) {

                //tratar la línea
                //Cada línea es una matriculación
                ArrayList<Asignatura> asignaturas = new ArrayList<Asignatura>();

                //dividir por el separador
                String[] ld = linea.split(Global.cadena_separacion_asignaturas);

                //para cada cadena anotar la asignatura
                for (String texto_asignatura : ld) {

                    //Hacemos el trim a la cadena
                    texto_asignatura = texto_asignatura.trim();

                    //Si el texto es igual a la cadena vacía lo obviamos
                    if (!"".equals(texto_asignatura)) {


                        //comprobamos si existe
                        Asignatura asignatura = Asignatura.existentes.get(texto_asignatura);
                        //Si no existe la ignoramos (ya que estamos validando con la lista de entrada)
                        if (asignatura != null) {
                            //Añadimos la asognatura a la lista para la matriculación
                            asignaturas.add(asignatura);
                        }


                    }
                }

                //Aquí están todas las asignaturas añadidas y enlazadas
                Matriculacion matriculacion = new Matriculacion();
                //Montamos la matriculación
                matriculacion.asignaturas = asignaturas;
                //Y la añadimos a la lista
                matriculas.add(matriculacion);

                linea = br.readLine();
            }

            //Aquí tenemos todas las matriculaciones en la lista "matriculas"
            //montamos el objeto de datos de respuesta
            da.matriculaciones = matriculas;



            //------------------------------------------------------------------------------------------------------

        } catch (FileNotFoundException ex) {
            Logger.getLogger(Cargador.class.getName()).log(Level.SEVERE, null, ex);

        } catch (IOException ex) {
            Logger.getLogger(Cargador.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            try {
                fr.close();
            } catch (IOException ex) {
                Logger.getLogger(Cargador.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        return da;
    }
}
