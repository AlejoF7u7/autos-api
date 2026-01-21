package com.concesionario.autos_api.controller;

import com.concesionario.autos_api.model.Auto;
import com.concesionario.autos_api.model.Usuario;
import com.concesionario.autos_api.service.AutoService;
import com.concesionario.autos_api.service.UsuarioService;
import jakarta.validation.Valid;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.Optional;

@Controller
public class WebController {

    private final AutoService autoService;
    private final UsuarioService usuarioService;

    public WebController(AutoService autoService, UsuarioService usuarioService) {
        this.autoService = autoService;
        this.usuarioService = usuarioService;
    }

    @GetMapping("/")
    public String inicio() { return "redirect:/login"; }

    @GetMapping("/login")
    public String mostrarLogin() { return "login"; }

    @PostMapping("/login-web")
    public String procesarLogin(@RequestParam String email, @RequestParam String password, RedirectAttributes atributos) {
        Optional<Usuario> usuario = usuarioService.login(email, password);
        if (usuario.isPresent()) {
            return "redirect:/web/autos?rol=" + usuario.get().getRol().getNombre();
        } else {
            atributos.addFlashAttribute("error", "Credenciales incorrectas");
            return "redirect:/login";
        }
    }

    // --- AQUÍ ESTABA EL PROBLEMA, YA ESTÁ CORREGIDO ---
    @GetMapping("/web/autos")
    public String listarAutosWeb(@RequestParam(required = false, defaultValue = "CLIENTE") String rol,
                                 @RequestParam(required = false) String busqueda,
                                 Model model) {

        // CORRECCIÓN: Si hay texto en la búsqueda, usamos el servicio de buscar
        if (busqueda != null && !busqueda.isEmpty()) {
            model.addAttribute("listaAutos", autoService.buscarPorPalabra(busqueda));
        } else {
            // Si está vacío, mostramos todos
            model.addAttribute("listaAutos", autoService.listarTodos());
        }

        model.addAttribute("rolUsuario", rol);
        model.addAttribute("palabraBusqueda", busqueda); // Mantiene el texto en la cajita
        return "lista-autos";
    }

    @GetMapping("/web/autos/nuevo")
    public String mostrarFormulario(Model model) {
        model.addAttribute("auto", new Auto());
        return "formulario-auto";
    }

    @GetMapping("/web/autos/editar/{id}")
    public String editarAuto(@PathVariable Long id, Model model) {
        Optional<Auto> auto = autoService.obtenerPorId(id);
        if (auto.isPresent()) {
            model.addAttribute("auto", auto.get());
            return "formulario-auto";
        }
        return "redirect:/web/autos?rol=ADMINISTRADOR";
    }

    @PostMapping("/web/autos/guardar")
    public String guardarAutoWeb(@Valid @ModelAttribute Auto auto, BindingResult result, Model model) {
        if (result.hasErrors()) {
            return "formulario-auto";
        }
        if(auto.getDisponible() == null) auto.setDisponible(true);
        try {
            autoService.guardarAuto(auto);
        } catch (Exception e) {
            model.addAttribute("errorNegocio", e.getMessage());
            return "formulario-auto";
        }
        return "redirect:/web/autos?rol=ADMINISTRADOR";
    }

    @GetMapping("/web/autos/eliminar/{id}")
    public String eliminarAutoWeb(@PathVariable Long id) {
        autoService.eliminarAuto(id);
        return "redirect:/web/autos?rol=ADMINISTRADOR";
    }

    @GetMapping("/web/autos/comprar/{id}")
    public String comprarAutoWeb(@PathVariable Long id) {
        Optional<Auto> autoOpt = autoService.obtenerPorId(id);
        if (autoOpt.isPresent()) {
            Auto auto = autoOpt.get();
            auto.setDisponible(false);
            autoService.guardarAuto(auto);
        }
        return "redirect:/web/autos?rol=CLIENTE";
    }
}