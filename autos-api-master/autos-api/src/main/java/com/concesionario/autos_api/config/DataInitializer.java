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

@Configuration
public class DataInitializer {

    @Bean
    CommandLineRunner initDatabase(RolRepository rolRepo,
                                   UsuarioRepository usuarioRepo,
                                   AutoRepository autoRepo) {
        return args -> {

            Rol rolAdmin = crearRolSiNoExiste(rolRepo, "ADMIN");
            Rol rolCliente = crearRolSiNoExiste(rolRepo, "CLIENTE");

            if (usuarioRepo.findByEmail("admin@autos.com").isEmpty()) {
                Usuario admin = new Usuario();
                admin.setNombreCompleto("Admin Principal");
                admin.setEmail("admin@autos.com");
                admin.setPassword("admin123");
                admin.setRol(rolAdmin);
                admin.setSaldo(0.0);
                usuarioRepo.save(admin);
            }

            if (usuarioRepo.findByEmail("cliente@autos.com").isEmpty()) {
                Usuario cliente = new Usuario();
                cliente.setNombreCompleto("Cliente Millonario");
                cliente.setEmail("cliente@autos.com");
                cliente.setPassword("1234");
                cliente.setRol(rolCliente);
                cliente.setSaldo(60000.0);
                usuarioRepo.save(cliente);
            }

            if (autoRepo.count() == 0) {
                autoRepo.save(new Auto(null, "PBA-1234", "Toyota", "Corolla", 25000.0, true, "https://upload.wikimedia.org/wikipedia/commons/thumb/c/cb/F40_ferrari_20090509.jpg/800px-F40_ferrari_20090509.jpg"));
                autoRepo.save(new Auto(null, "UIO-9090", "Mercedes", "Benz", 85000.0, true, "https://upload.wikimedia.org/wikipedia/commons/thumb/4/44/2018_Mercedes-Benz_A200_AMG_Line_Premium_Automatic_1.3.jpg/800px-2018_Mercedes-Benz_A200_AMG_Line_Premium_Automatic_1.3.jpg"));
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