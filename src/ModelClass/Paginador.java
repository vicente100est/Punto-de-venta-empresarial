/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ModelClass;

import Interfaces.IClassModels;
import javax.swing.JLabel;
import javax.swing.JTable;

/**
 *
 * @author AlexJPZ
 */
public class Paginador extends ListClass implements IClassModels {

    private static int pageSize = 10, tab, maxReg, pageCount;
    private static int num_registro = 0, numPagi = 1, boton;
    private int fun;
    private JTable table;
    private JLabel label;

    public Paginador() {
    }

    public Paginador(int tab, JTable table, JLabel label, int fun) {
        this.tab = tab;
        this.fun = fun;
        this.label = label;
        this.table = table;
        cargarDatos();
    }

    public void cargarDatos() {
        switch (tab) {
            case 0:
                if (fun == 1) {
                    num_registro = 0;
                    numPagi = 1;
                    boton = 0;
                }
                tempoVentas = venta.getTempoVentas();
                venta.searchVentatemp(table, num_registro, pageSize);
                maxReg = tempoVentas.size();
                break;
            case 1:
                if (fun == 1) {
                    num_registro = 0;
                    numPagi = 1;
                    boton = 0;
                }
                numCliente = cliente.getClientes();
                cliente.searchCliente(table, "", num_registro, pageSize);
                maxReg = numCliente.size();
                break;
            case 2:
                if (fun == 1) {
                    num_registro = 0;
                    numPagi = 1;
                    boton = 0;
                }
                numProveedore = proveedor.getProveedores();
                proveedor.searchProveedores(table, "", num_registro, pageSize);
                maxReg = numProveedore.size();
                break;
            case 4:
                if (fun == 1) {
                    num_registro = 0;
                    numPagi = 1;
                    boton = 0;
                }
                productos = producto.producto();
                producto.searchProductos(table, "", num_registro, pageSize);
                maxReg = productos.size();
                break;
            case 5:
                if (fun == 1) {
                    num_registro = 0;
                    numPagi = 1;
                    boton = 0;
                }
                numTempoCompras = compra.getTempoCompras();
                compra.searchCompras(table, "", num_registro, pageSize);
                maxReg = numTempoCompras.size();
                break;
            case 8:
                if (fun == 1) {
                    num_registro = 0;
                    numPagi = 1;
                    boton = 0;
                }
                //listUsuario = ususrios.getUsuarios();
                ususrios.searchUsuarios(table, "", num_registro, pageSize);
                maxReg = ususrios.getUsuarios().size();
                break;
            case 9:
                if (fun == 1) {
                    num_registro = 0;
                    numPagi = 1;
                    boton = 0;
                }
                //listUsuario = ususrios.getUsuarios();
                inventario.getBodega("", num_registro, pageSize);
                maxReg = inventario.getInvBodega().size();
                break;
            case 10:
                if (fun == 1) {
                    num_registro = 0;
                    numPagi = 1;
                    boton = 0;
                }
                //listUsuario = ususrios.getUsuarios();
                inventario.getProductos("", num_registro, pageSize);
                maxReg = producto.producto().size();
                break;
            case 11:
                if (fun == 1) {
                    num_registro = 0;
                    numPagi = 1;
                    boton = 0;
                }
                //listUsuario = ususrios.getUsuarios();

                maxReg = inventario.searchVentas("", num_registro, pageSize);
                break;
        }
        pageCount = (maxReg / pageSize);
        //Ajuste el número de la página si la última página contiene una parte de la página.
        if ((maxReg % pageSize) > 0) {
            pageCount += 1;
        }

        label.setText("Paginas " + "1" + "/ " + String.valueOf(pageCount));
    }

    public void primero() {
        numPagi = 0;
        label.setText("Paginas " + "1" + "/ " + String.valueOf(pageCount));
        switch (tab) {
            case 0:
                venta.searchVentatemp(table, numPagi, pageSize);
                break;
            case 1:
                cliente.searchCliente(table, "", numPagi, pageSize);
                break;
            case 2:
                proveedor.searchProveedores(table, "", numPagi, pageSize);
                break;
            case 4:
                producto.searchProductos(table, "", numPagi, pageSize);
                break;
            case 5:
                compra.searchCompras(table, "", numPagi, pageSize);
                break;
            case 8:
                ususrios.searchUsuarios(table, "", numPagi, pageSize);
                break;
            case 9:
                inventario.getBodega("", numPagi, pageSize);
                break;
            case 10:
                inventario.getProductos("", numPagi, pageSize);
                break;
            case 11:
                inventario.searchVentas("", numPagi, pageSize);
                break;
        }

        boton = 1;
    }

