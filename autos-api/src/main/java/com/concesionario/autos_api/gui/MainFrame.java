package com.concesionario.autos_api.gui;

import javax.swing.*;
import java.awt.*;

public class MainFrame extends JFrame {

    private int rolUsuario; // Guardamos el rol aquí

    // Constructor recibe el rol
    public MainFrame(int rol) {
        this.rolUsuario = rol;

        setTitle("Concesionario - Menú Principal");
        setSize(600, 400);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(null);

        JLabel lblTitulo = new JLabel("Sistema de Gestión de Autos");
        lblTitulo.setFont(new Font("Arial", Font.BOLD, 24));
        lblTitulo.setHorizontalAlignment(SwingConstants.CENTER);
        lblTitulo.setBounds(50, 30, 500, 40);
        add(lblTitulo);

        // Mensaje del Rol
        String textoRol = (rol == 1) ? "Modo: ADMINISTRADOR" : "Modo: CLIENTE (Comprar)";
        JLabel lblRol = new JLabel(textoRol, SwingConstants.CENTER);
        lblRol.setForeground(Color.BLUE);
        lblRol.setBounds(50, 70, 500, 20);
        add(lblRol);

        JButton btnAutos = new JButton("📦 Ver Inventario de Autos");
        btnAutos.setBounds(150, 120, 300, 50);
        btnAutos.setFont(new Font("Arial", Font.PLAIN, 16));
        btnAutos.setCursor(new Cursor(Cursor.HAND_CURSOR));
        add(btnAutos);

        JButton btnSalir = new JButton("Cerrar Sesión");
        btnSalir.setBounds(150, 200, 300, 50);
        btnSalir.setCursor(new Cursor(Cursor.HAND_CURSOR));
        add(btnSalir);

        btnAutos.addActionListener(e -> {
            // Pasamos el rol a la ventana de autos
            new AutosFrame(rolUsuario).setVisible(true);
        });

        btnSalir.addActionListener(e -> {
            this.dispose();
            new LoginFrame().setVisible(true);
        });
    }
}