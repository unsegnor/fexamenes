/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package horariosadhoc;

import java.util.HashMap;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 *
 * @author Victor
 */
public class UtilesTest {
    
    public UtilesTest() {
    }

    /**
     * Test of guardarArchivo method, of class Utiles.
     */
    //@Test
    public void testGuardarArchivo() {
        System.out.println("guardarArchivo");
        String nombre = "";
        String contenido = "";
        Utiles.guardarArchivo(nombre, contenido);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of montarFG method, of class Utiles.
     */
    //@Test
    public void testMontarFG() {
        System.out.println("montarFG");
        DatosMatriculas dm = null;
        FrequenceGraph expResult = null;
        FrequenceGraph result = Utiles.montarFG(dm);
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of imprimir method, of class Utiles.
     */
    //@Test
    public void testImprimir() {
        System.out.println("imprimir");
        FrequenceGraph fg = null;
        String expResult = "";
        String result = Utiles.imprimir(fg);
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of imprimir_arbol method, of class Utiles.
     */
    //@Test
    public void testImprimir_arbol_Nodo() {
        System.out.println("imprimir_arbol");
        Nodo padre = null;
        String expResult = "";
        String result = Utiles.imprimir_arbol(padre);
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of imprimir_arbol method, of class Utiles.
     */
    //@Test
    public void testImprimir_arbol_3args() {
        System.out.println("imprimir_arbol");
        Nodo padre = null;
        String historia = "";
        boolean ultimo = false;
        String expResult = "";
        String result = Utiles.imprimir_arbol(padre, historia, ultimo);
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of dositemsets method, of class Utiles.
     */
    //@Test
    public void testDositemsets() {
        System.out.println("dositemsets");
        FrequenceGraph fg = null;
        HashMap expResult = null;
        HashMap result = Utiles.dositemsets(fg);
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of BL method, of class Utiles.
     */
    //@Test
    public void testBL() {
        System.out.println("BL");
        Solucion s = null;
        HashMap<ParAsig, Integer> afectados = null;
        int[] tiempos = null;
        Solucion expResult = null;
        Solucion result = Utiles.BL(s, afectados, tiempos);
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of BLprimerMejor method, of class Utiles.
     */
    //@Test
    public void testBLprimerMejor_3args_1() {
        System.out.println("BLprimerMejor");
        Solucion s = null;
        HashMap<ParAsig, Integer> afectados = null;
        int[] tiempos = null;
        Solucion expResult = null;
        Solucion result = Utiles.BLprimerMejor(s, afectados, tiempos);
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of BLprimerMejor method, of class Utiles.
     */
    //@Test
    public void testBLprimerMejor_3args_2() {
        System.out.println("BLprimerMejor");
        Sol s = null;
        HashMap<ParAsig, Integer> afectados = null;
        int[] tiempos = null;
        Sol expResult = null;
        Sol result = Utiles.ALprimerMejor(s, afectados, tiempos);
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of calcularHuecos method, of class Utiles.
     */
    @Test
    public void testCalcularHuecos() {
        System.out.println("calcularHuecos");
        int[] tiempos = {1,1,1,2,3,3,2,1};
        int[] expResult = {0,1,2,3,5,8,11,13,14};
        int[] result = Utiles.calcularHuecos(tiempos);
        assertArrayEquals(expResult, result);
    }
}