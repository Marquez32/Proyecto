package com.example.proyecto.modelos;

import java.sql.Blob;

//Modelo imagen
public class Imagen {

    private int idImagen;
    private Blob img;

    //Constructores
    public Imagen() {
    }

    public Imagen(int idImagen, Blob img) {
        this.idImagen = idImagen;
        this.img = img;
    }

    //Getters y Setters
    public int getIdImagen() {
        return idImagen;
    }

    public void setIdImagen(int idImagen) {
        this.idImagen = idImagen;
    }

    public Blob getImg() {
        return img;
    }

    public void setImg(Blob img) {
        this.img = img;
    }
}
