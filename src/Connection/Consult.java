/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Connection;

import Models.*;
import java.sql.SQLException;
import java.util.List;
import javax.swing.JOptionPane;
import org.apache.commons.dbutils.QueryRunner;
import org.apache.commons.dbutils.handlers.BeanListHandler;
import org.apache.commons.dbutils.handlers.ColumnListHandler;

/**
 *
 * @author AlexJPZ
 */
public class Consult extends Conexion {

    private QueryRunner QR = new QueryRunner();

    public List<Clientes> clientes() {
        try {
            numCliente = (List<Clientes>) QR.query(getConn(), "SELECT * FROM Clientes",
                    new BeanListHandler(Clientes.class));
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(null, "Error : " + ex);
        }
        return numCliente;
    }

    public void insert(String sql, Object[] data) {
        try {
            final QueryRunner qr = new QueryRunner(true);
            qr.insert(getConn(), sql, new ColumnListHandler(), data);
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(null, ex);
        }
    }

    public List<Reportes_clientes> reportesClientes(int idCliente, int funcion) {
        String where = "";
        String condicion = " clientes.IdCliente = reportes_clientes.IdCliente ";
        String campos = " clientes.IdCliente,clientes.ID,clientes.Nombre,clientes.Apellido,"
                + "reportes_clientes.IdRegistro,reportes_clientes.SaldoActual,"
                + "reportes_clientes.FechaActual,reportes_clientes.UltimoPago,reportes_clientes.FechaPago ";
        if (funcion == 1) {
            where = " WHERE reportes_clientes.IdCliente =" + idCliente;
        }
        try {
            reportes_clientes = (List<Reportes_clientes>) QR.query(getConn(),
                    "SELECT " + campos + " FROM reportes_clientes Inner Join clientes ON "
                    + condicion + where, new BeanListHandler(Reportes_clientes.class));
        } catch (SQLException ex) {
            System.out.println("Error : " + ex);
        }
        return reportes_clientes;
    }

    public void update(String sql, Object[] data) {
        try {
            final QueryRunner qr = new QueryRunner(true);
            qr.update(getConn(), sql, data);
        } catch (SQLException ex) {
            System.out.println("Error : " + ex);
        }
    }

    public void delete(String sql, int id) {
        try {
            final QueryRunner qr = new QueryRunner(true);
            if (0 < id) {
                qr.update(getConn(), sql, "%" + id + "%");
            } else {
                qr.update(getConn(), sql);
            }
        } catch (SQLException ex) {
            System.out.println("Error : " + ex);
        }

    }

    public List<Proveedores> proveedores() {
        try {
            numProveedore = (List<Proveedores>) QR.query(getConn(), "SELECT * FROM proveedores",
                    new BeanListHandler(Proveedores.class));
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(null, "Error : " + ex);
        }
        return numProveedore;
    }

    public List<Reportes_proveedores> reportesProveedor(int idProveedor) {
        String condicion = " proveedores.IdProveedor = reportes_proveedores.IdProveedor ";
        String campos = " proveedores.IdProveedor,proveedores.Proveedor,"
                + "reportes_proveedores.IdRegistro,reportes_proveedores.SaldoActual,"
                + "reportes_proveedores.FechaActual,reportes_proveedores.UltimoPago,"
                + "reportes_proveedores.FechaPago ";
        try {
            reportes_proveedores = (List<Reportes_proveedores>) QR.query(getConn(),
                    "SELECT " + campos + " FROM reportes_proveedores Inner Join proveedores ON "
                    + condicion + " WHERE reportes_proveedores.IdProveedor ="
                    + idProveedor, new BeanListHandler(Reportes_proveedores.class));
        } catch (SQLException ex) {
            System.out.println("Error : " + ex);
        }
        return reportes_proveedores;
    }

    public List<Usuarios> usuarios() {
        try {
            listUsuario = (List<Usuarios>) QR.query(getConn(), "SELECT * FROM usuarios",
                    new BeanListHandler(Usuarios.class));
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(null, "Error : " + ex);
        }
        return listUsuario;
    }

    public List<Cajas> cajas() {
        try {
            listCaja = (List<Cajas>) QR.query(getConn(), "SELECT * FROM cajas",
                    new BeanListHandler(Cajas.class));
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(null, "Error : " + ex);
        }
        return listCaja;
    }

    public List<Departamentos> departamentos() {
        try {
            departamento = (List<Departamentos>) QR.query(getConn(), "SELECT * FROM departamentos",
                    new BeanListHandler(Departamentos.class));
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(null, "Error : " + ex);
        }
        return departamento;
    }

    public List<Categorias> categorias() {
        try {
            categorias = (List<Categorias>) QR.query(getConn(), "SELECT * FROM categorias",
                    new BeanListHandler(Categorias.class));
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(null, "Error : " + ex);
        }
        return categorias;
    }

