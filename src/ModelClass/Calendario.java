/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ModelClass;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

/**
 *
 * @author AlexJPZ
 */
public class Calendario {

    private DateFormat dateFormat;
    private Date date = new Date();
    private Calendar c = new GregorianCalendar();
    private String fecha, Dia, Mes, year, hora, minutos, segundos, am_pm;
    private int mes;

    public Calendario() {
//        hora = Integer.toString(c.get(Calendar.HOUR));
//        minutos = Integer.toString(c.get(Calendar.MINUTE));
//        segundos = Integer.toString(c.get(Calendar.SECOND));
        switch (c.get(Calendar.AM_PM)) {
            case 0:
                am_pm = "am";
                break;
            case 1:
                am_pm = "pm";
                break;
        }
        dateFormat = new SimpleDateFormat("dd");
        Dia = dateFormat.format(date);
        dateFormat = new SimpleDateFormat("MM");
        Mes = dateFormat.format(date);
        dateFormat = new SimpleDateFormat("yyyy");
        year = dateFormat.format(date);
        dateFormat = new SimpleDateFormat("dd/MM/yyyy");
        fecha = dateFormat.format(date);
        dateFormat = new SimpleDateFormat("hh:mm:ss");
        hora = dateFormat.format(date) + " " + am_pm;
    }

    public String getFecha() {
        return fecha;
    }

    public String getHora() {
        return hora;
    }

    public String getDia() {
        return Dia;
    }

    public String getMes() {
        return Mes;
    }

    public String getYear() {
        return year;
    }

}
