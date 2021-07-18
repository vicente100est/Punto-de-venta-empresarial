/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ModelClass;

import Graphics.RenderCelda;
import Connection.Consult;
import Models.*;
import java.awt.Color;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ThreadLocalRandom;
import java.util.stream.Collectors;
import javax.swing.JCheckBox;
import javax.swing.JLabel;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.table.DefaultTableModel;

/**
 *
 * @author AlexJPZ
 */
public class Compra extends Consult {

    private String sql,codeBarra,codigoCompra;
    private Object[] obect;
    private DefaultTableModel modelo1, modelo2, modelo3;
    private double deuda = 0, enCaja, pago, deudas;
    private boolean deudaProveedor, verificar;

    public void guardarTempoCompra(String des, int cant, String precio) {
        double precio1 = Double.valueOf(precio);
        numTempoCompras = tempoCompras().stream()
                .filter(t -> t.getDescripcion().equals(des) && t.getPrecioCompra().equals("$" + formato.decimal(precio1)))
                .collect(Collectors.toList());
        if (0 < numTempoCompras.size()) {
            int cant1;
            double importe2, importe3;
            importe2 = precio1 * cant;
            importe3 = formato.reconstruir(numTempoCompras.get(0).getImporte().replace("$", ""));
            importe2 = importe2 + importe3;
            cant1 = cant + numTempoCompras.get(0).getCantidad();
            sql = "UPDATE tempo_compras SET Descripcion = ?,Cantidad = ?,PrecioCompra = ?,"
                    + "Importe = ? WHERE IdCompra =" + numTempoCompras.get(0).getIdCompra();
            obect = new Object[]{des, cant1, "$" + formato.decimal(precio1), "$" + formato.decimal(importe2)};
            update(sql, obect);
        } else {
            double importe;
            importe = precio1 * cant;
            sql = "INSERT INTO tempo_compras(Descripcion,Cantidad,PrecioCompra,Importe)"
                    + "VALUES(?,?,?,?)";
            obect = new Object[]{des, cant, "$" + formato.decimal(precio1), "$" + formato.decimal(importe)};
            insert(sql, obect);
        }
    }

    public void searchCompras(JTable table, String campo, int num_registro, int reg_por_pagina) {
        String[] registros = new String[5];
        String[] titulos = {"Id", "Descripcion", "Cantidad", "Precio Compra", "Importe"};
        modelo1 = new DefaultTableModel(null, titulos);
        if (campo.equals("")) {
            numTempoCompras = tempoCompras().stream()
                    .skip(num_registro).limit(reg_por_pagina)
                    .collect(Collectors.toList());
        } else {
            numTempoCompras = tempoCompras().stream()
                    .filter(C -> C.getDescripcion().startsWith(campo))
                    .skip(num_registro).limit(reg_por_pagina)
                    .collect(Collectors.toList());
        }
        numTempoCompras.forEach(item -> {
            registros[0] = String.valueOf(item.getIdCompra());
            registros[1] = item.getDescripcion();
            registros[2] = String.valueOf(item.getCantidad());
            registros[3] = item.getPrecioCompra();
            registros[4] = item.getImporte();
            modelo1.addRow(registros);
        });
        table.setModel(modelo1);
        table.setRowHeight(30);
        table.getColumnModel().getColumn(0).setMaxWidth(0);
        table.getColumnModel().getColumn(0).setMinWidth(0);
        table.getColumnModel().getColumn(0).setPreferredWidth(0);
        table.setDefaultRenderer(Object.class, new RenderCelda(4,0));
    }

    public List<Tempo_compras> getTempoCompras() {
        return tempoCompras();
    }

    public DefaultTableModel getModelo() {
        return modelo1;
    }

    public void importesTempo(JLabel label1, JLabel label2, JLabel label3) {
        deuda = 0;
        numTempoCompras = tempoCompras().stream()
                .collect(Collectors.toList());
        if (0 < numTempoCompras.size()) {
            numTempoCompras.forEach(item -> {
                double importes = formato.reconstruir(item.getImporte().replace("$", ""));
                deuda += importes;
            });
            label1.setText("$" + formato.decimal(deuda));
            label2.setText("$" + formato.decimal(deuda));
            label3.setText("$" + formato.decimal(deuda));
        } else {
            label1.setText("$0.00");
            label2.setText("$0.00");
            label3.setText("$0.00");
        }
    }

    public void updateTempoCompra(int id, String des, int cant, String precio) {
        double precio1, importe;
        precio1 = Double.valueOf(precio);
        importe = precio1 * cant;
        sql = "UPDATE tempo_compras SET Descripcion = ?,Cantidad = ?,PrecioCompra = ?,"
                + "Importe = ? WHERE IdCompra =" + id;
        obect = new Object[]{des, cant, "$" + formato.decimal(precio1), "$" + formato.decimal(importe)};
        update(sql, obect);
    }

