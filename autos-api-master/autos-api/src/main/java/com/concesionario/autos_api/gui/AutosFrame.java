package com.concesionario.autos_api.gui;

import com.concesionario.autos_api.model.Auto;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.List;

public class AutosFrame extends JFrame {

    private JTextField txtPlaca, txtMarca, txtModelo, txtPrecio, txtId;
    private JTable tablaAutos;
    private DefaultTableModel modeloTabla;
    private ApiClient apiClient;
    private int rolUsuario;

    // Botones
    private JButton btnGuardar, btnActualizar, btnEliminar, btnComprar, btnLimpiar;

    public AutosFrame(int rol) {
        this.rolUsuario = rol;
        apiClient = new ApiClient();

        setTitle("Inventario - " + (rol == 1 ? "ADMINISTRADOR" : "CLIENTE"));
        setSize(950, 650);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(null);

        iniciarComponentes();
        configurarPermisos();
        cargarDatosTabla();
    }

    private void iniciarComponentes() {
        JLabel lblTitulo = new JLabel("Catálogo de Autos");
        lblTitulo.setFont(new Font("Arial", Font.BOLD, 22));
        lblTitulo.setBounds(30, 20, 300, 30);
        add(lblTitulo);

        // --- CAMPOS ---
        JLabel lblId = new JLabel("ID:"); lblId.setBounds(30, 70, 30, 25); add(lblId);
        txtId = new JTextField(); txtId.setBounds(60, 70, 50, 25); txtId.setEditable(false); add(txtId);

        JLabel lblPlaca = new JLabel("Placa:"); lblPlaca.setBounds(130, 70, 50, 25); add(lblPlaca);
        txtPlaca = new JTextField(); txtPlaca.setBounds(180, 70, 100, 25); add(txtPlaca);

        JLabel lblMarca = new JLabel("Marca:"); lblMarca.setBounds(300, 70, 50, 25); add(lblMarca);
        txtMarca = new JTextField(); txtMarca.setBounds(350, 70, 120, 25); add(txtMarca);

        JLabel lblModelo = new JLabel("Modelo:"); lblModelo.setBounds(490, 70, 60, 25); add(lblModelo);
        txtModelo = new JTextField(); txtModelo.setBounds(550, 70, 100, 25); add(txtModelo);

        JLabel lblPrecio = new JLabel("Precio:"); lblPrecio.setBounds(670, 70, 60, 25); add(lblPrecio);
        txtPrecio = new JTextField(); txtPrecio.setBounds(730, 70, 80, 25); add(txtPrecio);

        // --- BOTONES ---
        btnGuardar = new JButton("Guardar Nuevo");
        btnGuardar.setBounds(550, 120, 130, 30);
        btnGuardar.setBackground(new Color(46, 204, 113));
        btnGuardar.setForeground(Color.WHITE);
        add(btnGuardar);

        btnActualizar = new JButton("Editar Selección");
        btnActualizar.setBounds(400, 120, 130, 30);
        btnActualizar.setBackground(new Color(52, 152, 219));
        btnActualizar.setForeground(Color.WHITE);
        add(btnActualizar);

        btnEliminar = new JButton("Eliminar");
        btnEliminar.setBounds(700, 120, 100, 30);
        btnEliminar.setBackground(new Color(231, 76, 60));
        btnEliminar.setForeground(Color.WHITE);
        add(btnEliminar);

        btnComprar = new JButton("COMPRAR AUTO SELECCIONADO");
        btnComprar.setBounds(400, 120, 400, 30);
        btnComprar.setBackground(new Color(241, 196, 15));
        btnComprar.setForeground(Color.BLACK);
        btnComprar.setFont(new Font("Arial", Font.BOLD, 14));
        btnComprar.setVisible(false);
        add(btnComprar);

        btnLimpiar = new JButton("Limpiar");
        btnLimpiar.setBounds(30, 120, 100, 30);
        add(btnLimpiar);

        // --- TABLA ---
        String[] columnas = {"ID", "Placa", "Marca", "Modelo", "Precio", "Estado"};
        modeloTabla = new DefaultTableModel(null, columnas) {
            @Override
            public boolean isCellEditable(int row, int column) { return false; }
        };
        tablaAutos = new JTable(modeloTabla);
        JScrollPane scroll = new JScrollPane(tablaAutos);
        scroll.setBounds(30, 180, 880, 400);
        add(scroll);

        // --- EVENTOS ---
        tablaAutos.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                int fila = tablaAutos.getSelectedRow();
                if (fila != -1) {
                    txtId.setText(modeloTabla.getValueAt(fila, 0).toString());
                    txtPlaca.setText(modeloTabla.getValueAt(fila, 1).toString());
                    txtMarca.setText(modeloTabla.getValueAt(fila, 2).toString());
                    txtModelo.setText(modeloTabla.getValueAt(fila, 3).toString());
                    txtPrecio.setText(modeloTabla.getValueAt(fila, 4).toString());
                }
            }
        });

        btnGuardar.addActionListener(e -> guardarAuto());
        btnActualizar.addActionListener(e -> actualizarAuto());
        btnEliminar.addActionListener(e -> eliminarAuto());
        btnLimpiar.addActionListener(e -> limpiarFormulario());
        btnComprar.addActionListener(e -> comprarAuto());
    }

    private void configurarPermisos() {
        if (rolUsuario == 1) { // ADMIN
            btnGuardar.setVisible(true);
            btnActualizar.setVisible(true);
            btnEliminar.setVisible(true);
            btnComprar.setVisible(false);
        } else { // CLIENTE
            btnGuardar.setVisible(false);
            btnActualizar.setVisible(false);
            btnEliminar.setVisible(false);
            btnComprar.setVisible(true);

            txtPlaca.setEditable(false);
            txtMarca.setEditable(false);
            txtModelo.setEditable(false);
            txtPrecio.setEditable(false);
        }
    }

    // --- LÓGICA CORREGIDA ---

    private void cargarDatosTabla() {
        modeloTabla.setRowCount(0);
        List<Auto> lista = apiClient.obtenerAutos();
        for (Auto a : lista) {
            String estado = (a.getDisponible() != null && a.getDisponible()) ? "Disponible" : "VENDIDO";
            modeloTabla.addRow(new Object[]{
                    a.getId(), a.getPlaca(), a.getMarca(), a.getModelo(), a.getPrecio(), estado
            });
        }
    }

    private void guardarAuto() {
        try {
            // CORRECCIÓN: Usamos Setters en lugar de constructor con argumentos
            Auto auto = new Auto();
            auto.setPlaca(txtPlaca.getText());
            auto.setMarca(txtMarca.getText());
            auto.setModelo(txtModelo.getText());
            auto.setPrecio(Double.parseDouble(txtPrecio.getText()));
            auto.setDisponible(true);

            if(apiClient.guardarAuto(auto)) {
                JOptionPane.showMessageDialog(this, "¡Auto guardado correctamente!");
                limpiarFormulario();
                cargarDatosTabla();
            } else {
                JOptionPane.showMessageDialog(this, "Error al guardar. Revisa la validación (Placa, Precio).");
            }
        } catch (Exception ex) { JOptionPane.showMessageDialog(this, "Datos inválidos."); }
    }

    private void actualizarAuto() {
        if (txtId.getText().isEmpty()) return;
        try {
            Long id = Long.parseLong(txtId.getText());
            Auto auto = new Auto();
            auto.setPlaca(txtPlaca.getText());
            auto.setMarca(txtMarca.getText());
            auto.setModelo(txtModelo.getText());
            auto.setPrecio(Double.parseDouble(txtPrecio.getText()));
            auto.setDisponible(true);

            if (apiClient.actualizarAuto(id, auto)) {
                JOptionPane.showMessageDialog(this, "¡Auto actualizado!");
                limpiarFormulario();
                cargarDatosTabla();
            }
        } catch (Exception ex) { JOptionPane.showMessageDialog(this, "Error al actualizar."); }
    }

    private void comprarAuto() {
        if (txtId.getText().isEmpty()) {
            JOptionPane.showMessageDialog(this, "Selecciona un auto disponible en la tabla.");
            return;
        }
        int fila = tablaAutos.getSelectedRow();
        String estadoActual = modeloTabla.getValueAt(fila, 5).toString();
        if(estadoActual.equals("VENDIDO")) {
            JOptionPane.showMessageDialog(this, "¡Este auto ya está vendido!", "Aviso", JOptionPane.WARNING_MESSAGE);
            return;
        }

        int confirm = JOptionPane.showConfirmDialog(this, "¿Comprar " + txtMarca.getText() + " " + txtModelo.getText() + "?");
        if (confirm == JOptionPane.YES_OPTION) {
            try {
                Long id = Long.parseLong(txtId.getText());
                // Creamos objeto con disponible = false
                Auto autoCompra = new Auto();
                autoCompra.setPlaca(txtPlaca.getText());
                autoCompra.setMarca(txtMarca.getText());
                autoCompra.setModelo(txtModelo.getText());
                autoCompra.setPrecio(Double.parseDouble(txtPrecio.getText()));
                autoCompra.setDisponible(false); // VENDIDO

                if (apiClient.actualizarAuto(id, autoCompra)) {
                    JOptionPane.showMessageDialog(this, "¡Compra exitosa! Felicidades.");
                    limpiarFormulario();
                    cargarDatosTabla();
                }
            } catch (Exception ex) { JOptionPane.showMessageDialog(this, "Error en la compra."); }
        }
    }

    private void eliminarAuto() {
        if (txtId.getText().isEmpty()) return;
        int confirm = JOptionPane.showConfirmDialog(this, "¿Seguro que deseas eliminar este registro?");
        if (confirm == JOptionPane.YES_OPTION) {
            if (apiClient.eliminarAuto(Long.parseLong(txtId.getText()))) {
                JOptionPane.showMessageDialog(this, "Registro eliminado.");
                limpiarFormulario();
                cargarDatosTabla();
            }
        }
    }

    private void limpiarFormulario() {
        txtId.setText(""); txtPlaca.setText(""); txtMarca.setText(""); txtModelo.setText(""); txtPrecio.setText("");
        tablaAutos.clearSelection();
    }
}