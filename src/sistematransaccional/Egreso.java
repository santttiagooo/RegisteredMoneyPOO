/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package sistematransaccional;

/**
 *
 * @author Santiago
 */
public class Egreso extends Dinero {
    public Egreso() {
        super(0, "", Dinero.TIPO_EGRESO);
    }
    public Egreso(double cantidad, String descripcion) {
        super(cantidad, descripcion, Dinero.TIPO_EGRESO);
    }
}
