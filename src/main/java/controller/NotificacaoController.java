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

    @GetMapping("/entidade/{entidadeId}")
    public String listarNotificacoesPorEntidade(@PathVariable Long entidadeId, Model model) {
        model.addAttribute("notificacoes", notificacaoService.listarPorEntidade(entidadeId));
        return "notificacao/lista";
    }

    @GetMapping("/nao-lidas")
    public String listarNotificacoesNaoLidas(Model model) {
        model.addAttribute("notificacoes", notificacaoService.listarNaoLidas());
        return "notificacao/lista";
    }

    @GetMapping("/entidade/{entidadeId}/nao-lidas")
    public String listarNotificacoesNaoLidasPorEntidade(@PathVariable Long entidadeId, Model model) {
        model.addAttribute("notificacoes", notificacaoService.listarNaoLidasPorEntidade(entidadeId));
        return "notificacao/lista";
    }

    @PostMapping("/{id}/marcar-lida")
    @ResponseBody
    public String marcarComoLida(@PathVariable Long id) {
        notificacaoService.marcarComoLida(id);
        return "Notificação marcada como lida";
    }

    @PostMapping("/entidade/{entidadeId}/marcar-todas-lidas")
    @ResponseBody
    public String marcarTodasComoLidas(@PathVariable Long entidadeId) {
        notificacaoService.marcarTodasComoLidas(entidadeId);
        return "Todas as notificações foram marcadas como lidas";
    }

    @GetMapping("/{id}/deletar")
    public String deletarNotificacao(@PathVariable Long id) {
        notificacaoService.deletar(id);
        return "redirect:/notificacoes";
    }
}