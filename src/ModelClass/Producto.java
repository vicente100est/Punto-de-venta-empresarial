/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ModelClass;

import Graphics.RenderCelda;
import Connection.Consult;
import Graphics.PaintLabel;
import Models.Categorias;
import Models.Departamentos;
import Models.Productos;
import java.awt.Color;
import java.awt.image.BufferedImage;
import java.util.List;
import java.util.concurrent.ThreadLocalRandom;
import java.util.stream.Collectors;
import javax.swing.DefaultComboBoxModel;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JTable;
import javax.swing.table.DefaultTableModel;
import net.sourceforge.jbarcodebean.model.Interleaved25;

/**
 *
 * @author AlexJPZ
 */
public class Producto extends Consult {

    private String sql, precios, codeBarra;
    private int idDpto;
    private Object[] obect;
    private Categorias cat = null;
    private Departamentos dpt = null;
    private DefaultTableModel modelo1, modelo2;
    private DefaultComboBoxModel model;

    public void getProductos(JTable table, String campo) {
        String[] registros = new String[8];
        String[] titulos = {"Id", "Producto", "Cantidad", "Precio", "Importe", "Proveedor", "Fecha", "Codigo"};
        modelo1 = new DefaultTableModel(null, titulos);
        if (campo.equals("")) {
            tempo_productos = tempoProductos().stream().collect(Collectors.toList());
        } else {
            tempo_productos = tempoProductos().stream()
                    .filter(t -> t.getProducto().startsWith(campo))
                    .collect(Collectors.toList());
        }

        tempo_productos.forEach(item -> {
            registros[0] = String.valueOf(item.getIdCompra());
            registros[1] = item.getProducto();
            registros[2] = String.valueOf(item.getCantidad());
            registros[3] = item.getPrecio();
            registros[4] = item.getImporte();
            registros[5] = item.getProveedor();
            registros[6] = item.getFecha();
            registros[7] = item.getCodigo();
            modelo1.addRow(registros);
        });
        table.setModel(modelo1);
        table.setRowHeight(30);
        table.getColumnModel().getColumn(0).setMaxWidth(0);
        table.getColumnModel().getColumn(0).setMinWidth(0);
        table.getColumnModel().getColumn(0).setPreferredWidth(0);
        table.setDefaultRenderer(Object.class, new RenderCelda(4,0));
    }

    public DefaultTableModel getModelo() {
        return modelo1;
    }

    public void codeBarra(JLabel label, String codigosBarra, String producto, String precio) {
        int codigo = 0, count = 0;
        if (codigosBarra.equals("0")) {
            if (precio.equals("")) {
                precios = "000";
            } else {
                precios = precio;
            }
            productos = productos().stream()
                    .filter(t -> t.getProducto().equals(producto)
                    && t.getPrecio().equals("$" + formato.decimal(Double.valueOf(precios))))
                    .collect(Collectors.toList());
            if (0 < productos.size()) {
                codeBarra = productos.get(0).getCodigo();
                codigo(label);
            } else {
                do {
                    codigo = ThreadLocalRandom.current().nextInt(100000, 1000000000 + 1);
                    codeBarra = String.valueOf(codigo);
                    productos = productos().stream()
                            .filter(p -> p.getCodigo().equals(codeBarra))
                            .collect(Collectors.toList());
                    count = productos.size();
                } while (count > 0);
                codigo(label);
            }
        } else {
            codeBarra = codigosBarra;
            codigo(label);
        }
    }

    private void codigo(JLabel label) {
        // nuestro tipo de codigo de barra
        barcode.setCodeType(new Interleaved25());
        // nuestro valor a codificar y algunas configuraciones mas
        barcode.setCode(codeBarra);
        barcode.setCheckDigit(true);
        BufferedImage bufferedImage = barcode.draw(new BufferedImage(160, 80,
                BufferedImage.TYPE_INT_RGB));
        PaintLabel p = new PaintLabel(bufferedImage);
        label.removeAll();
        label.add(p);

        label.repaint();
    }

    public boolean verificarPrecioVenta(JLabel label, String precioVenta, String precioCompra,
            int funcion) {
        double venta, compra;
        boolean verificar = false;
        if (funcion == 1) {
            if (!precioVenta.equalsIgnoreCase("") && !precioCompra.equalsIgnoreCase("")) {
                compra = formato.reconstruir(precioCompra.replace("$", ""));
                venta = Double.valueOf(precioVenta);
                if (compra > venta || compra == venta) {
                    label.setText("Ingrese un precio mayor al precio de compra");
                    label.setForeground(Color.RED);
                    verificar = false;
                } else {
                    label.setText("Precio venta");
                    label.setForeground(new Color(0, 153, 51));
                    verificar = true;
                }
            }
        } else {
            label.setText("Precio venta");
            label.setForeground(new Color(0, 153, 51));
            verificar = true;
        }
        return verificar;
    }

    public Departamentos getDepartamentos(JComboBox ComboBox, String departament) {
        model = new DefaultComboBoxModel();
        departamento = departamentos();
        if (0 < departamento.size()) {
            departamento.forEach(item -> {
                model.addElement(item);
                if (departament.equals(item.getDepartamento())) {
                    dpt = item;
                }
            });
            ComboBox.setModel(model);
        }
        return dpt;
    }

