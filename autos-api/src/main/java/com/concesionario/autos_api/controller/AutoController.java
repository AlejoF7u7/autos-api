package com.concesionario.autos_api.controller;

import com.concesionario.autos_api.model.Auto;
import com.concesionario.autos_api.repository.AutoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/autos")
public class AutoController {

    @Autowired
    private AutoRepository autoRepository;

    // 1. OBTENER TODOS
    @GetMapping
    public List<Auto> listarAutos() {
        return autoRepository.findAll();
    }

    // 2. GUARDAR NUEVO
    @PostMapping
    public Auto guardarAuto(@RequestBody Auto auto) {
        if(auto.isDisponible() == null) {
            auto.setDisponible(true); // Por defecto disponible
        }
        return autoRepository.save(auto);
    }

    // 3. ACTUALIZAR (Sirve para Editar datos y para COMPRAR/Cambiar estado)
    @PutMapping("/{id}")
    public ResponseEntity<Auto> actualizarAuto(@PathVariable Long id, @RequestBody Auto autoDetalles) {
        return autoRepository.findById(id)
                .map(auto -> {
                    // Actualizamos datos básicos
                    auto.setPlaca(autoDetalles.getPlaca());
                    auto.setMarca(autoDetalles.getMarca());
                    auto.setModelo(autoDetalles.getModelo());
                    auto.setPrecio(autoDetalles.getPrecio());

                    // Actualizamos el estado (Importante para la compra)
                    if(autoDetalles.isDisponible() != null) {
                        auto.setDisponible(autoDetalles.isDisponible());
                    }

                    return ResponseEntity.ok(autoRepository.save(auto));
                })
                .orElse(ResponseEntity.notFound().build());
    }

    // 4. ELIMINAR
    @DeleteMapping("/{id}")
    public void eliminarAuto(@PathVariable Long id) {
        autoRepository.deleteById(id);
    }
}