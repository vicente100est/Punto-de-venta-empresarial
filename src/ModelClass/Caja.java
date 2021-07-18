/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ModelClass;

import Connection.Consult;
import Graphics.RenderCelda;
import Models.Cajas;
import datechooser.beans.DateChooserCombo;
import java.awt.Color;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.List;
import java.util.stream.Collectors;
import javax.swing.JLabel;
import javax.swing.JSpinner;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.SpinnerNumberModel;
import javax.swing.table.DefaultTableModel;

/**
 *
 * @author AlexJPZ
 */
public class Caja extends Consult {

    private String sql, fecha_inicio, ingresos, importIngresos;
    private String estado, inicial;
    private Object[] object;
    private List<Cajas> listCajas;
    private List<JLabel> labelCajas;
    private List<JTextField> textField;
    private DateChooserCombo dateChooserCombo;
    private DefaultTableModel modelo1, modelo2;
    private JTable table, table2;
    private JSpinner spinner;
    private static int idCaja, numCaja, idUsuario;
    private double cajaIngreso, retirarIngresos, newIngresos;
    private double ingreso;

    public Caja() {

    }

    public Caja(Object[] objectCajas, List<JLabel> labelCajas, List<JTextField> textField) {
        this.labelCajas = labelCajas;
        this.textField = textField;
        table = (JTable) objectCajas[0];
        dateChooserCombo = (DateChooserCombo) objectCajas[1];
        idUsuario = (int) objectCajas[2];
        table2 = (JTable) objectCajas[3];
        spinner = (JSpinner) objectCajas[4];
    }

    public List<Cajas> getCaja() {
        listCajas = cajas().stream()
                .filter(c -> c.isEstado() == true)
                .collect(Collectors.toList());
        return listCajas;
    }

    public void insertCajarEgistro(int idUsuario, String nombre, String apellido, String usuario,
            String role, int idCaja, int caja, boolean estado, String hora, String fecha) {
        sql = "INSERT INTO cajas_registros(IdUsuario,Nombre,Apellido,Usuario"
                + ",Role,IdCaja,Caja,Estado,Hora,Fecha)"
                + "VALUES(?,?,?,?,?,?,?,?,?,?)";
        object = new Object[]{idUsuario, nombre, apellido, usuario, role, idCaja, caja, estado, hora, fecha};
        insert(sql, object);
    }

    public void update(int idCaja, boolean estado) {
        sql = "UPDATE cajas SET Estado = ? WHERE IdCaja =" + idCaja;
        object = new Object[]{estado};
        update(sql, object);
    }

    public void restablecerCajas() {
        idCaja = 0;
        numCaja = 0;
        labelCajas.get(0).setText("$0.00");
        labelCajas.get(2).setText("$0.00");
        textField.get(0).setText("");
        Calendar c = new GregorianCalendar();
        dateChooserCombo.setSelectedDate(c);
        getIngresos();
        getCajas();
        SpinnerNumberModel model = new SpinnerNumberModel(
                new Integer(1), // Dato visualizado al inicio en el spinner 
                new Integer(1), // Límite inferior 
                new Integer(10), // Límite superior 
                new Integer(1) // incremento-decremento 
        );
        spinner.setModel(model);
         textField.get(1).setText("");
        labelCajas.get(4).setText("#0");
    }

    public void getIngresos() {
        fecha_inicio = dateChooserCombo.getSelectedPeriodSet().toString();
//        String[] parts = fecha_inicio.split("/");
//        int dia = Integer.valueOf(parts[0]);
//        int mes = Integer.valueOf(parts[1]);
//        fecha_inicio = dia + "/" + mes + "/" + parts[2];
        String[] registros = new String[5];
        String[] titulos = {"Id", "Caja", "Ingreso", "Tipo", "Fecha"};
        modelo1 = new DefaultTableModel(null, titulos);

        cajasIngresos = cajasIngresos().stream()
                .filter(t -> t.getType().equals("Ventas") && t.getFecha().equals(fecha_inicio))
                .collect(Collectors.toList());
        if (0 < cajasIngresos.size()) {
            cajasIngresos.forEach(item -> {
                registros[0] = String.valueOf(item.getId());
                registros[1] = String.valueOf(item.getCaja());
                registros[2] = item.getIngreso();
                registros[3] = item.getType();
                registros[4] = item.getFecha();
                modelo1.addRow(registros);
            });
        }
        table.setModel(modelo1);
        table.setRowHeight(30);
        table.getColumnModel().getColumn(0).setMaxWidth(0);
        table.getColumnModel().getColumn(0).setMinWidth(0);
        table.getColumnModel().getColumn(0).setPreferredWidth(0);
    }