    public List<Tempo_compras> tempoCompras() {
        try {
            numTempoCompras = (List<Tempo_compras>) QR.query(getConn(), "SELECT * FROM tempo_compras",
                    new BeanListHandler(Tempo_compras.class));
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(null, "Error : " + ex);
        }
        return numTempoCompras;
    }

    public List<Reportes_proveedores> reportesProveedores() {
        String condicion = " proveedores.IdProveedor = reportes_proveedores.IdProveedor ";
        String campos = " proveedores.IdProveedor,proveedores.Proveedor,proveedores.Telefono,"
                + "proveedores.Email,reportes_proveedores.IdRegistro,reportes_proveedores.SaldoActual ";
        try {
            reportes_proveedores = (List<Reportes_proveedores>) QR.query(getConn(),
                    "SELECT " + campos + " FROM reportes_proveedores Inner Join proveedores ON "
                    + condicion, new BeanListHandler(Reportes_proveedores.class));
        } catch (SQLException ex) {
            System.out.println("Error : " + ex);
        }
        return reportes_proveedores;
    }

    public List<Cajas_ingresos> cajasIngresos() {
        try {
            cajasIngresos = (List<Cajas_ingresos>) QR.query(getConn(), "SELECT * FROM cajas_ingresos",
                    new BeanListHandler(Cajas_ingresos.class));
        } catch (SQLException ex) {
            System.out.println("Error : " + ex);
        }
        return cajasIngresos;
    }

    public List<Compras> compras() {
        try {
            compras = (List<Compras>) QR.query(getConn(), "SELECT * FROM compras",
                    new BeanListHandler(Compras.class));
        } catch (SQLException ex) {
            System.out.println("Error : " + ex);
        }
        return compras;
    }

    public List<Tempo_productos> tempoProductos() {
        String condicion = " tempo_productos.IdCompra = compras.IdCompra ";
        String campos = " tempo_productos.IdCompra,compras.Producto,compras.Cantidad,compras.Precio,"
                + "compras.Importe,compras.Proveedor,compras.Fecha,compras.Codigo ";
        try {
            tempo_productos = (List<Tempo_productos>) QR.query(getConn(),
                    "SELECT " + campos + " FROM tempo_productos Inner Join compras ON "
                    + condicion, new BeanListHandler(Tempo_productos.class));
        } catch (SQLException ex) {
            System.out.println("Error : " + ex);
        }
        return tempo_productos;
    }

    public List<Productos> productos() {
        try {
            productos = (List<Productos>) QR.query(getConn(), "SELECT * FROM productos",
                    new BeanListHandler(Productos.class));
        } catch (SQLException ex) {
            System.out.println("Error : " + ex);
        }
        return productos;
    }

    public List<Bodega> bodega() {
        try {
            bodega = (List<Bodega>) QR.query(getConn(), "SELECT * FROM bodega",
                    new BeanListHandler(Bodega.class));
        } catch (SQLException ex) {
            System.out.println("Error : " + ex);
        }
        return bodega;
    }

    public List<Tempo_ventas> TempoVentas() {
        try {
            tempoVentas = (List<Tempo_ventas>) QR.query(getConn(), "SELECT * FROM tempo_ventas",
                    new BeanListHandler(Tempo_ventas.class));
        } catch (SQLException ex) {
            System.out.println("Error : " + ex);
        }
        return tempoVentas;
    }

    public List<Roles> roles() {
        try {
            roles = (List<Roles>) QR.query(getConn(), "SELECT * FROM roles",
                    new BeanListHandler(Roles.class));
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(null, "Error : " + ex);
        }
        return roles;
    }

    public List<Cajas_registros> cajas_registros() {
        try {
            cajas_registros = (List<Cajas_registros>) QR.query(getConn(), "SELECT * FROM cajas_registros",
                    new BeanListHandler(Cajas_registros.class));
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(null, "Error : " + ex);
        }
        return cajas_registros;
    }

    public List<Productos> getBodega() {
        String condicion = " bodega.Codigo = productos.Codigo ";
        String campos = " bodega.Id,productos.Codigo,productos.Producto,bodega.Existencia,"
                + "bodega.Fecha ";
        try {
            productos = (List<Productos>) QR.query(getConn(),
                    "SELECT " + campos + " FROM bodega Inner Join productos ON "
                    + condicion, new BeanListHandler(Productos.class));
        } catch (SQLException ex) {
            System.out.println("Error : " + ex);
        }
        return productos;
    }

    public List<Ventas> ventas() {
        try {
            ventas = (List<Ventas>) QR.query(getConn(), "SELECT * FROM ventas",
                    new BeanListHandler(Ventas.class));
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(null, "Error : " + ex);
        }
        return ventas;
    }
}
