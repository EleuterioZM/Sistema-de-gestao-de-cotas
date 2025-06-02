package controller;

import model.Usuario;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import service.UsuarioService;

@Controller
public class RegistroController {

    private final UsuarioService usuarioService;

    public RegistroController(UsuarioService usuarioService) {
        this.usuarioService = usuarioService;
    }

    @GetMapping("/registro")
    public String mostrarFormularioRegistro(Model model) {
        model.addAttribute("usuario", new Usuario());
        return "registro";
    }

    @PostMapping("/registro")
    public String registrarUsuario(@ModelAttribute Usuario usuario, RedirectAttributes redirectAttributes) {
        try {
            // Verificar se o email já existe
            if (usuarioService.existeEmail(usuario.getEmail())) {
                redirectAttributes.addFlashAttribute("erro", "Este email já está cadastrado!");
                redirectAttributes.addFlashAttribute("usuario", usuario);
                return "redirect:/registro";
            }

            usuarioService.salvar(usuario);
            redirectAttributes.addFlashAttribute("sucesso", "Usuário cadastrado com sucesso!");
            return "redirect:/login";
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("erro", "Erro ao cadastrar usuário: " + e.getMessage());
            redirectAttributes.addFlashAttribute("usuario", usuario);
            return "redirect:/registro";
        }
    }
}