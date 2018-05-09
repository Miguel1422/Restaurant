package com.superescuadronalfa.restaurant.database;

import android.util.Log;

import net.sourceforge.jtds.jdbc.Driver;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class Conexion {
    private static String USERNAME = "newuser";
    private static String PASS = "123";
    private static String HOST = "192.168.1.107";
    private static String DB = "Restaurant";
    private static String PORT = "1433";

    public static Connection getConexion() {
        try {


            Driver c = (Driver) Class.forName("net.sourceforge.jtds.jdbc.Driver").newInstance();
            String connectionString = String.format("jdbc:jtds:sqlserver://%s:%s/%s;user=%s;password=%s", HOST, PORT, DB, USERNAME, PASS);
            Connection DbConn = DriverManager.getConnection(connectionString);
            Log.w("Connection", "open");
            return DbConn;
        } catch (InstantiationException | IllegalAccessException | ClassNotFoundException | SQLException e) {
            e.printStackTrace();
        } catch (Exception e) {
            System.out.println();
        }

        return null;
    }
}
