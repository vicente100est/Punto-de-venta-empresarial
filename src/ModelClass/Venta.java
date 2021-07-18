/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ModelClass;

import Connection.Consult;
import Graphics.RenderCelda;
import Models.*;
import java.awt.Color;
import java.util.ArrayList;
import java.util.List;
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
public class Venta extends Consult {

    private Object[] object;
    private String sql;
    private DefaultTableModel modelo1, modelo2;
    private double importe = 0, totalPagar = 0, ingresosTotales,ingresosInicial;
    private boolean verificar = false, suCambio = false;
    private int caja, idUsuario;

    public void start(int caja, int idUsuario) {
        this.caja = caja;
        this.idUsuario = idUsuario;
    }

    public List<Bodega> searchBodega(String codigo) {
        return bodega().stream()
                .filter(t -> t.getCodigo().equals(codigo)).collect(Collectors.toList());
    }

    public void saveVentasTempo(String codigo, int funcion, int caja, int idUsuario) {
        String importe, precios;
        int idTempo, cantidad = 1, existencia;
        double descuento, precio, importes;

        tempoVentas = TempoVentas().stream()
                .filter(t -> t.getCodigo().equals(codigo) && t.getCaja() == caja
                && t.getIdUsuario() == idUsuario).collect(Collectors.toList());
        productos = productos().stream()
                .filter(t -> t.getCodigo().equals(codigo)).collect(Collectors.toList());
        descuento = Double.valueOf(productos.get(0).getDescuento().replace("%", ""));
        precio =formato.reconstruir(productos.get(0).getPrecio().replace("$", ""));
        descuento = descuento / 100;
        descuento = precio * descuento;
        precio = precio - descuento;
        precios = "$" + formato.decimal(precio);
        if (0 < tempoVentas.size()) {
            cantidad = tempoVentas.get(0).getCantidad();
            if (funcion == 0) {
                cantidad++;
            } else {
                cantidad--;
            }
            importes = precio * cantidad;
            importe = "$" + formato.decimal(importes);
            sql = "UPDATE tempo_ventas SET Precio = ?,Cantidad = ?,"
                    + "Importe = ?,Caja = ?,IdUsuario = ? WHERE IdTempo =" + tempoVentas.get(0).getIdTempo();
            object = new Object[]{
                precios,
                cantidad,
                importe,
                caja,
                idUsuario
            };
            update(sql, object);
        } else {
            sql = "INSERT INTO tempo_ventas(Codigo,Descripcion,Precio,Cantidad,Importe,Caja,IdUsuario)"
                    + "VALUES(?,?,?,?,?,?,?)";

            object = new Object[]{
                productos.get(0).getCodigo(),
                productos.get(0).getProducto(),
                precios,
                1,
                precios,
                caja,
                idUsuario
            };
            insert(sql, object);
        }
        bodega = bodega().stream()
                .filter(t -> t.getCodigo().equals(codigo)).collect(Collectors.toList());
        existencia = bodega.get(0).getExistencia();
        if (existencia > 0) {
            existencia--;
            sql = "UPDATE bodega SET Existencia = ? WHERE Id =" + bodega.get(0).getId();
            object = new Object[]{
                existencia
            };
            update(sql, object);
        }

    }

    public void searchVentatemp(JTable table, int num_registro, int reg_por_pagina) {
        String[] registros = new String[6];
        String[] titulos = {"IdTempo", "Codigo", "Descripcion", "Precio", "Cantidad", "Importe"};
        modelo1 = new DefaultTableModel(null, titulos);

        tempoVentas = TempoVentas().stream()
                .filter(t -> t.getCaja() == caja && t.getIdUsuario() == idUsuario)
                .skip(num_registro).limit(reg_por_pagina)
                .collect(Collectors.toList());
        if (0 < tempoVentas.size()) {
            tempoVentas.forEach(item -> {
                registros[0] = String.valueOf(item.getIdTempo());
                registros[1] = item.getCodigo();
                registros[2] = item.getDescripcion();
                registros[3] = item.getPrecio();
                registros[4] = String.valueOf(item.getCantidad());
                registros[5] = item.getImporte();
                modelo1.addRow(registros);
            });

        }
        table.setModel(modelo1);
        table.setRowHeight(30);
        table.getColumnModel().getColumn(0).setMaxWidth(0);
        table.getColumnModel().getColumn(0).setMinWidth(0);
        table.getColumnModel().getColumn(0).setPreferredWidth(0);
        table.setDefaultRenderer(Object.class, new RenderCelda(0,0));
    }

