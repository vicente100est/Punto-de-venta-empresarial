/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Models;

/**
 *
 * @author AJPDHN
 */
public class Rventas {
    private final String Codigo;
    private final String Descripcion;
    private final String PrecioVenta;
    private final int Cantidad;
    private final String PrecioCompra;
    private final String Ganancias;

    public Rventas(String Codigo, String Descripcion, String PrecioVenta, int Cantidad, String PrecioCompra, String Ganancias) {
        this.Codigo = Codigo;
        this.Descripcion = Descripcion;
        this.PrecioVenta = PrecioVenta;
        this.Cantidad = Cantidad;
        this.PrecioCompra = PrecioCompra;
        this.Ganancias = Ganancias;
    }

    public String getCodigo() {
        return Codigo;
    }

    public String getDescripcion() {
        return Descripcion;
    }

    public String getPrecioVenta() {
        return PrecioVenta;
    }

    public int getCantidad() {
        return Cantidad;
    }

    public String getPrecioCompra() {
        return PrecioCompra;
    }

    public String getGanancias() {
        return Ganancias;
    }
    
}
