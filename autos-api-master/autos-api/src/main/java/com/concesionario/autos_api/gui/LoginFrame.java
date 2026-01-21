package com.concesionario.autos_api.gui;

import javax.swing.*;
import java.awt.*;

public class LoginFrame extends JFrame {

    private JTextField txtUsuario;
    private JPasswordField txtPassword;
    private JButton btnIngresar;

    public LoginFrame() {
        setTitle("Concesionario - Acceso");
        setSize(350, 250);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(null);
        setResizable(false);
        iniciarComponentes();
    }

    private void iniciarComponentes() {
        JLabel lblTitulo = new JLabel("Iniciar Sesión");
        lblTitulo.setFont(new Font("Arial", Font.BOLD, 18));
        lblTitulo.setBounds(110, 10, 150, 30);
        add(lblTitulo);

        JLabel lblUser = new JLabel("Usuario:");
        lblUser.setBounds(40, 60, 80, 25);
        add(lblUser);

        txtUsuario = new JTextField();
        txtUsuario.setBounds(130, 60, 160, 25);
        add(txtUsuario);

        JLabel lblPass = new JLabel("Contraseña:");
        lblPass.setBounds(40, 100, 80, 25);
        add(lblPass);

        txtPassword = new JPasswordField();
        txtPassword.setBounds(130, 100, 160, 25);
        add(txtPassword);

        btnIngresar = new JButton("Ingresar");
        btnIngresar.setBounds(110, 150, 120, 30);
        btnIngresar.setCursor(new Cursor(Cursor.HAND_CURSOR));
        add(btnIngresar);

        // Acción del botón
        btnIngresar.addActionListener(e -> {
            String usuario = txtUsuario.getText();
            String pass = new String(txtPassword.getPassword());

            if (usuario.isEmpty() || pass.isEmpty()) {
                JOptionPane.showMessageDialog(null, "¡Por favor ingresa todos los datos!");
                return;
            }

            ApiClient cliente = new ApiClient();

            // Usamos el nuevo método que devuelve el número de Rol
            int rolId = cliente.loginObtenerRol(usuario, pass);

            if (rolId != -1) {
                String rolTexto = (rolId == 1) ? "Administrador" : "Cliente";
                JOptionPane.showMessageDialog(null, "¡Bienvenido! Rol: " + rolTexto);

                this.dispose();

                // Pasamos el Rol al menú principal
                new MainFrame(rolId).setVisible(true);

            } else {
                JOptionPane.showMessageDialog(null, "Datos incorrectos", "Error", JOptionPane.ERROR_MESSAGE);
            }
        });
    }

    public static void main(String[] args) {
        try { UIManager.setLookAndFeel("javax.swing.plaf.nimbus.NimbusLookAndFeel"); } catch (Exception e) {}
        SwingUtilities.invokeLater(() -> new LoginFrame().setVisible(true));
    }
}