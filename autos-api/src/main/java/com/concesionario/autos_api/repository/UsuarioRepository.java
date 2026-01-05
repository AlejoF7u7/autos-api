package com.concesionario.autos_api.repository;

import com.concesionario.autos_api.model.Usuario;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;

public interface UsuarioRepository extends JpaRepository<Usuario, Long> {
    // Método mágico para buscar usuarios por su correo
    Optional<Usuario> findByEmail(String email);
}