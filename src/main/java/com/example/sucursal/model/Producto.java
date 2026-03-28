package com.example.sucursal.model;

public class Producto {
    private String id;
    private String nombre;
    private int stock;

    public Producto() {}

    public String getId() { return id; }
    public void setId(String id) { this.id = id; }
    public String getNombre() { return nombre; }
    public void setNombre(String nombre) { this.nombre = nombre; }
    public int getStock() { return stock; }
    public void setStock(int stock) { this.stock = stock; }
    
}
