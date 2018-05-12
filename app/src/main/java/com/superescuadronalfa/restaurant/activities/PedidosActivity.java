package com.superescuadronalfa.restaurant.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;

import com.superescuadronalfa.restaurant.R;

public class PedidosActivity extends AppCompatActivity {
    public static final String EXTRA_MESA = "com.superescuadronalfa.restaurant.ID_MESA";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pedidos);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent agregarProducto = new Intent(PedidosActivity.this, ProductosActivity.class);
                startActivityForResult(agregarProducto, ProductosActivity.AGREGAR_PRODUCTO);
                // Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG).setAction("Action", null).show();
            }
        });
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);


        Bundle b = getIntent().getExtras();
        // TextView mTextView = findViewById(R.id.textViewPedidos);
        // mTextView.setText(((Mesa) b.getParcelable(EXTRA_MESA)).getNombre());
        // tv.setText();
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
