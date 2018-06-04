package com.superescuadronalfa.restaurant.dbEntities.control;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Base64;
import android.util.Log;
import android.widget.ImageView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.superescuadronalfa.restaurant.app.AppConfig;
import com.superescuadronalfa.restaurant.app.AppController;
import com.superescuadronalfa.restaurant.database.DBRestaurant;
import com.superescuadronalfa.restaurant.dbEntities.CategoriaProducto;
import com.superescuadronalfa.restaurant.dbEntities.Producto;
import com.superescuadronalfa.restaurant.dbEntities.TipoProducto;
import com.superescuadronalfa.restaurant.helper.SessionManager;
import com.superescuadronalfa.restaurant.io.ImageUtils;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ControlProductos implements IControlEntidad<Producto> {
    private static ControlProductos instance;
    private static String ID_PRODUCTO = "id_producto";
    private static String NOMBRE_PRODUCTO = "nombre_producto";
    private static String IMAGE_DIRECTORY = "productos_cache";

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
    public List<Producto> getListaFromJSON(JSONArray result) throws JSONException {
        ArrayList<Producto> productos = new ArrayList<>();
        for (int i = 0; i < result.length(); i++) {
            JSONObject producto = result.getJSONObject(i);
            Producto ac = fromJSON(producto);
            ac.setTipoProductos(ControlTipoProducto.getInstance().getListaFromJSON(producto.getJSONArray("tipos")));
            productos.add(ac);
        }
        return productos;
    }


    public List<Producto> getListaFromJSON(JSONObject result) throws JSONException {
        ArrayList<Producto> productos = new ArrayList<>();
        for (int i = 0; i < result.length(); i++) {
            JSONObject producto = result.getJSONObject("asd");
            Producto ac = fromJSON(producto);
            ac.setTipoProductos(ControlTipoProducto.getInstance().getListaFromJSON(result.getJSONArray("tipos")));
            productos.add(ac);
        }
        return productos;
    }

    @Override
    public Producto fromResultSet(ResultSet result) throws SQLException {
        int iDProducto = result.getInt(ID_PRODUCTO);
        String nombreProducto = result.getString(NOMBRE_PRODUCTO);
        CategoriaProducto categoriaProducto = ControlCategorias.getInstance().fromResultSet(result);
        return new Producto(iDProducto, nombreProducto, categoriaProducto);
    }

    @Override
    public Producto fromJSON(JSONObject result) throws JSONException {
        int iDProducto = result.getInt(ID_PRODUCTO);
        String nombreProducto = result.getString(NOMBRE_PRODUCTO);
        CategoriaProducto categoriaProducto = ControlCategorias.getInstance().fromJSON(result);
        return new Producto(iDProducto, nombreProducto, categoriaProducto);
    }

    public boolean eliminarCacheImagenes(Context context) {
        ImageUtils utils = new ImageUtils(context, IMAGE_DIRECTORY);
        return utils.deletePath();
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

    public Bitmap burcarImagen(Producto producto, Context context) {
        if (!AppConfig.USE_CONNECTOR)
            throw new RuntimeException("Solo se puede usar con el conector");
        String findImage = "SELECT imagen FROM ProductoImagen WHERE id_imagen = ?";
        Bitmap image = null;
        try {
            ImageUtils utils = new ImageUtils(context, IMAGE_DIRECTORY);
            image = utils.loadImageFromStorage(producto.getIdProducto() + ".png");

            if (image != null) return image;
            if (AppConfig.USE_CONNECTOR) {
                ResultSet result = DBRestaurant.ejecutaConsulta(findImage, producto.getIdProducto());
                if (!result.next()) return null;
                byte[] encodedImage = result.getBytes("imagen");
                image = BitmapFactory.decodeByteArray(encodedImage, 0, encodedImage.length);
                utils.saveToInternalStorage(image, producto.getIdProducto() + ".png");
            }
            return image;
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            DBRestaurant.close();
        }

        return null;
    }

    private void burcarImagenJSON(final Producto producto, final Context context, final ImageView mImageView) {
        String tag_string_req = "req_image";
        String imageUrl = AppConfig.URL_GET_PRODUCTO_IMAGE + "?id_producto=" + producto.getIdProducto();
        final ImageUtils utils = new ImageUtils(context, IMAGE_DIRECTORY);
        StringRequest imageRequest = new StringRequest(Request.Method.POST, imageUrl, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONObject jObj = new JSONObject(response);
                    boolean error = jObj.getBoolean("error");

                    // Check for error node in json
                    if (!error) {
                        String image = jObj.getString("image");
                        byte[] encodedImage = Base64.decode(image, Base64.DEFAULT);
                        Bitmap img = BitmapFactory.decodeByteArray(encodedImage, 0, encodedImage.length);
                        mImageView.setImageBitmap(img);
                        producto.setImage(img);
                        utils.saveToInternalStorage(img, producto.getIdProducto() + ".png");
                    } else {
                        // Error  Get the error message
                        String errorMsg = jObj.getString("error_msg");
                        Toast.makeText(context, errorMsg, Toast.LENGTH_LONG).show();
                    }
                } catch (JSONException e) {
                    // JSON error
                    e.printStackTrace();
                    Toast.makeText(context, "Json error: " + e.getMessage(), Toast.LENGTH_LONG).show();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e(ControlProductos.class.getName(), "Error, no se pudo cargar la imagen, comprueba tu conexion" + error.getMessage());
                Toast.makeText(context, "Error, no se pudo cargar la imagen comprueba tu conexion " + (error.getMessage() != null ? error.getMessage() : error.toString()), Toast.LENGTH_LONG).show();
            }
        }) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<>();
                params.put("api_key", new SessionManager(context).getApiKey());
                return params;
            }
        };


        // Adding request to request queue
        AppController.getInstance().addToRequestQueue(imageRequest, tag_string_req);
    }

    public void burcarImagen(Producto producto, Context context, ImageView mImageView) {
        String findImage = "SELECT imagen FROM ProductoImagen WHERE id_imagen = ?";
        Bitmap image = null;
        try {
            ImageUtils utils = new ImageUtils(context, IMAGE_DIRECTORY);
            image = utils.loadImageFromStorage(producto.getIdProducto() + ".png");

            if (image != null) {
                mImageView.setImageBitmap(image);
                producto.setImage(image);
                return;
            }
            if (AppConfig.USE_CONNECTOR) {
                ResultSet result = DBRestaurant.ejecutaConsulta(findImage, producto.getIdProducto());
                if (!result.next()) {
                    Log.d("Error", "No se pudo cargar la imagen");
                    return;
                }
                byte[] encodedImage = result.getBytes("imagen");
                image = BitmapFactory.decodeByteArray(encodedImage, 0, encodedImage.length);
                utils.saveToInternalStorage(image, producto.getIdProducto() + ".png");
                mImageView.setImageBitmap(image);
                producto.setImage(image);
            } else {
                burcarImagenJSON(producto, context, mImageView);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            if (AppConfig.USE_CONNECTOR)
                DBRestaurant.close();
        }
    }
}
