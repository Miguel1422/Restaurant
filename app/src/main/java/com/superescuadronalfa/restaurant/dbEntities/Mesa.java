package com.superescuadronalfa.restaurant.dbEntities;

import android.os.Parcel;
import android.os.Parcelable;

public class Mesa implements Parcelable {
    public static final Creator<Mesa> CREATOR = new Creator<Mesa>() {
        @Override
        public Mesa createFromParcel(Parcel in) {
            return new Mesa(in);
        }

        @Override
        public Mesa[] newArray(int size) {
            return new Mesa[size];
        }
    };
    private int idMesa;
    private int idEstado;
    private String nombre;
    private EstadoMesa estadoMesa;

    public Mesa(int idMesa, int idEstado, String nombre) {
        this.idMesa = idMesa;
        this.idEstado = idEstado;
        this.nombre = nombre;
    }

    protected Mesa(Parcel in) {
        idMesa = in.readInt();
        idEstado = in.readInt();
        nombre = in.readString();
        estadoMesa = in.readParcelable(EstadoMesa.class.getClassLoader());
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(idMesa);
        dest.writeInt(idEstado);
        dest.writeString(nombre);
        dest.writeParcelable(estadoMesa, flags);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public EstadoMesa getEstadoMesa() {
        return estadoMesa;
    }

    public void setEstadoMesa(EstadoMesa estadoMesa) {
        idEstado = estadoMesa.getIdEstadoMesa();
        this.estadoMesa = estadoMesa;
    }

    public int getIdMesa() {
        return idMesa;
    }

    public void setIdMesa(int idMesa) {
        this.idMesa = idMesa;
    }

    public int getIdEstado() {
        return idEstado;
    }

    public void setIdEstado(int idEstado) {
        this.idEstado = idEstado;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }
}
