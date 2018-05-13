package com.superescuadronalfa.restaurant.dbEntities;

import android.graphics.Bitmap;
import android.os.Parcel;
import android.os.Parcelable;

import com.superescuadronalfa.restaurant.dbEntities.control.ControlProductos;

public class Producto implements Parcelable {
    private int idProducto;
    private String nombreProducto;
    private CategoriaProducto categoriaProducto;
    public static final Creator<Producto> CREATOR = new Creator<Producto>() {
        @Override
        public Producto createFromParcel(Parcel in) {
            return new Producto(in);
        }

        @Override
        public Producto[] newArray(int size) {
            return new Producto[size];
        }
    };

    public Producto(int idProducto, String nombreProducto, CategoriaProducto categoriaProducto) {
        this.idProducto = idProducto;
        this.nombreProducto = nombreProducto;
        this.categoriaProducto = categoriaProducto;
    }

    private Bitmap image;

    protected Producto(Parcel in) {
        idProducto = in.readInt();
        nombreProducto = in.readString();
        image = in.readParcelable(Bitmap.class.getClassLoader());
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(idProducto);
        dest.writeString(nombreProducto);
        dest.writeParcelable(image, flags);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public int getIdProducto() {
        return idProducto;
    }

    public void setIdProducto(int idProducto) {
        this.idProducto = idProducto;
    }

    public String getNombreProducto() {
        return nombreProducto;
    }

    public void setNombreProducto(String nombreProducto) {
        this.nombreProducto = nombreProducto;
    }

    public CategoriaProducto getCategoriaProducto() {
        return categoriaProducto;
    }

    public void setCategoriaProducto(CategoriaProducto categoriaProducto) {
        this.categoriaProducto = categoriaProducto;
    }

    private void loadImage() {
        // TODO Implementar cache
        image = ControlProductos.getInstance().burcarImagen(this);
    }

    public Bitmap getImage() {
        if (image == null) loadImage();
        return image;
    }

    public void setImage(Bitmap image) {
        this.image = image;
    }
}