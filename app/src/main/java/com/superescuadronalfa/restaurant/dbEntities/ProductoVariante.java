package com.superescuadronalfa.restaurant.dbEntities;

import android.os.Parcel;
import android.os.Parcelable;

import java.math.BigDecimal;
import java.util.Objects;

public class ProductoVariante implements Parcelable{
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


    protected ProductoVariante(Parcel in) {
        idProductoVariante = in.readInt();
        nombreVariante = in.readString();
        descripcion = in.readString();
        disponible = in.readByte() != 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(idProductoVariante);
        dest.writeString(nombreVariante);
        dest.writeString(descripcion);
        dest.writeByte((byte) (disponible ? 1 : 0));
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<ProductoVariante> CREATOR = new Creator<ProductoVariante>() {
        @Override
        public ProductoVariante createFromParcel(Parcel in) {
            return new ProductoVariante(in);
        }

        @Override
        public ProductoVariante[] newArray(int size) {
            return new ProductoVariante[size];
        }
    };

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


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ProductoVariante that = (ProductoVariante) o;
        return idProductoVariante == that.idProductoVariante;
    }

    @Override
    public int hashCode() {

        return Objects.hash(idProductoVariante);
    }
}
