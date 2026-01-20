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

    
    public Auto guardarAuto(Auto auto) {

        
        if (auto.getMarca().equalsIgnoreCase("Lada")) {
            throw new IllegalArgumentException("Lo sentimos, ya no trabajamos con la marca Lada.");
        }

        // VALIDACIÓN DE NEGOCIO 2: Coherencia de precios
    
        if (auto.getMarca().equalsIgnoreCase("Toyota") && auto.getPrecio() < 10000) {
            throw new IllegalArgumentException("El precio parece incorrecto para un Toyota.");
        }

        return autoRepository.save(auto);
    }


    public Optional<Auto> obtenerPorId(Long id) { return autoRepository.findById(id); }
    public void eliminarAuto(Long id) { autoRepository.deleteById(id); }
}
