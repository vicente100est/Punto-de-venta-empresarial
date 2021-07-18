/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Models;

/**
 *
 * @author AlexJPZ
 */
public class Reportes_proveedores extends Proveedores{
     private int IdRegistro;
     private int IdProveedor;
     private String SaldoActual;
     private String FechaActual;
     private String UltimoPago;
     private String FechaPago;

    public int getIdRegistro() {
        return IdRegistro;
    }

    public void setIdRegistro(int IdRegistro) {
        this.IdRegistro = IdRegistro;
    }

    public int getIdProveedor() {
        return IdProveedor;
    }

    public void setIdProveedor(int IdProveedor) {
        this.IdProveedor = IdProveedor;
    }

    public String getSaldoActual() {
        return SaldoActual;
    }

    public void setSaldoActual(String SaldoActual) {
        this.SaldoActual = SaldoActual;
    }

    public String getFechaActual() {
        return FechaActual;
    }

    public void setFechaActual(String FechaActual) {
        this.FechaActual = FechaActual;
    }

    public String getUltimoPago() {
        return UltimoPago;
    }

    public void setUltimoPago(String UltimoPago) {
        this.UltimoPago = UltimoPago;
    }

    public String getFechaPago() {
        return FechaPago;
    }

    public void setFechaPago(String FechaPago) {
        this.FechaPago = FechaPago;
    }
     
}
