/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ModelClass;

import Connection.Consult;
import Models.Categorias;
import Models.Departamentos;
import java.util.List;
import java.util.stream.Collectors;
import javax.swing.JTable;
import javax.swing.table.DefaultTableModel;

/**
 *
 * @author AlexJPZ
 */
public class Departamento extends Consult {

    private String sql;
    private Object[] obect;
    private List<Categorias> categoriasoFilter;
    private List<Departamentos> departamentoFilter;
    private DefaultTableModel modelo, modelo1;

    public boolean insertDptoCat(String dptocat, int idDpto, String type) {
        boolean valor = false;
        if (type.equals("dpto")) {
            departamentoFilter = departamentos().stream()
                    .filter(d -> d.getDepartamento().equals(dptocat))
                    .collect(Collectors.toList());
            if (0 == departamentoFilter.size()) {
                sql = "INSERT INTO departamentos(Departamento)VALUES(?)";
                obect = new Object[]{dptocat};
                insert(sql, obect);
                valor = true;
            }
            departamentoFilter.clear();
        } else {
            categoriasoFilter = categorias().stream()
                    .filter(c -> c.getCategoria().equals(dptocat))
                    .collect(Collectors.toList());
            if (0 == categoriasoFilter.size()) {
                sql = "INSERT INTO categorias(Categoria,IdDpto)VALUES(?,?)";
                obect = new Object[]{dptocat, idDpto};
                insert(sql, obect);
                valor = true;
            }
            categoriasoFilter.clear();
        }
        return valor;
    }

    public void searchDepartamentos(JTable table, String campo) {
        String[] registros = new String[2];
        String[] titulos = {"Id", "Departamentos"};
        modelo = new DefaultTableModel(null, titulos);
        if (campo.equals("")) {
            departamentoFilter = departamentos().stream().collect(Collectors.toList());
        } else {
            departamentoFilter = departamentos().stream()
                    .filter(d -> d.getDepartamento().startsWith(campo))
                    .collect(Collectors.toList());
        }
        departamentoFilter.forEach(item -> {
            registros[0] = String.valueOf(item.getIdDpto());
            registros[1] = item.getDepartamento();
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

    public boolean updateDptoCat(String dptocat, int idDpto, int idCat, String type) {
        boolean valor = false;
        if (type.equals("dpto")) {
            departamentoFilter = departamentos().stream()
                    .filter(d -> d.getDepartamento().equals(dptocat))
                    .collect(Collectors.toList());
            if (0 == departamentoFilter.size() || idDpto == departamentoFilter.get(0).getIdDpto()) {
                sql = "UPDATE departamentos SET Departamento = ? WHERE IdDpto =" + idDpto;
                obect = new Object[]{dptocat};
                update(sql, obect);
                valor = true;
            }

        } else {
            categoriasoFilter = categorias().stream()
                    .filter(c -> c.getCategoria().equals(dptocat))
                    .collect(Collectors.toList());
            if (0 == categoriasoFilter.size() || idCat == categoriasoFilter.get(0).getIdCat()) {
                sql = "UPDATE categorias SET Categoria = ?,IdDpto = ? WHERE IdCat =" + idCat;
                obect = new Object[]{dptocat, idDpto};
                update(sql, obect);
                valor = true;
            }

        }
        return valor;

    }

    public void getCategorias(JTable table, int idDpto) {
        String[] registros = new String[3];
        String[] titulos = {"Id", "Categorias", "idDpto"};
        modelo1 = new DefaultTableModel(null, titulos);

        categoriasoFilter = categorias().stream()
                .filter(c -> c.getIdDpto() == idDpto)
                .collect(Collectors.toList());

        categoriasoFilter.forEach(item -> {
            registros[0] = String.valueOf(item.getIdCat());
            registros[1] = item.getCategoria();
            registros[2] = String.valueOf(item.getIdDpto());
            modelo1.addRow(registros);
        });
        table.setModel(modelo1);
        table.setRowHeight(30);
        table.getColumnModel().getColumn(0).setMaxWidth(0);
        table.getColumnModel().getColumn(0).setMinWidth(0);
        table.getColumnModel().getColumn(2).setMaxWidth(0);
        table.getColumnModel().getColumn(2).setMinWidth(0);
        table.getColumnModel().getColumn(0).setPreferredWidth(0);
        table.getColumnModel().getColumn(2).setPreferredWidth(0);
    }

    public DefaultTableModel getModeloCat() {
        return modelo1;
    }

    public void deleteDptoCat(int idDpto, int idCat, String type) {
        if (type.equals("dpto")) {
            sql = "DELETE FROM departamentos WHERE IdDpto LIKE ?";
            delete(sql, idDpto);
            sql = "DELETE FROM categorias WHERE IdDpto LIKE ?";
            delete(sql, idDpto);
        } else {
            sql = "DELETE FROM categorias WHERE IdCat LIKE ?";
            delete(sql, idCat);
        }
    }
}
