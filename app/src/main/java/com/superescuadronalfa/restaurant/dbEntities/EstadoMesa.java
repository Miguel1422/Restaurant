package com.superescuadronalfa.restaurant.dbEntities;

import android.os.Parcel;
import android.os.Parcelable;

public class EstadoMesa implements Parcelable {
    public static final Creator<EstadoMesa> CREATOR = new Creator<EstadoMesa>() {
        @Override
        public EstadoMesa createFromParcel(Parcel in) {
            return new EstadoMesa(in);
        }

        @Override
        public EstadoMesa[] newArray(int size) {
            return new EstadoMesa[size];
        }
    };
    private int idEstadoMesa;
    private String nombre;

    public EstadoMesa(int idEstadoMesa, String nombre) {
        this.idEstadoMesa = idEstadoMesa;
        this.nombre = nombre;
    }

    protected EstadoMesa(Parcel in) {
        idEstadoMesa = in.readInt();
        nombre = in.readString();
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(idEstadoMesa);
        dest.writeString(nombre);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public int getIdEstadoMesa() {
        return idEstadoMesa;
    }

    public void setIdEstadoMesa(int idEstadoMesa) {
        this.idEstadoMesa = idEstadoMesa;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }
}
