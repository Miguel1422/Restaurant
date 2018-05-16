package com.superescuadronalfa.restaurant.dbEntities;

import com.superescuadronalfa.restaurant.dbEntities.control.ControlOrdenProducto;

import java.math.BigDecimal;
import java.util.List;

public class OrdenProducto {
    private int idOrdenProducto;
    private Orden orden;
    private TipoProducto tipoProducto;
    private BigDecimal precio;
    private int cantidad;
    private String comentarios;
    private String status;
    private List<ProductoVariante> variantesDeLaOrden;

    public OrdenProducto(int idOrdenProducto, TipoProducto tipoProducto, BigDecimal precio, int cantidad, String comentarios, String status) {
        this.idOrdenProducto = idOrdenProducto;
        this.tipoProducto = tipoProducto;
        this.precio = precio;
        this.cantidad = cantidad;
        this.comentarios = comentarios;
        this.status = status;
    }

    public int getIdOrdenProducto() {
        return idOrdenProducto;
    }

    public List<ProductoVariante> getVariantesDeLaOrden() {
        if (variantesDeLaOrden == null) loadVariantes();
        return variantesDeLaOrden;
    }

    public void setVariantesDeLaOrden(List<ProductoVariante> variantesDeLaOrden) {
        this.variantesDeLaOrden = variantesDeLaOrden;
    }

    private void loadVariantes() {
        variantesDeLaOrden = ControlOrdenProducto.getInstance().variantesDeLaOrden(this);
    }

    public Orden getOrden() {
        if (orden == null) throw new NullPointerException("Error Orden es nulo(OrdenProducto)");
        return orden;
    }

    public void setOrden(Orden orden) {
        this.orden = orden;
    }

    public TipoProducto getTipoProducto() {
        if (tipoProducto == null)
            throw new NullPointerException("Error Tipo producto es nulo(OrdenProducto)");
        return tipoProducto;
    }

    public BigDecimal getPrecio() {
        return precio;
    }

    public int getCantidad() {
        return cantidad;
    }

    public String getComentarios() {
        return comentarios;
    }

    public String getStatus() {
        return status;
    }
}
