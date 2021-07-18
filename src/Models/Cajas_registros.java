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
public class Cajas_registros {

    private int IdCajaTempo;
    private int IdUsuario;
    private String Nombre;
    private String Apellido;
    private String Usuario;
    private String Role;
    private int IdCaja;
    private int Caja;
    private boolean Estado;
    private String Hora;
    private String Fecha;

    public int getIdCajaTempo() {
        return IdCajaTempo;
    }

    public void setIdCajaTempo(int IdCajaTempo) {
        this.IdCajaTempo = IdCajaTempo;
    }

    public int getIdUsuario() {
        return IdUsuario;
    }

    public void setIdUsuario(int IdUsuario) {
        this.IdUsuario = IdUsuario;
    }

    public String getNombre() {
        return Nombre;
    }

    public void setNombre(String Nombre) {
        this.Nombre = Nombre;
    }

    public String getApellido() {
        return Apellido;
    }

    public void setApellido(String Apellido) {
        this.Apellido = Apellido;
    }

    public String getUsuario() {
        return Usuario;
    }

    public void setUsuario(String Usuario) {
        this.Usuario = Usuario;
    }

    public String getRole() {
        return Role;
    }

    public void setRole(String Role) {
        this.Role = Role;
    }

    public int getIdCaja() {
        return IdCaja;
    }

    public void setIdCaja(int IdCaja) {
        this.IdCaja = IdCaja;
    }

    public int getCaja() {
        return Caja;
    }

    public void setCaja(int Caja) {
        this.Caja = Caja;
    }

    public boolean isEstado() {
        return Estado;
    }

    public void setEstado(boolean Estado) {
        this.Estado = Estado;
    }

    public String getHora() {
        return Hora;
    }

    public void setHora(String Hora) {
        this.Hora = Hora;
    }

    public String getFecha() {
        return Fecha;
    }

    public void setFecha(String Fecha) {
        this.Fecha = Fecha;
    }

}
