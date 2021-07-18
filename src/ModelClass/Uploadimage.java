/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ModelClass;

import Interfaces.IClassModels;
import Interfaces.IUploadimage;
import java.awt.Image;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import javax.swing.ImageIcon;
import javax.swing.JFileChooser;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.filechooser.FileNameExtensionFilter;

/**
 *
 * @author AlexJPZ
 */
public class Uploadimage extends javax.swing.JFrame implements IUploadimage, IClassModels {

    private File archivo;
    private static String urlOrigen = null;

    @Override
    public void CargarImagen(JLabel label, boolean valor, String imagen) {
        if (imagen == null) {
            if (valor) {
                abrirArchivo.setFileFilter(new FileNameExtensionFilter("Archivos de Imagen", "jpg", "png", "gif"));
                int respuesta = abrirArchivo.showOpenDialog(this);
                if (respuesta == JFileChooser.APPROVE_OPTION) {
                    archivo = abrirArchivo.getSelectedFile();
                    urlOrigen = archivo.getAbsolutePath();

                }
            } else {
                urlOrigen = "C:\\Users\\AJpag\\Downloads\\Punto de ventas java1\\src\\Images\\fotos\\default.png";
            }
        } else {
            urlOrigen = imagen;
        }
        Image foto = getToolkit().getImage(urlOrigen);
        foto = foto.getScaledInstance(240, 170, 1);
        label.setIcon(new ImageIcon(foto));
    }

    @Override
    public void CopiarImagen(String fileName) {
        String imgDestino = "C:\\Users\\AJpag\\Downloads\\Punto de ventas java\\src\\Images\\fotos\\" + fileName + ".png";
        try {
            if (urlOrigen == null) {
                urlOrigen = "C:\\Users\\AJpag\\Downloads\\Punto de ventas java\\src\\Images\\fotos\\default.png";
            }
            if (!urlOrigen.equals(imgDestino)) {
                FileInputStream fregis = new FileInputStream(urlOrigen);
                FileOutputStream fsalida = new FileOutputStream(imgDestino, true);
                int b = fregis.read();
                while (b != -1) {
                    fsalida.write(b);
                    b = fregis.read();
                }
                fsalida.flush();
                fsalida.close();
                fregis.close();
            }

        } catch (IOException ioe) {
            JOptionPane.showMessageDialog(null, "Error al Generar Copia");
        }
        urlOrigen = null;
    }

    public static String getUrlOrigen() {
        return urlOrigen;
    }

}
