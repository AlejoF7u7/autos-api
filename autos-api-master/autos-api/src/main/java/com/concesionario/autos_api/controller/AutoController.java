package com.concesionario.autos_api.controller;

import com.concesionario.autos_api.exception.ResourceNotFoundException;
import com.concesionario.autos_api.model.Auto;
import com.concesionario.autos_api.service.AutoService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
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

    // 1. LISTAR TODOS (GET /api/autos)
    @GetMapping
    public List<Auto> listarAutos() {
        return autoService.listarTodos();
    }

    // 2. OBTENER POR ID (GET /api/autos/{id}) -> ¡Este te faltaba!
    @GetMapping("/{id}")
    public ResponseEntity<Auto> obtenerAutoPorId(@PathVariable Long id) {
        Auto auto = autoService.obtenerPorId(id)
                .orElseThrow(() -> new ResourceNotFoundException("No se encontró el auto con ID: " + id));
        return ResponseEntity.ok(auto);
    }

    // 3. CREAR NUEVO (POST /api/autos/Crear)
    @PostMapping("/crear")
    public ResponseEntity<Auto> guardarAuto(@Valid @RequestBody Auto auto) {
        if (auto.getDisponible() == null) {
            auto.setDisponible(true);
        }
        Auto nuevoAuto = autoService.guardarAuto(auto);

        return new ResponseEntity<>(nuevoAuto, HttpStatus.CREATED);
    }

    // 4. ACTUALIZAR (PUT /api/autos/{id})
    @PutMapping("/{id}")
    public ResponseEntity<Auto> actualizarAuto(@PathVariable Long id, @Valid @RequestBody Auto autoDetalles) {
        Auto autoExistente = autoService.obtenerPorId(id)
                .orElseThrow(() -> new ResourceNotFoundException("No se puede actualizar. Auto no encontrado con ID: " + id));

        autoExistente.setPlaca(autoDetalles.getPlaca());
        autoExistente.setMarca(autoDetalles.getMarca());
        autoExistente.setModelo(autoDetalles.getModelo());
        autoExistente.setPrecio(autoDetalles.getPrecio());

        if (autoDetalles.getDisponible() != null) {
            autoExistente.setDisponible(autoDetalles.getDisponible());
        }

        Auto autoActualizado = autoService.guardarAuto(autoExistente);
        return ResponseEntity.ok(autoActualizado);
    }

    // 5. ELIMINAR (DELETE /api/autos/{id})
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminarAuto(@PathVariable Long id) {
        Auto auto = autoService.obtenerPorId(id)
                .orElseThrow(() -> new ResourceNotFoundException("No se puede eliminar. Auto no encontrado con ID: " + id));

        autoService.eliminarAuto(id);
        return ResponseEntity.noContent().build(); // 204 No Content
    }
}