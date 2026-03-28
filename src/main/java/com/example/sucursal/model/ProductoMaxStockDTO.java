package com.example.sucursal.model;

public class ProductoMaxStockDTO {
    private String sucursalNombre;
    private String productoNombre;
    private int stock;

    public ProductoMaxStockDTO(String sucursalNombre, String productoNombre, int stock) {
        this.sucursalNombre = sucursalNombre;
        this.productoNombre = productoNombre;
        this.stock = stock;
    }

    public String getSucursalNombre() { return sucursalNombre; }
    public String getProductoNombre() { return productoNombre; }
    public int getStock() { return stock; }
}