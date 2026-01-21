package com.concesionario.autos_api.config;

import com.concesionario.autos_api.model.Auto;
import com.concesionario.autos_api.model.Rol;
import com.concesionario.autos_api.model.Usuario;
import com.concesionario.autos_api.repository.AutoRepository;
import com.concesionario.autos_api.repository.RolRepository;
import com.concesionario.autos_api.repository.UsuarioRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.Optional;

@Configuration
public class DataInitializer {

    @Bean
    CommandLineRunner initDatabase(RolRepository rolRepo,
                                   UsuarioRepository usuarioRepo,
                                   AutoRepository autoRepo) {
        return args -> {
            // 1. Crear Roles
            Rol rolAdmin = crearRolSiNoExiste(rolRepo, "ADMINISTRADOR");
            Rol rolCliente = crearRolSiNoExiste(rolRepo, "CLIENTE");

            // 2. Crear Usuario Admin
            if (usuarioRepo.findByEmail("admin@autos.com").isEmpty()) {
                Usuario admin = new Usuario();
                admin.setNombreCompleto("Admin Principal");
                admin.setEmail("admin@autos.com");
                admin.setPassword("admin123");
                admin.setRol(rolAdmin);
                usuarioRepo.save(admin);
                System.out.println("✅ ADMIN CREADO: admin@autos.com / admin123");
            }

            // 3. Crear Usuario Cliente de prueba
            if (usuarioRepo.findByEmail("cliente@autos.com").isEmpty()) {
                Usuario cliente = new Usuario();
                cliente.setNombreCompleto("Cliente Prueba");
                cliente.setEmail("cliente@autos.com");
                cliente.setPassword("1234");
                cliente.setRol(rolCliente);
                usuarioRepo.save(cliente);
                System.out.println("✅ CLIENTE CREADO: cliente@autos.com / 1234");
            }

            // 4. PRECARGA DE AUTOS (Para que la lista no salga vacía al inicio)
            if (autoRepo.count() == 0) {
                autoRepo.save(new Auto(null, "PBA-1234", "Toyota", "Corolla", 25000.0, true));
                autoRepo.save(new Auto(null, "GCA-5678", "Chevrolet", "Sail", 14500.0, true));
                autoRepo.save(new Auto(null, "UIO-9090", "Mazda", "CX-5", 32000.0, true));
                System.out.println("✅ 3 AUTOS DE PRUEBA CREADOS");
            }
        };
    }

    private Rol crearRolSiNoExiste(RolRepository repo, String nombre) {
        return repo.findByNombre(nombre).orElseGet(() -> {
            Rol nuevoRol = new Rol();
            nuevoRol.setNombre(nombre);
            return repo.save(nuevoRol);
        });
    }
}