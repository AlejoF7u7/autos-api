package com.concesionario.autos_api;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

// OJO: Ya no importamos SwingUtilities ni LoginFrame aquí para evitar errores en el servidor

@SpringBootApplication
public class AutosApiApplication {

    public static void main(String[] args) {
        // 1. Configuramos para que NO necesite pantalla obligatoriamente (Vital para Railway)
        // Si lo pones en "false", Railway fallará. Si lo pones en "true", Swing no abre.
        // TRUCO: Detectamos si estamos en un servidor o en tu PC.
        String entorno = System.getenv("RAILWAY_ENVIRONMENT");

        if (entorno != null) {
            // Estamos en la nube (Railway) -> Modo sin cabeza (Headless)
            System.setProperty("java.awt.headless", "true");
            System.out.println("🚀 MODO NUBE DETECTADO: Interfaz gráfica DESACTIVADA.");
        } else {
            // Estamos en tu PC -> Permitimos ventanas
            System.setProperty("java.awt.headless", "false");
            System.out.println("💻 MODO LOCAL: Puedes ejecutar el LoginFrame manualmente si quieres.");
        }

        // Arrancar Spring Boot (El Backend y la Web)
        SpringApplication.run(AutosApiApplication.class, args);


    }
}