package com.superescuadronalfa.restaurant.dbEntities;

import android.os.Parcel;
import android.os.Parcelable;

import com.superescuadronalfa.restaurant.dbEntities.control.ControlOrdenProducto;

import java.math.BigDecimal;
import java.util.List;

public class OrdenProducto implements Parcelable {
    private int idOrdenProducto;
    private Orden orden;
    private TipoProducto tipoProducto;
    private BigDecimal precio;
    private int cantidad;
    private String comentarios;
    private String status;
    private List<ProductoVariante> variantesDeLaOrden;

    public OrdenProducto(int idOrdenProducto, Orden ordenProducto, TipoProducto tipoProducto, BigDecimal precio, int cantidad, String comentarios, String status) {
        this.orden = ordenProducto;
        this.idOrdenProducto = idOrdenProducto;
        this.tipoProducto = tipoProducto;
        this.precio = precio;
        this.cantidad = cantidad;
        this.comentarios = comentarios;
        this.status = status;
    }


    protected OrdenProducto(Parcel in) {
        idOrdenProducto = in.readInt();
        orden = in.readParcelable(Orden.class.getClassLoader());
        tipoProducto = in.readParcelable(TipoProducto.class.getClassLoader());
        cantidad = in.readInt();
        comentarios = in.readString();
        status = in.readString();
        variantesDeLaOrden = in.createTypedArrayList(ProductoVariante.CREATOR);
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(idOrdenProducto);
        dest.writeParcelable(orden, flags);
        dest.writeParcelable(tipoProducto, flags);
        dest.writeInt(cantidad);
        dest.writeString(comentarios);
        dest.writeString(status);
        dest.writeTypedList(variantesDeLaOrden);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<OrdenProducto> CREATOR = new Creator<OrdenProducto>() {
        @Override
        public OrdenProducto createFromParcel(Parcel in) {
            return new OrdenProducto(in);
        }

        @Override
        public OrdenProducto[] newArray(int size) {
            return new OrdenProducto[size];
        }
    };

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
