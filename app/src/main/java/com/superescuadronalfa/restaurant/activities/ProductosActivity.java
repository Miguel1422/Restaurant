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

import com.superescuadronalfa.restaurant.R;
import com.superescuadronalfa.restaurant.activities.adapters.MyProductoItemRecyclerViewAdapter;
import com.superescuadronalfa.restaurant.activities.adapters.SectionsPagerAdapter;
import com.superescuadronalfa.restaurant.dbEntities.Producto;
import com.superescuadronalfa.restaurant.dbEntities.TipoProducto;
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
        Log.d("Create", "Creating the Productos acivirty");
        new LoadContent().execute();
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
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressLint("StaticFieldLeak")
    private void loadAndShowVariantes(final Producto p) {
        if (!p.hasTiposLoaded()) {
            progressBar.setVisibility(View.VISIBLE);
            new AsyncTask<Void, Void, Boolean>() {
                @Override
                protected Boolean doInBackground(Void... voids) {
                    p.getTipoProductos().size();
                    return true;
                }

                @Override
                protected void onPostExecute(Boolean aBoolean) {
                    super.onPostExecute(aBoolean);
                    progressBar.setVisibility(View.GONE);

                    if (aBoolean) {
                        showVariantes(p);
                    } else {
                        Toast.makeText(getApplicationContext(), "Ha ocurrido un error, comprueba tu conexion", Toast.LENGTH_LONG).show();
                    }
                }
            }.execute();
        } else {
            showVariantes(p);
        }
    }

    private void showVariantes(Producto p) {
        if (p.getTipoProductos().size() < 1) {
            Toast.makeText(getApplicationContext(), "El producto seleccionado no tiene tamaños, consulte al administrador para agregarlos", Toast.LENGTH_LONG).show();
            return;
        }
        String colors[] = new String[p.getTipoProductos().size()];
        final TipoProducto[] tipos = new TipoProducto[colors.length];
        p.getTipoProductos().toArray(tipos);


        for (int i = 0; i < colors.length; i++) {
            colors[i] = tipos[i].getNombreTipo(); // + " " + CurrencyConverter.currencyFormat(tipos[i].getPrecioTipo());
        }

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setNegativeButton("Cancelar", null);
        builder.setTitle("Escoge un tamaño");
        builder.setItems(colors, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                Toast.makeText(getApplicationContext(), tipos[which].getNombreTipo() + " pressed", Toast.LENGTH_SHORT).show();
            }
        });
        builder.show();
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
                mSectionsPagerAdapter = new SectionsPagerAdapter(getSupportFragmentManager(), productosPorCategoria, new MyProductoItemRecyclerViewAdapter.OnListFragmentInteractionListener() {
                    @Override
                    public void onListFragmentInteraction(Producto item) {
                        Toast.makeText(getApplicationContext(), item.getNombreProducto() + " pressed", Toast.LENGTH_SHORT).show();
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

                setResult(RESULT_OK/*, Intent*/);
                // Agrega tabs y llena los fragmeents
            } else {
                Toast.makeText(getApplicationContext(), "Error no se pudieron cargar lasd categorias", Toast.LENGTH_LONG).show();
            }

            progressBar.setVisibility(View.GONE);
        }


    }


}
