package controller;

import lombok.RequiredArgsConstructor;
import model.Pagamento;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import service.PagamentoService;

@Controller
@RequestMapping("/pagamentos")
@RequiredArgsConstructor
public class PagamentoController {
    private final PagamentoService pagamentoService;

    @GetMapping
    public String listarPagamentos(Model model) {
        model.addAttribute("pagamentos", pagamentoService.listarTodos());
        return "pagamento/lista";
    }

    @GetMapping("/novo")
    public String novoPagamento(Model model) {
        model.addAttribute("pagamento", new Pagamento());
        return "pagamento/form";
    }

    @PostMapping
    public String salvarPagamento(@ModelAttribute Pagamento pagamento) {
        pagamentoService.salvar(pagamento);
        return "redirect:/pagamentos";
    }

    @GetMapping("/{id}/editar")
    public String editarPagamento(@PathVariable Long id, Model model) {
        model.addAttribute("pagamento", pagamentoService.buscarPorId(id));
        return "pagamento/form";
    }

    @GetMapping("/{id}/deletar")
    public String deletarPagamento(@PathVariable Long id) {
        pagamentoService.deletar(id);
        return "redirect:/pagamentos";
    }
}