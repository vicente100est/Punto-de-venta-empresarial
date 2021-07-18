/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ModelClass;

import Connection.Consult;
import Graphics.RenderCelda;
import Models.Bodega;
import Models.Productos;
import Models.Ventas;
import datechooser.beans.DateChooserCombo;
import java.awt.Color;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.stream.Collectors;
import javax.swing.JCheckBox;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JSpinner;
import javax.swing.JTabbedPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.SpinnerNumberModel;
import javax.swing.table.DefaultTableModel;

/**
 *
 * @author AJpag
 */
public class Inventario extends Consult {

    private static int idRegistro, existencia, pageSize = 2;
    private String sql;
    private Object[] object;
    private List<JLabel> labelBodega;
    private List<JTextField> textFieldBodega;
    private DefaultTableModel modelo1, modelo2, modelo3;
    private SpinnerNumberModel model;
    private JTable table1, table2, table3;
    private JSpinner spinnerBodega;
    private JCheckBox checkBoxBodega, CheckBox_MaxVentas;
    private JTabbedPane tabbedPane;
    private DateChooserCombo dateChooser_Inicio, dateChooser_Final;
    private SimpleDateFormat formateador = new SimpleDateFormat("dd/MM/yyyy");

    public Inventario() {

    }

    public Inventario(Object[] objectBodega, List<JLabel> labelBodega, List<JTextField> textFieldBodega) {
        this.labelBodega = labelBodega;
        this.textFieldBodega = textFieldBodega;
        table1 = (JTable) objectBodega[0];
        spinnerBodega = (JSpinner) objectBodega[1];
        checkBoxBodega = (JCheckBox) objectBodega[2];
        tabbedPane = (JTabbedPane) objectBodega[3];
        table2 = (JTable) objectBodega[4];
        dateChooser_Inicio = (DateChooserCombo) objectBodega[5];
        dateChooser_Final = (DateChooserCombo) objectBodega[6];
        CheckBox_MaxVentas = (JCheckBox) objectBodega[7];
        table3 = (JTable) objectBodega[8];
    }

    //BODEGA
    public void getBodega(String campo, int num_registro, int reg_por_pagina) {
        String[] titulos = {"Id", "Codigo", "Producto", "Existencia", "Fecha"};
        modelo1 = new DefaultTableModel(null, titulos);
        Number existencia = (Number) spinnerBodega.getValue();
        if (0 < getBodega().size()) {

            if (campo.equals("")) {
                if (checkBoxBodega.isSelected()) {
                    productos = getBodega().stream().filter(b -> b.getExistencia() <= existencia.intValue())
                            .skip(num_registro).limit(reg_por_pagina)
                            .collect(Collectors.toList());
                } else {
                    productos = getBodega().stream().skip(num_registro).limit(reg_por_pagina)
                            .collect(Collectors.toList());
                }
            } else {
                if (checkBoxBodega.isSelected()) {
                    productos = getBodega().stream().filter(b -> b.getExistencia() <= existencia.intValue()
                            && b.getCodigo().startsWith(campo) || b.getExistencia() <= existencia.intValue()
                            && b.getProducto().startsWith(campo))
                            .skip(num_registro).limit(reg_por_pagina)
                            .collect(Collectors.toList());
                } else {
                    productos = getBodega().stream().filter(b -> b.getCodigo().startsWith(campo)
                            || b.getProducto().startsWith(campo))
                            .skip(num_registro).limit(reg_por_pagina)
                            .collect(Collectors.toList());
                }
            }
        }
        productos.forEach(item -> {
            String[] registros = {
                String.valueOf(item.getId()),
                item.getCodigo(),
                item.getProducto(),
                String.valueOf(item.getExistencia()),
                item.getFecha()
            };
            modelo1.addRow(registros);
        });
        table1.setModel(modelo1);
        table1.setRowHeight(30);
        table1.getColumnModel().getColumn(0).setMaxWidth(0);
        table1.getColumnModel().getColumn(0).setMinWidth(0);
        table1.getColumnModel().getColumn(0).setPreferredWidth(0);
        table1.setDefaultRenderer(Object.class, new RenderCelda(9, existencia.intValue()));
    }

