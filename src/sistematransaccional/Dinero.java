/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package sistematransaccional;

import java.time.LocalDateTime;

/**
 *
 * @author Santiago
 */

public class Dinero {
    
    public static final byte TIPO_INGRESO = 1;
    public static final byte TIPO_EGRESO = 0;
    public static final byte TIPO_SALDO = 2;
  
    private double cantidad;
    private String descripcion;
    private byte tipo;
    private LocalDateTime fecha;
    public Dinero(double cantidad, String descripcion, byte tipo) {
        this.cantidad = cantidad;
        this.descripcion = descripcion;
        this.tipo = tipo;
    }
    public double getCantidad() {
        return cantidad;
    }
    public void setCantidad(double cantidad) {
        this.cantidad = cantidad;
    }
    public String getDescripcion() {
        return descripcion;
    }
    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }
    public byte getTipo() {
        return tipo;
    }
    public String getTipoString(){
        if(this.esIngreso()){
            return "Ingreso";
        }else if(this.esEgreso()){
            return "Egreso";
        }else if(this.esSaldo()){
            return "Saldo";
        }else{
            return "SinTipo";
        }
    }
    private void setTipo(byte tipo) {
        this.tipo = tipo;
    }  
    public boolean esIngreso(){
        return (tipo == Dinero.TIPO_INGRESO);
    }
    public boolean esEgreso(){
        return (tipo == Dinero.TIPO_EGRESO);
    
    }
    public boolean esSaldo(){
        return (tipo == Dinero.TIPO_SALDO);
    
    }
    public LocalDateTime getFecha() {
        return fecha;
    }
    public void setFecha(LocalDateTime fecha) {
        this.fecha = fecha;
    }
    @Override
    public String toString(){
        return "Dinero Tipo: " + (esIngreso()?"Ingreso":"Egreso")
                + "\nCantidad: $" + Double.toString(getCantidad())
                + "\nDescripcion: " + getDescripcion();
    }
    
}
