package controller;

import lombok.RequiredArgsConstructor;
import model.Notificacao;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import service.NotificacaoService;

@Controller
@RequestMapping("/notificacoes")
@RequiredArgsConstructor
public class NotificacaoController {
    private final NotificacaoService notificacaoService;

    @GetMapping
    public String listarNotificacoes(Model model) {
        model.addAttribute("notificacoes", notificacaoService.listarTodos());
        return "notificacao/lista";
    }

    @GetMapping("/novo")
    public String novaNotificacao(Model model) {
        model.addAttribute("notificacao", new Notificacao());
        return "notificacao/form";
    }

    @PostMapping
    public String salvarNotificacao(@ModelAttribute Notificacao notificacao) {
        notificacaoService.salvar(notificacao);
        return "redirect:/notificacoes";
    }

    @GetMapping("/{id}/editar")
    public String editarNotificacao(@PathVariable Long id, Model model) {
        model.addAttribute("notificacao", notificacaoService.buscarPorId(id));
        return "notificacao/form";
    }

    @GetMapping("/{id}/deletar")
    public String deletarNotificacao(@PathVariable Long id) {
        notificacaoService.deletar(id);
        return "redirect:/notificacoes";
    }
}