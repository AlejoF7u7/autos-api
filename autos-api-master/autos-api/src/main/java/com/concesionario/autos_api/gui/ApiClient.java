package com.concesionario.autos_api.gui;

import com.concesionario.autos_api.model.Auto;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ApiClient {

    private static final String URL_API = "http://localhost:8080/api";
    private final HttpClient client;
    private final ObjectMapper mapper;

    public ApiClient() {
        this.client = HttpClient.newHttpClient();
        this.mapper = new ObjectMapper();
    }

    // Login: Devuelve ID del rol (1=Admin, 2=Cliente, -1=Error)
    public int loginObtenerRol(String email, String password) {
        try {
            Map<String, String> datos = new HashMap<>();
            datos.put("email", email);
            datos.put("password", password);
            String jsonInput = mapper.writeValueAsString(datos);

            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create(URL_API + "/auth/login"))
                    .header("Content-Type", "application/json")
                    .POST(HttpRequest.BodyPublishers.ofString(jsonInput))
                    .build();

            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

            if (response.statusCode() == 200) {
                JsonNode rootNode = mapper.readTree(response.body());
                if (rootNode.has("rol")) {
                    return rootNode.get("rol").get("id").asInt();
                }
            }
        } catch (Exception e) { e.printStackTrace(); }
        return -1;
    }

    public List<Auto> obtenerAutos() {
        try {
            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create(URL_API + "/autos"))
                    .GET()
                    .build();
            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
            if (response.statusCode() == 200) {
                return mapper.readValue(response.body(), new TypeReference<List<Auto>>() {});
            }
        } catch (Exception e) { e.printStackTrace(); }
        return new ArrayList<>();
    }

    public boolean guardarAuto(Auto auto) {
        try {
            String jsonInput = mapper.writeValueAsString(auto);
            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create(URL_API + "/autos"))
                    .header("Content-Type", "application/json")
                    .POST(HttpRequest.BodyPublishers.ofString(jsonInput))
                    .build();
            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
            // Aceptamos 200 (OK) o 201 (Created)
            return response.statusCode() == 200 || response.statusCode() == 201;
        } catch (Exception e) { e.printStackTrace(); return false; }
    }

    public boolean actualizarAuto(Long id, Auto auto) {
        try {
            String jsonInput = mapper.writeValueAsString(auto);
            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create(URL_API + "/autos/" + id))
                    .header("Content-Type", "application/json")
                    .PUT(HttpRequest.BodyPublishers.ofString(jsonInput))
                    .build();
            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
            return response.statusCode() == 200;
        } catch (Exception e) { e.printStackTrace(); return false; }
    }

    public boolean eliminarAuto(Long id) {
        try {
            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create(URL_API + "/autos/" + id))
                    .DELETE()
                    .build();
            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
            return response.statusCode() == 200 || response.statusCode() == 204;
        } catch (Exception e) { e.printStackTrace(); return false; }
    }
}