    public void searchProveedores(JTable table, String campo) {
        String[] registros = new String[5];
        String[] titulos = {"Id", "Proveedor", "Email", "Telefono", "Saldo"};
        modelo2 = new DefaultTableModel(null, titulos);
        if (campo.equals("")) {
            reportes_proveedores = new ArrayList<>();
        } else {
            reportes_proveedores = reportesProveedores().stream()
                    .filter(P -> P.getProveedor().startsWith(campo) || P.getEmail().startsWith(campo)
                    || P.getTelefono().startsWith(campo))
                    .collect(Collectors.toList());
        }
        reportes_proveedores.forEach(item -> {
            registros[0] = String.valueOf(item.getIdProveedor());
            registros[1] = item.getProveedor();
            registros[2] = item.getEmail();
            registros[3] = item.getTelefono();
            registros[4] = item.getSaldoActual();
            modelo2.addRow(registros);
        });
        table.setModel(modelo2);
        table.setRowHeight(30);
        table.getColumnModel().getColumn(0).setMaxWidth(0);
        table.getColumnModel().getColumn(0).setMinWidth(0);
        table.getColumnModel().getColumn(0).setPreferredWidth(0);
    }

    public DefaultTableModel getModelo2() {
        return modelo2;
    }

    public void deleteCompras(int idCompra) {
        if (0 < idCompra) {
            sql = "DELETE FROM tempo_compras WHERE IdCompra LIKE ?";
            delete(sql, idCompra);
        } else {
            sql = "DELETE FROM tempo_compras";
            delete(sql, 0);
        }

    }

    public void getIngresos(JLabel label) {
        enCaja = 0;
        cajasIngresos = cajasIngresos().stream()
                .filter(c -> c.getFecha().equals(new Calendario().getFecha()))
                .collect(Collectors.toList());
        if (0 < cajasIngresos.size()) {
            cajasIngresos.forEach(item -> {
                String data = item.getIngreso().replace("$", "");
                double importes = formato.reconstruir(data.replace(" ", ""));
                enCaja += importes;
            });
            label.setText("$" + formato.decimal(enCaja));
        } else {
            label.setText("$0.00");
        }
    }

    public boolean verificarPago(JTextField TextField, JLabel Label, JCheckBox CheckBox,
            JLabel Label1, JLabel Label2, JLabel Label3, int idProveedor) {
        verificar = false;
        deudaProveedor = false;
        if (!TextField.getText().equalsIgnoreCase("")) {
            if (idProveedor != 0) {
                pago = Double.valueOf(TextField.getText());
                if (enCaja == 0) {
                    if (CheckBox.isSelected()) {
                        if (pago > deuda) {
                            Label.setText("Sea sobrepasado del pago");
                            Label.setForeground(Color.RED);
                        } else {
                            Label.setText("Se solicito un crédito al proveedor");
                            Label.setForeground(Color.RED);
                            deudaProveedor = true;
                            verificar = true;
                            pagos(pago, idProveedor, Label1, Label2, Label3);
                        }
                    } else {
                        if (pago > deuda) {
                            Label.setText("Sea sobrepasado del pago");
                            Label.setForeground(Color.RED);
                        } else {
                            Label.setText("No hay saldo en caja");
                            Label.setForeground(Color.RED);
                            pagos(pago, idProveedor, Label1, Label2, Label3);
                        }
                    }
                } else {
                    if (pago > enCaja) {
                        if (CheckBox.isSelected()) {
                            if (pago > deuda) {
                                Label.setText("Sea sobrepasado del pago");
                                Label.setForeground(Color.RED);
                            } else {
                                Label.setText("Se genera una deuda del sistema al proveedor");
                                Label.setForeground(Color.RED);
                                deudaProveedor = true;
                                verificar = true;
                                pagos(pago, idProveedor, Label1, Label2, Label3);
                            }
                        } else {
                            if (pago > deuda) {
                                Label.setText("Sea sobrepasado del pago");
                                Label.setForeground(Color.RED);
                            } else {
                                Label.setText("No hay ingresos suficientes en caja");
                                Label.setForeground(Color.RED);
                                pagos(pago, idProveedor, Label1, Label2, Label3);
                            }
                        }
                    } else {
                        if (pago == deuda) {
                            if (CheckBox.isSelected()) {
                                if (pago > deuda) {
                                    Label.setText("Sea sobrepasado del pago");
                                    Label.setForeground(Color.RED);
                                } else {
                                    Label.setText("Se genera una deuda del sistema al proveedor");
                                    Label.setForeground(Color.RED);
                                    deudaProveedor = true;
                                    verificar = true;
                                    pagos(pago, idProveedor, Label1, Label2, Label3);
                                }
                            } else {
                                Label.setText("Monto a pagar");
                                Label.setForeground(new Color(0, 153, 51));
                                verificar = true;
                            }
                        } else {
                            if (pago > deuda) {
                                Label.setText("Sea sobrepasado del pago");
                                Label.setForeground(Color.RED);
                            } else {
                                if (CheckBox.isSelected()) {
                                    Label.setText("Se genera deuda del sistema al proveedor");
                                    Label.setForeground(Color.RED);
                                    deudaProveedor = true;
                                    verificar = true;
                                    pagos(pago, idProveedor, Label1, Label2, Label3);
                                } else {
                                    Label.setText("Pago insuficiente");
                                    Label.setForeground(Color.RED);
                                    pagos(pago, idProveedor, Label1, Label2, Label3);
                                }
                            }

                        }
                    }
                }
            } else {
                Label.setText("Seleccione un proveedor");
                Label.setForeground(Color.RED);
                TextField.setText("");
            }
        }
        return verificar;
    }

