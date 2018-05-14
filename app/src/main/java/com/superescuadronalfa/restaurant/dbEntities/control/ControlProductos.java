package com.superescuadronalfa.restaurant.dbEntities.control;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import com.superescuadronalfa.restaurant.database.DBRestaurant;
import com.superescuadronalfa.restaurant.dbEntities.CategoriaProducto;
import com.superescuadronalfa.restaurant.dbEntities.Producto;
import com.superescuadronalfa.restaurant.dbEntities.TipoProducto;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class ControlProductos implements IControlEntidad<Producto> {
    private static ControlProductos instance;
    private static String ID_PRODUCTO = "id_producto";
    private static String NOMBRE_PRODUCTO = "nombre_producto";

    private ControlProductos() {
    }

    public static ControlProductos getInstance() {
        if (instance == null) instance = new ControlProductos();
        return instance;
    }

    @Override
    public boolean agregar(Producto entidad) {
        return false;
    }

    @Override
    public boolean editar(Producto entidad) {
        return false;
    }

    @Override
    public boolean eliminar(Producto entidad) {
        return false;
    }

    @Override
    public List<Producto> getLista() {
        String query = "SELECT * FROM Producto AS P\n" +
                "INNER JOIN CategoriaProducto AS CP\n" +
                "ON P.id_categoria = CP.id_categoria\n" +
                "ORDER BY CP.nombre_categoria, P.nombre_producto";
        try {
            ArrayList<Producto> lista = new ArrayList<>();
            ResultSet result = DBRestaurant.ejecutaConsulta(query);
            while (result.next()) {
                Producto p = fromResultSet(result);
                lista.add(p);
            }
            return lista;
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            DBRestaurant.close();
        }
        return null;

    }

    @Override
    public Producto fromResultSet(ResultSet result) throws SQLException {
        int iDProducto = result.getInt(ID_PRODUCTO);
        String nombreProducto = result.getString(NOMBRE_PRODUCTO);
        CategoriaProducto categoriaProducto = ControlCategorias.getInstance().fromResultSet(result);
        return new Producto(iDProducto, nombreProducto, categoriaProducto);
    }

    public List<TipoProducto> tiposDelProducto(Producto p) {
        String buscarTipos = "" +
                "SELECT * FROM TipoProducto WHERE id_producto = ?\n" +
                "ORDER BY precio_tipo, nombre_tipo";
        try {
            ResultSet result = DBRestaurant.ejecutaConsulta(buscarTipos, p.getIdProducto());
            ArrayList<TipoProducto> tipos = new ArrayList<>();
            while (result.next()) {
                TipoProducto actual = ControlTipoProducto.getInstance().fromResultSet(result);
                actual.setProducto(p);
                tipos.add(actual);
            }
            return tipos;
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            DBRestaurant.close();
        }
        return null;
    }

    public Bitmap burcarImagen(Producto producto) {
        // TODO Implementar cache
        String findImage = "SELECT imagen FROM ProductoImagen WHERE id_imagen = ?";

        try {
            ResultSet result = DBRestaurant.ejecutaConsulta(findImage, producto.getIdProducto());
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
