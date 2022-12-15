/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package sistematransaccional;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 *
 * @author Santiago
 */
public class Cuenta {

    private double saldo;
    private final HashMap<Integer, Dinero> transacciones;

    public Cuenta() {
        saldo = 0d;
        transacciones = new HashMap<>();
    }

    public double getSaldo() {
        return saldo;
    }

    public void setSaldo(double saldo) {
        this.saldo = saldo;
    }

    /**
     * Funcion para agregar un ingreso al mapa de transacciones
     * 
     * @param id ID de la transaccion
     * @param ingreso Objeto de Ingreso a Agregar
     * @return Dinero correspondiente al saldo actual de este objeto Cuenta
     */
    public double agregarIngreso(int id, Ingreso ingreso) {
        transacciones.put(id, ingreso);
        saldo+=ingreso.getCantidad();
        return saldo;
    }

    /**
     * Funcion para agregar un ingreso al mapa de transacciones
     *
     * @param id ID de la transaccion
     * @param egreso Objeto de Egreso a Agregar
     * @return Dinero correspondiente al saldo actual de este objeto Cuenta
     */
    public double agregarEgreso(int id, Egreso egreso) {
        transacciones.put(id, egreso);   
        saldo-=egreso.getCantidad();
        return saldo;
    }

    public HashMap<Integer, Dinero> getTransacciones() {
        return transacciones;
    }

    /**
     * Esta función valida las transacciones que existen en el objeto y
     * actualiza el valor de saldo, Es importante ya que hace las operaciones de
     * sumar ingresos y restar egresos
     *
     * @return
     */
    public double validarSaldo() {
        saldo = getSumaDeIngresos() - getSumaDeEgresos();
        return saldo;
    }

    /**
     * Crea una lista con los Ingresos actuales de esta Cuenta
     *
     * @return
     */
    public List<Ingreso> getListaDeIngresos() {
        List<Ingreso> listaIngresos = new ArrayList<>();
        transacciones.forEach((id, dinero) -> {
            if (dinero.getTipo() == Dinero.TIPO_INGRESO) {
                listaIngresos.add((Ingreso) dinero);
            }
        });
        return listaIngresos;
    }

    /**
     * Crea una lista con los Egresos actuales de esta Cuenta
     *
     * @return
     */
    public List<Egreso> getListaDeEgresos() {
        List<Egreso> listaEgresos = new ArrayList<>();
        transacciones.forEach((id, dinero) -> {
            if (dinero.getTipo() == Dinero.TIPO_EGRESO) {
                listaEgresos.add((Egreso) dinero);
            }
        });
        return listaEgresos;
    }

    /**
     * Suma todos los ingresos de la Cuenta
     *
     * @return Double con cantidad correspondiente a la suma de todos los Ingresos
     */
    public double getSumaDeIngresos() {
        return transacciones.entrySet().
                stream().filter((entry) -> (entry.getValue().getTipo() == Dinero.TIPO_INGRESO))
                .map((entry) -> entry.getValue().getCantidad())
                .reduce(0d, (accumulator, _item) -> accumulator + _item);
    }
    
    /**
     * Suma todos los egresos de la Cuenta
     *
     * @return  Double con cantidad correspondiente a la suma de todos los Egresos
     */
    public double getSumaDeEgresos() {
        return transacciones.entrySet().
                stream().filter((entry) -> (entry.getValue().getTipo() == Dinero.TIPO_EGRESO))
                .map((entry) -> entry.getValue().getCantidad())
                .reduce(0d, (accumulator, _item) -> accumulator + _item);
    }

}
