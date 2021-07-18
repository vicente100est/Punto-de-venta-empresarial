/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ModelClass;

import Connection.Consult;
import Models.*;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import javax.swing.JPanel;
import javax.swing.JTable;
import javax.swing.table.DefaultTableModel;

/**
 *
 * @author AlexJPZ
 */
public class Proveedor extends Consult{

    private DefaultTableModel modelo, modelo2;
    private List<Proveedores> proveedor, proveedor1, proveedor2, proveedorFilter;
    private String sql;
    private Object[] object;

    public List<Proveedores> insertProveedor(String Proveedor, String Email, String Telefono) {
        proveedor1 = proveedores().stream()
                .filter(P -> P.getTelefono().equals(Telefono) || P.getEmail().equals(Email))
                .collect(Collectors.toList());
        if (0 == proveedor1.size()) {
            sql = "INSERT INTO proveedores(Proveedor,Telefono,Email)"
                    + "VALUES(?,?,?)";
            object = new Object[]{Proveedor, Telefono, Email};
            insert(sql, object);
            sql = "INSERT INTO reportes_proveedores(IdProveedor,SaldoActual,FechaActual"
                    + ",UltimoPago,FechaPago)VALUES(?,?,?,?,?)";
            proveedor = proveedores();
            int pos = proveedor.size();
            pos--;
             int idProveedor = proveedor.get(pos).getIdProveedor();
             object = new Object[]{idProveedor, "$0.00", "Sin fecha", "$0.00", "Sin fecha"};
            insert(sql, object);
        }
        return proveedor1;
    }

    public List<Proveedores> getProveedores() {
        return proveedores();
    }

    public void searchProveedores(JTable table, String campo, int num_registro, int reg_por_pagina) {
        String[] registros = new String[4];
        String[] titulos = {"Id", "Proveedor", "Email", "Telefono"};
        modelo = new DefaultTableModel(null, titulos);
        proveedor = proveedores();
        if (campo.equals("")) {
            proveedorFilter = proveedor.stream()
                    .skip(num_registro).limit(reg_por_pagina)
                    .collect(Collectors.toList());
        } else {
            proveedorFilter = proveedor.stream()
                    .filter(P -> P.getProveedor().startsWith(campo) || P.getEmail().startsWith(campo)
                    || P.getTelefono().startsWith(campo))
                    .skip(num_registro).limit(reg_por_pagina)
                    .collect(Collectors.toList());
        }
        proveedorFilter.forEach(item -> {
            registros[0] = String.valueOf(item.getIdProveedor());
            registros[1] = item.getProveedor();
            registros[2] = item.getEmail();
            registros[3] = item.getTelefono();
            modelo.addRow(registros);
        });
        table.setModel(modelo);
        table.setRowHeight(30);
        table.getColumnModel().getColumn(0).setMaxWidth(0);
        table.getColumnModel().getColumn(0).setMinWidth(0);
        table.getColumnModel().getColumn(0).setPreferredWidth(0);
    }

    public DefaultTableModel getModelo() {
        return modelo;
    }

    public DefaultTableModel reportesProveeedor(JTable table, int idProveedor) {
        String[] registros = new String[6];
        String[] titulos = {"Id", "Proveedor", "Saldo Actual",
            "Fecha Actual", "Ultimo Pago", "Fecha Pago"};
        modelo2 = new DefaultTableModel(null, titulos);
        List<Reportes_proveedores> resportes = reportesProveedor(idProveedor);
        resportes.forEach(item -> {
            registros[0] = String.valueOf(item.getIdRegistro());
            registros[1] = item.getProveedor();
            registros[2] = item.getSaldoActual();
            registros[3] = item.getFechaActual();
            registros[4] = item.getUltimoPago();
            registros[5] = item.getFechaPago();
            modelo2.addRow(registros);
        });
        table.setModel(modelo2);
        table.setRowHeight(30);
        table.getColumnModel().getColumn(0).setMaxWidth(0);
        table.getColumnModel().getColumn(0).setMinWidth(0);
        table.getColumnModel().getColumn(0).setPreferredWidth(0);
        return modelo2;
    }

    public List<Proveedores> updateProveedor(int idProveedor, String Proveedor, String Email, String Telefono) {
        proveedor1 = proveedores().stream()
                .filter(P -> P.getTelefono().equals(Telefono))
                .collect(Collectors.toList());
        proveedor2 = proveedores().stream()
                .filter(P -> P.getEmail().equals(Email))
                .collect(Collectors.toList());
        List<Proveedores> listFinal = new ArrayList<Proveedores>();
        listFinal.addAll(proveedor1);
        listFinal.addAll(proveedor2);
        if (2 == listFinal.size()) {
            if (idProveedor == proveedor1.get(0).getIdProveedor()
                    && idProveedor == proveedor2.get(0).getIdProveedor()) {
                update(idProveedor, Proveedor, Email, Telefono);
                listFinal.clear();
            }
        } else {
            if (0 == listFinal.size()) {
                update(idProveedor, Proveedor, Email, Telefono);
                listFinal.clear();
            } else {
                if (0 != proveedor1.size()) {
                    if (idProveedor == proveedor1.get(0).getIdProveedor()) {
                        update(idProveedor, Proveedor, Email, Telefono);
                        listFinal.clear();
                    }
                }
                if (0 != proveedor2.size()) {
                    if (idProveedor == proveedor2.get(0).getIdProveedor()) {
                        update(idProveedor, Proveedor, Email, Telefono);
                        listFinal.clear();
                    }
                }
            }
        }
        return listFinal;
    }

    private void update(int idProveedor, String Proveedor, String Email, String Telefono) {
        sql = "UPDATE proveedores SET Proveedor = ?,Telefono = ?,"
                + "Email = ? WHERE IdProveedor =" + idProveedor;
        Object[] proveedor = new Object[]{Proveedor, Telefono, Email};
        update(sql, proveedor);
    }

    public void deleteProveedor(int idProveedor, int idRegistro) {
        sql = "DELETE FROM reportes_proveedores WHERE IdRegistro LIKE ?";
        delete(sql, idRegistro);
        sql = "DELETE FROM proveedores WHERE IdProveedor LIKE ?";
        delete(sql, idProveedor);
    }

    public void updateRepostes(String saldoActual, String fecha, String pago, int idProveedor) {
        List<Reportes_proveedores> resportes = reportesProveedor(idProveedor);
        int idRegistro = resportes.get(0).getIdRegistro();
        sql = "UPDATE reportes_proveedores SET IdProveedor = ?,SaldoActual = ?,FechaActual = ?"
                + ",UltimoPago = ?,FechaPago = ? WHERE IdRegistro =" + idRegistro;
        Object[] reporte = new Object[]{idProveedor, "$" + saldoActual, fecha, "$" + pago, fecha};
        update(sql, reporte);
    }
   
}
