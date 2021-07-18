/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ModelClass;

import Connection.Consult;
import Interfaces.IUploadimage;
import Models.Cajas;
import Models.Roles;
import Models.Usuarios;
import java.awt.Color;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.List;
import java.util.stream.Collectors;
import javax.swing.DefaultComboBoxModel;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.table.DefaultTableModel;

/**
 *
 * @author AlexJPZ
 */
public class Usuario extends Consult implements IUploadimage {

    private Calendar c = new GregorianCalendar();
    private Caja caja = new Caja();
    private List<Usuarios> listUsuarios;
    private List<Cajas> listCaja;
    private DefaultComboBoxModel model;
    private static DefaultTableModel modelo1, modelo2;
    private Object[] object;
    private JTextField textField1, textField2, textField3, textField4, textField5, textField6, textField7;
    private JLabel label1, label2, label3, label4, label5, label6, label7,label8,label9;
    private JComboBox ComboBoxUser_Roles;
    private JTable table;
    private String sql, imagenes;
    private static String fileName, accion = "insert";
    private static int pageSize = 2, idUsuario;

    public Usuario() {
        listUsuario = new ArrayList<Usuarios>();
        listCaja = new ArrayList<Cajas>();
    }

    public Usuario(Object[] textFieldObject, Object[] labelsObject, JComboBox ComboBoxUser_Roles,
            JTable table) {
        this.table = table;
        this.ComboBoxUser_Roles = ComboBoxUser_Roles;
        textField1 = (JTextField) textFieldObject[0];
        textField2 = (JTextField) textFieldObject[1];
        textField3 = (JTextField) textFieldObject[2];
        textField4 = (JTextField) textFieldObject[3];
        textField5 = (JTextField) textFieldObject[4];
        textField6 = (JTextField) textFieldObject[5];
        textField7 = (JTextField) textFieldObject[6];

        label1 = (JLabel) labelsObject[0];
        label2 = (JLabel) labelsObject[1];
        label3 = (JLabel) labelsObject[2];
        label4 = (JLabel) labelsObject[3];
        label5 = (JLabel) labelsObject[4];
        label6 = (JLabel) labelsObject[5];
        label7 = (JLabel) labelsObject[6];
        label8 = (JLabel) labelsObject[7];
        label9 = (JLabel) labelsObject[8];
    }

    public Object[] login(String usuario, String password) {
        listUsuario.clear();
        listUsuarios = usuarios().stream()
                .filter(P -> P.getUsuario().equals(usuario))
                .collect(Collectors.toList());
        if (0 < listUsuarios.size()) {
            try {
                String pass = Encriptar.decrypt(listUsuarios.get(0).getPassword());
                if (pass.equals(password)) {
                    listUsuario = listUsuarios;
                    int idUsuario = listUsuarios.get(0).getIdUsuario();
                    String nombre = listUsuarios.get(0).getNombre();
                    String apellido = listUsuarios.get(0).getApellido();
                    String user = listUsuarios.get(0).getUsuario();
                    String role = listUsuarios.get(0).getRole();
                    if (role.equals("Admin")) {
                        caja.insertCajarEgistro(idUsuario, nombre, apellido, user, role, 0, 0, false, new Calendario().getHora(), new Calendario().getFecha());
                    } else {
                        listCaja = caja.getCaja();
                        int idCaja = listCaja.get(0).getIdCaja();
                        int cajas = listCaja.get(0).getCaja();
                        boolean estado = listCaja.get(0).isEstado();
                        caja.update(idCaja, false);
                        caja.insertCajarEgistro(idUsuario, nombre, apellido, user, role, idCaja, cajas, estado, new Calendario().getHora(), new Calendario().getFecha());
                    }
                }
            } catch (Exception ex) {

            }
        }else{
            //error al iniciar sesion con cualquier dato
           
             listUsuario = new ArrayList<Usuarios>();
        
        }
        Object[] objects = {listUsuario, listCaja};
        return objects;
    }

