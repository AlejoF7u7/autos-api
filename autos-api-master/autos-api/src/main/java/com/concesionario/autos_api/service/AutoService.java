package com.concesionario.autos_api.service;

import com.concesionario.autos_api.model.Auto;
import com.concesionario.autos_api.model.Usuario;
import com.concesionario.autos_api.repository.AutoRepository;
import com.concesionario.autos_api.repository.UsuarioRepository;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.Optional;

@Service
public class AutoService {

    private final AutoRepository autoRepository;
    private final UsuarioRepository usuarioRepository; // NECESARIO PARA COBRAR

    public AutoService(AutoRepository autoRepository, UsuarioRepository usuarioRepository) {
        this.autoRepository = autoRepository;
        this.usuarioRepository = usuarioRepository;
    }

    public List<Auto> listarTodos() {
        return autoRepository.findAll();
    }

    public List<Auto> buscarPorPalabra(String palabra) {
        if (palabra != null && !palabra.isEmpty()) {
            return autoRepository.findByMarcaContainingIgnoreCaseOrModeloContainingIgnoreCase(palabra, palabra);
        }
        return autoRepository.findAll();
    }

    public Optional<Auto> obtenerPorId(Long id) {
        return autoRepository.findById(id);
    }

    public Auto guardarAuto(Auto auto) {
        if (auto.getMarca().equalsIgnoreCase("Lada")) throw new IllegalArgumentException("No vendemos Lada.");
        return autoRepository.save(auto);
    }

    public void eliminarAuto(Long id) {
        autoRepository.deleteById(id);
    }

    // logica de compra
    public void procesarCompra(Long idAuto, String emailCliente) {
        // 1. Buscar Auto
        Auto auto = autoRepository.findById(idAuto)
                .orElseThrow(() -> new IllegalArgumentException("Auto no encontrado"));

        if (!auto.getDisponible()) {
            throw new IllegalArgumentException("¡Lo sentimos! Este auto ya fue vendido.");
        }

        // 2. Buscar Cliente
        Usuario cliente = usuarioRepository.findByEmail(emailCliente)
                .orElseThrow(() -> new IllegalArgumentException("Cliente no encontrado"));

        // 3. VERIFICAR DINERO
        if (cliente.getSaldo() < auto.getPrecio()) {
            throw new IllegalArgumentException("Saldo insuficiente. Tienes $" + cliente.getSaldo() +
                    " pero el auto cuesta $" + auto.getPrecio());
        }

        // 4. Realizar la transacción
        cliente.setSaldo(cliente.getSaldo() - auto.getPrecio()); // Restar dinero
        auto.setDisponible(false); // Marcar como vendido

        // 5. Guardar cambios
        usuarioRepository.save(cliente);
        autoRepository.save(auto);
    }
}