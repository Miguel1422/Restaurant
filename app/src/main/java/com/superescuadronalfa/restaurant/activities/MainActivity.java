package com.superescuadronalfa.restaurant.activities;

import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v4.graphics.drawable.RoundedBitmapDrawable;
import android.support.v4.graphics.drawable.RoundedBitmapDrawableFactory;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.superescuadronalfa.restaurant.LoginActivity;
import com.superescuadronalfa.restaurant.R;
import com.superescuadronalfa.restaurant.activities.adapters.MyMesaItemRecyclerViewAdapter;
import com.superescuadronalfa.restaurant.dbEntities.Mesa;
import com.superescuadronalfa.restaurant.dbEntities.Trabajador;
import com.superescuadronalfa.restaurant.dbEntities.control.ControlMesas;

import java.util.List;

public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    public static final String EXTRA_TRABAJADOR = "com.superescuadronalfa.restaurant.ID_TRABAJADOR";
    public static Context CONTEXT;


    private NavigationView navigationView;
    private RecyclerView rv;
    private ProgressBar progressBar;

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
        Trabajador t = (Trabajador) extras.get(EXTRA_TRABAJADOR);

        progressBar = findViewById(R.id.progressBar);

        progressBar.setVisibility(View.VISIBLE);


        rv = findViewById(R.id.rv);

        LinearLayoutManager llm = new LinearLayoutManager(this);


        rv.setLayoutManager(llm);
        rv.setHasFixedSize(true);

        initializeData();

        UserLoggedTask task = new UserLoggedTask(t);
        task.execute();
        CONTEXT = getApplicationContext();
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

    private void initializeData() {
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
            Intent login = new Intent(getApplicationContext(), LoginActivity.class);
            startActivity(login);
            finish();
        }

        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
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
                View header = navigationView.getHeaderView(0);
                TextView navBarTitle = header.findViewById(R.id.nav_header_title);
                navBarTitle.setText(trabajador.getNombre());

                TextView navBarSubTitle = header.findViewById(R.id.nav_header_subtitle);
                navBarSubTitle.setText(trabajador.getApellidos());

                ImageView imageView = header.findViewById(R.id.nav_header_imageView);
                // imageView.setImageBitmap(trabajador.getImage());

                // Toast.makeText(MainActivity.this.getApplicationContext(), "Bien", Toast.LENGTH_SHORT).show();
                imageView.setImageDrawable(dr);

                for (Mesa m : mesas) {
                    // Toast.makeText(MainActivity.this.getApplicationContext(), "Mesa " + m.getNombre() + " " + m.getEstadoMesa().getNombre(), Toast.LENGTH_SHORT).show();
                }
                initializeAdapter(mesas);
            } else
                Toast.makeText(MainActivity.this.getApplicationContext(), "Error", Toast.LENGTH_SHORT).show();
            progressBar.setVisibility(View.GONE);
        }

        @Override
        protected void onCancelled() {

            MainActivity.this.finish();
        }
    }
}
