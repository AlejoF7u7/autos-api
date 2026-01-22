package com.concesionario.autos_api.model;

import jakarta.persistence.*;
import lombok.Data; // Esto crea automáticamente los Getters y Setters (incluido setSaldo)

@Data
@Entity
@Table(name = "usuarios")
public class Usuario {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String nombreCompleto;

    @Column(unique = true, nullable = false)
    private String email;

    private String password;

    private Double saldo = 0.0;

    @ManyToOne
    @JoinColumn(name = "rol_id")
    private Rol rol;
}