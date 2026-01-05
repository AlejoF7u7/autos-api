package com.concesionario.autos_api.repository;

import com.concesionario.autos_api.model.Auto;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AutoRepository extends JpaRepository<Auto, Long> {
    // Aquí ya tenemos listos métodos como save(), findAll(), deleteById(), etc.
}
