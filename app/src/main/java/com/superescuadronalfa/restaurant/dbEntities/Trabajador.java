package com.superescuadronalfa.restaurant.dbEntities;

import android.graphics.Bitmap;
import android.os.Parcel;
import android.os.Parcelable;

import com.superescuadronalfa.restaurant.dbEntities.control.ControlTrabajadores;

import java.util.Date;

public class Trabajador implements Parcelable {

    public static final Creator<Trabajador> CREATOR = new Creator<Trabajador>() {
        @Override
        public Trabajador createFromParcel(Parcel in) {
            return new Trabajador(in);
        }

        @Override
        public Trabajador[] newArray(int size) {
            return new Trabajador[size];
        }
    };
    private int idTrabajador;
    private String nombre;
    private String apellidos;
    private Date fechaNacimiento;
    private String direccion;
    private String telefono;
    private int idPuesto;
    private Puesto puesto;
    private Bitmap image;

    public Trabajador() {
    }

    public Trabajador(String nombre, String apellidos, Date fechaNacimiento, String direccion, String telefono, int idPuesto) {
        this.nombre = nombre;
        this.apellidos = apellidos;
        this.fechaNacimiento = fechaNacimiento;
        this.direccion = direccion;
        this.telefono = telefono;
        this.idPuesto = idPuesto;
    }

    protected Trabajador(Parcel in) {
        idTrabajador = in.readInt();
        nombre = in.readString();
        apellidos = in.readString();
        direccion = in.readString();
        telefono = in.readString();
        idPuesto = in.readInt();
    }

    public int getIdTrabajador() {
        return idTrabajador;
    }

    public void setIdTrabajador(int idTrabajador) {
        this.idTrabajador = idTrabajador;
    }

    public String getNombre() {

        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getApellidos() {
        return apellidos;
    }

    public void setApellidos(String apellidos) {
        this.apellidos = apellidos;
    }

    public Date getFechaNacimiento() {
        return fechaNacimiento;
    }

    public void setFechaNacimiento(Date fechaNacimiento) {
        this.fechaNacimiento = fechaNacimiento;
    }

    public String getDireccion() {
        return direccion;
    }

    public void setDireccion(String direccion) {
        this.direccion = direccion;
    }

    public String getTelefono() {
        return telefono;
    }

    public void setTelefono(String telefono) {
        this.telefono = telefono;
    }

    public int getIdPuesto() {
        return idPuesto;
    }

    public void setIdPuesto(int idPuesto) {
        this.idPuesto = idPuesto;
    }

    public Puesto getPuesto() {
        return puesto;
    }

    public void setPuesto(Puesto puesto) {
        this.puesto = puesto;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public Bitmap getImage() {
        if (image == null) loadImage();
        return image;
    }

    public void setImage(Bitmap image) {
        this.image = image;
    }

    private void loadImage() {
        image = ControlTrabajadores.getInstance().burcarImagen(this);
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(idTrabajador);
        dest.writeString(nombre);
        dest.writeString(apellidos);
        dest.writeString(direccion);
        dest.writeString(telefono);
        dest.writeInt(idPuesto);
    }
}
