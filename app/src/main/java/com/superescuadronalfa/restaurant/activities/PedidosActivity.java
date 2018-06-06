package com.superescuadronalfa.restaurant.activities;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.superescuadronalfa.restaurant.R;
import com.superescuadronalfa.restaurant.activities.adapters.MyPedidosItemRecyclerViewAdapter;
import com.superescuadronalfa.restaurant.app.AppConfig;
import com.superescuadronalfa.restaurant.app.AppController;
import com.superescuadronalfa.restaurant.dbEntities.Mesa;
import com.superescuadronalfa.restaurant.dbEntities.Orden;
import com.superescuadronalfa.restaurant.dbEntities.OrdenProducto;
import com.superescuadronalfa.restaurant.dbEntities.ProductoVariante;
import com.superescuadronalfa.restaurant.dbEntities.control.ControlOrdenProducto;
import com.superescuadronalfa.restaurant.dbEntities.control.ControlOrdenes;
import com.superescuadronalfa.restaurant.helper.SessionManager;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class PedidosActivity extends AppCompatActivity {
    public static final String EXTRA_MESA = "com.superescuadronalfa.restaurant.ID_MESA";
    private static final String TAG = PedidosActivity.class.getSimpleName();
    public static final int EDITAR_PEDIDO = 77;
    private Mesa mesa;
    private Orden orden;
    private RecyclerView rv;
    private ProgressBar progressBar;
    private MyPedidosItemRecyclerViewAdapter adap;
    private FloatingActionButton fab;
    private SwipeRefreshLayout swipeLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pedidos);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        fab = findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent agregarProducto = new Intent(PedidosActivity.this, ProductosActivity.class);
                agregarProducto.putExtra(ProductosActivity.EXTRA_ORDEN, orden);
                startActivityForResult(agregarProducto, ProductosActivity.AGREGAR_PRODUCTO);
            }
        });
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);


        Bundle b = getIntent().getExtras();
        Mesa mesa;


        rv = findViewById(R.id.rv);
        LinearLayoutManager llm = new LinearLayoutManager(this);
        rv.setLayoutManager(llm);
        rv.setHasFixedSize(true);


        rv.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                if (dy > 0 && fab.isShown()) {
                    fab.hide();
                } else if (dy < 0 && !fab.isShown()) {
                    fab.show();
                }
            }

            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                if (newState == RecyclerView.SCROLL_STATE_IDLE) {
                    // fab.show();
                }

                super.onScrollStateChanged(recyclerView, newState);
            }

        });

        mesa = null;
        progressBar = findViewById(R.id.progressBarPedidos);
        if (b != null && (mesa = b.getParcelable(EXTRA_MESA)) != null) {
            this.mesa = mesa;
            if (AppConfig.USE_CONNECTOR) {
                new LoadOrdenMesa().execute(mesa);
            } else {
                loadOrdenMesa(mesa);
            }
        } else {
            Log.d("Error", " No se tiene una mesa");
            finish();
        }

        swipeLayout = findViewById(R.id.swipe_layout);

        final Mesa finalMesa = mesa;
        swipeLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                if (AppConfig.USE_CONNECTOR) {
                    new LoadOrdenMesa().execute(finalMesa);
                } else {
                    loadOrdenMesa(finalMesa);
                }
            }
        });
        swipeLayout.setColorSchemeResources(R.color.colorPrimary,
                android.R.color.holo_green_dark,
                android.R.color.holo_orange_dark,
                android.R.color.holo_blue_dark);

    }

    private void loadOrdenMesa(Mesa mesa) {
        String tag_string_req = "req_pedidos";
        String urlGetMesas = AppConfig.URL_GET_ORDEN + "?id_mesa=" + mesa.getIdMesa();
        StringRequest strReq = new StringRequest(Request.Method.POST, urlGetMesas, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.d(TAG, "Login Response: " + response.toString());
                try {
                    JSONObject jObj = new JSONObject(response);
                    boolean error = jObj.getBoolean("error");

                    // Check for error node in json
                    if (!error) {
                        JSONObject ordenJSON = jObj.getJSONObject("orden");
                        Orden orden = ControlOrdenes.getInstance().fromJSON(ordenJSON);
                        JSONArray array = ordenJSON.getJSONArray("pedidos");
                        List<OrdenProducto> productos = ControlOrdenProducto.getInstance().getListaFromJSON(array, orden);
                        Collections.sort(productos);
                        initializeAdapter(productos);
                        fab.setVisibility(View.VISIBLE);
                        PedidosActivity.this.orden = orden;
                    } else {
                        if (jObj.opt("orden") != null) {
                            crearOrdenDialog();
                            return;
                        }
                        // Error  Get the error message
                        String errorMsg = jObj.getString("error_msg");
                        Toast.makeText(getApplicationContext(), "Error " + errorMsg, Toast.LENGTH_LONG).show();
                    }
                } catch (JSONException e) {
                    // JSON error
                    e.printStackTrace();
                    Toast.makeText(getApplicationContext(), "Json error: " + e.getMessage(), Toast.LENGTH_LONG).show();
                } finally {
                    progressBar.setVisibility(View.GONE);
                    swipeLayout.setRefreshing(false);
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e(TAG, "Error: " + error.getMessage());
                Toast.makeText(getApplicationContext(), "Error no se pudo cargar el contenido comprueba tu conexion " + (error.getMessage() != null ? error.getMessage() : error.toString()), Toast.LENGTH_LONG).show();
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

    private void crearOrden() {
        String tag_string_req = "req_pedidos";
        String urlGetMesas = AppConfig.URL_ADD_ORDEN + "?id_mesa=" + mesa.getIdMesa();
        StringRequest strReq = new StringRequest(Request.Method.POST, urlGetMesas, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.d(TAG, "Login Response: " + response.toString());
                try {
                    JSONObject jObj = new JSONObject(response);
                    boolean error = jObj.getBoolean("error");

                    // Check for error node in json
                    if (!error) {
                        JSONObject ordenJSON = jObj.getJSONObject("orden");
                        PedidosActivity.this.orden = ControlOrdenes.getInstance().fromJSON(ordenJSON);
                        JSONArray array = ordenJSON.getJSONArray("pedidos");
                        Orden orden = ControlOrdenes.getInstance().fromJSON(ordenJSON);
                        List<OrdenProducto> productos = ControlOrdenProducto.getInstance().getListaFromJSON(array, orden);
                        initializeAdapter(productos);
                        fab.setVisibility(View.VISIBLE);
                    } else {
                        if (jObj.opt("orden") != null) {
                            crearOrdenDialog();
                            return;
                        }
                        // Error  Get the error message
                        String errorMsg = jObj.getString("error_msg");
                        Toast.makeText(getApplicationContext(), "Error " + errorMsg, Toast.LENGTH_LONG).show();
                    }
                } catch (JSONException e) {
                    // JSON error
                    e.printStackTrace();
                    Toast.makeText(getApplicationContext(), "Json error: " + e.getMessage(), Toast.LENGTH_LONG).show();
                } finally {
                    progressBar.setVisibility(View.GONE);
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e(TAG, "Login Error: " + error.getMessage());
                Toast.makeText(getApplicationContext(), "Error no se pudo crear la orden comprueba tu conexion " + (error.getMessage() != null ? error.getMessage() : error.toString()), Toast.LENGTH_LONG).show();
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

    private void eliminarPedido(OrdenProducto pedido) {
        String tag_string_req = "req_pedidos";
        String urlGetMesas = AppConfig.URL_DELETE_PEDIDO + "?id_orden_producto=" + pedido.getIdOrdenProducto();
        StringRequest strReq = new StringRequest(Request.Method.POST, urlGetMesas, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.d(TAG, "Login Response: " + response.toString());
                try {
                    JSONObject jObj = new JSONObject(response);
                    boolean error = jObj.getBoolean("error");

                    // Check for error node in json
                    if (!error) {
                        Toast.makeText(getApplicationContext(), "Eliminado", Toast.LENGTH_LONG).show();
                        checkScroll();
                    } else {
                        String errorMsg = jObj.getString("error_msg");
                        Toast.makeText(getApplicationContext(), "Error " + errorMsg, Toast.LENGTH_LONG).show();
                        finish();
                    }
                } catch (JSONException e) {
                    // JSON error
                    e.printStackTrace();
                    Toast.makeText(getApplicationContext(), "Json error: " + e.getMessage(), Toast.LENGTH_LONG).show();
                } finally {
                    progressBar.setVisibility(View.GONE);
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e(TAG, "Error: Comprueba tu conexion " + error.getMessage());
                Toast.makeText(getApplicationContext(), "Error no se pudo eliminar el pedido comprueba tu conexion " + (error.getMessage() != null ? error.getMessage() : error.toString()), Toast.LENGTH_LONG).show();
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

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == ProductosActivity.AGREGAR_PRODUCTO) {
            if (resultCode == RESULT_OK) {
                if (AppConfig.USE_CONNECTOR)
                    new LoadOrdenMesa().execute(mesa);
                else
                    loadOrdenMesa(mesa);
            }
        } else if (requestCode == EDITAR_PEDIDO) {
            if (resultCode == RESULT_OK) {
                if (AppConfig.USE_CONNECTOR)
                    new LoadOrdenMesa().execute(mesa);
                else
                    loadOrdenMesa(mesa);
            }
        }
    }

    private void removeListItem(int index, OrdenProducto item) {
        if (adap == null) return;
        adap.deleteItem(index);

        if (AppConfig.USE_CONNECTOR)
            new EliminarPedido().execute(item);
        else {
            eliminarPedido(item);
        }


    }

    private void checkScroll() {
        boolean canScroll = rv.canScrollVertically(-1) || rv.canScrollVertically(1) || rv.canScrollVertically(-1);
        if (!canScroll && !fab.isShown()) {
            fab.show();
        }
    }


    private void initializeAdapter(List<OrdenProducto> ordenProductos) {
        for (OrdenProducto op : ordenProductos) {
            for (ProductoVariante pv : op.getVariantesDeLaOrden()) {
                // Toast.makeText(this, pv.getNombreVariante(), Toast.LENGTH_SHORT).show();
            }
        }

        adap = new MyPedidosItemRecyclerViewAdapter(this, ordenProductos, new MyPedidosItemRecyclerViewAdapter.OnListFragmentInteractionListener() {
            @Override
            public void onListFragmentInteraction(OrdenProducto item) {

            }

            @Override
            public void onRemoveClicked(final OrdenProducto item, final int index) {
                AlertDialog.Builder builder = new AlertDialog.Builder(PedidosActivity.this);
                builder.setTitle("¿Esta seguro que desea eliminar el pedido?");
                builder.setPositiveButton("Aceptar", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        removeListItem(index, item);
                    }
                });
                builder.setNegativeButton("Cancelar", null);
                builder.show();
            }

            @Override
            public void onEditClicked(OrdenProducto item, int index) {
                Intent intent = new Intent(PedidosActivity.this, EditarPedidoActivity.class);
                intent.putExtra(EditarPedidoActivity.EXTRA_PEDIDO, item);
                startActivityForResult(intent, EDITAR_PEDIDO);
            }

            @Override
            public void onImageClicked(final OrdenProducto item, int index) {
                AlertDialog dialog;
                final AlertDialog.Builder builder = new AlertDialog.Builder(PedidosActivity.this);

                builder.setNegativeButton("Cancelar", null);
                builder.setTitle("Selecciona un estado");

                final String[] estados = OrdenProducto.ESTADOS;
                int selectedIndex = Arrays.asList(estados).indexOf(item.getStatus());
                builder.setSingleChoiceItems(estados, selectedIndex, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        if (AppConfig.USE_CONNECTOR) {
                        } else {
                            cambiarEstado(item, estados[which]);
                        }
                        dialog.dismiss();
                    }

                });
                dialog = builder.create();
                dialog.show();
            }


        });

        // rv.setAdapter(adapter);
        rv.setAdapter(adap);
    }

    private void cambiarEstado(final OrdenProducto nuevo, String estado) {
        nuevo.setStatus(estado);
        String tag_string_req = "req_editar_pedido";
        String urlGetMesas = AppConfig.URL_EDITAR_PEDIDO;
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
                        loadOrdenMesa(mesa);
                    } else {
                        String errorMsg = jObj.getString("error_msg");
                        Toast.makeText(getApplicationContext(), "Error " + errorMsg, Toast.LENGTH_LONG).show();
                        finish();
                    }
                } catch (JSONException e) {
                    // JSON error
                    e.printStackTrace();
                    Toast.makeText(getApplicationContext(), "Json error: " + e.getMessage(), Toast.LENGTH_LONG).show();
                } finally {
                    progressBar.setVisibility(View.GONE);
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e(TAG, "Error, comprueba tu conexion: " + error.getMessage());
                Toast.makeText(getApplicationContext(), "Error, No se edito el pedido comprueba tu conexion " + (error.getMessage() != null ? error.getMessage() : error.toString()), Toast.LENGTH_LONG).show();
                finish();
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

    private String listVariantesToString(List<ProductoVariante> variantesList) {
        StringBuilder variantes = new StringBuilder();
        for (ProductoVariante pv : variantesList) {
            if (variantes.length() > 0) variantes.append(',');
            variantes.append(pv.getIdProductoVariante());
        }
        return variantes.toString();
    }

    // Muestar un dialogo para preguntar si se desea crear una orden
    private void crearOrdenDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(PedidosActivity.this);
        builder.setTitle("No existe una orden ¿Crear?");
        builder.setPositiveButton("Aceptar", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                if (AppConfig.USE_CONNECTOR)
                    new CrearOrden().execute(mesa);
                else {
                    crearOrden();
                }
            }
        });
        builder.setNegativeButton("Cancelar", null);

        builder.setOnDismissListener(new DialogInterface.OnDismissListener() {
            @Override
            public void onDismiss(DialogInterface dialog) {
                finish();
            }
        });

        try {
            builder.show();
        } catch (Exception e) {
            Log.d("Error", "No se pudo crer la ventana");
            finish();
        }
    }

    private class EliminarPedido extends AsyncTask<OrdenProducto, Void, Boolean> {

        @Override
        protected Boolean doInBackground(OrdenProducto... ordenProductos) {
            if (!AppConfig.USE_CONNECTOR) throw new RuntimeException("Se debe usar el conector");
            return ControlOrdenProducto.getInstance().eliminar(ordenProductos[0]);
        }

        @Override
        protected void onPostExecute(Boolean aBoolean) {
            super.onPostExecute(aBoolean);
            if (aBoolean) {
                Toast.makeText(PedidosActivity.this.getApplicationContext(), "Se ha eliminado el pedido", Toast.LENGTH_SHORT).show();
                checkScroll();
            } else {
                Toast.makeText(PedidosActivity.this.getApplicationContext(), "Error comprueba tu conexion", Toast.LENGTH_LONG).show();
            }
        }
    }

    private class CrearOrden extends AsyncTask<Mesa, Void, Boolean> {
        private Orden ordentemp;

        @Override
        protected Boolean doInBackground(Mesa... mesas) {
            if (!AppConfig.USE_CONNECTOR) throw new RuntimeException("Se debe usar el conector");
            Mesa m = mesas[0];
            Orden nueva = new Orden(m);
            // TODO Poner la mesa como ocupada
            // MEJOR hacerlo como stored Procedure
            return ControlOrdenes.getInstance().agregar(nueva);
        }

        @Override
        protected void onPostExecute(Boolean aBoolean) {
            super.onPostExecute(aBoolean);
            if (aBoolean) {
                orden = ordentemp;
                Toast.makeText(getApplicationContext(), "Orden creada", Toast.LENGTH_SHORT).show();
                fab.setVisibility(View.VISIBLE);
            } else {
                Toast.makeText(getApplicationContext(), "Error, comprueba tu conexion", Toast.LENGTH_SHORT).show();
            }
        }
    }

    private class LoadOrdenMesa extends AsyncTask<Mesa, Void, Integer> {
        private static final int ORDEN_NO_CREADA = 1; // No existen ordenes para la mesa
        private static final int ORDEN_CARGADA = 2; // Correco orden cargada
        private static final int ERROR = 3; // Ha ocurrido un error
        private Mesa mesa;

        @Override
        protected Integer doInBackground(Mesa... mesas) {
            if (!AppConfig.USE_CONNECTOR) throw new RuntimeException("Se debe usar el conector");
            mesa = mesas[0];
            orden = ControlOrdenes.getInstance().fromMesa(mesa);
            if (orden == null) return ORDEN_NO_CREADA;
            if (orden.getProductos() != null) { // Carga los productos
                for (OrdenProducto op : orden.getProductos()) {
                    op.getVariantesDeLaOrden().size();// Carga las ordenes
                }
                return ORDEN_CARGADA;
            }
            return ERROR;
        }

        @Override
        protected void onPostExecute(Integer result) {
            super.onPostExecute(result);
            switch (result) {
                case ORDEN_CARGADA:
                    initializeAdapter(orden.getProductos());
                    fab.setVisibility(View.VISIBLE);
                    break;
                case ORDEN_NO_CREADA:
                    crearOrdenDialog();
                    break;
                case ERROR:
                    Toast.makeText(getApplicationContext(), "Erro", Toast.LENGTH_SHORT).show();
                    break;
            }
            progressBar.setVisibility(View.GONE);
            swipeLayout.setRefreshing(false);
        }

        @Override
        protected void onCancelled(Integer integer) {
            finish();
        }
    }

}
