package com.concesionario.autos_api.controller;

import com.concesionario.autos_api.model.Auto;
import com.concesionario.autos_api.service.AutoService;
import jakarta.validation.Valid; 
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/autos")
public class AutoController {

    
    private final AutoService autoService;

    public AutoController(AutoService autoService) {
        this.autoService = autoService;
    }

    // 1. OBTENER TODOS
    @GetMapping
    public List<Auto> listarAutos() {
        return autoService.listarTodos();
    }

    // 2. GUARDAR NUEVO 
    @PostMapping
    public ResponseEntity<Auto> guardarAuto(@Valid @RequestBody Auto auto) {
        
        if(auto.getDisponible() == null) {
            auto.setDisponible(true);
        }
        return ResponseEntity.ok(autoService.guardarAuto(auto));
    }

    // 3. ACTUALIZAR
    @PutMapping("/{id}")
    public ResponseEntity<Auto> actualizarAuto(@PathVariable Long id, @Valid @RequestBody Auto autoDetalles) {
        return autoService.obtenerPorId(id)
                .map(auto -> {
                    auto.setPlaca(autoDetalles.getPlaca());
                    auto.setMarca(autoDetalles.getMarca());
                    auto.setModelo(autoDetalles.getModelo());
                    auto.setPrecio(autoDetalles.getPrecio());

                    if(autoDetalles.getDisponible() != null) {
                        auto.setDisponible(autoDetalles.getDisponible());
                    }

                    return ResponseEntity.ok(autoService.guardarAuto(auto));
                })
                .orElse(ResponseEntity.notFound().build());
    }

    // 4. ELIMINAR
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminarAuto(@PathVariable Long id) {
        autoService.eliminarAuto(id);
        return ResponseEntity.noContent().build();
    }
}
