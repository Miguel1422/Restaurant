package com.superescuadronalfa.restaurant.dbEntities.control;

import com.superescuadronalfa.restaurant.dbEntities.ProductoVariante;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.math.BigDecimal;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.StringTokenizer;

public class ControlProductoVariantes implements IControlEntidad<ProductoVariante> {
    private static final String ID_PRODUCTO_VARIANTE = "id_producto_variante";
    private static final String NOMBRE_VARIANTE = "nombre_variante";
    private static final String DESCRIPCION = "descripcion";
    private static final String DISPONIBLE = "disponible";
    private static final String PRECIO_VARIANTE = "precio_variante";
    private static ControlProductoVariantes instance;

    private ControlProductoVariantes() {
    }

    public static ControlProductoVariantes getInstance() {
        if (instance == null) instance = new ControlProductoVariantes();
        return instance;
    }

    @Override
    public boolean agregar(ProductoVariante entidad) {
        return false;
    }

    @Override
    public boolean editar(ProductoVariante entidad) {
        return false;
    }

    @Override
    public boolean eliminar(ProductoVariante entidad) {
        return false;
    }

    @Override
    public List<ProductoVariante> getLista() {
        return null;
    }

    @Override
    public List<ProductoVariante> getListaFromJSON(JSONArray result) {
        return null;
    }

    @Override
    public ProductoVariante fromResultSet(ResultSet result) throws SQLException {
        int id = result.getInt(ID_PRODUCTO_VARIANTE);
        String nombre = result.getString(NOMBRE_VARIANTE);
        String descripcion = result.getString(DESCRIPCION);
        boolean disponible = result.getBoolean(DISPONIBLE);
        BigDecimal precio = result.getBigDecimal(PRECIO_VARIANTE);
        return new ProductoVariante(id, nombre, descripcion, disponible, precio);
    }

    @Override
    public ProductoVariante fromJSON(JSONObject result) throws JSONException {
        int id = result.getInt(ID_PRODUCTO_VARIANTE);
        String nombre = result.getString(NOMBRE_VARIANTE);
        String descripcion = result.getString(DESCRIPCION);
        boolean disponible = result.getBoolean(DISPONIBLE);
        BigDecimal precio = new BigDecimal(result.getString(PRECIO_VARIANTE));
        return new ProductoVariante(id, nombre, descripcion, disponible, precio);
    }

    public String fromListToString(List<ProductoVariante> variantes) {
        StringBuilder variantesString = new StringBuilder();
        for (ProductoVariante pv : variantes) {
            if (variantesString.length() > 0) variantesString.append(',');
            variantesString.append(pv.getIdProductoVariante());
        }
        return variantesString.toString();
    }

    public List<ProductoVariante> fromStringList(String variantes) {
        if (variantes == null || variantes.equals("null")) return new ArrayList<>();
        ArrayList<ProductoVariante> listvariantes = new ArrayList<>();
        StringTokenizer st = new StringTokenizer(variantes, "|");
        while (st.hasMoreTokens()) {
            StringTokenizer st2 = new StringTokenizer(st.nextToken(), ",");
            int id = Integer.parseInt(st2.nextToken().trim());
            String nombre = st2.nextToken().trim();
            BigDecimal precio = new BigDecimal(st2.nextToken().trim());
            listvariantes.add(new ProductoVariante(id, nombre, "TODO agregar descripcion aqui", true, precio));
            // TODO agregar descripcion
        }
        return listvariantes;
    }

    /****
     * @param result Resuslset donde esta guardada la informacion (variantes como una lista)
     * @return La lista con esas variantes
     */
    public List<ProductoVariante> fromResultSetList(ResultSet result) throws SQLException {
        String variantes = result.getString("variantes");
        return fromStringList(variantes);
    }

    public List<ProductoVariante> fromJSONList(JSONObject result) throws JSONException {
        String variantes = result.getString("variantes");
        if (variantes == null || variantes.equals("null")) return new ArrayList<>();
        StringTokenizer st = new StringTokenizer(variantes, "|");

        ArrayList<ProductoVariante> listvariantes = new ArrayList<>();
        while (st.hasMoreTokens()) {
            StringTokenizer st2 = new StringTokenizer(st.nextToken(), ",");
            int id = Integer.parseInt(st2.nextToken().trim());
            String nombre = st2.nextToken().trim();
            BigDecimal precio = new BigDecimal(st2.nextToken().trim());
            listvariantes.add(new ProductoVariante(id, nombre, "TODO agregar descripcion aqui", true, precio));
            // TODO agregar descripcion
        }
        return listvariantes;
    }
}
