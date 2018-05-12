package com.superescuadronalfa.restaurant.activities;

import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.superescuadronalfa.restaurant.R;
import com.superescuadronalfa.restaurant.activities.adapters.SectionsPagerAdapter;
import com.superescuadronalfa.restaurant.dbEntities.Producto;
import com.superescuadronalfa.restaurant.dbEntities.control.ControlProductos;
import com.superescuadronalfa.restaurant.dbEntities.helpers.OrganizarProductos;

import java.util.List;

public class ProductosActivity extends AppCompatActivity {

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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_productos);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        progressBar = findViewById(R.id.progressBar);

        progressBar.setVisibility(View.VISIBLE);
        new LoadContent().execute();
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
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    private class LoadContent extends AsyncTask<Void, Void, Boolean> {
        private List<List<Producto>> productosPorCategoria;

        @Override
        protected Boolean doInBackground(Void... voids) {
            List<Producto> productos = ControlProductos.getInstance().getLista();

            productosPorCategoria = OrganizarProductos.organizaPorCategorias(productos);

            return productos != null;
        }

        @Override
        protected void onPostExecute(final Boolean success) {
            if (success) {
                // Create the adapter that will return a fragment for each of the three
                // primary sections of the activity.
                mSectionsPagerAdapter = new SectionsPagerAdapter(getSupportFragmentManager(), productosPorCategoria);

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

                setResult(RESULT_OK/*, Intent*/);
                // Agrega tabs y llena los fragmeents
            } else {
                Toast.makeText(getApplicationContext(), "Error no se pudieron cargar lasd categorias", Toast.LENGTH_LONG).show();
            }

            progressBar.setVisibility(View.GONE);
        }


    }


}
