package com.superescuadronalfa.restaurant.dbEntities;

import com.superescuadronalfa.restaurant.dbEntities.control.ControlTipoProducto;

import java.math.BigDecimal;
import java.util.List;

public class TipoProducto {
    private int idTipoProducto;
    private Producto producto;
    private String nombreTipo;
    private BigDecimal precioTipo;
    private List<ProductoVariante> variantes;

    public TipoProducto(int idTipoProducto, String nombreTipo, BigDecimal precioTipo) {
        this.idTipoProducto = idTipoProducto;
        this.nombreTipo = nombreTipo;
        this.precioTipo = precioTipo;
    }

    public int getIdTipoProducto() {
        return idTipoProducto;
    }

    public Producto getProducto() {
        if (producto == null) throw new NullPointerException("Producto es nulo");
        return producto;
    }

    public void setProducto(Producto producto) {
        this.producto = producto;
    }

    public List<ProductoVariante> getVariantes() {
        if (variantes == null) loadVariantes();
        if (variantes == null) throw new NullPointerException("No se han cargado las variantes");
        return variantes;
    }

    private void loadVariantes() {
        variantes = ControlTipoProducto.getInstance().getVariantes(this);
    }

    public String getNombreTipo() {
        return nombreTipo;
    }

    public BigDecimal getPrecioTipo() {
        return precioTipo;
    }
}
