package com.example.sucursal.service;

import com.example.sucursal.model.Franquicia;
import com.example.sucursal.model.ProductoMaxStockDTO;
import com.google.api.core.ApiFuture;
import com.google.cloud.firestore.DocumentReference;
import com.google.cloud.firestore.Firestore;
import com.google.cloud.firestore.QuerySnapshot;
import com.google.cloud.firestore.WriteResult;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class FranquiciaService {

    @Autowired
    private Firestore firestore;

    public Mono<String> crearFranquicia(Franquicia franquicia) {
        DocumentReference docRef = firestore.collection("franquicias").document();
        franquicia.setId(docRef.getId());

        return Mono.create(sink -> {
            ApiFuture<WriteResult> result = docRef.set(franquicia);
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

    public Mono<Void> actualizarFranquicia(String franquiciaId, String nuevoNombre) {
        if (franquiciaId == null || nuevoNombre == null) {
            return Mono.error(new IllegalArgumentException("Los parámetros no pueden ser nulos"));
        }
        return Mono.create(sink -> {
            DocumentReference docRef = firestore.collection("franquicias")
                    .document(franquiciaId);

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

    private <T> Mono<T> toMono(ApiFuture<T> apiFuture) {
        return Mono.create(sink -> {
            apiFuture.addListener(() -> {
                try {
                    sink.success(apiFuture.get());
                } catch (Exception e) {
                    sink.error(e);
                }
            }, Runnable::run);
        });
    }

    public Flux<ProductoMaxStockDTO> obtenerProductosMaxStockPorSucursal(String franquiciaId) {
        if (franquiciaId == null) {
            return Flux.error(new IllegalArgumentException("El franquiciaId no puede ser nulo"));
        }
        return toMono(firestore.collection("franquicias").document(franquiciaId).collection("sucursales").get())
                .flatMapMany(snapshot -> Flux.fromIterable(snapshot.getDocuments()))
                .flatMap(sucursalDoc -> {
                    String nombreSucursal = sucursalDoc.getString("nombre");

                    ApiFuture<QuerySnapshot> query = sucursalDoc.getReference().collection("productos")
                            .orderBy("stock", com.google.cloud.firestore.Query.Direction.DESCENDING)
                            .limit(1)
                            .get();

                    return toMono(query).map(prodSnapshot -> {
                        if (!prodSnapshot.isEmpty()) {
                            var prodDoc = prodSnapshot.getDocuments().get(0);
                            return new ProductoMaxStockDTO(
                                    nombreSucursal,
                                    prodDoc.getString("nombre"),
                                    prodDoc.getLong("stock").intValue());
                        }
                        return new ProductoMaxStockDTO(nombreSucursal, "Sin productos", 0);
                    });
                });
    }
}