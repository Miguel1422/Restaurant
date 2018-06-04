package com.superescuadronalfa.restaurant.activities;

import android.annotation.SuppressLint;
import android.content.DialogInterface;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.superescuadronalfa.restaurant.AppConfig;
import com.superescuadronalfa.restaurant.R;
import com.superescuadronalfa.restaurant.activities.adapters.MyProductoItemRecyclerViewAdapter;
import com.superescuadronalfa.restaurant.activities.adapters.SectionsPagerAdapter;
import com.superescuadronalfa.restaurant.app.AppController;
import com.superescuadronalfa.restaurant.dbEntities.Orden;
import com.superescuadronalfa.restaurant.dbEntities.OrdenProducto;
import com.superescuadronalfa.restaurant.dbEntities.Producto;
import com.superescuadronalfa.restaurant.dbEntities.TipoProducto;
import com.superescuadronalfa.restaurant.dbEntities.control.ControlOrdenProducto;
import com.superescuadronalfa.restaurant.dbEntities.control.ControlProductos;
import com.superescuadronalfa.restaurant.dbEntities.helpers.OrganizarProductos;
import com.superescuadronalfa.restaurant.helper.SessionManager;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ProductosActivity extends AppCompatActivity {

    private static final String TAG = ProductosActivity.class.getSimpleName();
    public static final String EXTRA_ORDEN = "com.superescuadronalfa.restaurant.EXTRA_ORDEN";
    public static final int AGREGAR_PRODUCTO = 123;
    /**
     * The {@link android.support.v4.view.PagerAdapter} that will provide
     * fragments for each of the sections. We use a
     * {@link FragmentPagerAdapter} derivative, which will keep every
     * loaded fragment in memory. If this becomes too memory intensive, it
     * may be best to switch to a
     * {@link android.support.v4.app.FragmentStatePagerAdapter}.
     */
    private SectionsPagerAdapter mSectionsPagerAdapter;

    /**
     * The {@link ViewPager} that will host the section contents.
     */
    private ViewPager mViewPager;
    private TabLayout tabLayout;
    private ProgressBar progressBar;
    private Orden ordenActual;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_productos);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);


        Bundle extras = getIntent().getExtras();

        ordenActual = extras.getParcelable(EXTRA_ORDEN);

        progressBar = findViewById(R.id.progressBar);

        progressBar.setVisibility(View.VISIBLE);
        Log.d("Create", "Creating the Productos acivirty");
        if (AppConfig.USE_CONNECTOR) {
            new LoadContent().execute();
        } else {
            loadContent();
        }
    }


    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        //Clear the Activity's bundle of the subsidiary fragments' bundles.
        outState.clear();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_productos, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            deleteCahe();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressLint("StaticFieldLeak")
    private void deleteCahe() {
        new AsyncTask<Void, Void, Boolean>() {
            @Override
            protected Boolean doInBackground(Void... voids) {
                return ControlProductos.getInstance().eliminarCacheImagenes(ProductosActivity.this);
            }

            @Override
            protected void onPostExecute(Boolean aBoolean) {
                super.onPostExecute(aBoolean);
                if (aBoolean) {
                    Toast.makeText(ProductosActivity.this, "Se han eliminado las imagenes", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(ProductosActivity.this, "No se han podido eliminar", Toast.LENGTH_LONG).show();
                }
                finish();
            }
        }.execute();
    }

    @SuppressLint("StaticFieldLeak")
    private void loadAndShowVariantes(final Producto producto) {
        if (!AppConfig.USE_CONNECTOR && !producto.hasTiposLoaded()) {
            Toast.makeText(getApplicationContext(), "Este error no deberia presentarse, WHAT?", Toast.LENGTH_LONG).show();
            return;
        }
        if (!producto.hasTiposLoaded()) {
            progressBar.setVisibility(View.VISIBLE);
            new AsyncTask<Void, Void, Boolean>() {
                @Override
                protected Boolean doInBackground(Void... voids) {
                    producto.getTipoProductos().size();
                    return true;
                }

                @Override
                protected void onPostExecute(Boolean aBoolean) {
                    super.onPostExecute(aBoolean);
                    progressBar.setVisibility(View.GONE);

                    if (aBoolean) {
                        showVariantes(producto);
                    } else {
                        Toast.makeText(getApplicationContext(), "Ha ocurrido un error, comprueba tu conexion", Toast.LENGTH_LONG).show();
                    }
                }
            }.execute();
        } else {
            showVariantes(producto);
        }
    }

    private void showVariantes(Producto p) {
        if (p.getTipoProductos().size() < 1) {
            Toast.makeText(getApplicationContext(), "El producto seleccionado no tiene tamaños, consulte al administrador para agregarlos", Toast.LENGTH_LONG).show();
            return;
        }
        String variantes[] = new String[p.getTipoProductos().size()];
        final TipoProducto[] tipos = new TipoProducto[variantes.length];
        p.getTipoProductos().toArray(tipos);


        for (int i = 0; i < variantes.length; i++) {
            variantes[i] = tipos[i].getNombreTipo(); // + " " + CurrencyConverter.currencyFormat(tipos[i].getPrecioTipo());
        }

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setNegativeButton("Cancelar", null);
        builder.setTitle("Escoge un tamaño");
        builder.setItems(variantes, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                if (AppConfig.USE_CONNECTOR)
                    agregarProducto(tipos[which]);
                else {
                    agregarProductoPHP(tipos[which]);
                }
                // Toast.makeText(getApplicationContext(), tipos[which].getNombreTipo() + " pressed", Toast.LENGTH_SHORT).show();
            }


        });
        builder.show();
    }

    private void agregarProductoPHP(final TipoProducto tipo) {
        if (ordenActual == null) {
            Toast.makeText(getApplicationContext(), "Error no se pudo crear el pedido comprueba tu conexion ", Toast.LENGTH_LONG).show();
            finish();
        }
        final OrdenProducto op = new OrdenProducto(0, ordenActual, tipo, tipo.getPrecioTipo(), 1, "", "En cola");
        String tag_string_req = "req_pedidos";
        String urlGetMesas = AppConfig.URL_ADD_PEDIDO;
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
                        setResult(RESULT_OK/*, Intent*/);
                    } else {

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
                Toast.makeText(getApplicationContext(), "Error no se pudo crear el pedido comprueba tu conexion " + (error.getMessage() != null ? error.getMessage() : error.toString()), Toast.LENGTH_LONG).show();
                finish();
            }
        }) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<>();
                params.put("api_key", new SessionManager(getApplicationContext()).getApiKey());
                params.put("id_orden", op.getOrden().getIdOrden() + "");
                params.put("id_tipo_producto", op.getTipoProducto().getIdTipoProducto() + "");
                params.put("id_variantes", "");
                params.put("cantidad", op.getCantidad() + "");
                params.put("comentarios", op.getComentarios());
                return params;
            }
        };

        // Adding request to request queue
        AppController.getInstance().addToRequestQueue(strReq, tag_string_req);
    }

    @SuppressLint("StaticFieldLeak")
    private void agregarProducto(TipoProducto tipo) {
        final OrdenProducto op = new OrdenProducto(0, ordenActual, tipo, tipo.getPrecioTipo(), 1, "", "En cola");
        new AsyncTask<Void, Void, Boolean>() {
            @Override
            protected Boolean doInBackground(Void... voids) {
                return ControlOrdenProducto.getInstance().agregar(op);
            }

            @Override
            protected void onPostExecute(Boolean aBoolean) {
                super.onPostExecute(aBoolean);
                if (aBoolean) {
                    Toast.makeText(ProductosActivity.this, "Se ha agregado el pedido", Toast.LENGTH_SHORT).show();
                    setResult(RESULT_OK/*, Intent*/);
                    // TODO llevar a editar el pedido
                } else {
                    Toast.makeText(ProductosActivity.this, "Error comprueba tu conexion", Toast.LENGTH_LONG).show();
                }
            }
        }.execute();
    }


    private void initializeAdapter(List<List<Producto>> productosPorCategoria) {
        mSectionsPagerAdapter = new SectionsPagerAdapter(getSupportFragmentManager(), productosPorCategoria, new MyProductoItemRecyclerViewAdapter.OnListFragmentInteractionListener() {
            @Override
            public void onListFragmentInteraction(Producto item) {
                // Toast.makeText(getApplicationContext(), item.getNombreProducto() + " pressed", Toast.LENGTH_SHORT).show();
                loadAndShowVariantes(item);
            }
        });

        // Set up the ViewPager with the sections adapter.
        mViewPager = findViewById(R.id.container);
        mViewPager.setAdapter(mSectionsPagerAdapter);

        tabLayout = findViewById(R.id.tabs);
        mViewPager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(tabLayout));
        tabLayout.addOnTabSelectedListener(new TabLayout.ViewPagerOnTabSelectedListener(mViewPager));


        // Agrega todas las categorias al tabLayout
        for (List<Producto> categoria : productosPorCategoria) {
            tabLayout.addTab(tabLayout.newTab().setText(categoria.get(0).getCategoriaProducto().getNombreCategoria()));
        }

        // Agrega tabs y llena los fragmeents

    }


    private void loadContent() {
        String tag_string_req = "req_editar_pedido";
        String urlGetMesas = AppConfig.URL_GET_PRODUCTOS;
        StringRequest strReq = new StringRequest(Request.Method.POST, urlGetMesas, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.d(TAG, "Login Response: " + response.toString());
                try {
                    JSONObject jObj = new JSONObject(response);
                    boolean error = jObj.getBoolean("error");

                    // Check for error node in json
                    if (!error) {
                        List<Producto> productos = ControlProductos.getInstance().getListaFromJSON(jObj.getJSONArray("productos"));
                        List<List<Producto>> productosPorCategoria = OrganizarProductos.organizaPorCategorias(productos);
                        initializeAdapter(productosPorCategoria);
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
                Toast.makeText(getApplicationContext(), "Error no se pudoo cargar el contenido comprueba tu conexion " + (error.getMessage() != null ? error.getMessage() : error.toString()), Toast.LENGTH_LONG).show();
            }
        }) {
            // Se tiene que gacer pos post porque en los comentarions se puede agregar cosas raras
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
        private List<List<Producto>> productosPorCategoria;

        @Override
        protected Boolean doInBackground(Void... voids) {
            List<Producto> productos = ControlProductos.getInstance().getLista();
            for (Producto p : productos) {
                if (p.getImage() == null) {
                    throw new RuntimeException("No se pudo recuperar la imagen");
                }
            }

            productosPorCategoria = OrganizarProductos.organizaPorCategorias(productos);

            return productos != null;
        }

        @Override
        protected void onPostExecute(final Boolean success) {
            if (success) {
                // Create the adapter that will return a fragment for each of the three
                // primary sections of the activity.
                initializeAdapter(productosPorCategoria);
            } else {
                Toast.makeText(getApplicationContext(), "Error no se pudieron cargar lasd categorias", Toast.LENGTH_LONG).show();
            }

            progressBar.setVisibility(View.GONE);
        }


    }


}
