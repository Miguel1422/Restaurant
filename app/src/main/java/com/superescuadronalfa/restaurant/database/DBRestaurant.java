package com.superescuadronalfa.restaurant.database;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class DBRestaurant {
    private static Connection conexion;
    private static PreparedStatement statement;
    private static CallableStatement storedProcedure;

    public static boolean ejecutaComando(String query, Object... params) {
        conexion = Conexion.getConexion();
        try {
            statement = conexion.prepareStatement(query);
            int index = 1;
            for (Object o : params) {
                statement.setObject(index++, o);
            }
            boolean res = statement.executeUpdate() > 0;
            statement.close();
            conexion.close();
            return res;
        } catch (SQLException | NullPointerException e) {
            e.printStackTrace();
        }
        return false;
    }

    public static ResultSet ejecutaConsulta(String query, Object... params) {
        conexion = Conexion.getConexion();
        try {
            statement = conexion.prepareStatement(query);
            int index = 1;
            for (Object o : params) {
                statement.setObject(index++, o);
            }
            ResultSet res = statement.executeQuery();
            return res;
        } catch (SQLException | NullPointerException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public static ResultSet ejecutaConsultaPreparada(String query, Object... params) {
        conexion = Conexion.getConexion();
        try {
            storedProcedure = conexion.prepareCall(query);
            int index = 1;
            for (Object o : params) {
                storedProcedure.setObject(index++, o);
            }
            ResultSet res = storedProcedure.executeQuery();
            return res;
        } catch (SQLException | NullPointerException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public static void close() {
        if (statement != null) {
            try {
                statement.close();
                statement = null;
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        if (conexion != null) {
            try {
                conexion.close();
                conexion = null;
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        if (storedProcedure != null) {
            try {
                storedProcedure.close();
                storedProcedure = null;
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }

    }
}
