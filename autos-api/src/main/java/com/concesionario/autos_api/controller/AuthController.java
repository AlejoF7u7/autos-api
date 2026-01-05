package com.concesionario.autos_api.controller;

import com.concesionario.autos_api.model.Usuario;
import com.concesionario.autos_api.service.UsuarioService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    private final UsuarioService usuarioService;

    public AuthController(UsuarioService usuarioService) {
        this.usuarioService = usuarioService;
    }

    // Endpoint: POST http://localhost:8080/api/auth/login
    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody Usuario loginRequest) {
        // Intentamos loguear con el email y password que nos mandan
        Optional<Usuario> usuario = usuarioService.login(loginRequest.getEmail(), loginRequest.getPassword());

        if (usuario.isPresent()) {
            return ResponseEntity.ok(usuario.get()); // ¡Éxito! Devolvemos el usuario
        } else {
            return ResponseEntity.status(401).body("Credenciales incorrectas"); // Fallo
        }
    }
}