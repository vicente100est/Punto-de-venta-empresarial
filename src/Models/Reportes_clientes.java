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
public class Reportes_clientes  extends Clientes{
     private int IdRegistro;
     private int IdCliente;
     private String SaldoActual;
     private String FechaActual;
     private String UltimoPago;
     private String FechaPago;
     private String ID;

    public int getIdRegistro() {
        return IdRegistro;
    }

    public void setIdRegistro(int IdRegistro) {
        this.IdRegistro = IdRegistro;
    }

    public int getIdCliente() {
        return IdCliente;
    }

    public void setIdCliente(int IdCliente) {
        this.IdCliente = IdCliente;
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

    public String getID() {
        return ID;
    }

    public void setID(String ID) {
        this.ID = ID;
    }
     
     
}
