package Conexion;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class Conexion {

    private String db = "punto_de_ventas";
    private String user = "root";
    private String password = "";
    private String urlMysql = "jdbc:mysql://localhost/" + db + "?SslMode=none";
    private String urlSql = "jdbc:sqlserver://localhost:1433;databaseName=" + db + ";integratedSecurity=true;";

    private Connection conn = null;

    public Conexion() {
        try {
            //Obtener driver MySQL
            //Class.forName("com.mysql.jdbc.Driver");
            //conn = DriverManager.getConnection(this.urlMysql, this.user, this.password);

            Class.forName("com.microsoft.sqlserver.jdbc.SQLServerDriver");
            conn = DriverManager.getConnection(urlSql);

            if (conn != null) {
                System.out.println("Conexion a la base de datos " + this.db + "... Listo");
            }
        } catch (ClassNotFoundException | SQLException ex) {
            System.out.println("Error: " + ex);
        }

    }

    public Connection getConn() {
        return conn;
    }

}
