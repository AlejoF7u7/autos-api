package com.concesionario.autos_api.config;

import com.concesionario.autos_api.model.Rol;
import com.concesionario.autos_api.model.Usuario;
import com.concesionario.autos_api.repository.RolRepository;
import com.concesionario.autos_api.repository.UsuarioRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.Optional;

@Configuration
public class DataInitializer {

    @Bean
    CommandLineRunner initDatabase(RolRepository rolRepo, UsuarioRepository usuarioRepo) {
        return args -> {
            // 1. Crear Roles si no existen
            Rol rolAdmin = crearRolSiNoExiste(rolRepo, "ADMINISTRADOR");
            Rol rolAnalista = crearRolSiNoExiste(rolRepo, "CLIENTE");

            // 2. Crear un Usuario Administrador por defecto si no existe
            if (usuarioRepo.findByEmail("admin@autos.com").isEmpty()) {
                Usuario admin = new Usuario();
                admin.setNombreCompleto("Admin Principal");
                admin.setEmail("admin@autos.com");
                admin.setPassword("admin123"); // En la vida real esto se encripta, pero para tu deber sirve así
                admin.setRol(rolAdmin);

                usuarioRepo.save(admin);
                System.out.println("✅ USUARIO ADMIN CREADO: admin@autos.com / admin123");
            }
        };
    }

    // Método auxiliar para no repetir código
    private Rol crearRolSiNoExiste(RolRepository repo, String nombre) {
        Optional<Rol> rolExistente = repo.findByNombre(nombre);
        if (rolExistente.isPresent()) {
            return rolExistente.get();
        } else {
            Rol nuevoRol = new Rol();
            nuevoRol.setNombre(nombre);
            repo.save(nuevoRol);
            System.out.println("✅ ROL CREADO: " + nombre);
            return nuevoRol;
        }
    }
}