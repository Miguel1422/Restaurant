package com.superescuadronalfa.restaurant.dbEntities;

import com.superescuadronalfa.restaurant.dbEntities.control.ControlOrdenes;

import java.util.List;

public class Orden {
    private int idOrden;
    private List<OrdenProducto> productos;
    private Mesa mesa;

    public Orden(int idOrden) {
        this.idOrden = idOrden;
    }

    public Orden(Mesa m) {
        this.mesa = m;
    }

    public int getIdOrden() {
        return idOrden;
    }

    public List<OrdenProducto> getProductos() {
        if (productos == null) loadProductos();
        return productos;
    }

    private void loadProductos() {
        productos = ControlOrdenes.getInstance().productosDeLaOrden(this);
    }

    public Mesa getMesa() {
        return mesa;
    }

    public void setMesa(Mesa mesa) {
        this.mesa = mesa;
    }
}
