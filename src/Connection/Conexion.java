/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Connection;

import ModelClass.ListClass;
import java.sql.Connection;
import java.sql.DriverManager;

/**
 *
 * @author AlexJPZ
 */
public class Conexion extends ListClass {

    private String db = "system_ventas";
    private String user = "root";
    private String password = "";
    private String urlMysql = "jdbc:mysql://localhost/" + db + "?SslMode=none";
    private String urlSql = "jdbc:sqlserver://localhost:1433;databaseName=" + db + ";integratedSecurity=true;";
    private Connection conn = null;
//1. Cambie el driver por la siguiente ruta
//Class.forName("com.mysql.cj.jdbc.Driver");
//2. Para el problema de la zona horaria  en la parte de la definicion de la url puse lo siguiente:
//private String url = "jdbc:mysql://localhost/"+db+"?useTimezone=true&serverTimezone=UTC";

    public Conexion() {
        try {
//            obtenemos el driver de para mysql
            Class.forName("com.mysql.jdbc.Driver");
            conn = DriverManager.getConnection(this.urlMysql, this.user, this.password);
            //Conexion a SQL Server
            //obtenemos el driver de para SQL Server
//          Class.forName("com.microsoft.sqlserver.jdbc.SQLServerDriver");
//          conn =DriverManager.getConnection(urlSql);

            if (conn != null) {
                System.out.println("Conexión a la base de datos " + this.db + "...... Listo ");
            }
        } catch (Exception ex) {
            System.out.println("Error : " + ex);
        }

    }

    public Connection getConn() {
        return conn;
    }
}
