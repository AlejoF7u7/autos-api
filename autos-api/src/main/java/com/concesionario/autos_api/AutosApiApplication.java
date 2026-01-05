package com.concesionario.autos_api;

import com.concesionario.autos_api.gui.LoginFrame;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import javax.swing.SwingUtilities;

@SpringBootApplication
public class AutosApiApplication {

    public static void main(String[] args) {
        // A. Configuración para permitir ventanas visuales
        System.setProperty("java.awt.headless", "false");

        // B. Arrancar el Servidor (Backend)
        SpringApplication.run(AutosApiApplication.class, args);

        // C. Abrir la Ventana de Login (Frontend) automáticamente
        SwingUtilities.invokeLater(() -> {
            new LoginFrame().setVisible(true);
        });
    }
}