package com.superescuadronalfa.restaurant.dbEntities;


public class User {

    private int idTrabajador;
    private String username;
    private byte[] hash;
    private byte[] salt;

    private Trabajador trabajador;

    public User() {
    }

    public User(int idTrabajador, String username, byte[] hash, byte[] salt) {
        this.idTrabajador = idTrabajador;
        this.username = username;
        this.hash = hash;
        this.salt = salt;
        this.trabajador = trabajador;
    }

    public User(String username, byte[] hash, byte[] salt) {
        this.username = username;
        this.hash = hash;
        this.salt = salt;
    }

    public int getIdTrabajador() {
        return idTrabajador;
    }

    public void setIdTrabajador(int idTrabajador) {
        this.idTrabajador = idTrabajador;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public byte[] getHash() {
        return hash;
    }

    public void setHash(byte[] hash) {
        this.hash = hash;
    }

    public byte[] getSalt() {
        return salt;
    }

    public void setSalt(byte[] salt) {
        this.salt = salt;
    }

    public Trabajador getTrabajador() {
        return trabajador;
    }

    public void setTrabajador(Trabajador trabajador) {
        this.trabajador = trabajador;
    }


    public User busca(String username) {
        return null;
    }

}
