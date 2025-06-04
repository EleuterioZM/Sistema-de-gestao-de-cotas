package controller;

import lombok.RequiredArgsConstructor;
import model.Notificacao;
import org.springframework.security.web.csrf.CsrfToken;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import service.NotificacaoService;

@Controller
@RequestMapping("/notificacoes")
@RequiredArgsConstructor
public class NotificacaoController {
    private final NotificacaoService notificacaoService;

    private void addCsrfTokenToModel(Model model, CsrfToken token) {
        if (token != null) {
            model.addAttribute("_csrf", token);
        }
    }

    @GetMapping
    public String listarNotificacoes(Model model, CsrfToken token) {
        model.addAttribute("notificacoes", notificacaoService.listarTodos());
        model.addAttribute("filtro", "todas");
        addCsrfTokenToModel(model, token);
        return "notificacao/lista";
    }

    @GetMapping("/entidade/{entidadeId}")
    public String listarNotificacoesPorEntidade(@PathVariable Integer entidadeId, Model model, CsrfToken token) {
        model.addAttribute("notificacoes", notificacaoService.listarPorEntidade(entidadeId));
        model.addAttribute("entidadeId", entidadeId);
        model.addAttribute("filtro", "entidade");
        addCsrfTokenToModel(model, token);
        return "notificacao/lista";
    }

    @GetMapping("/nao-lidas")
    public String listarNotificacoesNaoLidas(Model model, CsrfToken token) {
        model.addAttribute("notificacoes", notificacaoService.listarNaoLidas());
        model.addAttribute("filtro", "nao-lidas");
        addCsrfTokenToModel(model, token);
        return "notificacao/lista";
    }

    @GetMapping("/lidas")
    public String listarNotificacoesLidas(Model model, CsrfToken token) {
        model.addAttribute("notificacoes", notificacaoService.listarLidas());
        model.addAttribute("filtro", "lidas");
        addCsrfTokenToModel(model, token);
        return "notificacao/lista";
    }

    @GetMapping("/entidade/{entidadeId}/nao-lidas")
    public String listarNotificacoesNaoLidasPorEntidade(@PathVariable Integer entidadeId, Model model, CsrfToken token) {
        model.addAttribute("notificacoes", notificacaoService.listarNaoLidasPorEntidade(entidadeId));
        model.addAttribute("entidadeId", entidadeId);
        model.addAttribute("filtro", "entidade");
        addCsrfTokenToModel(model, token);
        return "notificacao/lista";
    }

    @PostMapping("/{id}/marcar-lida")
    @ResponseBody
    public String marcarComoLida(@PathVariable Integer id) {
        notificacaoService.marcarComoLida(id);
        return "Notificação marcada como lida";
    }

    @PostMapping("/entidade/{entidadeId}/marcar-todas-lidas")
    @ResponseBody
    public String marcarTodasComoLidas(@PathVariable Integer entidadeId) {
        notificacaoService.marcarTodasComoLidas(entidadeId);
        return "Todas as notificações foram marcadas como lidas";
    }

    @GetMapping("/{id}/deletar")
    public String deletarNotificacao(@PathVariable Integer id) {
        notificacaoService.deletar(id);
        return "redirect:/notificacoes";
    }
}