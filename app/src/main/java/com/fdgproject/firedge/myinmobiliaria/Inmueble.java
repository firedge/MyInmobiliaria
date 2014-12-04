package com.fdgproject.firedge.myinmobiliaria;

/**
 * Created by Firedge on 27/11/2014.
 */
public class Inmueble {
    //Localidad, dirección, tipo y precio
    private int id;
    private String localidad, direccion, tipo;
    private double precio;

    public Inmueble(){}

    public Inmueble(String localidad, String direccion, String tipo, double precio) {
        this.localidad = localidad;
        this.direccion = direccion;
        this.tipo = tipo;
        this.precio = precio;
    }

    public Inmueble(int id, String localidad, String direccion, String tipo, double precio) {
        this.id = id;
        this.localidad = localidad;
        this.direccion = direccion;
        this.tipo = tipo;
        this.precio = precio;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getLocalidad() {
        return localidad;
    }

    public void setLocalidad(String localidad) {
        this.localidad = localidad;
    }

    public String getDireccion() {
        return direccion;
    }

    public void setDireccion(String direccion) {
        this.direccion = direccion;
    }

    public String getTipo() {
        return tipo;
    }

    public void setTipo(String tipo) {
        this.tipo = tipo;
    }

    public double getPrecio() {
        return precio;
    }

    public void setPrecio(double precio) {
        this.precio = precio;
    }

    @Override
    public String toString() {
        return "Inmueble{" +
                "localidad='" + localidad + '\'' +
                ", direccion='" + direccion + '\'' +
                ", tipo='" + tipo + '\'' +
                ", precio=" + precio +
                '}';
    }


}
