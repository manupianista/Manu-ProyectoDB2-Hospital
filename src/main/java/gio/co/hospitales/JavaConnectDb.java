package gio.co.hospitales;

import java.sql.Connection;
import java.sql.DriverManager;
//import java.sql.SQLException;

public class JavaConnectDb {

    public static int hospNum = 1;

    public static int getHospNum() {
        return hospNum;
    }

    public static Connection connectDbH(int num) {
        Connection conn = null;
        int numHospital = 1;
        try {
            Class.forName("oracle.jdbc.driver.OracleDriver");
            switch (numHospital) {
                case 1:
                    conn = DriverManager.getConnection("jdbc:oracle:thin:@192.168.69.4:1521", "c##hospital1", "adminm");
                    break;
                case 2:
                    //conexion del abue
                    conn = DriverManager.getConnection("jdbc:oracle:thin:@25.65.236.60:1521:orcl", "c##hospital2", "adminm");
                    break;
                case 3:
                    //conexion de manu
                    conn = DriverManager.getConnection("jdbc:oracle:thin:@25.74.104.162:1521:xe", "c##hospital3", "manu");
                    break;
                default:
                    //por defecto es al hospital 1
                    conn = DriverManager.getConnection("jdbc:oracle:thin:@192.168.69.4:1521:XE", "c##hospital1", "adminm");
                    break;
            }
        } catch (Exception e) {
            System.err.println(e);
        }
        return conn;

    }
}




