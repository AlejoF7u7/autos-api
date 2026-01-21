package com.concesionario.autos_api.service;

import com.concesionario.autos_api.model.Auto;
import com.concesionario.autos_api.repository.AutoRepository;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.Optional;

@Service
public class AutoService {

    private final AutoRepository autoRepository;

    public AutoService(AutoRepository autoRepository) {
        this.autoRepository = autoRepository;
    }

    public List<Auto> listarTodos() {
        return autoRepository.findAll();
    }

    public Optional<Auto> obtenerPorId(Long id) {
        return autoRepository.findById(id);
    }

    // --- MÉTODO QUE FALTABA ACTIVAR ---
    public List<Auto> buscarPorPalabra(String palabra) {
        if (palabra != null && !palabra.isEmpty()) {
            return autoRepository.findByMarcaContainingIgnoreCaseOrModeloContainingIgnoreCase(palabra, palabra);
        }
        return autoRepository.findAll();
    }

    public Auto guardarAuto(Auto auto) {
        if (auto.getMarca().equalsIgnoreCase("Lada")) {
            throw new IllegalArgumentException("No trabajamos con la marca Lada.");
        }
        if (auto.getMarca().equalsIgnoreCase("Toyota") && auto.getPrecio() < 10000) {
            throw new IllegalArgumentException("Precio sospechoso para un Toyota.");
        }
        return autoRepository.save(auto);
    }

    public void eliminarAuto(Long id) {
        autoRepository.deleteById(id);
    }
}