    public DefaultComboBoxModel getRoles(JComboBox ComboBox, String role) {
        model = new DefaultComboBoxModel();
        if (0 < roles().size()) {
            if (role != null) {
                List<Roles> roles = roles().stream().filter(r -> r.getRole().equals(role))
                        .collect(Collectors.toList());
                model.addElement(roles.get(0));
            }
            roles().forEach(item -> {
                if (role != null) {
                    if (!role.equals(item.getRole())) {
                        model.addElement(item);
                    }
                } else {
                    model.addElement(item);
                }

            });
            ComboBox.setModel(model);
        }
        return model;
    }

    public boolean registrarUsuario() {
        boolean valor = false;
        if (textField1.getText().equals("")) {
            label1.setText("Ingrese el nombre");
            label1.setForeground(Color.RED);
            textField1.requestFocus();
        } else {
            if (textField2.getText().equals("")) {
                label2.setText("Ingrese el apellido");
                label2.setForeground(Color.RED);
                textField2.requestFocus();
            } else {
                if (textField3.getText().equals("")) {
                    label3.setText("Ingrese el telefono");
                    label3.setForeground(Color.RED);
                    textField3.requestFocus();
                } else {
                    if (textField4.getText().equals("")) {
                        label4.setText("Ingrese la direccion");
                        label4.setForeground(Color.RED);
                        textField4.requestFocus();
                    } else {
                        if (textField5.getText().equals("")) {
                            label5.setText("Ingrese el email");
                            label5.setForeground(Color.RED);
                            textField5.requestFocus();
                        } else {
                            if (textField6.getText().equals("")) {
                                label6.setText("Ingrese el password");
                                label6.setForeground(Color.RED);
                                textField6.requestFocus();
                            } else {
                                if (textField7.getText().equals("")) {
                                    label7.setText("Ingrese el usuario");
                                    label7.setForeground(Color.RED);
                                    textField7.requestFocus();
                                } else {
                                    if (evento.isEmail(textField5.getText())) {
                                        int count;
                                        List<Usuarios> listEmail = usuarios().stream()
                                                .filter(u -> u.getEmail().equals(textField5.getText()))
                                                .collect(Collectors.toList());
                                        count = listEmail.size();
                                        List<Usuarios> listUsuarios = usuarios().stream()
                                                .filter(u -> u.getUsuario().equals(textField7.getText()))
                                                .collect(Collectors.toList());
                                        count += listUsuarios.size();
                                        if (count == 2) {
                                            if (idUsuario == listUsuarios.get(0).getIdUsuario()
                                                    && idUsuario == listEmail.get(0).getIdUsuario()) {
                                                valor = true;
                                                valor = ejecutar(valor);
                                            } else {
                                                if (idUsuario != listEmail.get(0).getIdUsuario()) {
                                                    label5.setText("El email ya esta registrado");
                                                    label5.setForeground(Color.RED);
                                                    textField5.requestFocus();
                                                    valor = false;
                                                }
                                                if (idUsuario != listUsuarios.get(0).getIdUsuario()) {
                                                    label7.setText("El usuario ya esta registrado");
                                                    label7.setForeground(Color.RED);
                                                    textField7.requestFocus();
                                                    valor = false;
                                                }
                                            }
                                        } else {
                                            if (count == 0) {
                                                valor = true;
                                                valor = ejecutar(valor);
                                            } else {
                                                if (0 != listEmail.size()) {
                                                    if (idUsuario == listEmail.get(0).getIdUsuario()) {
                                                        valor = true;
                                                    } else {
                                                        label5.setText("El email ya esta registrado");
                                                        label5.setForeground(Color.RED);
                                                        textField5.requestFocus();
                                                        valor = false;
                                                    }
                                                }
                                                if (0 != listUsuarios.size()) {
                                                    if (idUsuario == listUsuarios.get(0).getIdUsuario()) {
                                                        valor = true;
                                                    } else {
                                                        label7.setText("El usuario ya esta registrado");
                                                        label7.setForeground(Color.RED);
                                                        textField7.requestFocus();
                                                        valor = false;
                                                    }
                                                }
                                                valor = ejecutar(valor);
                                            }
                                        }
                                    } else {
                                        label5.setText("El email no es valido");
                                        label5.setForeground(Color.RED);
                                        textField5.requestFocus();
                                        valor = false;
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }

        return valor;
    }

    private boolean ejecutar(boolean valor) {
        boolean data = false;
        if (valor) {
            data = insertUsuario();
        }
        return data;
    }

    private boolean insertUsuario() {
        boolean valor = false;
        String archivoOrigen = Uploadimage.getUrlOrigen();
        imagenes = textField5.getText();
        if (archivoOrigen != null) {
            CopiarImagen(imagenes);
        } else {
            if (fileName != null) {
                imagenes = fileName;
            } else {
                CopiarImagen(imagenes);
            }
        }
        Roles roles = (Roles) ComboBoxUser_Roles.getSelectedItem();
        switch (accion) {
            case "insert":
                sql = "INSERT INTO usuarios(Nombre,Apellido,Telefono,Direccion,"
                        + "Email,Usuario,Password,Role,Imagen) VALUES(?,?,?,?,?,?,?,?,?)";
                try {
                    object = new Object[]{
                        textField1.getText(),
                        textField2.getText(),
                        textField3.getText(),
                        textField4.getText(),
                        textField5.getText(),
                        textField7.getText(),
                        Encriptar.encrypt(textField6.getText()),
                        roles.getRole(),
                        textField5.getText()
                    };
                    insert(sql, object);
                valor = true;
                } catch (Exception ex) {
                    valor = false;
                }
                
                break;
                
            case "update":
                sql = "UPDATE usuarios SET Nombre = ?,Apellido = ?,Telefono = ?,Direccion = ?,Email = ?"
                        + ",Usuario = ?,Password = ?,Role = ?,Imagen = ? WHERE IdUsuario =" + idUsuario;
                try {
                    object = new Object[]{
                        textField1.getText(),
                        textField2.getText(),
                        textField3.getText(),
                        textField4.getText(),
                        textField5.getText(),
                        textField7.getText(),
                        Encriptar.encrypt(textField6.getText()),
                        roles.getRole(),
                        textField5.getText()
                    };
                     update(sql, object);
                    valor = true;
                } catch (Exception ex) {
                    valor = false;
                }
                break;
        }

        return valor;
    }

    public void searchUsuarios(JTable table, String campo, int num_registro, int reg_por_pagina) {
        String[] registros = new String[10];
        String[] titulos = {"IdUsuario", "Nombre", "Apellido", "Telefono", "Direccion",
            "Email", "Usuario", "Password", "Role", "Imagen"};
        modelo1 = new DefaultTableModel(null, titulos);
        if (campo.equals("")) {
            listUsuario = usuarios().stream()
                    .skip(num_registro).limit(reg_por_pagina)
                    .collect(Collectors.toList());
        } else {
            listUsuario = usuarios().stream()
                    .filter(u -> u.getNombre().startsWith(campo) || u.getApellido().startsWith(campo)
                    || u.getEmail().startsWith(campo) || u.getTelefono().startsWith(campo))
                    .skip(num_registro).limit(reg_por_pagina)
                    .collect(Collectors.toList());
        }
        listUsuario.forEach(item -> {
            registros[0] = String.valueOf(item.getIdUsuario());
            registros[1] = item.getNombre();
            registros[2] = item.getApellido();
            registros[3] = item.getTelefono();
            registros[4] = item.getDireccion();
            registros[5] = item.getEmail();
            registros[6] = item.getUsuario();
            registros[7] = item.getPassword();
            registros[8] = item.getRole();
            registros[9] = item.getImagen();

            modelo1.addRow(registros);
        });
        table.setModel(modelo1);
        table.setRowHeight(30);
        table.getColumnModel().getColumn(0).setMaxWidth(0);
        table.getColumnModel().getColumn(0).setMinWidth(0);
        table.getColumnModel().getColumn(0).setPreferredWidth(0);

        table.getColumnModel().getColumn(7).setMaxWidth(0);
        table.getColumnModel().getColumn(7).setMinWidth(0);
        table.getColumnModel().getColumn(7).setPreferredWidth(0);

        table.getColumnModel().getColumn(9).setMaxWidth(0);
        table.getColumnModel().getColumn(9).setMinWidth(0);
        table.getColumnModel().getColumn(9).setPreferredWidth(0);
    }

    public void dataTableUsuarios() {
        accion = "update";
        int filas = table.getSelectedRow();
        idUsuario = Integer.valueOf((String) modelo1.getValueAt(filas, 0));
        textField1.setText((String) modelo1.getValueAt(filas, 1));
        textField2.setText((String) modelo1.getValueAt(filas, 2));
        textField3.setText((String) modelo1.getValueAt(filas, 3));
        textField4.setText((String) modelo1.getValueAt(filas, 4));
        textField5.setText((String) modelo1.getValueAt(filas, 5));
        textField7.setText((String) modelo1.getValueAt(filas, 6));
        String pass;
        try {
            pass = Encriptar.decrypt((String) modelo1.getValueAt(filas, 7));
            textField6.setText(pass);
        } catch (Exception ex) {

        }
        ComboBoxUser_Roles.setModel(getRoles(ComboBoxUser_Roles, (String) modelo1.getValueAt(filas, 8)));
        fileName = (String) modelo1.getValueAt(filas, 9);
        String imgDestino = "E:\\Google Drive\\Documentos\\NetBeans\\Proyects\\Punto de ventas java\\src\\Images\\fotos\\" + fileName + ".png";
        CargarImagen(label8, false, imgDestino);
        label1.setForeground(new Color(0, 153, 51));
        label2.setForeground(new Color(0, 153, 51));
        label3.setForeground(new Color(0, 153, 51));
        label4.setForeground(new Color(0, 153, 51));
        label5.setForeground(new Color(0, 153, 51));
        label6.setForeground(new Color(0, 153, 51));
        label7.setForeground(new Color(0, 153, 51));
    }

    public void restablecerUsuarios() {
        accion = "insert";
        idUsuario = 0;
        textField1.setText("");
        textField2.setText("");
        textField3.setText("");
        textField4.setText("");
        textField5.setText("");
        textField6.setText("");
        textField7.setText("");
        label1.setText("Nombre");
        label1.setForeground(new Color(70, 106, 124));
        label2.setText("Apellido");
        label2.setForeground(new Color(70, 106, 124));
        label3.setText("Telefono");
        label3.setForeground(new Color(70, 106, 124));
        label4.setText("Direccion");
        label4.setForeground(new Color(70, 106, 124));
        label5.setText("Email");
        label5.setForeground(new Color(70, 106, 124));
        label6.setText("Password");
        label6.setForeground(new Color(70, 106, 124));
        label7.setText("Usiario");
        label7.setForeground(new Color(70, 106, 124));
        getRoles(ComboBoxUser_Roles, null);
        searchUsuarios(table, "", 0, pageSize);
        CargarImagen(label8, false, null);
        new Paginador(8, table, label9, 1).primero();
    }

    @Override
    public void CargarImagen(JLabel label, boolean valor, String urlImagen) {
        imagen.CargarImagen(label, valor, urlImagen);
    }

    @Override
    public void CopiarImagen(String fileName) {
        imagen.CopiarImagen(fileName);
    }
    public List<Usuarios> getUsuarios(){
        return usuarios();
    }
}
