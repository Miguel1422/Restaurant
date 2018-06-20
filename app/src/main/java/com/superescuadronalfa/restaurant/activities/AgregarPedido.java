package com.superescuadronalfa.restaurant.activities;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.superescuadronalfa.restaurant.R;
import com.superescuadronalfa.restaurant.activities.adapters.MyVairanteItemRecyclerViewAdapter;
import com.superescuadronalfa.restaurant.app.AppConfig;
import com.superescuadronalfa.restaurant.app.AppController;
import com.superescuadronalfa.restaurant.dbEntities.Orden;
import com.superescuadronalfa.restaurant.dbEntities.OrdenProducto;
import com.superescuadronalfa.restaurant.dbEntities.Producto;
import com.superescuadronalfa.restaurant.dbEntities.ProductoVariante;
import com.superescuadronalfa.restaurant.dbEntities.TipoProducto;
import com.superescuadronalfa.restaurant.dbEntities.control.ControlTipoProducto;
import com.superescuadronalfa.restaurant.helper.SessionManager;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class AgregarPedido extends AppCompatActivity {
    public static final String EXTRA_ORDEN = "com.superescuadronalfa.restaurant.EXTRA_ORDEN";
    public static final String EXTRA_PRODUCTO = "com.superescuadronalfa.restaurant.EXTRA_PRODUCTO";
    private static final String TAG = EditarPedidoActivity.class.getSimpleName();
    private Spinner spinner;
    private Spinner spinnerCan;
    private ProgressBar progressBar;
    private RecyclerView listVariantes;
    private EditText txtComentarios;

    private Orden orden;
    private Producto producto;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_agregar_pedido);

        Bundle extras = getIntent().getExtras();

        orden = extras.getParcelable(EXTRA_ORDEN);
        producto = extras.getParcelable(EXTRA_PRODUCTO);
        if (orden == null || producto == null) {
            Toast.makeText(getApplicationContext(), "No se ha cargado la informacion del pedido", Toast.LENGTH_LONG).show();
            Log.d("Error", "No se ha cargado la informacion del pedido");
            finish();
            return;
        }


        spinner = findViewById(R.id.spinner);
        spinnerCan = findViewById(R.id.spinnerCan);

        Button btn = findViewById(R.id.button);
        progressBar = findViewById(R.id.progressBar);

        progressBar.setVisibility(View.VISIBLE);
        listVariantes = findViewById(R.id.rv);
        txtComentarios = findViewById(R.id.editText2);


        if (AppConfig.USE_CONNECTOR) {

        } else {
            loadContent(producto);
        }
    }


    @SuppressLint("StaticFieldLeak")
    public void btnAceptarClick(View v) {
        agregarProductoPHP();
    }

    private void agregarProductoPHP() {
        if (orden == null) {
            Toast.makeText(getApplicationContext(), "Error no se pudo crear el pedido comprueba tu conexion ", Toast.LENGTH_LONG).show();
            finish();
        }
        MyVairanteItemRecyclerViewAdapter adapter = (MyVairanteItemRecyclerViewAdapter) listVariantes.getAdapter();

        TipoProducto tipo = ((TipoProducto) spinner.getSelectedItem());
        final OrdenProducto op = new OrdenProducto(0, orden, tipo, tipo.getPrecioTipo(), ((Integer) spinnerCan.getSelectedItem()), txtComentarios.getText().toString(), "En cola");
        op.setVariantesDeLaOrden(adapter.getSelectedItems());

        String tag_string_req = "req_pedidos";
        String urlGetMesas = AppConfig.getInstance().getUrlAddPedido();
        StringRequest strReq = new StringRequest(Request.Method.POST, urlGetMesas, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.d(TAG, "Login Response: " + response.toString());
                try {
                    JSONObject jObj = new JSONObject(response);
                    boolean error = jObj.getBoolean("error");

                    // Check for error node in json
                    if (!error) {
                        Toast.makeText(getApplicationContext(), "Agregado", Toast.LENGTH_LONG).show();
                        finish();
                    } else {

                        // Error  Get the error message
                        String errorMsg = jObj.getString("error_msg");
                        Toast.makeText(getApplicationContext(), "Error " + errorMsg, Toast.LENGTH_LONG).show();
                    }
                } catch (JSONException e) {
                    // JSON error
                    e.printStackTrace();
                    // Toast.makeText(getApplicationContext(), "Json error: " + e.getMessage(), Toast.LENGTH_LONG).show();
                    Toast.makeText(getApplicationContext(), "Error, no se ha podido conectar PHP  con la BD", Toast.LENGTH_LONG).show();
                } finally {
                    progressBar.setVisibility(View.GONE);
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e(TAG, "Login Error: " + error.getMessage());
                Toast.makeText(getApplicationContext(), "Error no se pudo crear el pedido comprueba tu conexion " + (error.getMessage() != null ? error.getMessage() : error.toString()), Toast.LENGTH_LONG).show();
                progressBar.setVisibility(View.GONE);

            }
        }) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<>();
                params.put("api_key", new SessionManager(getApplicationContext()).getApiKey());
                params.put("id_orden", op.getOrden().getIdOrden() + "");
                params.put("id_tipo_producto", op.getTipoProducto().getIdTipoProducto() + "");
                params.put("id_variantes", listVariantesToString(op.getVariantesDeLaOrden()));
                params.put("cantidad", op.getCantidad() + "");
                params.put("comentarios", op.getComentarios());
                return params;
            }
        };

        // Adding request to request queue
        AppController.getInstance().addToRequestQueue(strReq, tag_string_req);
    }

    private String listVariantesToString(List<ProductoVariante> variantesList) {
        StringBuilder variantes = new StringBuilder();
        for (ProductoVariante pv : variantesList) {
            if (variantes.length() > 0) variantes.append(',');
            variantes.append(pv.getIdProductoVariante());
        }
        return variantes.toString();
    }


    private void initializeAdapter(List<TipoProducto> tipos) {
        ArrayAdapter<TipoProducto> adapter = new ArrayAdapter<>(AgregarPedido.this, android.R.layout.simple_dropdown_item_1line, tipos);
        spinner.setAdapter(adapter);
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                loadVariantes(((TipoProducto) parent.getItemAtPosition(position)));
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        spinner.setSelection(0);


        // Cantidad para el otro spinner
        Integer[] items = new Integer[20];
        for (int i = 0; i < 20; i++) {
            items[i] = i + 1;
        }

        ArrayAdapter<Integer> adapter2 = new ArrayAdapter<>(AgregarPedido.this, android.R.layout.simple_list_item_1, items);
        spinnerCan.setAdapter(adapter2);

        spinnerCan.setSelection(0);


        listVariantes.setLayoutManager(new LinearLayoutManager(AgregarPedido.this));
        listVariantes.setHasFixedSize(true);
    }

    private void loadVariantes(TipoProducto tipoProducto) {
        MyVairanteItemRecyclerViewAdapter adapter = new MyVairanteItemRecyclerViewAdapter(tipoProducto.getVariantes(), new MyVairanteItemRecyclerViewAdapter.OnListFragmentInteractionListener() {
            @Override
            public void onListFragmentInteraction(ProductoVariante item) {

            }
        });

        listVariantes.setAdapter(adapter);
    }

    private void loadContent(Producto producto) {
        String tag_string_req = "req_editar_pedido";
        String urlGetMesas = AppConfig.getInstance().getUrlGetTipos() + "?id_producto=" + producto.getIdProducto();

        StringRequest strReq = new StringRequest(Request.Method.POST, urlGetMesas, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.d(TAG, "Login Response: " + response.toString());
                try {
                    JSONObject jObj = new JSONObject(response);
                    boolean error = jObj.getBoolean("error");

                    // Check for error node in json
                    if (!error) {
                        List<TipoProducto> tipos = ControlTipoProducto.getInstance().getListaFromJSON(jObj.getJSONArray("tipos"));
                        initializeAdapter(tipos);
                    } else {
                        String errorMsg = jObj.getString("error_msg");
                        Toast.makeText(getApplicationContext(), "Error " + errorMsg, Toast.LENGTH_LONG).show();
                        finish();
                    }
                } catch (JSONException e) {
                    // JSON error
                    e.printStackTrace();
                    Toast.makeText(getApplicationContext(), "Error, no se ha podido conectar PHP  con la BD", Toast.LENGTH_LONG).show();
                    //Toast.makeText(getApplicationContext(), "Json error: " + e.getMessage(), Toast.LENGTH_LONG).show();
                } finally {

                    progressBar.setVisibility(View.GONE);
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e(TAG, "Error, comprueba tu conexion: " + error.getMessage());
                Toast.makeText(getApplicationContext(), "Error, no pudo cargarse el contenido comprueba tu conexion " + (error.getMessage() != null ? error.getMessage() : error.toString()), Toast.LENGTH_LONG).show();
                finish();
            }
        }) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<>();
                params.put("api_key", new SessionManager(getApplicationContext()).getApiKey());
                return params;
            }
        };

        // Adding request to request queue
        AppController.getInstance().addToRequestQueue(strReq, tag_string_req);
    }
}
