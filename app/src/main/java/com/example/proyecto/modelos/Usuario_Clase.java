package com.example.proyecto.modelos;

//Modelo Usuario_Clase
public class Usuario_Clase {
    private int id;
    private int idClase;

    //Constructores
    public Usuario_Clase() {
    }

    public Usuario_Clase(int id, int idClase) {
        this.id = id;
        this.idClase = idClase;
    }

    //Getters y Setters
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getIdClase() {
        return idClase;
    }

    public void setIdClase(int idClase) {
        this.idClase = idClase;
    }
}
