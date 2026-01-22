package com.concesionario.autos_api.config;

// 👇 ESTOS IMPORTS SON LOS QUE TE FALTABAN O ESTABAN FALLANDO
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
            // 1. Crear Roles
            Rol rolAdmin = crearRolSiNoExiste(rolRepo, "ADMINISTRADOR");
            Rol rolCliente = crearRolSiNoExiste(rolRepo, "CLIENTE");

            // 2. Crear Admin (Sin dinero, solo gestiona)
            if (usuarioRepo.findByEmail("admin@autos.com").isEmpty()) {
                Usuario admin = new Usuario();
                admin.setNombreCompleto("Admin Principal");
                admin.setEmail("admin@autos.com");
                admin.setPassword("admin123");
                admin.setRol(rolAdmin);
                admin.setSaldo(0.0);
                usuarioRepo.save(admin);
                System.out.println("✅ ADMIN CREADO");
            }

            // 3. Crear Cliente Millonario de prueba ($60,000)
            if (usuarioRepo.findByEmail("cliente@autos.com").isEmpty()) {
                Usuario cliente = new Usuario();
                cliente.setNombreCompleto("Cliente Millonario");
                cliente.setEmail("cliente@autos.com");
                cliente.setPassword("1234");
                cliente.setRol(rolCliente);
                cliente.setSaldo(60000.0); // Billetera cargada
                usuarioRepo.save(cliente);
                System.out.println("✅ CLIENTE CREADO CON SALDO");
            }

            // 4. Crear Autos de Prueba (Con constructor lleno)
            if (autoRepo.count() == 0) {
                // Toyota (Alcanzable)
                autoRepo.save(new Auto(null, "PBA-1234", "Toyota", "Corolla", 25000.0, true, "https://acroadtrip.blob.core.windows.net/catalogo-imagenes/m/RT_V_0aa034070a2d488e89542714c330df32.jpg"));

                // Mercedes (Muy caro para el cliente de prueba)
                autoRepo.save(new Auto(null, "UIO-9090", "Mercedes", "Benz", 85000.0, true, "https://acroadtrip.blob.core.windows.net/catalogo-imagenes/m/RT_V_9c71618751534b72960f473c40156994.jpg"));

                System.out.println("✅ AUTOS DE PRUEBA CREADOS");
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