    public void restablecerBodega() {
        idRegistro = 0;
        existencia = 0;
        switch (tabbedPane.getSelectedIndex()) {
            case 0:
                getBodega("", 0, pageSize);
                model = new SpinnerNumberModel(
                        new Integer(1), // Dato visualizado al inicio en el spinner 
                        new Integer(1), // Límite inferior 
                        new Integer(100), // Límite superior 
                        new Integer(1) // incremento-decremento 
                );
                spinnerBodega.setModel(model);
                textFieldBodega.get(0).setText("");
                new Paginador(9, table1, labelBodega.get(1), 1).primero();
                break;
            case 1:
                textFieldBodega.get(1).setText("");
                textFieldBodega.get(2).setText("");
                labelBodega.get(2).setText("Lista de productos");
                labelBodega.get(2).setForeground(new Color(102, 102, 102));
                labelBodega.get(4).setForeground(new Color(102, 102, 102));
                labelBodega.get(5).setForeground(new Color(102, 102, 102));
                getProductos("", 0, pageSize);
                new Paginador(10, table2, labelBodega.get(3), 1).primero();
                break;

            case 2:
                Calendar c = new GregorianCalendar();
                dateChooser_Inicio.setSelectedDate(c);
                dateChooser_Final.setSelectedDate(c);
                searchVentas("", 0, pageSize);
                new Paginador(10, table3, labelBodega.get(6), 1).primero();
                break;
        }

    }

    public void dataTableBodega() {
        int filas = table1.getSelectedRow();
        idRegistro = Integer.valueOf((String) modelo1.getValueAt(filas, 0));
        existencia = Integer.valueOf((String) modelo1.getValueAt(filas, 3));
        textFieldBodega.get(0).setText("" + existencia);
    }

    public void updateExistencia() {
        if (textFieldBodega.get(0).getText().equals("")) {
            labelBodega.get(0).setText("Ingrese el dato en el campo");
            labelBodega.get(0).setForeground(Color.RED);
            textFieldBodega.get(0).requestFocus();
        } else {
            sql = "UPDATE bodega SET Existencia = ? WHERE Id =" + idRegistro;
            object = new Object[]{textFieldBodega.get(0).getText()};
            update(sql, object);
        }
    }

    public List<Productos> getInvBodega() {
        return getBodega();
    }

    //PRODUCTOS
    public void getProductos(String campo, int num_registro, int reg_por_pagina) {
        String[] titulos = {"Id", "Codigo", "Producto", "Precio", "Descuento", "Departamento", "Categoria"};
        modelo2 = new DefaultTableModel(null, titulos);

        if (0 < productos().size()) {
            if (campo.equals("")) {
                productos = productos().stream().skip(num_registro).limit(reg_por_pagina)
                        .collect(Collectors.toList());
            } else {
                productos = productos().stream()
                        .filter(p -> p.getCodigo().startsWith(campo) || p.getProducto().startsWith(campo))
                        .skip(num_registro).limit(reg_por_pagina)
                        .collect(Collectors.toList());
            }
            productos.forEach(item -> {
                String[] registros = {
                    String.valueOf(item.getIdProducto()),
                    item.getCodigo(),
                    item.getProducto(),
                    item.getPrecio(),
                    item.getDescuento(),
                    item.getDepartamento(),
                    item.getCategoria()
                };
                modelo2.addRow(registros);
            });
            table2.setModel(modelo2);
            table2.setRowHeight(30);
            table2.getColumnModel().getColumn(0).setMaxWidth(0);
            table2.getColumnModel().getColumn(0).setMinWidth(0);
            table2.getColumnModel().getColumn(0).setPreferredWidth(0);
            table2.setDefaultRenderer(Object.class, new RenderCelda(10, 0));
        }
    }

    public void dataTableProductos() {
        int filas = table2.getSelectedRow();
        idRegistro = Integer.valueOf((String) modelo2.getValueAt(filas, 0));
    }

