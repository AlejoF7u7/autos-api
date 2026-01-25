package com.concesionario.autos_api.controller;

import com.concesionario.autos_api.model.Auto;
import com.concesionario.autos_api.model.Usuario;
import com.concesionario.autos_api.repository.RolRepository;
import com.concesionario.autos_api.repository.UsuarioRepository;
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
@SessionAttributes("usuarioSesion")
public class WebController {

    private final AutoService autoService;
    private final UsuarioService usuarioService;
    private final UsuarioRepository usuarioRepository;

    public WebController(AutoService autoService, UsuarioService usuarioService,
                         RolRepository rolRepository, UsuarioRepository usuarioRepository) {
        this.autoService = autoService;
        this.usuarioService = usuarioService;
        this.usuarioRepository = usuarioRepository;
    }

    @GetMapping("/")
    public String inicio() { return "redirect:/login"; }

    @GetMapping("/login")
    public String mostrarLogin() { return "login"; }

    @PostMapping("/login-web")
    public String procesarLogin(@RequestParam String email, @RequestParam String password, Model model, RedirectAttributes atributos) {
        Optional<Usuario> usuario = usuarioService.login(email, password);
        if (usuario.isPresent()) {
            model.addAttribute("usuarioSesion", usuario.get());
            return "redirect:/web/autos";
        } else {
            atributos.addFlashAttribute("error", "Credenciales incorrectas");
            return "redirect:/login";
        }
    }

    @GetMapping("/registro")
    public String mostrarRegistro(Model model) {
        model.addAttribute("usuario", new Usuario());
        return "registro";
    }

    @PostMapping("/registro")
    public String procesarRegistro(@ModelAttribute Usuario usuario, RedirectAttributes atributos) {
        if (usuarioRepository.findByEmail(usuario.getEmail()).isPresent()) {
            atributos.addFlashAttribute("error", "El correo ya existe.");
            return "redirect:/registro";
        }
        usuario.setSaldo(60000.0);
        usuarioService.registrarUsuario(usuario);
        atributos.addFlashAttribute("exito", "Cuenta creada. Saldo inicial: $60,000.");
        return "redirect:/login";
    }

    @GetMapping("/web/autos")
    public String listarAutosWeb(@RequestParam(required = false) String busqueda,
                                 @SessionAttribute(name = "usuarioSesion", required = false) Usuario usuario,
                                 Model model) {

        if (usuario == null) return "redirect:/login";

        Optional<Usuario> usuarioDb = usuarioRepository.findById(usuario.getId());

        if (usuarioDb.isEmpty()) {
            return "redirect:/login";
        }

        Usuario usuarioActualizado = usuarioDb.get();
        model.addAttribute("usuarioSesion", usuarioActualizado);

        if (busqueda != null && !busqueda.isEmpty()) {
            model.addAttribute("listaAutos", autoService.buscarPorPalabra(busqueda));
        } else {
            model.addAttribute("listaAutos", autoService.listarTodos());
        }
        model.addAttribute("palabraBusqueda", busqueda);
        return "lista-autos";
    }

    @GetMapping("/web/autos/nuevo")
    public String crearAuto(Model model) {
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
        return "redirect:/web/autos";
    }

    @PostMapping("/web/autos/guardar")
    public String guardarAutoWeb(@Valid @ModelAttribute Auto auto, BindingResult result, Model model) {
        if (result.hasErrors()) return "formulario-auto";
        if(auto.getDisponible() == null) auto.setDisponible(true);
        try {
            autoService.guardarAuto(auto);
        } catch (Exception e) {
            model.addAttribute("errorNegocio", e.getMessage());
            return "formulario-auto";
        }
        return "redirect:/web/autos";
    }

    @DeleteMapping("/web/autos/eliminar/{id}")
    public String eliminarAutoWeb(@PathVariable Long id) {
        autoService.eliminarAuto(id);
        return "redirect:/web/autos";
    }

    @PatchMapping("/web/autos/comprar/{id}")
    public String comprarAutoWeb(@PathVariable Long id,
                                 @SessionAttribute("usuarioSesion") Usuario usuario,
                                 RedirectAttributes atributos) {
        try {
            autoService.procesarCompra(id, usuario.getEmail());
            atributos.addFlashAttribute("exito", "¡Compra exitosa!");
        } catch (Exception e) {
            atributos.addFlashAttribute("error", e.getMessage());
        }
        return "redirect:/web/autos";
    }
}