    private void pagos(double pago, int idProveedor, JLabel Label1, JLabel Label2, JLabel Label3) {
        double saldo;
        if (enCaja == 0) {
            deudas = deuda;
        } else if (deuda > enCaja) {
            deudas = deuda - enCaja;
        } else if (deuda == pago) {
            deudas = deuda - pago;
        } else {
            deudas = deuda - pago;
        }
        String data = "$" + formato.decimal(deudas);
        Label1.setText(data);
        Label2.setText(data);
        saldo = formato.reconstruir(getReporte(idProveedor).get(0)
                .getSaldoActual().replace("$", ""));
        saldo += deudas;
        Label3.setText("$" + formato.decimal(saldo));
    }

    public List<Reportes_proveedores> getReporte(int idProveedor) {
        reportes_proveedores = reportesProveedor(idProveedor).stream()
                .filter(p -> p.getIdProveedor() == idProveedor)
                .collect(Collectors.toList());
        return reportes_proveedores;
    }

    public void saveCompras(String proveedor, int idProveedor, String usuario, int idUsuario,
            String role) {
        int count1, count2, ini, idRegistro;
        sql = "INSERT INTO compras(Producto,Cantidad,Precio,Importe,IdProveedor,Proveedor,IdUsuario"
                + ",Usuario,Role,Dia,Mes,Year,Fecha,Codigo)VALUES(?,?,?,?,?,?,?,?,?,?,?,?,?,?)";
        numTempoCompras = tempoCompras().stream()
                .collect(Collectors.toList());
        if (0 < numTempoCompras.size()) {
            numTempoCompras.forEach(item -> {
                codigoCompra = codeCompra(item.getDescripcion(),item.getPrecioCompra());
                obect = new Object[]{
                    item.getDescripcion(),
                    item.getCantidad(),
                    item.getPrecioCompra(),
                    item.getImporte(),
                    idProveedor,
                    proveedor,
                    idUsuario,
                    usuario,
                    role,
                    new Calendario().getDia(),
                    new Calendario().getMes(),
                    new Calendario().getYear(),
                    new Calendario().getFecha(),
                    codigoCompra
                };
                insert(sql, obect);
            });
            count1 = numTempoCompras.size();
            if (deudaProveedor) {
                reportes_proveedores = getReporte(idProveedor);
                idRegistro = reportes_proveedores.get(0).getIdRegistro();
                sql = "UPDATE reportes_proveedores SET IdProveedor = ?,SaldoActual = ?,FechaActual = ?,"
                        + "UltimoPago = ?,FechaPago = ? WHERE IdRegistro =" + idRegistro;
                double saldo = formato.reconstruir(reportes_proveedores.get(0)
                        .getSaldoActual().replace("$", ""));
                String ultimoPago = reportes_proveedores.get(0).getUltimoPago();
                String fechaPago = reportes_proveedores.get(0).getFechaPago();
                saldo += deudas;
                String dataSaldo = "$" + formato.decimal(saldo);
                Object[] reporte = new Object[]{
                    idProveedor,
                    dataSaldo,
                    new Calendario().getFecha(),
                    ultimoPago,
                    fechaPago
                };
                update(sql, reporte);
            }
            count2 = compras().size();
            if (count1 == count2) {
                ini = 0;
            } else {
                ini = count2 - count1;
            }
            for (int i = ini; i < count2; i++) {
                sql = "INSERT INTO tempo_productos(IdCompra)VALUES(?)";
                obect = new Object[]{compras().get(i).getIdCompra()};
                insert(sql, obect);
            }
        }
    }
    public String codeCompra(String producto, String precio){
        int codigo, count;
        compras = compras().stream()
                .filter(p -> p.getProducto().equals(producto)&& p.getPrecio().equals(precio)
                        && p.getYear().equals(new Calendario().getYear()))
                .collect(Collectors.toList());
        if (0 < compras.size()) {
            codeBarra = compras.get(0).getCodigo();
        }else{
            do{
                codigo = ThreadLocalRandom.current().nextInt(100000, 1000000000 + 1);
                codeBarra = String.valueOf(codigo);
                compras = compras().stream()
                        .filter(p -> p.getCodigo().equals(codeBarra)
                        && p.getYear().equals(new Calendario().getYear()))
                        .collect(Collectors.toList());
                count = compras.size();
            }while (count > 0);
        }
        return codeBarra;
    }
}
