package com.example.sucursal.controller;

import com.example.sucursal.model.Franquicia;
import com.example.sucursal.model.ProductoMaxStockDTO;
import com.example.sucursal.service.FranquiciaService;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/franquicias")
public class FranquiciaController {

    @Autowired
    private FranquiciaService franquiciaService;

    @PostMapping("/crear")
    @ResponseStatus(HttpStatus.CREATED)
    public Mono<String> agregarFranquicia(@RequestBody Franquicia franquicia) {
        // Estilo funcional: transformamos el resultado directamente
        return franquiciaService.crearFranquicia(franquicia)
                .map(id -> "Franquicia creada con ID: " + id)
                .onErrorResume(e -> Mono.just("Error: " + e.getMessage()));
    }

    // PUT: Actualizar nombre (El Plus)
    @PutMapping("/actualizar/{franquiciaId}")
    public Mono<String> actualizarFranquicia(@PathVariable String franquiciaId,
            @RequestBody Franquicia franquicia) {
        try {
            if (franquiciaId == null || franquicia.getNombre() == null) {
                return Mono.error(new IllegalArgumentException("Franquicia ID y nuevo nombre no pueden ser nulos"));
            }

            return franquiciaService.actualizarFranquicia(franquiciaId, franquicia.getNombre())
                    .thenReturn("Nombre de franquicia actualizado con éxito");
        } catch (Exception e) {
            return Mono.error(e);
        }

    }

    @GetMapping("/{fId}/max-stock")
    public Flux<ProductoMaxStockDTO> obtenerMaxStock(@PathVariable String fId) {
        return franquiciaService.obtenerProductosMaxStockPorSucursal(fId);
    }
}