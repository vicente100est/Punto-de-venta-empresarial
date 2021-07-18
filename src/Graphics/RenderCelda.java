/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Graphics;

import Connection.Consult;
import Models.Bodega;
import java.awt.Color;
import java.awt.Component;
import java.util.List;
import java.util.stream.Collectors;
import javax.swing.JLabel;
import javax.swing.JTable;
import javax.swing.table.DefaultTableCellRenderer;

/**
 *
 * @author AlexJPZ
 */
public class RenderCelda extends DefaultTableCellRenderer {

    private int colum;
    private int valor;
    private Consult consult;
    private String codigo;

    public RenderCelda(int colum, int valor) {
        this.colum = colum;
        this.valor = valor;
        consult = new Consult();
    }

    public Component getTableCellRendererComponent(JTable table,
            Object value,
            boolean isSelected,
            boolean hasFocus,
            int row,
            int column) {

        JLabel cell = (JLabel) super.getTableCellRendererComponent(table, value, isSelected,
                hasFocus, row, column);
        switch (colum) {
            case 0:
                if (column == 5 || column == 3) {
                    cell.setForeground(new Color(0, 153, 102));
                } else {
                    cell.setForeground(Color.BLACK);
                }
                break;
            case 1:
                if (column == 4 || column == 6) {
                    cell.setForeground(new Color(0, 153, 102));
                } else {
                    cell.setForeground(Color.BLACK);
                }
                break;
            case 4:
                if (column == colum || column == 3) {
                    cell.setForeground(new Color(0, 153, 102));
                } else {
                    cell.setForeground(Color.BLACK);
                }
                break;
            case 8:
                if (column == 2) {
                    if (cell.getText().equals("Activa")) {
                        cell.setForeground(Color.RED);
                    } else {
                        cell.setForeground(new Color(0, 153, 102));
                    }

                } else {
                    cell.setForeground(Color.BLACK);
                }
                break;
            case 9:
                if (column == 3) {
                    if (valor >= Integer.valueOf(cell.getText())) {
                        cell.setForeground(Color.RED);
                    } else {
                        cell.setForeground(new Color(0, 153, 102));
                    }

                } else {
                    cell.setForeground(Color.BLACK);
                }
                break;
            case 10:
                if (column == 3 || column == 4) {
                    cell.setForeground(new Color(0, 153, 102));
                } else {
                    if (column == 1) {
                        codigo = cell.getText();
                    }
                    if (column == 2) {
                        List<Bodega> bodega = consult.bodega().stream()
                                .filter(b -> b.getCodigo().equals(codigo))
                                .collect(Collectors.toList());
                        if (0 == bodega.size()) {
                            cell.setForeground(Color.RED);
                        } else {
                            if (0 == bodega.get(0).getExistencia()) {
                                cell.setForeground(Color.RED);
                            } else {
                                cell.setForeground(new Color(0, 153, 102));
                            }
                        }
                    } else {
                        cell.setForeground(Color.BLACK);
                    }

                }
                break;
        }
        return cell;
    }
}
