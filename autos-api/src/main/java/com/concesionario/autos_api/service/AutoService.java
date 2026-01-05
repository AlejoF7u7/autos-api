package com.concesionario.autos_api.service;

import com.concesionario.autos_api.model.Auto;
import com.concesionario.autos_api.repository.AutoRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class AutoService {

    private final AutoRepository autoRepository;

    // Inyección de dependencias (Conectar con el repositorio)
    public AutoService(AutoRepository autoRepository) {
        this.autoRepository = autoRepository;
    }

    // 1. Obtener TODOS los autos (Para el Administrador)
    public List<Auto> listarTodos() {
        return autoRepository.findAll();
    }

    // 2. Obtener solo los DISPONIBLES (Para mostrar al Cliente)
    public List<Auto> listarDisponibles() {
        // Filtramos la lista para devolver solo los que tienen disponible = true
        return autoRepository.findAll().stream()
                .filter(Auto::isDisponible)
                .toList();
    }

    // 3. Guardar o Actualizar un auto
    public Auto guardarAuto(Auto auto) {
        return autoRepository.save(auto);
    }

    // 4. Buscar un auto por ID
    public Optional<Auto> obtenerPorId(Long id) {
        return autoRepository.findById(id);
    }

    // 5. Eliminar definitivamente (Solo Admin)
    public void eliminarAuto(Long id) {
        autoRepository.deleteById(id);
    }

    // 6. ¡LA LÓGICA DE VENTA! 💰
    // En lugar de borrarlo, solo marcamos que ya no está disponible
    public void venderAuto(Long id) {
        Optional<Auto> autoOpt = autoRepository.findById(id);
        if (autoOpt.isPresent()) {
            Auto auto = autoOpt.get();
            auto.setDisponible(false); // Ya se vendió
            autoRepository.save(auto); // Guardamos el cambio
        }
    }
}