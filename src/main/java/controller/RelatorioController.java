package controller;

import lombok.RequiredArgsConstructor;
import model.Relatorio;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import service.RelatorioService;

@Controller
@RequestMapping("/relatorios")
@RequiredArgsConstructor
public class RelatorioController {
    private final RelatorioService relatorioService;

    @GetMapping
    public String listarRelatorios(Model model) {
        model.addAttribute("relatorios", relatorioService.listarTodos());
        return "relatorio/lista";
    }

    @GetMapping("/novo")
    public String novoRelatorio(Model model) {
        model.addAttribute("relatorio", new Relatorio());
        return "relatorio/form";
    }

    @PostMapping
    public String salvarRelatorio(@ModelAttribute Relatorio relatorio) {
        relatorioService.salvar(relatorio);
        return "redirect:/relatorios";
    }

    @GetMapping("/{id}/editar")
    public String editarRelatorio(@PathVariable Long id, Model model) {
        model.addAttribute("relatorio", relatorioService.buscarPorId(id));
        return "relatorio/form";
    }

    @GetMapping("/{id}/deletar")
    public String deletarRelatorio(@PathVariable Long id) {
        relatorioService.deletar(id);
        return "redirect:/relatorios";
    }
}