    public DefaultTableModel getModelo() {
        return modelo1;
    }

    public void importes(JLabel label, int caja, int idUsuario) {
        importe = 0;
        tempoVentas = TempoVentas().stream()
                .filter(t -> t.getCaja() == caja
                && t.getIdUsuario() == idUsuario).collect(Collectors.toList());
        if (0 < tempoVentas.size()) {
            tempoVentas.forEach(item -> {
                importe += formato.reconstruir(item.getImporte().replace("$", ""));
            });
            label.setText("$" + formato.decimal(importe));
        } else {
            label.setText("$0.00");
        }
    }

    public void deleteVenta(String codigo, int cant, int caja, int idUsuario) {
        int cantidad = 0, existencia = 0;
        tempoVentas = TempoVentas().stream()
                .filter(t -> t.getCodigo().equals(codigo) && t.getCaja() == caja
                && t.getIdUsuario() == idUsuario).collect(Collectors.toList());
        if (0 < tempoVentas.size()) {
            cantidad = tempoVentas.get(0).getCantidad();
            bodega = bodega().stream()
                    .filter(t -> t.getCodigo().equals(codigo)).collect(Collectors.toList());
            if (0 < bodega.size()) {
                existencia = bodega.get(0).getExistencia();
                if (cant == 1) {
                    existencia += cantidad;
                    sql = "DELETE FROM tempo_ventas WHERE IdTempo LIKE ?";
                    delete(sql, tempoVentas.get(0).getIdTempo());
                } else {
                    existencia++;
                    saveVentasTempo(codigo, 1, caja, idUsuario);
                }
                sql = "UPDATE bodega SET Existencia = ? WHERE Id =" + bodega.get(0).getId();
                object = new Object[]{
                    existencia
                };
                update(sql, object);
            }
        }
    }

    public void pagosCliente(JTextField textField, JLabel label1, JLabel label2, JLabel label3, JCheckBox checkBox) {
        double pago, pagar;
        if (textField.getText().equals("")) {
            label1.setText("Su cambio");
            label1.setForeground(new Color(0, 153, 51));
            label2.setText("$0.00");
        } else {
            pagar = importe;
            pago = Double.valueOf(textField.getText());
            if (pago >= pagar) {
                totalPagar = pago - pagar;
                if (totalPagar > ingresosTotales) {
                    label1.setText("No hay ingresos en caja");
                    label1.setForeground(Color.RED);
                    verificar = false;
                    suCambio = false;
                } else {
                    if (checkBox.isSelected()) {
                        label1.setText("Desmarque la opción crédito");
                        label1.setForeground(Color.RED);
                        verificar = false;
                        suCambio = false;
                    } else {
                        label1.setText("Su cambio");
                        label1.setForeground(new Color(0, 153, 51));
                        totalPagar = pago - pagar;
                        verificar = true;
                        suCambio = true;
                    }
                }
            }
            if (pago < pagar) {
                label1.setText("Pago insuficiente");
                label1.setForeground(Color.RED);
                totalPagar = pagar - pago;
                if (checkBox.isSelected()) {
                    verificar = true;
                } else {
                    verificar = false;
                }
                suCambio = false;
            }
            label2.setText("$" + formato.decimal(totalPagar));
        }
        label3.setText("Pagó con");
        label3.setForeground(new Color(0, 153, 51));
    }

    public void reportesCliente(JTable table, String campo) {
        String[] registros = new String[8];
        String[] titulos = {"Id", "ID", "Nombre", "Apellido", "Saldo Actual", "Fecha Actual",
            "Ultimo Pago", "Fecha Pago"};
        modelo2 = new DefaultTableModel(null, titulos);
        if (campo.equals("")) {
            reportes_clientes = new ArrayList<>();
        } else {
            reportes_clientes = reportesClientes(0, 2).stream()
                    .filter(t -> t.getID().startsWith(campo)
                    || t.getNombre().startsWith(campo)).collect(Collectors.toList());
        }
        if (0 < reportes_clientes.size()) {
            reportes_clientes.forEach(item -> {
                registros[0] = String.valueOf(item.getIdRegistro());
                registros[1] = item.getID();
                registros[2] = item.getNombre();
                registros[3] = item.getApellido();
                registros[4] = item.getSaldoActual();
                registros[5] = item.getFechaActual();
                registros[6] = item.getUltimoPago();
                registros[7] = item.getFechaPago();
                modelo2.addRow(registros);
            });
        }
        table.setModel(modelo2);
        table.setRowHeight(30);
        table.getColumnModel().getColumn(0).setMaxWidth(0);
        table.getColumnModel().getColumn(0).setMinWidth(0);
        table.getColumnModel().getColumn(0).setPreferredWidth(0);
        table.setDefaultRenderer(Object.class, new RenderCelda(1,0));
    }

