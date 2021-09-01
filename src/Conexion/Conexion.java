package Conexion;

import com.sun.jdi.connect.spi.Connection;

public class Conexion{
    private String db = "punto_de_ventas";
    private String user = "root";
    private String password = "";
    private String urlMysql = "jdbc:mysql://localhost/" + db + "?SslMode=none";
    private String urlSql = "jdbc:sqlserver://localhost:1433;databaseName=" + db + ";integratedSecurity=true;";
    private Connection conn = null;
    
    public Conexion(){
        
    }
}