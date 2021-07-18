/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ModelClass;

import java.text.DecimalFormat;
import java.text.ParseException;

/**
 *
 * @author AlexJPZ
 */
public class FormatDecimal {

    DecimalFormat formateador = new DecimalFormat("###,###,##0.00");
    Number numero;

    public String decimal(double formato) {
        return formateador.format(formato);
    }

    public double reconstruir(String formato) {
        try {
            numero = formateador.parse(formato.replace(" ", ""));
        } catch (ParseException ex) {
            System.out.println("Error : " + ex);
        }
        return numero.doubleValue();
    }
}