    public void dataCliente(JCheckBox checkBox, JTextField textField_Pagos,
            JTextField textField_Buscar, JTable table, List<JLabel> labels) {
        String deuda1, nombre, apellido;
        double deuda2, deudaTotal;
        if (checkBox.isSelected()) {
            if (textField_Pagos.getText().equals("")) {
                if (checkBox.isSelected() == false) {
                    labels.get(0).setText("$0.00");
                    labels.get(1).setText("$0.00");
                    labels.get(2).setText(labels.get(0).getText());
                    labels.get(3).setText("Nombre");
                    labels.get(4).setText("$0.00");
                    labels.get(5).setText("$0.00");
                    labels.get(6).setText("--/--/--");
                }
            } else {
                if (verificar) {
                    if (!textField_Buscar.getText().equalsIgnoreCase("")) {
                        int filas = table.getSelectedRow();
                        deuda1 = (String) modelo2.getValueAt(filas, 4);
                        deuda2 = formato.reconstruir(deuda1.replace("$", ""));
                        deudaTotal = deuda2 + totalPagar;
                        labels.get(0).setText("$" + formato.decimal(deudaTotal));
                        nombre = (String) modelo2.getValueAt(filas, 2);
                        apellido = (String) modelo2.getValueAt(filas, 3);

                        labels.get(3).setText(nombre + " " + apellido);
                        labels.get(1).setText("$" + formato.decimal(totalPagar));
                        labels.get(4).setText(deuda1);
                        labels.get(2).setText("$" + formato.decimal(deudaTotal));
                        labels.get(5).setText((String) modelo2.getValueAt(filas, 6));
                        labels.get(6).setText(new Calendario().getFecha());
                    }
                }
            }
        } else {
            labels.get(0).setText("$0.00");
            labels.get(1).setText("$0.00");
            labels.get(2).setText(labels.get(0).getText());
            labels.get(3).setText("");
            labels.get(4).setText("$0.00");
            labels.get(5).setText("$0.00");
            labels.get(6).setText("--/--/--");
            textField_Buscar.setText("");
        }
    }

    public void ingresosCajas(JLabel label1, JLabel label2, JLabel label3, int caja, int idUsuario) {
        ingresosTotales = 0;
        cajaIngresoInicial = cajasIngresos().stream()
                .filter(t -> t.getCaja() == caja && t.getIdUsuario() == idUsuario
                && t.getType().equals("Inicial") && t.getFecha().equals(new Calendario().getFecha()))
                .collect(Collectors.toList());
        if (0 < cajaIngresoInicial.size()) {

            cajaIngresoInicial.forEach(item -> {
                ingresosTotales += formato.reconstruir(item.getIngreso().replace("$", ""));
            });
            label1.setText("$" + formato.decimal(ingresosTotales));
            label1.setForeground(new Color(102, 102, 102));

        } else {
            label1.setText("$0.00");
            label1.setForeground(Color.RED);
        }
        cajaIngresoVenta = cajasIngresos().stream()
                .filter(t -> t.getCaja() == caja && t.getIdUsuario() == idUsuario
                && t.getType().equals("Ventas") && t.getFecha().equals(new Calendario().getFecha()))
                .collect(Collectors.toList());
        if (0 < cajaIngresoVenta.size()) {
            String data = cajaIngresoVenta.get(0).getIngreso();
            label2.setText(data);
            label2.setForeground(new Color(102, 102, 102));
            ingresosTotales += formato.reconstruir(data.replace("$", ""));
        } else {
            label2.setText("$0.00");
            label2.setForeground(Color.RED);
        }
        label3.setText("$" + formato.decimal(ingresosTotales));
        label3.setForeground(new Color(102, 102, 102));
    }

