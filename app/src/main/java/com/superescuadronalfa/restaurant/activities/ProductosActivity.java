package com.superescuadronalfa.restaurant.activities;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.superescuadronalfa.restaurant.R;

public class ProductosActivity extends AppCompatActivity {
    public static final int AGREGAR_PRODUCTO = 1217;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_productos);

        setResult(RESULT_OK/*, intent*/);
    }
}
