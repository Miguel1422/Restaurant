package com.superescuadronalfa.restaurant.dbEntities;

import android.os.Parcel;
import android.os.Parcelable;

import com.superescuadronalfa.restaurant.dbEntities.control.ControlTipoProducto;

import java.math.BigDecimal;
import java.util.List;
import java.util.Objects;

public class TipoProducto implements Parcelable{
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

    protected TipoProducto(Parcel in) {
        idTipoProducto = in.readInt();
        producto = in.readParcelable(Producto.class.getClassLoader());
        nombreTipo = in.readString();
        variantes = in.createTypedArrayList(ProductoVariante.CREATOR);
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(idTipoProducto);
        dest.writeParcelable(producto, flags);
        dest.writeString(nombreTipo);
        dest.writeTypedList(variantes);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<TipoProducto> CREATOR = new Creator<TipoProducto>() {
        @Override
        public TipoProducto createFromParcel(Parcel in) {
            return new TipoProducto(in);
        }

        @Override
        public TipoProducto[] newArray(int size) {
            return new TipoProducto[size];
        }
    };

    public int getIdTipoProducto() {
        return idTipoProducto;
    }

    public Producto getProducto() {
        if (producto == null) throw new NullPointerException("Producto es nulo");
        return producto;
    }

    public void setVariantes(List<ProductoVariante> variantes) {
        this.variantes = variantes;
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

    @Override
    public String toString() {
        return nombreTipo;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        TipoProducto that = (TipoProducto) o;
        return idTipoProducto == that.idTipoProducto &&
                Objects.equals(nombreTipo, that.nombreTipo);
    }

    @Override
    public int hashCode() {

        return Objects.hash(idTipoProducto, nombreTipo);
    }
}
