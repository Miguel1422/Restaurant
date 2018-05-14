package com.superescuadronalfa.restaurant.dbEntities;

import java.math.BigDecimal;

public class ProductoVariante {
    private int idProductoVariante;
    private TipoProducto tipoProducto;
    private String nombreVariante;
    private String descripcion;
    private boolean disponible;
    private BigDecimal Variante;

    public ProductoVariante(int idProductoVariante, String nombreVariante, String descripcion, boolean disponible, BigDecimal variante) {
        this.idProductoVariante = idProductoVariante;
        this.nombreVariante = nombreVariante;
        this.descripcion = descripcion;
        this.disponible = disponible;
        Variante = variante;
    }


    public int getIdProductoVariante() {
        return idProductoVariante;
    }

    public TipoProducto getTipoProducto() {
        if (tipoProducto == null) throw new NullPointerException("El tipo es nulo");
        return tipoProducto;
    }

    public void setTipoProducto(TipoProducto tipoProducto) {
        this.tipoProducto = tipoProducto;
    }

    public String getNombreVariante() {
        return nombreVariante;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public boolean isDisponible() {
        return disponible;
    }

    public BigDecimal getVariante() {
        return Variante;
    }
}
