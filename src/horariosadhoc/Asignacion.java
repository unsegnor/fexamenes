/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package horariosadhoc;

/**
 *
 * @author Victor
 */
class Asignacion {
    //Relaciona una asignatura con un número

    Asignatura asignatura = null;
    int numero = -1;

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();

        sb.append("(").append(this.asignatura).append(", ").append(this.numero).append(")");


        return sb.toString();

    }
}
