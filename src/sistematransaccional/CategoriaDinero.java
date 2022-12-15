/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package sistematransaccional;

import java.util.ArrayList;
import java.util.List;
import utilidades.ComboItem;

/**
 *
 * @author Santiago
 * Esta clase tiene las categorias de ingresos y egresos registrados en el software
 */
public class CategoriaDinero {
    
    public static final byte INGRESO_SUELDO_MENSUAL = 0;
    public static final byte INGRESO_PAGO_OCASIONAL = 1;
    public static final byte INGRESO_GANANCIA_NEGOCIO = 2;
    public static final byte INGRESO_VENTA_PRODUCTO = 3;
    public static final byte INGRESO_OTROS_INGRESOS = 4;
    public static final byte EGRESO_PAGO_RECIBOS_PUBLICOS = 5;
    public static final byte EGRESO_COMPRA_PRODUCTO = 6;
    public static final byte EGRESO_INVERSION = 7;
    public static final byte EGRESO_PAGO_DEUDA = 8;
    public static final byte EGRESO_OTROS_EGRESOS = 9;
    public static final byte EGRESO_PAGO_EMPLEADOS = 10;

    
    public static List<ComboItem> getListaIngresosComboItem(){
        List<ComboItem> listaIngresosComboItem = new ArrayList<>();
        listaIngresosComboItem.add(new ComboItem("Sueldo Mensual",INGRESO_SUELDO_MENSUAL));
        listaIngresosComboItem.add(new ComboItem("Pago Ocasional",INGRESO_PAGO_OCASIONAL));
        listaIngresosComboItem.add(new ComboItem("Ganancia Negocio",INGRESO_GANANCIA_NEGOCIO));
        listaIngresosComboItem.add(new ComboItem("Venta Producto",INGRESO_VENTA_PRODUCTO));
        listaIngresosComboItem.add(new ComboItem("Otros Ingresos",INGRESO_OTROS_INGRESOS));
        return listaIngresosComboItem;
    }
    public static List<ComboItem> getListaEgresosComboItem(){
        List<ComboItem> listaEgresosComboItem = new ArrayList<>();
        listaEgresosComboItem.add(new ComboItem("Pago recibo publico",EGRESO_PAGO_RECIBOS_PUBLICOS));
        listaEgresosComboItem.add(new ComboItem("Compra de Producto",EGRESO_COMPRA_PRODUCTO));
        listaEgresosComboItem.add(new ComboItem("Inversion",EGRESO_INVERSION));
        listaEgresosComboItem.add(new ComboItem("Pago de deuda",EGRESO_PAGO_DEUDA));
        listaEgresosComboItem.add(new ComboItem("Otros egresos",EGRESO_OTROS_EGRESOS));
        listaEgresosComboItem.add(new ComboItem("Pago a empleados",EGRESO_PAGO_EMPLEADOS));
        return listaEgresosComboItem;
    }
}
