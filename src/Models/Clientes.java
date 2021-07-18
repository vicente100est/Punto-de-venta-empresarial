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
public class Clientes {
     private int IdCliente;
     private String ID;
     private String Nombre;
     private String Apellido;
     private String Direccion;
     private String Telefono;

    public Clientes() {
    }

//    public Clientes(int IdCliente, String ID, String Nombre, String Apellido, String Direccion, String Telefono) {
//        this.IdCliente = IdCliente;
//        this.ID = ID;
//        this.Nombre = Nombre;
//        this.Apellido = Apellido;
//        this.Direccion = Direccion;
//        this.Telefono = Telefono;
//    }

    public int getIdCliente() {
        return IdCliente;
    }

    public void setIdCliente(int IdCliente) {
        this.IdCliente = IdCliente;
    }

    public String getID() {
        return ID;
    }

    public void setID(String ID) {
        this.ID = ID;
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

    public String getDireccion() {
        return Direccion;
    }

    public void setDireccion(String Direccion) {
        this.Direccion = Direccion;
    }

    public String getTelefono() {
        return Telefono;
    }

    public void setTelefono(String Telefono) {
        this.Telefono = Telefono;
    }
     
}
