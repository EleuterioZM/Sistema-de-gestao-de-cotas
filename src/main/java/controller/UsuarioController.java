package controller;

import model.Usuario;
import model.Entidade;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import service.UsuarioService;
import service.EntidadeService;

@Controller
@RequestMapping("/usuarios")
public class UsuarioController {

    @Autowired
    private UsuarioService usuarioService;

    @Autowired
    private EntidadeService entidadeService;

    @GetMapping
    public String listarUsuarios(Model model) {
        model.addAttribute("usuarios", usuarioService.listarTodos());
        return "usuarios/lista";
    }

    @GetMapping("/novo")
    public String novoUsuario(Model model) {
        model.addAttribute("usuario", new Usuario());
        model.addAttribute("entidades", entidadeService.listarTodos());
        return "usuarios/form";
    }

    @PostMapping
    public String salvarUsuario(@ModelAttribute Usuario usuario) {
        usuarioService.salvar(usuario);
        return "redirect:/usuarios";
    }

    @GetMapping("/editar/{id}")
    public String editarUsuario(@PathVariable Integer id, Model model) {
        model.addAttribute("usuario", usuarioService.buscarPorId(id));
        model.addAttribute("entidades", entidadeService.listarTodos());
        return "usuarios/form";
    }

    @PostMapping("/excluir")
    public String excluirUsuario(@RequestParam Integer id) {
        usuarioService.deletar(id);
        return "redirect:/usuarios";
    }
}