    public boolean cobrar(JCheckBox checkBox_Credito, JTextField textField_Pagos, JTable table,
            List<JLabel> labels) {
        boolean valor = false;
        if (textField_Pagos.getText().equals("")) {
            labels.get(7).setText("Ingrese el pago");
            labels.get(7).setForeground(Color.RED);
            textField_Pagos.requestFocus();
        } else {
            if (verificar) {
                String saldoActual,IDCliente = null;
                double deuda = 0, deudaActual,pagos;
                int idRegistro = 0;
                pagos = formato.reconstruir(textField_Pagos.getText());
                if (checkBox_Credito.isSelected()) {
                    if (table.getSelectedRows().length > 0) {
                        int filas = table.getSelectedRow();
                        idRegistro = Integer.valueOf((String) modelo2.getValueAt(filas, 0));
                        IDCliente = (String) modelo2.getValueAt(filas, 1);
                        saldoActual = (String) modelo2.getValueAt(filas, 4);
                        deudaActual = formato.reconstruir(saldoActual.replace("$", ""));
                        deuda = totalPagar + deudaActual;
                        valor = insertVentas(checkBox_Credito, idRegistro, deuda, pagos,
                                IDCliente, labels);
                    } else {
                        if (verificar) {
                            labels.get(8).setText("Seleccione un cliente");
                            labels.get(8).setForeground(Color.RED);
                        }
                    }
                } else {
                    if (verificar) {
                        if (pagos >= importe) {
                            valor = insertVentas(checkBox_Credito, idRegistro, deuda, pagos,
                                    IDCliente, labels);
                        }
                    }
                }
            }
        }
        return valor;
    }

    private boolean insertVentas(JCheckBox checkBox_Credito, int idRegistro, double deuda,
            double pagos, String IDCliente, List<JLabel> labels) {
        boolean valor = false;
        tempoVentas = TempoVentas().stream()
                .filter(t -> t.getCaja() == caja && t.getIdUsuario() == idUsuario)
                .collect(Collectors.toList());
        if (0 < tempoVentas.size()) {
            sql = "INSERT INTO ventas(Codigo,Descripcion,Precio,Cantidad,Importe,Dia,"
                    + "Mes,Year,Fecha,Caja,IdUsuario,Compra)VALUES(?,?,?,?,?,?,?,?,?,?,?,?)";
            tempoVentas.forEach(item -> {
                productos = productos().stream()
                .filter(t -> t.getCodigo().equals(item.getCodigo())).collect(Collectors.toList());
                object = new Object[]{
                    item.getCodigo(),
                    item.getDescripcion(),
                    item.getPrecio(),
                    item.getCantidad(),
                    item.getImporte(),
                    new Calendario().getDia(),
                    new Calendario().getMes(),
                    new Calendario().getYear(),
                    new Calendario().getFecha(),
                    caja,
                    idUsuario,
                    productos.get(0).getCompra()
                };
                insert(sql, object);
            });

            if (checkBox_Credito.isSelected()) {
                sql = "UPDATE reportes_clientes SET SaldoActual = ?,FechaActual = ?"
                        + " WHERE IdRegistro =" + idRegistro;
                Object[] reporte = new Object[]{
                    "$" + formato.decimal(deuda),
                    new Calendario().getFecha()
                };
                update(sql, reporte);
                sql = "INSERT INTO creditos_ventas(Total,Pago,Credito,Dia,Mes,Year,"
                        + "Fecha,Cliente,Caja,IdUsuario)VALUES(?,?,?,?,?,?,?,?,?,?)";
                object = new Object[]{
                    "$" + formato.decimal(importe),
                    "$" + formato.decimal(pagos),
                    "$" + formato.decimal(totalPagar),
                    new Calendario().getDia(),
                    new Calendario().getMes(),
                    new Calendario().getYear(),
                    new Calendario().getFecha(),
                    IDCliente,
                    caja,
                    idUsuario
                };
                insert(sql, object);
            }
            cajaIngresoVenta = cajasIngresos().stream()
                    .filter(t -> t.getCaja() == caja && t.getIdUsuario() == idUsuario
                    && t.getType().equals("Ventas") && t.getFecha().equals(new Calendario().getFecha()))
                    .collect(Collectors.toList());
            if (0 < cajaIngresoVenta.size()) {
                double ingresos = pagos + formato.reconstruir(cajaIngresoVenta.get(0)
                        .getIngreso().replace("$", ""));
                sql = "UPDATE cajas_ingresos SET Ingreso = ?"
                        + " WHERE Id =" + cajaIngresoVenta.get(0).getId();
                object = new Object[]{
                    "$" + formato.decimal(ingresos)
                };
                update(sql, object);
            } else {
                sql = "INSERT INTO cajas_ingresos(Caja,Ingreso,Type,IdUsuario,Dia,Mes,Year,"
                        + "Fecha)VALUES(?,?,?,?,?,?,?,?)";
                object = new Object[]{
                    caja,
                    "$" + formato.decimal(pagos),
                    "Ventas",
                    idUsuario,
                    new Calendario().getDia(),
                    new Calendario().getMes(),
                    new Calendario().getYear(),
                    new Calendario().getFecha()
                };
                insert(sql, object);
            }
            valor = true;
            if (suCambio) {
                cajaIngresoInicial = cajasIngresos().stream()
                        .filter(t -> t.getCaja() == caja && t.getIdUsuario() == idUsuario
                        && t.getType().equals("Inicial") && t.getFecha().equals(new Calendario().getFecha()))
                        .collect(Collectors.toList());
                if (0 < cajaIngresoInicial.size()) {
                    cajaIngresoInicial.forEach(item -> {
                        ingresosInicial += formato.reconstruir(item.getIngreso().replace("$", ""));
                    });
                  
                    if (0 < ingresosInicial) {
                        if (ingresosInicial > totalPagar || ingresosInicial == totalPagar) {
                            ingresosInicial -= totalPagar;
                            sql = "UPDATE cajas_ingresos SET Ingreso = ?"
                                    + " WHERE Id =" + cajaIngresoInicial.get(0).getId();
                            object = new Object[]{
                                "$" + formato.decimal(ingresosInicial)
                            };
                            update(sql, object);
                        } else {
                            valor = ingresosVentas(labels);
                        }
                    } else {
                        valor = ingresosVentas(labels);
                    }
                } else {
                    valor = ingresosVentas(labels);
                }
            }
        }
        return valor;
    }

