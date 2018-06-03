package com.superescuadronalfa.restaurant.dbEntities.control;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

public interface IControlEntidad<T> {
    boolean agregar(T entidad);

    boolean editar(T entidad);

    boolean eliminar(T entidad);

    List<T> getLista();

    List<T> getListaFromJSON(JSONArray result) throws JSONException;

    T fromResultSet(ResultSet result) throws SQLException;

    T fromJSON(JSONObject result) throws JSONException;
}