    public void updateProductos() {
        String precio, descuento;
        if (idRegistro != 0) {
            productos = productos().stream()
                    .filter(p -> p.getIdProducto() == idRegistro)
                    .collect(Collectors.toList());
            if (0 < productos.size()) {
                List<Bodega> bodega = bodega().stream().filter(b -> b.getCodigo()
                        .equals(productos.get(0).getCodigo()))
                        .collect(Collectors.toList());
                if (0 == bodega.size()) {
                    labelBodega.get(2).setText("Producto no disponible ");
                    labelBodega.get(2).setForeground(Color.RED);
                } else {
                    if (0 == bodega.get(0).getExistencia()) {
                        labelBodega.get(2).setText("Producto no disponible ");
                        labelBodega.get(2).setForeground(Color.RED);
                    } else {
                        if (textFieldBodega.get(1).getText().equals("")) {
                            precio = productos.get(0).getPrecio();
                        } else {
                            double data = formato.reconstruir(textFieldBodega.get(1).getText());
                            precio = "$" + formato.decimal(data);
                        }
                        if (textFieldBodega.get(2).getText().equals("")) {
                            descuento = productos.get(0).getDescuento();
                        } else {
                            double data = formato.reconstruir(textFieldBodega.get(2).getText());
                            descuento = "$" + formato.decimal(data);
                        }
                        sql = "UPDATE productos SET Precio = ?,Descuento = ? WHERE IdProducto =" + idRegistro;
                        object = new Object[]{precio, descuento};
                        update(sql, object);
                        restablecerBodega();
                    }
                }
            }
        } else {
            labelBodega.get(2).setText("Seleccione un producto ");
            labelBodega.get(2).setForeground(Color.RED);
        }
    }

    /*VENTAS*/
    public int searchVentas(String campo, int num_pagina, int reg_por_pagina) {
        String[] titulos = {"Id", "Codigo", "Descripcion",
            "Precio", "Cantidad", "Importe", "Dia",
            "Mes", "Year", "Fecha", "Caja"};
        modelo3 = new DefaultTableModel(null, titulos);
        List<Ventas> query = new ArrayList<Ventas>();
        int valor = 0;
        try {
            String fecha_inicio = dateChooser_Inicio.getSelectedPeriodSet().toString();
            Date fechaDate1 = formateador.parse(dateChooser_Inicio.getSelectedPeriodSet().toString());
            Date fechaDate2 = formateador.parse(dateChooser_Final.getSelectedPeriodSet().toString());
            if (campo.equals("")) {
                if (CheckBox_MaxVentas.isSelected()) {
                    if (fechaDate2.before(fechaDate1)) {
                        JOptionPane.showMessageDialog(null, "La fecha final debe ser mayor a la fecha de inicial");
                    } else {
                        query = maxVentas(filtrarProductosFechas(fecha_inicio));
                        valor = query.size();
                    }
                } else {
                    if (fechaDate2.before(fechaDate1)) {
                        JOptionPane.showMessageDialog(null, "La fecha final debe ser mayor a la fecha de inicial");
                    } else {
                        query = filtrarProductosFechas(fecha_inicio);
                        valor = query.size();
                    }
                }
            } else {
                if (CheckBox_MaxVentas.isSelected()) {
                    if (fechaDate2.before(fechaDate1)) {
                        JOptionPane.showMessageDialog(null, "La fecha final debe ser mayor a la fecha inicial");
                    } else {
                        query = maxVentas(filtrarProductosFechas(fecha_inicio)).stream()
                                .filter(p -> p.getCodigo().startsWith(campo) || p.getDescripcion().startsWith(campo))
                                .skip(num_pagina).limit(reg_por_pagina)
                                .collect(Collectors.toList());
                        valor = query.size();
                    }
                } else {
                    if (fechaDate2.before(fechaDate1)) {
                        JOptionPane.showMessageDialog(null, "La fecha final debe ser mayor a la fecha inicial");
                    } else {
                        query = filtrarProductosFechas(fecha_inicio).stream()
                                .filter(p -> p.getCodigo().startsWith(campo) || p.getDescripcion().startsWith(campo))
                                .skip(num_pagina).limit(reg_por_pagina)
                                .collect(Collectors.toList());
                        valor = query.size();
                    }
                }
            }
        } catch (ParseException ex) {
        }
        query.forEach(item -> {
            Object[] registros = {
                item.getIdVenta(),
                item.getCodigo(),
                item.getDescripcion(),
                item.getPrecio(),
                item.getCantidad(),
                item.getImporte(),
                item.getDia(),
                item.getMes(),
                item.getYear(),
                item.getFecha()
            };
            modelo3.addRow(registros);
        });
        table3.setModel(modelo3);
        table3.setRowHeight(30);
        table3.getColumnModel().getColumn(0).setMaxWidth(0);
        table3.getColumnModel().getColumn(0).setMinWidth(0);
        table3.getColumnModel().getColumn(0).setPreferredWidth(0);
        return valor;
    }

