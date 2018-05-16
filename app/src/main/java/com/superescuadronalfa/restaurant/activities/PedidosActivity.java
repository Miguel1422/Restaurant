package com.superescuadronalfa.restaurant.activities;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.superescuadronalfa.restaurant.R;
import com.superescuadronalfa.restaurant.activities.adapters.MyPedidosItemRecyclerViewAdapter;
import com.superescuadronalfa.restaurant.dbEntities.Mesa;
import com.superescuadronalfa.restaurant.dbEntities.Orden;
import com.superescuadronalfa.restaurant.dbEntities.OrdenProducto;
import com.superescuadronalfa.restaurant.dbEntities.ProductoVariante;
import com.superescuadronalfa.restaurant.dbEntities.control.ControlOrdenes;

import java.util.List;

public class PedidosActivity extends AppCompatActivity {
    public static final String EXTRA_MESA = "com.superescuadronalfa.restaurant.ID_MESA";
    private Mesa mesa;
    private Orden orden;
    private RecyclerView rv;
    private ProgressBar progressBar;
    private MyPedidosItemRecyclerViewAdapter adap;
    private FloatingActionButton fab;
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

        progressBar = findViewById(R.id.progressBar);
        if (b != null && (mesa = b.getParcelable(EXTRA_MESA)) != null) {
            this.mesa = mesa;
            progressBar.setVisibility(View.VISIBLE);
            new LoadOrdenMesa().execute(mesa);
        } else {
            Log.d("Error", " No se tiene una mesa");
            finish();
        }
    }


    private void removeListItem(int index) {
        if (adap == null) return;
        adap.deleteItem(index);
    }


    private void initializeAdapter(List<OrdenProducto> ordenProductos) {

        for (OrdenProducto op : ordenProductos) {
            for (ProductoVariante pv : op.getVariantesDeLaOrden()) {
                Toast.makeText(this, pv.getNombreVariante(), Toast.LENGTH_SHORT).show();
            }
        }

        adap = new MyPedidosItemRecyclerViewAdapter(this, ordenProductos, new MyPedidosItemRecyclerViewAdapter.OnListFragmentInteractionListener() {
            @Override
            public void onListFragmentInteraction(OrdenProducto item) {

            }

            @Override
            public void onRemoveClicked(OrdenProducto item, final int index) {
                AlertDialog.Builder builder = new AlertDialog.Builder(PedidosActivity.this);
                builder.setTitle("¿Esta seguro que desea eliminar el pedido?");
                builder.setPositiveButton("Aceptar", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        removeListItem(index);
                    }
                });
                builder.setNegativeButton("Cancelar", null);
                builder.show();
            }


        });

        // rv.setAdapter(adapter);
        rv.setAdapter(adap);
    }

    private class LoadOrdenMesa extends AsyncTask<Mesa, Void, Integer> {
        private static final int ORDEN_NO_CREADA = 1; // No existen ordenes para la mesa
        private static final int ORDEN_CARGADA = 2; // Correco orden cargada
        private static final int ERROR = 3; // Ha ocurrido un error
        private Mesa mesa;

        @Override
        protected Integer doInBackground(Mesa... mesas) {
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
            progressBar.setVisibility(View.GONE);
            switch (result) {
                case ORDEN_CARGADA:
                    initializeAdapter(orden.getProductos());
                    fab.setVisibility(View.VISIBLE);
                    break;
                case ORDEN_NO_CREADA:

                    AlertDialog.Builder builder = new AlertDialog.Builder(PedidosActivity.this);
                    builder.setTitle("No existe una orden ¿Crear?");
                    builder.setPositiveButton("Aceptar", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            new CrearOrden().execute(mesa);
                        }
                    });
                    builder.setNegativeButton("Cancelar", null);

                    builder.setOnDismissListener(new DialogInterface.OnDismissListener() {
                        @Override
                        public void onDismiss(DialogInterface dialog) {
                            // Toast.makeText(getApplicationContext(), "Dismiss", Toast.LENGTH_SHORT).show();
                            finish();
                        }
                    });

                    builder.show();

                    break;
                case ERROR:
                    Toast.makeText(getApplicationContext(), "Erro", Toast.LENGTH_SHORT).show();
                    break;
            }
        }
    }

    private class CrearOrden extends AsyncTask<Mesa, Void, Boolean> {
        private Orden ordentemp;

        @Override
        protected Boolean doInBackground(Mesa... mesas) {
            Mesa m = mesas[0];
            Orden nueva = new Orden(m);
            // TODO Poner la mesa como ocupada
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

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == ProductosActivity.AGREGAR_PRODUCTO) {
            if (resultCode == RESULT_OK) {
                // Refrescar
                //
            }
        }
    }

}
