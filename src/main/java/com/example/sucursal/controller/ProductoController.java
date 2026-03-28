package com.example.sucursal.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.sucursal.service.ProductoService;
import com.example.sucursal.model.Producto;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping("/api/franquicia/{franquiciaId}/sucursales/{sucursalId}/productos")
public class ProductoController {

    @Autowired
    private ProductoService productoService;

    @PostMapping("/crear")
    public Mono<String> createProducto(@RequestBody Producto producto, @PathVariable String sucursalId,
            @PathVariable String franquiciaId) {
        try {
            if (producto == null || sucursalId == null || franquiciaId == null) {
                return Mono.error(
                        new IllegalArgumentException("Producto, Sucursal ID y Franquicia ID no pueden ser nulos"));
            }
            return productoService.crearProducto(franquiciaId, sucursalId, producto)
                    .map(id -> "Producto creado con ID: " + id);
        } catch (Exception e) {
            return Mono.error(e);
        }

    }

    @DeleteMapping("/{productId}/eliminar")
    public Mono<String> deleteProducto(@PathVariable String productId, @PathVariable String sucursalId,
            @PathVariable String franquiciaId) {
        try {
            if (productId == null || sucursalId == null || franquiciaId == null) {
                return Mono.error(
                        new IllegalArgumentException("Producto, Sucursal ID y Franquicia ID no pueden ser nulos"));
            }
            return productoService.eliminarProducto(franquiciaId, sucursalId, productId)
                    .map(id -> "Producto eliminado con ID: " + id);
        } catch (Exception e) {
            return Mono.error(e);
        }
    }

    @PatchMapping("/{productId}/stock")
    public Mono<String> actualizarStock(
            @PathVariable String franquiciaId,
            @PathVariable String sucursalId,
            @PathVariable String productId,
            @RequestBody Producto producto) {

        return productoService.actualizarStock(franquiciaId, sucursalId, productId, producto.getStock())
                .thenReturn("Stock actualizado con éxito a: " + producto.getStock());
    }

    @PatchMapping("/{productId}/nombre")
    public Mono<String> actualizarNombre(
            @PathVariable String franquiciaId,
            @PathVariable String sucursalId,
            @PathVariable String productId,
            @RequestBody Producto producto) {

        return productoService.actualizarNombreProducto(franquiciaId, sucursalId, productId, producto.getNombre())
                .thenReturn("El nombre del producto ha sido actualizado a: " + producto.getNombre());
    }
}
