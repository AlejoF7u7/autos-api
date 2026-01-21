package com.concesionario.autos_api.model;

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
    @Pattern(regexp = "^[A-Z]{3}-\\d{3,4}$", message = "La placa debe tener formato AAA-1234")
    @Column(unique = true)
    private String placa;

    @NotBlank(message = "La marca es obligatoria")
    private String marca;

    @NotBlank(message = "El modelo es obligatorio")
    private String modelo;

    @NotNull(message = "El precio es obligatorio")
    @Min(value = 500, message = "El precio debe ser mayor a 500")
    private Double precio;

    // Campo para saber si está vendido o no
    private Boolean disponible = true;
}