    public Categorias getCategorias(JComboBox ComboBox1, JComboBox ComboBox2, int idDptos,
            String categori) {
        model = new DefaultComboBoxModel();
        if (idDptos == 0) {
            Departamentos dpt = (Departamentos) ComboBox1.getSelectedItem();
            idDpto = dpt.getIdDpto();
        } else {
            idDpto = idDptos;
        }
        categorias = categorias();
        if (0 < categorias.size()) {
            List<Categorias> categoria = categorias().stream()
                    .filter(c -> c.getIdDpto() == idDpto)
                    .collect(Collectors.toList());
            categoria.forEach(item -> {
                model.addElement(item);
                if (categori.equals(item.getCategoria())) {
                    cat = item;
                }
            });
            ComboBox2.setModel(model);
        }
        return cat;
    }

    public void saveProducto(String producto, int cantidad, String precio,
            String departamento, String categoria, String accion, int idProducto,String codigo) {
        int count, cant;
        double precio1 = Double.valueOf(precio);
        switch (accion) {
            case "insert":

                productos = productos().stream()
                        .filter(t -> t.getProducto().equals(producto)
                        && t.getPrecio().equals("$" + formato.decimal(precio1)))
                        .collect(Collectors.toList());
                if (productos.isEmpty()) {
                    sql = "INSERT INTO productos(Codigo,Producto,Precio,Descuento"
                            + ",Departamento,Categoria,Year,Fecha,Compra) VALUES(?,?,?,?,?,?,?,?,?)";
                    obect = new Object[]{
                        codeBarra,
                        producto,
                        "$" + formato.decimal(precio1),
                        "%0.00",
                        departamento,
                        categoria,
                        new Calendario().getYear(),
                        new Calendario().getFecha(),
                        codigo
                    };
                    insert(sql, obect);
                }
                bodega = bodega().stream()
                        .filter(t -> t.getCodigo().equals(codeBarra)).collect(Collectors.toList());
                if (0 < bodega.size()) {
                    cant = cantidad + bodega.get(0).getExistencia();
                    sql = "UPDATE bodega SET IdProducto = ?,Codigo = ?,Existencia = ?,"
                            + "Dia = ?,Mes = ?,Year = ?,Fecha = ? WHERE Id =" + bodega.get(0).getId();
                    obect = new Object[]{
                        bodega.get(0).getIdProducto(),
                        bodega.get(0).getCodigo(),
                        cant,
                        new Calendario().getDia(),
                        new Calendario().getMes(),
                        new Calendario().getYear(),
                        new Calendario().getFecha()
                    };
                    update(sql, obect);
                } else {
                    productos = productos();
                    count = productos.size();
                    if (0 < count) {
                        count--;
                        sql = "INSERT INTO bodega(IdProducto,Codigo,Existencia,Dia,Mes,Year,Fecha)"
                                + "VALUES(?,?,?,?,?,?,?)";
                        obect = new Object[]{
                            productos.get(count).getIdProducto(),
                            codeBarra,
                            cantidad,
                            new Calendario().getDia(),
                            new Calendario().getMes(),
                            new Calendario().getYear(),
                            new Calendario().getFecha()
                        };
                        insert(sql, obect);
                    }
                }
                deleteProducto(idProducto);
                break;
            case "update":
                productos = productos().stream()
                        .filter(t -> t.getIdProducto() == idProducto)
                        .collect(Collectors.toList());
                sql = "UPDATE productos SET Codigo = ?,Producto = ?,Precio = ?,Descuento = ?,"
                        + "Departamento = ?,Categoria = ? "
                        + "WHERE IdProducto =" + idProducto;
                obect = new Object[]{
                    codeBarra,
                    producto,
                    "$" + formato.decimal(precio1),
                    productos.get(0).getDescuento(),
                    departamento,
                    categoria
                };
                update(sql, obect);
                break;
        }
    }

    public void searchProductos(JTable table, String campo, int num_registro, int reg_por_pagina) {
        String[] registros = new String[7];
        String[] titulos = {"Id", "Codigo", "Producto", "Precio", "Descuento", "Departamento", "Categoria"};
        modelo2 = new DefaultTableModel(null, titulos);
        if (campo.equals("")) {
            productos = productos().stream()
                    .skip(num_registro).limit(reg_por_pagina)
                    .collect(Collectors.toList());
        } else {
            productos = productos().stream()
                    .filter(p -> p.getProducto().startsWith(campo))
                    .skip(num_registro).limit(reg_por_pagina)
                    .collect(Collectors.toList());
        }
        productos.forEach(item -> {
            registros[0] = String.valueOf(item.getIdProducto());
            registros[1] = item.getCodigo();
            registros[2] = item.getProducto();
            registros[3] = item.getPrecio();
            registros[4] = item.getDescuento();
            registros[5] = item.getDepartamento();
            registros[6] = item.getCategoria();
            modelo2.addRow(registros);
        });
        table.setModel(modelo2);
        table.setRowHeight(30);
        table.getColumnModel().getColumn(0).setMaxWidth(0);
        table.getColumnModel().getColumn(0).setMinWidth(0);
        table.getColumnModel().getColumn(0).setPreferredWidth(0);
        table.setDefaultRenderer(Object.class, new RenderCelda(4,0));
    }

    public DefaultTableModel getModelo1() {
        return modelo2;
    }

    public List<Productos> producto() {
        return productos();
    }

    public void deleteProducto(int idProducto) {
        sql = "DELETE FROM tempo_productos WHERE IdCompra LIKE ?";
        delete(sql, idProducto);
    }
}
