/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Interfaces;

import javax.swing.JLabel;

/**
 *
 * @author AlexJPZ
 */
public interface IUploadimage {
    void CargarImagen(JLabel label,boolean valor,String imagen);

    void CopiarImagen(String fileName);
}
