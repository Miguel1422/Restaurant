package com.superescuadronalfa.restaurant.activities;

import android.annotation.SuppressLint;
import android.os.AsyncTask;
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
import com.superescuadronalfa.restaurant.dbEntities.OrdenProducto;
import com.superescuadronalfa.restaurant.dbEntities.Producto;
import com.superescuadronalfa.restaurant.dbEntities.ProductoVariante;
import com.superescuadronalfa.restaurant.dbEntities.TipoProducto;
import com.superescuadronalfa.restaurant.dbEntities.control.ControlOrdenProducto;
import com.superescuadronalfa.restaurant.dbEntities.control.ControlTipoProducto;
import com.superescuadronalfa.restaurant.helper.SessionManager;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class EditarPedidoActivity extends AppCompatActivity {

    public static final String EXTRA_PEDIDO = "com.superescuadronalfa.restaurant.EXTRA_PEDIDO";
    private static final String TAG = EditarPedidoActivity.class.getSimpleName();
    private OrdenProducto pedido;
    private Spinner spinner;
    private Spinner spinnerCan;
    private ProgressBar progressBar;
    private RecyclerView listVariantes;
    private EditText txtComentarios;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_editar_pedido);
        Bundle extras = getIntent().getExtras();

        pedido = extras.getParcelable(EXTRA_PEDIDO);

        if (pedido == null) {
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

        txtComentarios.setText(pedido.getComentarios());

        if (AppConfig.USE_CONNECTOR) {
            new LoadContent().execute();
        } else {
            loadContent(pedido.getTipoProducto().getProducto());
        }
    }


    @SuppressLint("StaticFieldLeak")
    public void btnAceptarClick(View v) {
        if (progressBar.getVisibility() == View.VISIBLE) return;
        setResult(RESULT_OK);
        MyVairanteItemRecyclerViewAdapter adapter = (MyVairanteItemRecyclerViewAdapter) listVariantes.getAdapter();

        if (txtComentarios.getText().toString().length() >= 255) {
            Toast.makeText(EditarPedidoActivity.this, "Ese si que es un comentario largo, necesitas hacerlo mas corto :(", Toast.LENGTH_SHORT).show();
            return;
        }
        final OrdenProducto nuevo =
                new OrdenProducto(
                        pedido.getIdOrdenProducto(),
                        pedido.getOrden(),
                        ((TipoProducto) spinner.getSelectedItem()),
                        pedido.getPrecio(),
                        ((Integer) spinnerCan.getSelectedItem()),
                        txtComentarios.getText().toString(),
                        pedido.getStatus());

        nuevo.setVariantesDeLaOrden(adapter.getSelectedItems());

        progressBar.setVisibility(View.VISIBLE);
        if (AppConfig.USE_CONNECTOR) {
            new AsyncTask<Void, Void, Boolean>() {
                @Override
                protected Boolean doInBackground(Void... voids) {
                    return ControlOrdenProducto.getInstance().editar(nuevo);
                }

                @Override
                protected void onPostExecute(Boolean aBoolean) {
                    super.onPostExecute(aBoolean);
                    if (aBoolean) {

                        Toast.makeText(EditarPedidoActivity.this, "Exito", Toast.LENGTH_SHORT).show();
                        finish();
                    } else {
                        Toast.makeText(EditarPedidoActivity.this, "Error comprueba tu conexion", Toast.LENGTH_LONG).show();
                    }
                }
            }.execute();
        } else {
            editarPedido(nuevo);
        }

    }

    private String listVariantesToString(List<ProductoVariante> variantesList) {
        StringBuilder variantes = new StringBuilder();
        for (ProductoVariante pv : variantesList) {
            if (variantes.length() > 0) variantes.append(',');
            variantes.append(pv.getIdProductoVariante());
        }
        return variantes.toString();
    }


    private void editarPedido(final OrdenProducto nuevo) {
        String tag_string_req = "req_editar_pedido";
        String urlGetMesas = AppConfig.getInstance().getUrlEditarPedido();
        StringRequest strReq = new StringRequest(Request.Method.POST, urlGetMesas, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.d(TAG, "Login Response: " + response.toString());
                try {
                    JSONObject jObj = new JSONObject(response);
                    boolean error = jObj.getBoolean("error");

                    // Check for error node in json
                    if (!error) {
                        Toast.makeText(getApplicationContext(), "Orden editada", Toast.LENGTH_SHORT).show();
                        finish();
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
                Toast.makeText(getApplicationContext(), "Error, No se edito el pedido comprueba tu conexion " + (error.getMessage() != null ? error.getMessage() : error.toString()), Toast.LENGTH_LONG).show();
            }
        }) {
            // Se tiene que gacer pos post porque en los comentarions se puede agregar cosas raras
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<>();
                params.put("api_key", new SessionManager(getApplicationContext()).getApiKey());
                params.put("id_orden_producto", "" + nuevo.getIdOrdenProducto());
                params.put("id_tipo_producto", "" + nuevo.getTipoProducto().getIdTipoProducto());
                params.put("id_variantes", listVariantesToString(nuevo.getVariantesDeLaOrden()));
                params.put("cantidad", "" + nuevo.getCantidad());
                params.put("comentarios", "" + nuevo.getComentarios());
                params.put("status", "" + nuevo.getStatus());
                return params;
            }
        };

        // Adding request to request queue
        AppController.getInstance().addToRequestQueue(strReq, tag_string_req);
    }


    private void initializeAdapter(List<TipoProducto> tipos) {
        ArrayAdapter<TipoProducto> adapter = new ArrayAdapter<>(EditarPedidoActivity.this, android.R.layout.simple_dropdown_item_1line, tipos);
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

        int idSelected = adapter.getPosition(pedido.getTipoProducto());
        spinner.setSelection(idSelected);


        // Cantidad para el otro spinner
        Integer[] items = new Integer[20];
        for (int i = 0; i < 20; i++) {
            items[i] = i + 1;
        }

        ArrayAdapter<Integer> adapter2 = new ArrayAdapter<>(EditarPedidoActivity.this, android.R.layout.simple_list_item_1, items);
        spinnerCan.setAdapter(adapter2);

        spinnerCan.setSelection(adapter2.getPosition(pedido.getCantidad()));


        listVariantes.setLayoutManager(new LinearLayoutManager(EditarPedidoActivity.this));
        listVariantes.setHasFixedSize(true);
    }

    private void loadVariantes(TipoProducto tipoProducto) {
        MyVairanteItemRecyclerViewAdapter adapter = new MyVairanteItemRecyclerViewAdapter(tipoProducto.getVariantes(), new MyVairanteItemRecyclerViewAdapter.OnListFragmentInteractionListener() {
            @Override
            public void onListFragmentInteraction(ProductoVariante item) {

            }
        });

        if (tipoProducto.equals(pedido.getTipoProducto())) {
            adapter.setSelectedItems(pedido.getVariantesDeLaOrden());
        }
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

    private class LoadContent extends AsyncTask<Void, Void, Boolean> {

        private List<TipoProducto> tipos;

        @Override
        protected Boolean doInBackground(Void... voids) {
            pedido.getTipoProducto().getProducto();
            tipos = ControlTipoProducto.getInstance().getLista(pedido.getTipoProducto().getProducto());
            return tipos != null;
        }

        @Override
        protected void onPostExecute(Boolean aBoolean) {
            super.onPostExecute(aBoolean);

            if (aBoolean) {
                initializeAdapter(tipos);
            } else {
                Toast.makeText(EditarPedidoActivity.this, "Comprueba tu conexion", Toast.LENGTH_LONG).show();
            }

            progressBar.setVisibility(View.GONE);
        }
    }

}
