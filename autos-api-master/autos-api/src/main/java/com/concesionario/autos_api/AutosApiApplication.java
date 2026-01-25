package com.concesionario.autos_api;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;



@SpringBootApplication
public class AutosApiApplication {

    public static void main(String[] args) {

        String entorno = System.getenv("RAILWAY_ENVIRONMENT");

        if (entorno != null) {

            System.setProperty("java.awt.headless", "true");
            System.out.println(" MODO NUBE DETECTADO: Interfaz gráfica DESACTIVADA.");
        } else {

            System.setProperty("java.awt.headless", "false");
            System.out.println(" MODO LOCAL: Puedes ejecutar el LoginFrame manualmente si quieres.");
        }


        SpringApplication.run(AutosApiApplication.class, args);


    }
}