package com.superescuadronalfa.restaurant.activities;

import android.annotation.SuppressLint;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.Toast;

import com.superescuadronalfa.restaurant.R;
import com.superescuadronalfa.restaurant.activities.adapters.MyVairanteItemRecyclerViewAdapter;
import com.superescuadronalfa.restaurant.dbEntities.OrdenProducto;
import com.superescuadronalfa.restaurant.dbEntities.ProductoVariante;
import com.superescuadronalfa.restaurant.dbEntities.TipoProducto;
import com.superescuadronalfa.restaurant.dbEntities.control.ControlOrdenProducto;
import com.superescuadronalfa.restaurant.dbEntities.control.ControlTipoProducto;

import java.util.List;

public class EditarPedidoActivity extends AppCompatActivity {

    public static final String EXTRA_PEDIDO = "com.superescuadronalfa.restaurant.EXTRA_PEDIDO";

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

        if (pedido == null)
            throw new NullPointerException("No se ha encontrado un pepdido para la orden");
        spinner = findViewById(R.id.spinner);
        spinnerCan = findViewById(R.id.spinnerCan);

        Button btn = findViewById(R.id.button);
        progressBar = findViewById(R.id.progressBar);

        progressBar.setVisibility(View.VISIBLE);
        listVariantes = findViewById(R.id.rv);
        txtComentarios = findViewById(R.id.editText2);

        txtComentarios.setText(pedido.getComentarios());


        new LoadContent().execute();
    }

    @SuppressLint("StaticFieldLeak")
    public void btnAceptarClick(View v) {
        if (progressBar.getVisibility() == View.VISIBLE) return;
        MyVairanteItemRecyclerViewAdapter adapter = (MyVairanteItemRecyclerViewAdapter) listVariantes.getAdapter();

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

        new AsyncTask<Void, Void, Boolean>() {
            @Override
            protected Boolean doInBackground(Void... voids) {
                return ControlOrdenProducto.getInstance().editar(nuevo);
            }

            @Override
            protected void onPostExecute(Boolean aBoolean) {
                super.onPostExecute(aBoolean);
                if (aBoolean) {
                    setResult(RESULT_OK);
                    Toast.makeText(EditarPedidoActivity.this, "Exito", Toast.LENGTH_SHORT).show();
                    finish();
                } else {
                    Toast.makeText(EditarPedidoActivity.this, "Error comprueba tu conexion", Toast.LENGTH_LONG).show();
                }
            }
        }.execute();

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


            } else {
                Toast.makeText(EditarPedidoActivity.this, "Comprueba tu conexion", Toast.LENGTH_LONG).show();
            }

            progressBar.setVisibility(View.GONE);
        }
    }

}
