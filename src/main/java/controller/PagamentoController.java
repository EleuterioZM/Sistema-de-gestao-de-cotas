package controller;

import lombok.RequiredArgsConstructor;
import model.Pagamento;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import service.PagamentoService;
import service.CotaService;

@Controller
@RequestMapping("/pagamentos")
@RequiredArgsConstructor
public class PagamentoController {
    private final PagamentoService pagamentoService;

    @Autowired
    private CotaService cotaService;

    @GetMapping
    public String listarPagamentos(Model model) {
        model.addAttribute("pagamentos", pagamentoService.listarTodos());
        return "pagamento/lista";
    }

    @GetMapping("/novo")
    public String novoPagamento(Model model) {
        model.addAttribute("pagamento", new Pagamento());
        model.addAttribute("cotas", cotaService.listarTodos());
        return "pagamento/form";
    }

    @GetMapping("/{id}/editar")
    public String editarPagamento(@PathVariable Long id, Model model) {
        model.addAttribute("pagamento", pagamentoService.buscarPorId(id));
        model.addAttribute("cotas", cotaService.listarTodos());
        return "pagamento/form";
    }

    @PostMapping
    public String salvarPagamento(@ModelAttribute Pagamento pagamento) {
        pagamentoService.salvar(pagamento);
        return "redirect:/pagamentos";
    }

    @GetMapping("/{id}/deletar")
    public String deletarPagamento(@PathVariable Long id) {
        pagamentoService.deletar(id);
        return "redirect:/pagamentos";
    }

    @GetMapping("/{id}/visualizar")
    public String visualizar(@PathVariable Long id, Model model) {
        model.addAttribute("pagamento", pagamentoService.buscarPorId(id));
        return "pagamento/visualizar";
    }
}