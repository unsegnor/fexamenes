/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package horariosadhoc;

/**
 * Par de asignaturas
 *
 * @author Victor
 */
public class ParAsig {

    Asignatura a;
    Asignatura b;

    
    
    public ParAsig(){}
    
    public ParAsig(Asignatura a, Asignatura b){
        this.a = a;
        this.b = b;
    }
    
    @Override
   public int hashCode(){
       int numa = a.ID.hashCode();
       int numb = b.ID.hashCode();
       
       return ((numa+numb)*(numa+numb+1)+numb)>>2;
   }
    
    @Override
    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        }
        if (obj == this) {
            return true;
        }
        if (obj.getClass() != getClass()) {
            return false;
        }
        
        ParAsig pa = (ParAsig) obj;
        return (pa.a == this.a && pa.b == this.b);
                //|| pa.b == this.a && pa.a == this.b);
    }
}
