package com.superescuadronalfa.restaurant.dbEntities.control;

import com.superescuadronalfa.restaurant.database.DBRestaurant;
import com.superescuadronalfa.restaurant.dbEntities.EstadoMesa;
import com.superescuadronalfa.restaurant.dbEntities.Mesa;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class ControlMesas implements IControlEntidad<Mesa> {

    private static final String FIELD_ID_MESA = "id_mesa";
    private static final String FIELD_ID_ESTADO = "id_estado";
    private static final String FIELD_NOMBRE_MESA = "nombre_mesa";
    private static ControlMesas instance;

    private ControlMesas() {
    }

    public static ControlMesas getInstance() {
        if (instance == null) instance = new ControlMesas();
        return instance;
    }

    @Override
    public boolean agregar(Mesa entidad) {
        return false;
    }

    @Override
    public boolean editar(Mesa entidad) {
        return false;
    }

    @Override
    public boolean eliminar(Mesa entidad) {
        return false;
    }

    @Override
    public List<Mesa> getLista() {
        String listMesas = "SELECT * FROM Mesa AS M\n" +
                "INNER JOIN EstadoMesa AS EM\n" +
                "ON M.id_estado = EM.id_estado_mesa";

        ArrayList<Mesa> list = new ArrayList<>();
        try {
            ResultSet result = DBRestaurant.ejecutaConsulta(listMesas);
            while (result.next()) {
                Mesa ac = fromResultSet(result);
                EstadoMesa em = ControlEstadoMesas.getInstance().fromResultSet(result);
                ac.setEstadoMesa(em);
                list.add(ac);
            }
            return list;

        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            DBRestaurant.close();
        }

        return null;
    }

    @Override
    public List<Mesa> getListaFromJSON(JSONArray result) throws JSONException {
        ArrayList<Mesa> mesas = new ArrayList<>();
        for (int i = 0; i < result.length(); i++) {
            JSONObject mesaJSON = result.getJSONObject(i);
            Mesa ac = fromJSON(mesaJSON);
            EstadoMesa em = ControlEstadoMesas.getInstance().fromJSON(mesaJSON);
            ac.setEstadoMesa(em);
            mesas.add(ac);
        }
        return mesas;
    }


    @Override
    public Mesa fromResultSet(ResultSet result) throws SQLException {
        int idMesa = result.getInt(FIELD_ID_MESA);
        int idEstado = result.getInt(FIELD_ID_ESTADO);
        String nombre = result.getString(FIELD_NOMBRE_MESA);

        return new Mesa(idMesa, idEstado, nombre);
    }

    @Override
    public Mesa fromJSON(JSONObject result) throws JSONException {
        int idMesa = result.getInt(FIELD_ID_MESA);
        int idEstado = result.getInt(FIELD_ID_ESTADO);
        String nombre = result.getString(FIELD_NOMBRE_MESA);

        return new Mesa(idMesa, idEstado, nombre);
    }
}