    private boolean ingresosVentas(List<JLabel> labels) {
        double ingresosVenta = 0, ingresosInicial = 0;
        boolean valor = false;
        cajaIngresoVenta = cajasIngresos().stream()
                .filter(t -> t.getCaja() == caja && t.getIdUsuario() == idUsuario
                && t.getType().equals("Ventas") && t.getFecha().equals(new Calendario().getFecha()))
                .collect(Collectors.toList());
        if (0 < cajaIngresoVenta.size()) {
            String ingreso = cajaIngresoVenta.get(0).getIngreso().replace("$", "");
            ingresosVenta = formato.reconstruir(ingreso);
            if (ingresosVenta > totalPagar || ingresosVenta == totalPagar) {
                if (0 < cajaIngresoInicial.size()) {
                    String ingresoIni = cajaIngresoInicial.get(0).getIngreso().replace("$", "");
                    ingresosInicial = formato.reconstruir(ingresoIni);
                    totalPagar -= ingresosInicial;

                    sql = "UPDATE cajas_ingresos SET Ingreso = ?"
                            + " WHERE Id =" + cajaIngresoInicial.get(0).getId();
                    object = new Object[]{
                        "$0.00"
                    };
                    update(sql, object);
                }
                ingresosVenta -= totalPagar;
                sql = "UPDATE cajas_ingresos SET Ingreso = ?"
                        + " WHERE Id =" + cajaIngresoVenta.get(0).getId();
                object = new Object[]{
                    "$" + formato.decimal(ingresosVenta)
                };
                update(sql, object);

                valor = true;
            } else {
                if (totalPagar < ingresosTotales || ingresosTotales == totalPagar) {
                    if (0 < cajaIngresoInicial.size()) {
                        String ingresoIni = cajaIngresoInicial.get(0).getIngreso().replace("$", "");
                        ingresosInicial = formato.reconstruir(ingresoIni);
                        totalPagar -= ingresosInicial;

                        sql = "UPDATE cajas_ingresos SET Ingreso = ?"
                                + " WHERE Id =" + cajaIngresoInicial.get(0).getId();
                        object = new Object[]{
                            "$0.00"
                        };
                        update(sql, object);
                    }
                    ingresosVenta -= totalPagar;
                    sql = "UPDATE cajas_ingresos SET Ingreso = ?"
                            + " WHERE Id =" + cajaIngresoVenta.get(0).getId();
                    object = new Object[]{
                        "$" + formato.decimal(ingresosVenta)
                    };
                    update(sql, object);

                    valor = true;
                } else {
                    labels.get(9).setText("No hay ingresos");
                    labels.get(8).setForeground(Color.RED);
                    valor = false;
                }
            }
        } else {
            labels.get(9).setText("No hay ingresos");
            labels.get(8).setForeground(Color.RED);
            valor = false;
        }

        return valor;
    }

    public List<Tempo_ventas> getTempoVentas() {
        return TempoVentas().stream()
                .filter(t -> t.getCaja() == caja && t.getIdUsuario() == idUsuario)
                .collect(Collectors.toList());
    }
}
