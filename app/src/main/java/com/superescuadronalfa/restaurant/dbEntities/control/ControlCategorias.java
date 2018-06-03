package com.superescuadronalfa.restaurant.dbEntities.control;

import com.superescuadronalfa.restaurant.dbEntities.CategoriaProducto;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

public class ControlCategorias implements IControlEntidad<CategoriaProducto> {
    private static ControlCategorias instance;
    private static String ID_CATEGORIA = "id_categoria";
    private static String NOMBRE_CATEGORIA = "nombre_categoria";

    private ControlCategorias() {
    }

    public static ControlCategorias getInstance() {
        if (instance == null) instance = new ControlCategorias();
        return instance;
    }

    @Override
    public boolean agregar(CategoriaProducto entidad) {
        return false;
    }

    @Override
    public boolean editar(CategoriaProducto entidad) {
        return false;
    }

    @Override
    public boolean eliminar(CategoriaProducto entidad) {
        return false;
    }

    @Override
    public List<CategoriaProducto> getLista() {
        return null;
    }

    @Override
    public List<CategoriaProducto> getListaFromJSON(JSONArray result) {
        return null;
    }

    @Override
    public CategoriaProducto fromResultSet(ResultSet result) throws SQLException {
        int idCategoria = result.getInt(ID_CATEGORIA);
        String nombre = result.getString(NOMBRE_CATEGORIA);
        return new CategoriaProducto(idCategoria, nombre);
    }

    @Override
    public CategoriaProducto fromJSON(JSONObject result) throws JSONException {
        int idCategoria = result.getInt(ID_CATEGORIA);
        String nombre = result.getString(NOMBRE_CATEGORIA);
        return new CategoriaProducto(idCategoria, nombre);
    }
}
