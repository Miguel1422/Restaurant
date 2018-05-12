package com.superescuadronalfa.restaurant.dbEntities.helpers;

import com.superescuadronalfa.restaurant.dbEntities.Producto;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class OrganizarProductos {

    // Regreesa todas las categorias y un id para cada categoria
    private static HashMap<String, Integer> obtieneCategoria(List<Producto> productos) {
        HashMap<String, Integer> categoriasSeparadas = new HashMap<>();
        for (Producto p : productos) {
            String categoriaProductoActual = p.getCategoriaProducto().getNombreCategoria();
            if (!categoriasSeparadas.containsKey(categoriaProductoActual)) {
                categoriasSeparadas.put(categoriaProductoActual, categoriasSeparadas.size());
            }
        }
        return categoriasSeparadas;
    }

    public static List<List<Producto>> organizaPorCategorias(List<Producto> productos) {
        List<List<Producto>> categorias = new ArrayList<>();
        HashMap<String, Integer> categoriasConId = obtieneCategoria(productos);
        for (int i = 0; i < categoriasConId.size(); i++) {
            categorias.add(new ArrayList<Producto>());
        }
        for (Producto p : productos) {
            int id = categoriasConId.get(p.getCategoriaProducto().getNombreCategoria());
            categorias.get(id).add(p);
        }

        return categorias;
    }
}
