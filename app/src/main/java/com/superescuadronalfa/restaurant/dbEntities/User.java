package com.superescuadronalfa.restaurant.dbEntities;

import com.superescuadronalfa.restaurant.dbEntities.helpers.DBEntity;

public class User extends DBEntity {
    /*
    public int id_trabajador { get; set; }
        public string username { get; set; }
        public byte[] hash { get; set; }
        public byte[] salt { get; set; }

        public virtual Trabajador Trabajador { get; set; }
     */

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
