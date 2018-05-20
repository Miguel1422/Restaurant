package com.superescuadronalfa.restaurant.dbEntities;

import android.os.Parcel;
import android.os.Parcelable;

import com.superescuadronalfa.restaurant.dbEntities.control.ControlOrdenes;

import java.util.List;

public class Orden implements Parcelable {
    private int idOrden;
    private List<OrdenProducto> productos;
    private Mesa mesa;

    public Orden(int idOrden) {
        this.idOrden = idOrden;
    }

    public Orden(Mesa m) {
        this.mesa = m;
    }


    public static final Creator<Orden> CREATOR = new Creator<Orden>() {
        @Override
        public Orden createFromParcel(Parcel in) {
            return new Orden(in);
        }

        @Override
        public Orden[] newArray(int size) {
            return new Orden[size];
        }
    };

    protected Orden(Parcel in) {
        idOrden = in.readInt();
        mesa = in.readParcelable(Mesa.class.getClassLoader());
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(idOrden);
        dest.writeParcelable(mesa, flags);
    }

    @Override
    public int describeContents() {
        return 0;
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
