package com.concesionario.autos_api.model;

import jakarta.persistence.*;

@Entity
@Table(name = "autos")
public class Auto {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String placa;
    private String marca;
    private String modelo;
    private Double precio;

    // --- ESTE ERA EL CAMPO QUE FALTABA ---
    private Boolean disponible;

    public Auto() {
        // Por defecto, al crear un auto, asumimos que está disponible
        this.disponible = true;
    }

    public Auto(String placa, String marca, String modelo, Double precio) {
        this.placa = placa;
        this.marca = marca;
        this.modelo = modelo;
        this.precio = precio;
        this.disponible = true;
    }

    // --- GETTERS ---

    public Long getId() {
        return id;
    }

    public String getPlaca() {
        return placa;
    }

    public String getMarca() {
        return marca;
    }

    public String getModelo() {
        return modelo;
    }

    public Double getPrecio() {
        return precio;
    }

    // Este es el método que busca tu AutoService
    public Boolean isDisponible() {
        return disponible;
    }

    // --- SETTERS ---

    public void setId(Long id) {
        this.id = id;
    }

    public void setPlaca(String placa) {
        this.placa = placa;
    }

    public void setMarca(String marca) {
        this.marca = marca;
    }

    public void setModelo(String modelo) {
        this.modelo = modelo;
    }

    public void setPrecio(Double precio) {
        this.precio = precio;
    }

    // Este es el otro método que buscaba tu AutoService
    public void setDisponible(Boolean disponible) {
        this.disponible = disponible;
    }
}