    public void anterior() {
        if (pageCount != 1 || boton == 1) {
            if (numPagi > 0) {
                if (boton == 3 || boton == 4) {
                    numPagi -= 1;

                }

                label.setText("Paginas " + String.valueOf(numPagi)
                        + "/ " + String.valueOf(pageCount));
                numPagi -= 1;
                num_registro = pageSize * numPagi;
                switch (tab) {
                    case 0:
                        venta.searchVentatemp(table, num_registro, pageSize);
                        break;
                    case 1:
                        cliente.searchCliente(table, "", num_registro, pageSize);
                        break;
                    case 2:
                        proveedor.searchProveedores(table, "", num_registro, pageSize);
                        break;
                    case 4:
                        producto.searchProductos(table, "", num_registro, pageSize);
                        break;
                    case 5:
                        compra.searchCompras(table, "", num_registro, pageSize);
                        break;
                    case 8:
                        ususrios.searchUsuarios(table, "", num_registro, pageSize);
                        break;
                    case 9:
                        inventario.getBodega("", num_registro, pageSize);
                        break;
                    case 10:
                        inventario.getProductos("", num_registro, pageSize);
                        break;
                    case 11:
                        inventario.searchVentas("", num_registro, pageSize);
                        break;
                }
                boton = 2;
            }
        }
    }

    public void siguiente() {
        if (pageCount != 1) {
            if (numPagi < pageCount) {
                if (boton == 2 || boton == 1) {
                    if (numPagi == 0) {
                        numPagi += 1;
                    } else {
                        if (numPagi >= 1) {
                            numPagi += 1;
                        }
                    }
                }
                boton = 3;
                num_registro = pageSize * numPagi;
                switch (tab) {
                    case 0:
                        venta.searchVentatemp(table, num_registro, pageSize);
                        break;
                    case 1:
                        cliente.searchCliente(table, "", num_registro, pageSize);
                        break;
                    case 2:
                        proveedor.searchProveedores(table, "", num_registro, pageSize);
                        break;
                    case 4:
                        producto.searchProductos(table, "", num_registro, pageSize);
                        break;
                    case 5:
                        compra.searchCompras(table, "", num_registro, pageSize);
                        break;
                    case 8:
                        ususrios.searchUsuarios(table, "", num_registro, pageSize);
                        break;
                    case 9:
                        inventario.getBodega("", num_registro, pageSize);
                        break;
                    case 10:
                        inventario.getProductos("", num_registro, pageSize);
                        break;
                    case 11:
                        inventario.searchVentas("", num_registro, pageSize);
                        break;
                }
                numPagi += 1;
                label.setText("Paginas " + String.valueOf(numPagi)
                        + "/ " + String.valueOf(pageCount));
            } else {
                label.setText("Paginas " + String.valueOf(pageCount)
                        + "/ " + String.valueOf(pageCount));
            }
        }
    }

    public void ultimo() {
        numPagi = pageCount;
        numPagi--;
        num_registro = pageSize * numPagi;
        label.setText("Paginas " + String.valueOf(pageCount)
                + "/ " + String.valueOf(pageCount));
        switch (tab) {
            case 0:
                venta.searchVentatemp(table, num_registro, pageSize);
                break;
            case 1:
                cliente.searchCliente(table, "", num_registro, pageSize);
                break;
            case 2:
                proveedor.searchProveedores(table, "", num_registro, pageSize);
                break;
            case 4:
                producto.searchProductos(table, "", num_registro, pageSize);
                break;
            case 5:
                compra.searchCompras(table, "", num_registro, pageSize);
                break;
            case 8:
                ususrios.searchUsuarios(table, "", num_registro, pageSize);
                break;
            case 9:
                inventario.getBodega("", num_registro, pageSize);
                break;
            case 10:
                inventario.getProductos("", num_registro, pageSize);
                break;
            case 11:
                inventario.searchVentas("", num_registro, pageSize);
                break;
        }
        numPagi = pageCount;
        boton = 4;
    }
}
