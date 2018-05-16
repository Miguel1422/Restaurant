package com.superescuadronalfa.restaurant.dbEntities.control;

import com.superescuadronalfa.restaurant.database.DBRestaurant;
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
        return false;
    }

    @Override
    public boolean editar(OrdenProducto entidad) {
        return false;
    }

    @Override
    public boolean eliminar(OrdenProducto entidad) {
        return false;
    }

    @Override
    public List<OrdenProducto> getLista() {
        return null;
    }

    @Override
    public OrdenProducto fromResultSet(ResultSet result) throws SQLException {
        int idOrdenProducto = result.getInt("id_orden_producto");
        TipoProducto tp = ControlTipoProducto.getInstance().fromResultSet(result);

        Producto p = ControlProductos.getInstance().fromResultSet(result);
        tp.setProducto(p);

        BigDecimal precio = result.getBigDecimal("precio");
        int cantidad = result.getInt("cantidad");
        String comentarios = result.getString("comentarios");
        String status = result.getString("status");
        OrdenProducto op = new OrdenProducto(idOrdenProducto, tp, precio, cantidad, comentarios, status);
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
            ResultSet result = DBRestaurant.ejecutaConsulta(getVariantes, o.getIdOrdenProducto());
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
