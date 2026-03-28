package com.example.sucursal.service;

import com.example.sucursal.model.Sucursal;
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
public class SucursalService {

    @Autowired
    private Firestore firestore;

    public Mono<String> crearSucursal(String franquiciaId, Sucursal sucursal) {
        return Mono.create(sink -> {
            if (franquiciaId == null) {
                sink.error(new IllegalArgumentException("Franquicia ID no puede ser nulo"));
                return;
            }
            // Referencia: franquicias/ID_FRANQUICIA/sucursales/NUEVO_ID
            DocumentReference docRef = firestore.collection("franquicias")
                    .document(franquiciaId)
                    .collection("sucursales")
                    .document();

            Map<String, Object> data = new HashMap<>();
            data.put("id", docRef.getId());
            data.put("nombre", sucursal.getNombre());

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
}