    private List<Ventas> maxVentas(List<Ventas> query) {
        List<Ventas> listProduct = new ArrayList<>();
        for (Ventas item : query) {
            if (verificar(item, listProduct)) {
                listProduct.add(item);
            }
        }
        listProduct.sort((v1, v2) -> Integer.valueOf(v1.getCantidad()).compareTo(v2.getCantidad()));
        Collections.reverse(listProduct);
        return listProduct;
    }

    private boolean verificar(Ventas data, List<Ventas> listProduct) {
        int pos = 0, cant;
        double importe1, importe2, importe3;
        for (Ventas item : listProduct) {
            if (item.getCodigo().equals(data.getCodigo())) {
                importe1 = formato.reconstruir(item.getImporte().replace("$", ""));
                importe2 = formato.reconstruir(data.getImporte().replace("$", ""));
                importe3 = importe1 + importe2;
                String imporetes = "$" + formato.decimal(importe3);
                cant = item.getCantidad() + data.getCantidad();
                listProduct.remove(pos);
                item.setCantidad(cant);
                item.setImporte(imporetes);
                listProduct.add(pos, item);
                return false;
            }
            pos++;
            cant = 0;
        }
        return true;
    }

    private List<Ventas> filtrarProductosFechas(String fecha_inicio) {
        List<Ventas> listProduct = new ArrayList<>();
        try {

            Ventas venta = new Ventas();
            Date fechaDate1 = formateador.parse(dateChooser_Inicio.getSelectedPeriodSet().toString());
            Date fechaDate2 = formateador.parse(dateChooser_Final.getSelectedPeriodSet().toString());
            List<Ventas> listdb1 = ventas().stream().filter(b -> b.getFecha()
                    .equals(fecha_inicio))
                    .collect(Collectors.toList());
            if (0 < listdb1.size()) {
                List<Ventas> listdb2 = ventas().stream()
                        .filter(b -> b.getIdVenta() >= listdb1.get(0).getIdVenta())
                        .collect(Collectors.toList());
                for (Ventas item : listdb2) {
                    Date fechaDate3 = formateador.parse(item.getFecha());
                    if (fechaDate3.before(fechaDate2)
                            || 0 == fechaDate2.compareTo(fechaDate1)) {
                        venta.setIdVenta(item.getIdVenta());
                        venta.setCodigo(item.getCodigo());
                        venta.setDescripcion(item.getDescripcion());
                        venta.setPrecio(item.getPrecio());
                        venta.setCantidad(item.getCantidad());
                        venta.setImporte(item.getImporte());
                        venta.setDia(item.getDia());
                        venta.setMes(item.getMes());
                        venta.setYear(item.getYear());
                        venta.setFecha(item.getFecha());
                        venta.setCaja(item.getCaja());
                        venta.setIdUsuario(item.getIdUsuario());
                        listProduct.add(venta);
                        venta = new Ventas();
                    } else {
                        break;
                    }

                }
            }
        } catch (ParseException ex) {

        }
        return listProduct;
    }
}
