package com.example.sucursal.service;

import com.example.sucursal.model.Producto;
import com.google.api.core.ApiFuture;
import com.google.cloud.firestore.DocumentReference;
import com.google.cloud.firestore.Firestore;
import com.google.cloud.firestore.WriteResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

import java.util.HashMap;
import java.util.Map;

@Service
public class ProductoService {

    @Autowired
    private Firestore firestore;

    public Mono<String> crearProducto(String franquiciaId, String sucursalId, Producto producto) {
        return Mono.create(sink -> {
            if (franquiciaId == null || sucursalId == null) {
                sink.error(new IllegalArgumentException("Franquicia ID y Sucursal ID no pueden ser nulos"));
                return;
            }
            DocumentReference docRef = firestore.collection("franquicias").document(franquiciaId)
                    .collection("sucursales").document(sucursalId)
                    .collection("productos").document();

            Map<String, Object> data = new HashMap<>();
            data.put("id", docRef.getId());
            data.put("nombre", producto.getNombre());
            data.put("stock", producto.getStock());

            ApiFuture<WriteResult> result = docRef.set(data);

            result.addListener(() -> {
                try {
                    result.get();
                    sink.success(docRef.getId());
                } catch (Exception e) {
                    sink.error(e);
                }
            }, Runnable::run);
        });
    }

    public Mono<String> eliminarProducto(String franquiciaId, String sucursalId, String productoId) {
        return Mono.create(sink -> {
            if (franquiciaId == null || sucursalId == null || productoId == null) {
                sink.error(
                        new IllegalArgumentException("Franquicia ID, Sucursal ID y Producto ID no pueden ser nulos"));
                return;
            }
            DocumentReference docRef = firestore.collection("franquicias").document(franquiciaId)
                    .collection("sucursales").document(sucursalId)
                    .collection("productos").document(productoId);

            ApiFuture<WriteResult> result = docRef.delete();

            result.addListener(() -> {
                try {
                    result.get();
                    sink.success(docRef.getId());
                } catch (Exception e) {
                    sink.error(e);
                }
            }, Runnable::run);
        });
    }

    public Mono<Void> actualizarStock(String franquiciaId, String sucursalId, String productId, int nuevoStock) {
        return Mono.create(sink -> {
            if (franquiciaId == null || sucursalId == null || productId == null) {
                sink.error(
                        new IllegalArgumentException("Franquicia ID, Sucursal ID y Producto ID no pueden ser nulos"));
                return;
            }
            // La misma ruta jerárquica de siempre
            DocumentReference docRef = firestore.collection("franquicias").document(franquiciaId)
                    .collection("sucursales").document(sucursalId)
                    .collection("productos").document(productId);

            ApiFuture<WriteResult> result = docRef.update("stock", nuevoStock);

            result.addListener(() -> {
                try {
                    result.get();
                    sink.success();
                } catch (Exception e) {
                    sink.error(e);
                }
            }, Runnable::run);
        });
    }

    public Mono<Void> actualizarNombreProducto(String franquiciaId , String sucursalId, String productId, String nuevoNombre) {
        return Mono.create(sink -> {
            if (franquiciaId == null || sucursalId == null || productId == null) {
                sink.error(
                        new IllegalArgumentException("Franquicia ID, Sucursal ID y Producto ID no pueden ser nulos"));
                return;
            }
            DocumentReference docRef = firestore.collection("franquicias").document(franquiciaId)
                    .collection("sucursales").document(sucursalId)
                    .collection("productos").document(productId);

            ApiFuture<WriteResult> result = docRef.update("nombre", nuevoNombre);

            result.addListener(() -> {
                try {
                    result.get();
                    sink.success();
                } catch (Exception e) {
                    sink.error(e);
                }
            }, Runnable::run);
        });
    }
}