package com.superescuadronalfa.restaurant.database;

import android.util.Log;

import com.superescuadronalfa.restaurant.app.AppConfig;

import net.sourceforge.jtds.jdbc.Driver;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class Conexion {
    private static String USERNAME = "newuser";
    private static String PASS = "123";
    private static String HOST = "192.168.1.107";
    private static String DB = "Restaurant";
    private static String PORT = "1433";

    public static Connection getConexion() {
        if (!AppConfig.USE_CONNECTOR) {
            throw new RuntimeException("No se permite usar el conector");
        }
        try {
            Driver c = (Driver) Class.forName("net.sourceforge.jtds.jdbc.Driver").newInstance();
            String connectionString = String.format("jdbc:jtds:sqlserver://%s:%s/%s;user=%s;password=%s", HOST, PORT, DB, USERNAME, PASS);
            Connection DbConn = DriverManager.getConnection(connectionString);
            Log.w("Connection", "open");
            return DbConn;
        } catch (InstantiationException | IllegalAccessException | ClassNotFoundException | SQLException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }

        return null;
    }
}
