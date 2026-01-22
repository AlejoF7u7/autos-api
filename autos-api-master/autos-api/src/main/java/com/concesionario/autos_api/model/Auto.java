package com.concesionario.autos_api.model; // 👈 ESTA LÍNEA ES VITAL

import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

@Entity
@Table(name = "autos")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Auto {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank(message = "La placa es obligatoria")
    @Pattern(regexp = "^[A-Z]{3}-\\d{3,4}$", message = "Formato inválido (Ej: AAA-1234)")
    @Column(unique = true)
    private String placa;

    @NotBlank(message = "La marca es obligatoria")
    private String marca;

    @NotBlank(message = "El modelo es obligatorio")
    private String modelo;

    @NotNull(message = "El precio es obligatorio")
    @Min(value = 500, message = "El precio mínimo es $500")
    private Double precio;

    private Boolean disponible = true;

    // Campo de imagen (si te faltaba este, el DataInitializer fallaba)
    @Column(length = 2048)
    private String imagenUrl;
}