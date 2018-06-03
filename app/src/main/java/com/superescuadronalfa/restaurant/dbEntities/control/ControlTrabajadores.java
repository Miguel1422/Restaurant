package com.superescuadronalfa.restaurant.dbEntities.control;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import com.superescuadronalfa.restaurant.database.DBRestaurant;
import com.superescuadronalfa.restaurant.dbEntities.Trabajador;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Date;
import java.util.List;

public class ControlTrabajadores implements IControlEntidad<Trabajador> {
    private static ControlTrabajadores instance;

    private ControlTrabajadores() {
    }

    public static ControlTrabajadores getInstance() {
        if (instance == null) instance = new ControlTrabajadores();
        return instance;
    }

    @Override
    public boolean agregar(Trabajador entidad) {
        return false;
    }

    @Override
    public boolean editar(Trabajador entidad) {
        return false;
    }

    @Override
    public boolean eliminar(Trabajador entidad) {
        return false;
    }

    @Override
    public List<Trabajador> getLista() {
        return null;
    }

    @Override
    public List<Trabajador> getListaFromJSON(JSONArray result) {
        return null;
    }

    @Override
    public Trabajador fromResultSet(ResultSet result) throws SQLException {
        int id_trabajador = result.getInt("id_trabajador");
        String nombre = result.getString("nombre");
        String apellidos = result.getString("apellidos");
        Date fecha_nacimiento = result.getDate("fecha_nacimiento");
        String direccion = result.getString("direccion");
        String telefono = result.getString("telefono");
        int id_puesto = result.getInt("id_puesto");


        Trabajador t = new Trabajador(nombre, apellidos, fecha_nacimiento, direccion, telefono, id_puesto);
        t.setIdTrabajador(id_trabajador);
        return t;
    }

    @Override
    public Trabajador fromJSON(JSONObject result) throws JSONException {
        int id_trabajador = result.getInt("id_trabajador");
        String nombre = result.getString("nombre");
        String apellidos = result.getString("apellidos");
        Date fecha_nacimiento = new Date();
        String direccion = result.getString("direccion");
        String telefono = result.getString("telefono");
        int id_puesto = result.getInt("id_puesto");
        Trabajador t = new Trabajador(nombre, apellidos, fecha_nacimiento, direccion, telefono, id_puesto);
        t.setIdTrabajador(id_trabajador);
        return t;
    }

    public Bitmap burcarImagen(Trabajador trabajador) {
        String findImage = "SELECT imagen FROM TrabajadorImagen WHERE id_trabajador = ?";

        try {
            ResultSet result = DBRestaurant.ejecutaConsulta(findImage, trabajador.getIdTrabajador());
            if (!result.next()) return null;
            byte[] encodedImage = result.getBytes("imagen");
            Bitmap image = BitmapFactory.decodeByteArray(encodedImage, 0, encodedImage.length);
            return image;
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            DBRestaurant.close();
        }

        return null;
    }
}
