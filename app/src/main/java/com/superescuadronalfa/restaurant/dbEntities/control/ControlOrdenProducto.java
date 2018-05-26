package com.superescuadronalfa.restaurant.dbEntities.control;

import android.util.Log;

import com.superescuadronalfa.restaurant.database.DBRestaurant;
import com.superescuadronalfa.restaurant.dbEntities.Orden;
import com.superescuadronalfa.restaurant.dbEntities.OrdenProducto;
import com.superescuadronalfa.restaurant.dbEntities.Producto;
import com.superescuadronalfa.restaurant.dbEntities.ProductoVariante;
import com.superescuadronalfa.restaurant.dbEntities.TipoProducto;

import java.math.BigDecimal;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class ControlOrdenProducto implements IControlEntidad<OrdenProducto> {
    private static ControlOrdenProducto instance;

    private ControlOrdenProducto() {
    }

    public static ControlOrdenProducto getInstance() {
        if (instance == null) instance = new ControlOrdenProducto();
        return instance;
    }

    @Override
    public boolean agregar(OrdenProducto entidad) {
        String query = "EXECUTE agregarOrdenProducto @IDOrden = ?, @IDTipoProducto = ?, @IDVariantes = ?, @Cantidad = ?, @Comentarios = ?";
        StringBuilder variantes = new StringBuilder();
        for (ProductoVariante pv : entidad.getVariantesDeLaOrden()) {
            if (variantes.length() > 0) variantes.append(',');
            variantes.append(pv.getIdProductoVariante());
        }
        if (DBRestaurant.ejecutaComandoPreparada(query, entidad.getOrden().getIdOrden(), entidad.getTipoProducto().getIdTipoProducto(), variantes.toString(), entidad.getCantidad(), entidad.getComentarios())) {
            return true;
        }
        Log.d("Error ", entidad.getOrden().getIdOrden() + ", " + entidad.getTipoProducto().getIdTipoProducto() + "," + variantes.toString() + "," + entidad.getCantidad() + "," + entidad.getComentarios());
        return false;
    }

    private String listVariantesToString(List<ProductoVariante> variantesList) {
        StringBuilder variantes = new StringBuilder();
        for (ProductoVariante pv : variantesList) {
            if (variantes.length() > 0) variantes.append(',');
            variantes.append(pv.getIdProductoVariante());
        }
        return variantes.toString();
    }
    @Override
    public boolean editar(OrdenProducto entidad) {
        String query = "EXECUTE editarOrdenProducto \n" +
                "@IDOrdenProducto = ?, \n" +
                "@IDTipoProducto = ?,\n" +
                "@IDVariantes = ?,\n" +
                "@Cantidad = ?,\n" +
                "@Comentarios = ?";


        try {
            String variantes = listVariantesToString(entidad.getVariantesDeLaOrden());
            return DBRestaurant.ejecutaComandoPreparada(query, entidad.getIdOrdenProducto(), entidad.getTipoProducto().getIdTipoProducto(), variantes, entidad.getCantidad(), entidad.getComentarios());
        } catch (Exception e) {

        } finally {
            DBRestaurant.close();
        }

        return false;
    }

    @Override
    public boolean eliminar(OrdenProducto entidad) {
        String query = "DELETE FROM OrdenProducto WHERE id_orden_producto = ?";
        try {
            return DBRestaurant.ejecutaComando(query, entidad.getIdOrdenProducto());
        } catch (Exception e) {
            Log.d("Error", "No se pudo eliminar la orden");
        } finally {
            DBRestaurant.close();
        }
        return false;
    }

    @Override
    public List<OrdenProducto> getLista() {
        return null;
    }

    @Override
    public OrdenProducto fromResultSet(ResultSet result) {
        throw new RuntimeException("No implementado");
    }

    public OrdenProducto fromResultSet(ResultSet result, Orden orden) throws SQLException {
        int idOrdenProducto = result.getInt("id_orden_producto");
        TipoProducto tp = ControlTipoProducto.getInstance().fromResultSet(result);

        Producto p = ControlProductos.getInstance().fromResultSet(result);
        tp.setProducto(p);

        BigDecimal precio = result.getBigDecimal("precio");
        int cantidad = result.getInt("cantidad");
        String comentarios = result.getString("comentarios");
        String status = result.getString("status");
        OrdenProducto op = new OrdenProducto(idOrdenProducto, orden, tp, precio, cantidad, comentarios, status);
        op.setVariantesDeLaOrden(ControlProductoVariantes.getInstance().fromResultSetList(result));
        return op;
    }

    public List<ProductoVariante> variantesDeLaOrden(OrdenProducto o) {
        String getVariantes = "" +
                "SELECT * FROM VariantesDeLaOrden AS VO\n" +
                "INNER JOIN OrdenProducto AS OP\n" +
                "ON OP.id_orden_producto = VO.id_orden_producto\n" +
                "INNER JOIN ProductoVariante AS PV\n" +
                "ON PV.id_producto_variante = VO.id_variante\n" +
                "WHERE OP.id_orden_producto = ?";

        try {
            ArrayList<ProductoVariante> lista = new ArrayList<>();
            ResultSet result = DBRestaurant.ejecutaConsulta(getVariantes, o.getOrden().getIdOrden());
            while (result.next()) {
                ProductoVariante ac = ControlProductoVariantes.getInstance().fromResultSet(result);
                ac.setTipoProducto(o.getTipoProducto());
                lista.add(ac);
            }

            return lista;
        } catch (SQLException e) {

        } finally {
            DBRestaurant.close();
        }

        throw new RuntimeException("No se pudieron cargar");


    }
}
