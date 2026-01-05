package com.concesionario.autos_api.repository;

import com.concesionario.autos_api.model.Rol;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;

public interface RolRepository extends JpaRepository<Rol, Long> {
    // Método mágico para buscar un rol por su nombre
    Optional<Rol> findByNombre(String nombre);
}