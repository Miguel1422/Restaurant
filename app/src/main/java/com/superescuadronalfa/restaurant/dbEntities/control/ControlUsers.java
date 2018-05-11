package com.superescuadronalfa.restaurant.dbEntities.control;

import com.superescuadronalfa.restaurant.database.DBRestaurant;
import com.superescuadronalfa.restaurant.dbEntities.Trabajador;
import com.superescuadronalfa.restaurant.dbEntities.User;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

public class ControlUsers implements IControlEntidad<User> {
    private static String ADD = "INSERT INTO [User] VALUES (?, ?, ?, ?)";
    private static ControlUsers instance;

    private ControlUsers() {
    }

    public static ControlUsers getInstance() {
        if (instance == null) {
            instance = new ControlUsers();
        }
        return instance;
    }

    @Override
    public boolean agregar(User entidad) {
        return DBRestaurant.ejecutaComando(ADD, entidad.getIdTrabajador(), entidad.getUsername(), entidad.getHash(), entidad.getSalt());
    }

    @Override
    public boolean editar(User entidad) {
        return false;
    }

    @Override
    public boolean eliminar(User entidad) {
        return false;
    }

    @Override
    public List<User> getLista() {
        return null;
    }

    @Override
    public User fromResultSet(ResultSet result) throws SQLException {
        String user = result.getString("username");
        byte[] hash = result.getBytes("hash");
        byte[] salt = result.getBytes("salt");
        int idTrabajador = result.getInt("id_trabajador");
        User u = new User(idTrabajador, user.toString(), hash, salt);
        return u;
    }

    public User busca(String username) {
        String findUser = "" +
                "SELECT * FROM [User] AS u \n" +
                "INNER JOIN Trabajador AS t \n" +
                "ON u.id_trabajador = t.id_trabajador \n" +
                "WHERE u.username = ?";


        ResultSet result = DBRestaurant.ejecutaConsulta(findUser, username);
        try {
            if (!result.next()) return null;
            User u = fromResultSet(result);
            Trabajador t = ControlTrabajadores.getInstance().fromResultSet(result);
            u.setTrabajador(t);

            return u;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        DBRestaurant.close();
        return null;
    }


}
