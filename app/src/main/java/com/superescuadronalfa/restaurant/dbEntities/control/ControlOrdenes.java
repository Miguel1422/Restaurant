package com.superescuadronalfa.restaurant.dbEntities.control;

import android.util.Log;

import com.superescuadronalfa.restaurant.database.DBRestaurant;
import com.superescuadronalfa.restaurant.dbEntities.Mesa;
import com.superescuadronalfa.restaurant.dbEntities.Orden;
import com.superescuadronalfa.restaurant.dbEntities.OrdenProducto;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class ControlOrdenes implements IControlEntidad<Orden> {
    private static ControlOrdenes instance;

    private ControlOrdenes() {
    }

    public static ControlOrdenes getInstance() {
        if (instance == null) instance = new ControlOrdenes();
        return instance;
    }

    @Override
    public boolean agregar(Orden entidad) {
        String agregarOrden = "INSERT INTO Orden (id_mesa) VALUES (?)"; // TODO agregar trabajador aqui
        try {
            return DBRestaurant.ejecutaComando(agregarOrden, entidad.getMesa().getIdMesa());
        } catch (Exception e) {
            e.printStackTrace();
            Log.e("Error", "Error desconocido al intentar agregar orden");
        } finally {
            DBRestaurant.close();
        }
        return false;
    }

    @Override
    public boolean editar(Orden entidad) {
        return false;
    }

    @Override
    public boolean eliminar(Orden entidad) {
        return false;
    }

    @Override
    public List<Orden> getLista() {
        return null;
    }

    public List<OrdenProducto> productosDeLaOrden(Orden o) {
        String getProductos = "" +
                "SELECT * FROM OrdenProducto  AS OP\n" +
                "INNER JOIN TipoProducto AS TP\n" +
                "ON OP.id_tipo_producto = TP.id_tipo_producto\n" +
                "INNER JOIN Producto AS P\n" +
                "ON P.id_producto = TP.id_producto\n" +
                "INNER JOIN CategoriaProducto AS CP\n" +
                "ON CP.id_categoria = P.id_categoria\n" +
                "WHERE OP.id_orden = ?";

        String call = "EXECUTE getPedidosDeOrden @IDOrden = ?";
        try {
            ArrayList<OrdenProducto> lista = new ArrayList<>();
            // ResultSet result = DBRestaurant.ejecutaConsulta(getProductos, o.getIdOrden());
            ResultSet result = DBRestaurant.ejecutaConsultaPreparada(call, o.getIdOrden());
            while (result.next()) {
                OrdenProducto ordenProducto = ControlOrdenProducto.getInstance().fromResultSet(result, o);
                lista.add(ordenProducto);
            }
            return lista;
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            DBRestaurant.close();
        }
        throw new RuntimeException("Comprueba la cnexion a internet, no se pudo cargar los productos de la orden");
    }

    public Orden fromMesa(Mesa m) {
        String getMesa = "SELECT * FROM Orden WHERE id_mesa = ? AND activa = 1";
        try {
            ResultSet result = DBRestaurant.ejecutaConsulta(getMesa, m.getIdMesa());
            if (!result.next()) return null;
            Orden o = fromResultSet(result);
            o.setMesa(m);
            return o;
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            DBRestaurant.close();
        }
        throw new RuntimeException("COmprueba la cnexion a internet, no se pudoin cargar la orden");
    }

    @Override
    public Orden fromResultSet(ResultSet result) throws SQLException {
        int idOrden = result.getInt("id_orden");
        return new Orden(idOrden);
    }
}
