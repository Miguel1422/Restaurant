package com.superescuadronalfa.restaurant.dbEntities;

public class Puesto {
    private int idPuesto;
    private String nombre;
    private String descripcion;

    public Puesto() {
    }

    public Puesto(String nombre, String descripcion) {
        this.nombre = nombre;
        this.descripcion = descripcion;
    }

    public int getIdPuesto() {
        return idPuesto;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }
}
