package controller;

import lombok.RequiredArgsConstructor;
import model.Entidade;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import service.EntidadeService;

@Controller
@RequestMapping("/entidades")
@RequiredArgsConstructor
public class EntidadeController {
    private final EntidadeService entidadeService;

    @GetMapping
    public String listarEntidades(Model model) {
        model.addAttribute("entidades", entidadeService.listarTodos());
        return "entidade/lista";
    }

    @GetMapping("/novo")
    public String novaEntidade(Model model) {
        model.addAttribute("entidade", new Entidade());
        return "entidade/form";
    }

    @PostMapping
    public String salvarEntidade(@ModelAttribute Entidade entidade) {
        entidadeService.salvar(entidade);
        return "redirect:/entidades";
    }

    @GetMapping("/{id}/editar")
    public String editarEntidade(@PathVariable Long id, Model model) {
        model.addAttribute("entidade", entidadeService.buscarPorId(id));
        return "entidade/form";
    }

    @GetMapping("/{id}/deletar")
    public String deletarEntidade(@PathVariable Long id) {
        entidadeService.deletar(id);
        return "redirect:/entidades";
    }

    @GetMapping("/{id}/visualizar")
    public String visualizar(@PathVariable Long id, Model model) {
        model.addAttribute("entidade", entidadeService.buscarPorId(id));
        return "entidade/visualizar";
    }
}