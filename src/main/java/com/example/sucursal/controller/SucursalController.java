package com.example.sucursal.controller;

import com.example.sucursal.model.Sucursal;
import com.example.sucursal.service.SucursalService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping("/api/franquicias/{franquiciaId}/sucursales")
public class SucursalController {

    @Autowired
    private SucursalService sucursalService;

    @PostMapping("/crear")
    @ResponseStatus(HttpStatus.CREATED)
    public Mono<String> agregarSucursal(
            @PathVariable String franquiciaId,
            @RequestBody Sucursal sucursal) {
        try {
            if (franquiciaId == null || sucursal.getNombre() == null) {
                return Mono
                        .error(new IllegalArgumentException("Franquicia ID y nombre de sucursal no pueden ser nulos"));
            }
            return sucursalService.crearSucursal(franquiciaId, sucursal)
                    .map(id -> "Sucursal creada con éxito. ID: " + id);
        } catch (Exception e) {
            return Mono.error(e);
        }
    }
}