    public void dataTableCaja() {
        int filas = table.getSelectedRow();
        idCaja = Integer.valueOf((String) modelo1.getValueAt(filas, 0));
        numCaja = Integer.valueOf((String) modelo1.getValueAt(filas, 1));
        ingresos = (String) modelo1.getValueAt(filas, 2);
        labelCajas.get(0).setText(ingresos);
        labelCajas.get(2).setText(ingresos);
        if (textField.get(0).getText().equals("")) {
            labelCajas.get(1).setText("Retirar ingresos");
            labelCajas.get(1).setForeground(new Color(102, 102, 102));
        } else {
            labelCajas.get(1).setText("Retirar ingresos");
            labelCajas.get(1).setForeground(new Color(0, 153, 51));
        }
    }

    public boolean cajaIngresos() {
        boolean valor = false;
        if (ingresos != null) {
            if (textField.get(0).getText().equals("")) {
                labelCajas.get(1).setText("Retirar ingresos");
                labelCajas.get(1).setForeground(new Color(102, 102, 102));
                retirarIngrsos();
                valor = false;
            } else {
                labelCajas.get(1).setText("Retirar ingresos");
                labelCajas.get(1).setForeground(new Color(0, 153, 51));
                double ingreso1 = formato.reconstruir(ingresos.replace("$", ""));
                double ingreso2 = Double.valueOf(textField.get(0).getText());
                if (numCaja == 0) {
                    if (ingreso1 >= ingreso2) {
                        retirarIngrsos();
                        valor = true;
                    } else {
                        labelCajas.get(1).setText("Se a sobrepasado del ingreso");
                        labelCajas.get(1).setForeground(Color.RED);
                        valor = false;
                    }
                } else {
                    listCajas = cajas().stream().filter(c -> c.getCaja() == numCaja)
                            .collect(Collectors.toList());
                    if (listCajas.get(0).isEstado()) {
                        if (ingreso1 >= ingreso2) {
                            retirarIngrsos();
                            valor = true;
                        } else {
                            labelCajas.get(1).setText("Se a sobrepasado del ingreso");
                            labelCajas.get(1).setForeground(Color.RED);
                            valor = false;
                        }
                    } else {
                        if (ingreso1 > 100) {
                            double ingresos = ingreso1 - ingreso2;
                            if (ingresos <= 100) {
                                double retirar = ingreso1 - 100;
                                String data = formato.decimal(retirar);
                                labelCajas.get(1).setText("$" + data);
                                labelCajas.get(1).setForeground(Color.RED);
                                labelCajas.get(2).setText("$" + data);
                                if (retirar == ingreso2) {
                                    retirarIngrsos();
                                    valor = true;
                                } else {
                                    valor = false;
                                }
                            } else {
                                retirarIngrsos();
                                valor = true;
                            }
                        } else {
                            labelCajas.get(1).setText("No se pueden retirar los ingresos");
                            labelCajas.get(1).setForeground(Color.RED);
                            valor = false;
                        }
                    }
                }
            }
        } else {
            labelCajas.get(1).setText("Seleccione un ingresos");
            labelCajas.get(1).setForeground(Color.RED);
            valor = false;
        }
        return valor;
    }

    private void retirarIngrsos() {
        if (textField.get(0).getText().equals("")) {
            labelCajas.get(2).setText(ingresos);
        } else {
            cajaIngreso = formato.reconstruir(ingresos.replace("$", ""));
            retirarIngresos = formato.reconstruir(textField.get(0).getText());
            newIngresos = cajaIngreso - retirarIngresos;
            importIngresos = formato.decimal(newIngresos);
            labelCajas.get(2).setText("$" + importIngresos);
        }
    }

    public void guardarIngresos() {
        sql = "UPDATE cajas_ingresos SET Ingreso = ? WHERE Id =" + idCaja;
        object = new Object[]{"$" + importIngresos};
        update(sql, object);
        listUsuario = usuarios().stream()
                .filter(u -> u.getIdUsuario() == idUsuario)
                .collect(Collectors.toList());
        sql = "INSERT INTO reportes_ingresos(Admin,Caja,Ingreso,Fecha) VALUES(?,?,?,?)";
        String nombre = listUsuario.get(0).getNombre() + " " + listUsuario.get(0).getApellido();
        object = new Object[]{
            nombre,
            numCaja,
            "$" + formato.decimal(retirarIngresos),
            new Calendario().getFecha()
        };
        insert(sql, object);
    }

