/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ModelClass;

import Connection.Consult;
import Models.*;
import java.util.List;
import java.util.stream.Collectors;
import javax.swing.JPanel;
import javax.swing.JTable;
import javax.swing.table.DefaultTableModel;

/**
 *
 * @author AlexJPZ
 */
public class Cliente extends Consult {

    //private Consult consult = new Consult();
    private DefaultTableModel modelo, modelo2;
    private List<Clientes> cliente, clienteFilter;
    private int IdCliente;
    private String Id;
    private String sql;
    private Object[] obect;

    public boolean insertCliente(String ID, String Nombre, String Apellido,
            String Direccion, String Telefono) {
        boolean valor = false;
        cliente = clientes();
        clienteFilter = cliente.stream()
                .filter(c -> c.getID().equals(ID))
                .collect(Collectors.toList());
        if (0 == clienteFilter.size()) {

            sql = "INSERT INTO clientes(ID,Nombre,Apellido,Direccion,Telefono)"
                    + "VALUES(?,?,?,?,?)";
            obect = new Object[]{ID, Nombre, Apellido, Direccion, Telefono};
            insert(sql, obect);
            cliente = clientes();
            cliente.forEach(item -> {
                IdCliente = item.getIdCliente();
                Id = item.getID();
            });
            sql = "INSERT INTO reportes_clientes(IdCliente,SaldoActual,FechaActual,"
                    + "UltimoPago,FechaPago,ID)"
                    + "VALUES(?,?,?,?,?,?)";
            obect = new Object[]{IdCliente, "$0.00", "Sin fecha", "$0.00", "Sin fecha", Id};
            insert(sql, obect);
            valor = true;
        }
        return valor;
    }

    public List<Clientes> getClientes() {
        return clientes();
    }

    public void searchCliente(JTable table, String campo, int num_registro, int reg_por_pagina) {
        String[] registros = new String[6];
        String[] titulos = {"Id", "ID", "Nombre", "Apellido", "Direccion", "Telefono"};
        modelo = new DefaultTableModel(null, titulos);
        cliente = clientes();
        if (campo.equals("")) {
            clienteFilter = cliente.stream()
                    .skip(num_registro).limit(reg_por_pagina)
                    .collect(Collectors.toList());
        } else {
            clienteFilter = cliente.stream()
                    .filter(C -> C.getID().startsWith(campo) || C.getNombre().startsWith(campo)
                    || C.getApellido().startsWith(campo))
                    .skip(num_registro).limit(reg_por_pagina)
                    .collect(Collectors.toList());
        }
        clienteFilter.forEach(item -> {
            registros[0] = String.valueOf(item.getIdCliente());
            registros[1] = item.getID();
            registros[2] = item.getNombre();
            registros[3] = item.getApellido();
            registros[4] = item.getDireccion();
            registros[5] = item.getTelefono();
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

    public DefaultTableModel reportesCliente(JTable table, int idCliente) {
        String[] registros = new String[7];
        String[] titulos = {"Id", "Nombre", "Apellido", "Saldo Actual", "Fecha Actual", "Ultimo Pago", "Fecha Pago"};
        modelo2 = new DefaultTableModel(null, titulos);
        List<Reportes_clientes> resportes = reportesClientes(idCliente,1);
        resportes.forEach(item -> {
            registros[0] = String.valueOf(item.getIdRegistro());
            registros[1] = item.getNombre();
            registros[2] = item.getApellido();
            registros[3] = item.getSaldoActual();
            registros[4] = item.getFechaActual();
            registros[5] = item.getUltimoPago();
            registros[6] = item.getFechaPago();
            modelo2.addRow(registros);
        });
        table.setModel(modelo2);
        table.setRowHeight(30);
        table.getColumnModel().getColumn(0).setMaxWidth(0);
        table.getColumnModel().getColumn(0).setMinWidth(0);
        table.getColumnModel().getColumn(0).setPreferredWidth(0);
        return modelo2;
    }

    public boolean updateCliente(String id, String nombre, String apellido,
            String direccion, String telefono, int idCliente) {
        boolean valor = false;

        cliente = clientes();
        clienteFilter = cliente.stream()
                .filter(c -> c.getID().equals(id))
                .collect(Collectors.toList());
        int count = clienteFilter.size();
        clienteFilter = cliente.stream()
                .filter(c -> c.getIdCliente() == idCliente)
                .collect(Collectors.toList());
        if (0 == count || id.equals(clienteFilter.get(0).getID())) {

            sql = "UPDATE clientes SET ID = ?,Nombre = ?,Apellido = ?,"
                    + "Direccion = ?,Telefono = ? WHERE IdCliente =" + idCliente;
            Object[] cliente = new Object[]{id, nombre, apellido, direccion, telefono};
            update(sql, cliente);
            List<Reportes_clientes> resportes = reportesClientes(idCliente,1);
            int idRegistro = resportes.get(0).getIdRegistro();
            int IdCliente = resportes.get(0).getIdCliente();
            String SaldoActual = resportes.get(0).getSaldoActual();
            String FechaActual = resportes.get(0).getFechaActual();
            String UltimoPago = resportes.get(0).getUltimoPago();
            String FechaPago = resportes.get(0).getFechaPago();
            String ID = id;
            sql = "UPDATE reportes_clientes SET IdCliente = ?,SaldoActual = ?,FechaActual = ?"
                    + ",UltimoPago = ?,FechaPago = ? ,ID = ? WHERE IdRegistro =" + idRegistro;
            Object[] reporte = new Object[]{IdCliente, SaldoActual, FechaActual, UltimoPago, FechaPago, ID};
            update(sql, reporte);
            valor = true;
        }
        return valor;
    }

    public void deleteCliente(int idCliente, int idRegistro) {
        sql = "DELETE FROM reportes_clientes WHERE IdRegistro LIKE ?";
        delete(sql, idRegistro);
        sql = "DELETE FROM clientes WHERE IdCliente LIKE ?";
        delete(sql, idCliente);
    }

    public void updateRepostes(String saldoActual, String fecha, String pago, int idCliente) {
        List<Reportes_clientes> resportes = reportesClientes(idCliente,1);
        int idRegistro = resportes.get(0).getIdRegistro();
        int IdCliente = resportes.get(0).getIdCliente();
        String ID = resportes.get(0).getID();
        sql = "UPDATE reportes_clientes SET IdCliente = ?,SaldoActual = ?,FechaActual = ?"
                + ",UltimoPago = ?,FechaPago = ? ,ID = ? WHERE IdRegistro =" + idRegistro;
        Object[] reporte = new Object[]{IdCliente, "$" + saldoActual, fecha, "$" + pago, fecha, ID};
        update(sql, reporte);
    }

}
