package com.superescuadronalfa.restaurant.activities;

import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v4.graphics.drawable.RoundedBitmapDrawable;
import android.support.v4.graphics.drawable.RoundedBitmapDrawableFactory;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Base64;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.superescuadronalfa.restaurant.R;
import com.superescuadronalfa.restaurant.activities.adapters.MyMesaItemRecyclerViewAdapter;
import com.superescuadronalfa.restaurant.app.AppConfig;
import com.superescuadronalfa.restaurant.app.AppController;
import com.superescuadronalfa.restaurant.dbEntities.Mesa;
import com.superescuadronalfa.restaurant.dbEntities.Trabajador;
import com.superescuadronalfa.restaurant.dbEntities.control.ControlMesas;
import com.superescuadronalfa.restaurant.helper.SessionManager;
import com.superescuadronalfa.restaurant.io.ImageUtils;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    public static final String EXTRA_TRABAJADOR = "com.superescuadronalfa.restaurant.ID_TRABAJADOR";

    private static final String TAG = MainActivity.class.getSimpleName();
    private NavigationView navigationView;
    private RecyclerView rv;
    private ProgressBar progressBar;
    private ImageView imageView;
    private Trabajador trabajador;
    private SwipeRefreshLayout swipeLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);


        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        navigationView = findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        // Codigo agregado

        Bundle extras = getIntent().getExtras();
        if (extras == null || extras.get(EXTRA_TRABAJADOR) == null) {
            Toast.makeText(getApplicationContext(), "No se tiene mesero, ERROR", Toast.LENGTH_LONG).show();
            return;
        }
        trabajador = (Trabajador) extras.get(EXTRA_TRABAJADOR);

        progressBar = findViewById(R.id.progressBar);
        swipeLayout = findViewById(R.id.swipe_layout);

        swipeLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                if (AppConfig.USE_CONNECTOR) {
                    UserLoggedTask task = new UserLoggedTask(trabajador);
                    task.execute();
                } else {
                    userLogged();
                }
            }
        });
        swipeLayout.setColorSchemeResources(R.color.colorPrimary,
                android.R.color.holo_green_dark,
                android.R.color.holo_orange_dark,
                android.R.color.holo_blue_dark);

        progressBar.setVisibility(View.VISIBLE);


        rv = findViewById(R.id.rv);

        LinearLayoutManager llm = new LinearLayoutManager(this);


        rv.setLayoutManager(llm);
        rv.setHasFixedSize(true);


        View header = navigationView.getHeaderView(0);
        TextView navBarTitle = header.findViewById(R.id.nav_header_title);
        navBarTitle.setText(trabajador.getNombre());

        TextView navBarSubTitle = header.findViewById(R.id.nav_header_subtitle);
        navBarSubTitle.setText(trabajador.getApellidos());

        imageView = header.findViewById(R.id.nav_header_imageView);

        if (AppConfig.USE_CONNECTOR) {
            UserLoggedTask task = new UserLoggedTask(trabajador);
            task.execute();
        } else {
            userLogged();
        }
    }

    protected void onSaveInstanceState(Bundle bundle) {
        super.onSaveInstanceState(bundle);
        Bundle extras = getIntent().getExtras();
        Trabajador t = (Trabajador) extras.get(EXTRA_TRABAJADOR);

        bundle.putParcelable(EXTRA_TRABAJADOR, t);

    }

    private void initializeAdapter(List<Mesa> mesas) {

        MyMesaItemRecyclerViewAdapter adap = new MyMesaItemRecyclerViewAdapter(mesas, new MyMesaItemRecyclerViewAdapter.OnListFragmentInteractionListener() {
            @Override
            public void onListFragmentInteraction(Mesa item) {
                Intent mesaIntent = new Intent(MainActivity.this, PedidosActivity.class);
                mesaIntent.putExtra(PedidosActivity.EXTRA_MESA, item);
                startActivity(mesaIntent);
                // Toast.makeText(MainActivity.this, item.getNombre(), Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onListImageClicked(Mesa mItem) {

            }

            @Override
            public void onListButtonClicked(Mesa mItem) {

            }


        });
        // rv.setAdapter(adapter);
        rv.setAdapter(adap);
    }


    @Override
    public void onStart() {
        super.onStart();

    }

    @Override
    public void onResume() {
        super.onResume();


    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
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
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_camera) {
            // Handle the camera action
        } else if (id == R.id.nav_gallery) {

        } else if (id == R.id.nav_slideshow) {

        } else if (id == R.id.nav_manage) {

        } else if (id == R.id.nav_share) {

        } else if (id == R.id.nav_send) {

        } else if (id == R.id.nav_exit) {
            new SessionManager(this).logout();
            Intent login = new Intent(getApplicationContext(), LoginActivity.class);
            startActivity(login);
            finish();
        }

        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    private void userLogged() {
        String tag_string_req = "req_mesas";
        String urlGetMesas = AppConfig.URL_GET_MESAS;
        StringRequest strReq = new StringRequest(Request.Method.POST, urlGetMesas, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.d(TAG, "Login Response: " + response.toString());
                try {
                    JSONObject jObj = new JSONObject(response);
                    boolean error = jObj.getBoolean("error");

                    // Check for error node in json
                    if (!error) {
                        List<Mesa> mesas = ControlMesas.getInstance().getListaFromJSON(jObj.getJSONArray("mesas"));
                        initializeAdapter(mesas);
                        loadImage(trabajador);
                    } else {
                        // Error  Get the error message
                        String errorMsg = jObj.getString("error_msg");
                        Toast.makeText(getApplicationContext(), errorMsg, Toast.LENGTH_LONG).show();
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
                Log.e(TAG, "263 Login Error: " + error.getMessage());
                Toast.makeText(getApplicationContext(), "Error no se cargo el contenido comprueba tu conexion " + (error.getMessage() != null ? error.getMessage() : error.toString()), Toast.LENGTH_LONG).show();
            }
        }) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<>();
                params.put("api_key", new SessionManager(MainActivity.this).getApiKey());
                return params;
            }
        };

        // Adding request to request queue
        AppController.getInstance().addToRequestQueue(strReq, tag_string_req);
    }


    private void loadImage(final Trabajador trabajador) {
        String tag_string_req = "req_image";
        String imageUrl = AppConfig.URL_GET_TRABAJADOR_IMAGE + "?id_trabajador=" + trabajador.getIdTrabajador();

        final ImageUtils utils = new ImageUtils(MainActivity.this, "trabajadores");

        Bitmap image = utils.loadImageFromStorage("" + trabajador.getIdTrabajador());
        if (image != null) {
            setTrabajadorImage(image);
            return;
        }

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
                        setTrabajadorImage(img);
                        utils.saveToInternalStorage(img, trabajador.getIdTrabajador() + "");
                    } else {
                        // Error  Get the error message
                        String errorMsg = jObj.getString("error_msg");
                        Toast.makeText(getApplicationContext(), errorMsg, Toast.LENGTH_LONG).show();
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
                Log.e(TAG, "322 Login Error:" + error.getMessage());
                Toast.makeText(getApplicationContext(), "Error No se cargo la imagen comprueba tu conexion " + (error.getMessage() != null ? error.getMessage() : error.toString()), Toast.LENGTH_LONG).show();
            }
        }) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<>();
                params.put("api_key", new SessionManager(MainActivity.this).getApiKey());
                return params;
            }
        };


        // Adding request to request queue
        AppController.getInstance().addToRequestQueue(imageRequest, tag_string_req);
    }

    private void setTrabajadorImage(Bitmap image) {
        RoundedBitmapDrawable dr;
        dr = RoundedBitmapDrawableFactory.create(getResources(), image);
        dr.setCornerRadius(Math.max(image.getWidth(), image.getHeight()) / 2.0f);
        imageView.setImageDrawable(dr);
    }

    private class UserLoggedTask extends AsyncTask<Void, Void, Boolean> {

        private Trabajador trabajador;
        private RoundedBitmapDrawable dr;
        private List<Mesa> mesas;

        UserLoggedTask(Trabajador trabajador) {
            this.trabajador = trabajador;
        }

        @Override
        protected Boolean doInBackground(Void... voids) {
            Resources res = getResources();
            Bitmap src = trabajador.getImage();
            dr = RoundedBitmapDrawableFactory.create(res, src);
            dr.setCornerRadius(Math.max(src.getWidth(), src.getHeight()) / 2.0f);
            mesas = ControlMesas.getInstance().getLista();
            return true;
        }

        @Override
        protected void onPostExecute(final Boolean success) {
            if (success) {
                imageView.setImageDrawable(dr);
                initializeAdapter(mesas);
            } else
                Toast.makeText(MainActivity.this.getApplicationContext(), "Error", Toast.LENGTH_SHORT).show();
            swipeLayout.setRefreshing(false);
            progressBar.setVisibility(View.GONE);
        }

        @Override
        protected void onCancelled() {

            MainActivity.this.finish();
        }
    }
}
