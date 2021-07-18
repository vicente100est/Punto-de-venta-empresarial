/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Graphics;

import java.awt.Graphics;
import java.awt.Image;
import java.awt.image.BufferedImage;

/**
 *
 * @author AlexJPZ
 */
public class PaintLabel extends javax.swing.JLabel {
    private BufferedImage bufferedImage;

    public PaintLabel(BufferedImage bufferedImage) {
        this.setSize(200, 200);
        this.bufferedImage = bufferedImage;
    }
     @Override
    public void paintComponent(Graphics g){
        Image img = bufferedImage;
        g.drawImage(img, 0,0,this);
    }
}
