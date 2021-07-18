/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ModelClass;

import Connection.Consult;
import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.print.PageFormat;
import java.awt.print.Paper;
import java.awt.print.Printable;
import java.awt.print.PrinterException;
import java.awt.print.PrinterJob;
import javax.print.attribute.HashPrintRequestAttributeSet;
import javax.print.attribute.PrintRequestAttributeSet;
import javax.print.attribute.standard.MediaSizeName;
import javax.print.attribute.standard.OrientationRequested;
import javax.swing.JOptionPane;
import javax.swing.JPanel;

/**
 *
 * @author AlexJPZ
 */
public class Imprimir extends Consult implements Printable {

    private JPanel panel;
    private int pos = 0;
    private double importe = 0;

    public int print(Graphics graphics, PageFormat pageFormat, int pageIndex) {
        if (pageIndex == 0) {
            if (panel != null) {
                Graphics2D g2d = (Graphics2D) graphics;
                g2d.translate(pageFormat.getImageableX(), pageFormat.getImageableY());
                g2d.scale(0.70, 0.70); //Reducción de la impresión al 70%
                this.panel.printAll(graphics);
            } else {
                Font normalFont1 = new Font("serif", Font.PLAIN, 12);
                Font normalFont2 = new Font("serif", Font.PLAIN, 8);
                graphics.setFont(normalFont1);
                graphics.drawString("Factura de punto de ventas", 25, 30);
                graphics.setFont(normalFont2);
                graphics.drawString("Producto", 25, 50);
                graphics.drawString("Cantidad", 100, 50);
                graphics.drawString("Importe", 180, 50);
                tempoVentas = TempoVentas();
                if (0 < tempoVentas.size()) {
                    tempoVentas.forEach(item -> {
                        graphics.setColor(Color.BLACK);
                        graphics.drawString(item.getDescripcion(), 25, pos);
                        graphics.drawString(String.valueOf(item.getCantidad()), 100, pos);
                        graphics.setColor(new Color(0, 153, 102));
                        graphics.drawString(item.getImporte(), 180, pos);
                        pos += 15;
                        importe += formato.reconstruir(item.getImporte().replace("$", ""));
                    });
                    pos += 15;
                    graphics.setColor(Color.BLACK);
                    graphics.drawString("Total: ", 25, pos);
                    graphics.drawString("$" + formato.decimal(importe), 100, pos);
                    pos += 15;
//                   
                }
                graphics.drawString("PDHN", 110, pos);
                importe = 0;
            }

            return PAGE_EXISTS;
        } else {
            return NO_SUCH_PAGE;
        }
    }

    public void imprimirRecibo(JPanel panel) {
        this.panel = null;
        this.pos = 0;
        this.importe = 0;
        if (panel != null) {
            this.panel = panel;
        }
        //modificacion para imprimir en papel small
        PrintRequestAttributeSet aset = new HashPrintRequestAttributeSet();

        aset.add(OrientationRequested.PORTRAIT);
        aset.add(MediaSizeName.INVOICE);

        PrinterJob printerJob = PrinterJob.getPrinterJob();
        //Se pasa la instancia del JFrame al PrinterJob
        printerJob.setPrintable(this,opcionesRecibo(printerJob));
        //muestra ventana de dialogo para imprimir
        if (printerJob.printDialog()) {
            try {
                printerJob.print(aset);
                String sql = "DELETE FROM tempo_ventas";
                delete(sql, 0);
            } catch (PrinterException ex) {
                JOptionPane.showMessageDialog(null, "Error:" + ex);
            }
        }
    }
    public PageFormat opcionesRecibo(PrinterJob pj) {

        PageFormat pf = pj.defaultPage();
        Paper paper = pf.getPaper();

        double middleHeight = 8.0;
        double headerHeight = 2.0;
        double footerHeight = 2.0;
        double width = convert_CM_To_PPI(8);// El valor de la impresora solo conoce el punto por pulgada. El valor predeterminado es 72ppi
        double height = convert_CM_To_PPI(headerHeight + middleHeight + footerHeight);
        paper.setSize(width, height);
        paper.setImageableArea(
                0,
                10,
                width,
                height - convert_CM_To_PPI(1)
        );   // definir el tamaño del borde después de que el ancho del área de impresión sea de aproximadamente 180 puntos

        pf.setOrientation(PageFormat.PORTRAIT); // selecciona orientación vertical u horizontal pero para esta época vertical          
        pf.setPaper(paper);

        return pf;
      
    }

    protected static double convert_CM_To_PPI(double cm) {
        return toPPI(cm * 0.393600787);
    }

    protected static double toPPI(double inch) {
        return inch * 72d;
    }
}
