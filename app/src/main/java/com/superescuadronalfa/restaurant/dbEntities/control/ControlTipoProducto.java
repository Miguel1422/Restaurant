package com.superescuadronalfa.restaurant.dbEntities.control;

import com.superescuadronalfa.restaurant.database.DBRestaurant;
import com.superescuadronalfa.restaurant.dbEntities.Producto;
import com.superescuadronalfa.restaurant.dbEntities.ProductoVariante;
import com.superescuadronalfa.restaurant.dbEntities.TipoProducto;

import java.math.BigDecimal;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class ControlTipoProducto implements IControlEntidad<TipoProducto> {
    private static ControlTipoProducto instance;
    private static String ID_TIPO = "id_tipo_producto";
    private static String NOMBRE_TIPO = "nombre_tipo";
    private static String PRECIO_TIPO = "precio_tipo";
    private static String VARIANTES = "variantes";

    private ControlTipoProducto() {
    }

    public static ControlTipoProducto getInstance() {
        if (instance == null) instance = new ControlTipoProducto();
        return instance;
    }

    @Override
    public boolean agregar(TipoProducto entidad) {
        return false;
    }

    @Override
    public boolean editar(TipoProducto entidad) {
        return false;
    }

    @Override
    public boolean eliminar(TipoProducto entidad) {
        return false;
    }

    @Override
    public List<TipoProducto> getLista() {
        return null;
    }

    public List<ProductoVariante> getVariantes(TipoProducto tipoProducto) {
        String obtenerVariantes = "" +
                "SELECT * FROM ProductoVariante\n" +
                "WHERE id_tipo_producto = ?\n" +
                "ORDER BY nombre_variante";

        try {
            ResultSet result = DBRestaurant.ejecutaConsulta(obtenerVariantes, tipoProducto.getIdTipoProducto());
            ArrayList<ProductoVariante> variantes = new ArrayList<>();
            while (result.next()) {
                ProductoVariante nueva = ControlProductoVariantes.getInstance().fromResultSet(result);
                nueva.setTipoProducto(tipoProducto);
                variantes.add(nueva);
            }
            return variantes;
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            DBRestaurant.close();
        }
        return null;
    }

    @Override
    public TipoProducto fromResultSet(ResultSet result) throws SQLException {
        int idTipo = result.getInt(ID_TIPO);
        String nombreTipo = result.getString(NOMBRE_TIPO);
        BigDecimal precio = result.getBigDecimal(PRECIO_TIPO);
        return new TipoProducto(idTipo, nombreTipo, precio);
    }

    /**
     * @param result      Resultset con la iinformacion
     * @param hasVariants Si el resulset tiene las variantes
     * @return Tipo producto
     * @throws SQLException
     */
    public TipoProducto fromResultSet(ResultSet result, boolean hasVariants) throws SQLException {
        TipoProducto actual = fromResultSet(result);
        if (!hasVariants) return actual;
        actual.setVariantes(ControlProductoVariantes.getInstance().fromResultSetList(result));
        return actual;
    }

    public List<TipoProducto> getLista(Producto producto) {
        String query = "EXECUTE getTipoProductos @IDProducto = ?";
        try {
            ArrayList<TipoProducto> lista = new ArrayList<>();
            ResultSet result = DBRestaurant.ejecutaConsultaPreparada(query, producto.getIdProducto());
            while (result.next()) {
                TipoProducto actual = fromResultSet(result, true);
                actual.setProducto(producto);
                lista.add(actual);
            }
            return lista;
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            DBRestaurant.close();
        }
        return null;
    }
}
