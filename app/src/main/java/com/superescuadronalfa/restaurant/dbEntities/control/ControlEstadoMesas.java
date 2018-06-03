package com.superescuadronalfa.restaurant.dbEntities.control;

import com.superescuadronalfa.restaurant.dbEntities.EstadoMesa;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

public class ControlEstadoMesas implements IControlEntidad<EstadoMesa> {
    private static final String FIELD_ID_ESTADO_MESA = "id_estado_mesa";
    private static final String FIELD_NOMBRE_ESTADO = "nombre_estado";
    private static ControlEstadoMesas instance;

    private ControlEstadoMesas() {
    }

    public static ControlEstadoMesas getInstance() {
        if (instance == null) instance = new ControlEstadoMesas();
        return instance;
    }

    @Override
    public boolean agregar(EstadoMesa entidad) {
        return false;
    }

    @Override
    public boolean editar(EstadoMesa entidad) {
        return false;
    }

    @Override
    public boolean eliminar(EstadoMesa entidad) {
        return false;
    }

    @Override
    public List<EstadoMesa> getLista() {
        return null;
    }

    @Override
    public List<EstadoMesa> getListaFromJSON(JSONArray result) {
        return null;
    }

    @Override
    public EstadoMesa fromResultSet(ResultSet result) throws SQLException {
        int idEstadoMesa = result.getInt(FIELD_ID_ESTADO_MESA);
        String nombreEstado = result.getString(FIELD_NOMBRE_ESTADO);
        return new EstadoMesa(idEstadoMesa, nombreEstado);
    }

    @Override
    public EstadoMesa fromJSON(JSONObject result) throws JSONException {
        int idEstadoMesa = result.getInt(FIELD_ID_ESTADO_MESA);
        String nombreEstado = result.getString(FIELD_NOMBRE_ESTADO);
        return new EstadoMesa(idEstadoMesa, nombreEstado);
    }
}