    public void getCajas() {
        //String[] registros = new String[4];
        String[] titulos = {"Id", "Caja", "Estado", "Ingreso"};
        modelo2 = new DefaultTableModel(null, titulos);

        cajas().forEach(items -> {
            if (items.isEstado() == true) {
                estado = "Disponible";
                inicial = "$0.00";
            } else {
                estado = "Activa";
                cajasIngresos = cajasIngresos().stream()
                        .filter(t -> t.getCaja() == items.getCaja() && t.getType().equals("Inicial")
                        && t.getFecha().equals(new Calendario().getFecha()))
                        .collect(Collectors.toList());
                if (0 == cajasIngresos.size()) {
                    inicial = "$0.00";
                } else {
                    cajasIngresos.forEach(item -> {
                        ingreso += formato.reconstruir(item.getIngreso().replace("$", ""));
                    });
                    inicial = "$" + formato.decimal(ingreso);
                }
            }
            String[] registros = {
                String.valueOf(items.getIdCaja()),
                String.valueOf(items.getCaja()),
                estado,
                inicial
            };
            modelo2.addRow(registros);
        });
        ingreso = 0;
        table2.setModel(modelo2);
        table2.setRowHeight(30);
        table2.getColumnModel().getColumn(0).setMaxWidth(0);
        table2.getColumnModel().getColumn(0).setMinWidth(0);
        table2.getColumnModel().getColumn(0).setPreferredWidth(0);
        table2.setDefaultRenderer(Object.class, new RenderCelda(8,0));
    }

    public void registrarCajas() {
        Number caja = (Number) spinner.getValue();
        listCajas = cajas().stream()
                .filter(c -> c.getCaja() == caja.intValue())
                .collect(Collectors.toList());
        if (0 == listCajas.size()) {
            sql = "INSERT INTO cajas(Caja,Estado) VALUES(?,?)";
            object = new Object[]{
                caja.intValue(),
                true
            };
            insert(sql, object);
            restablecerCajas();
        } else {
            labelCajas.get(3).setText("El numero de caja ya esta regitrado");
            labelCajas.get(3).setForeground(Color.RED);
        }
    }

    public void dataCajaIngresos() {
        int filas = table2.getSelectedRow();
        idCaja = Integer.valueOf((String) modelo2.getValueAt(filas, 0));
        numCaja = Integer.valueOf((String) modelo2.getValueAt(filas, 1));
        labelCajas.get(4).setText("#" + numCaja);
    }

    public void insetarIngresoInicial() {
        cajas_registros = cajas_registros().stream()
                .filter(u -> u.getCaja() == numCaja && u.getFecha().equals(new Calendario().getFecha()))
                .collect(Collectors.toList());
        
        cajasIngresos = cajasIngresos().stream()
                .filter(t -> t.getCaja() == numCaja && t.getType().equals("Inicial") && t.getFecha().equals(new Calendario().getFecha()))
                .collect(Collectors.toList());
        if (0 < cajasIngresos.size()) {
            double ingresos = Double.valueOf(textField.get(1).getText());
            ingresos += formato.reconstruir(cajasIngresos.get(0).getIngreso().replace("$", ""));
             sql = "UPDATE cajas_ingresos SET Ingreso = ? WHERE Id =" + cajasIngresos.get(0).getId();
            object = new Object[]{"$"+formato.decimal(ingresos)};
            update(sql, object);
            
        }else{
            sql = "INSERT INTO cajas_ingresos(Caja,Ingreso,Type,IdUsuario,Dia,Mes,Year,Fecha) VALUES(?,?,?,?,?,?,?,?)";
        object = new Object[]{
            numCaja,
            "$"+formato.decimal(Double.valueOf(textField.get(1).getText())),
            "Inicial",
            cajas_registros.get(0).getIdUsuario(),
            new Calendario().getDia(),
            new Calendario().getMes(),
            new Calendario().getYear(),
            new Calendario().getFecha()
        };
        insert(sql, object);
        }
        
        
        restablecerCajas();
    }
}
