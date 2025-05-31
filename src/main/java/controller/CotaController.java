package controller;

import lombok.RequiredArgsConstructor;
import model.Cota;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import service.CotaService;

@Controller
@RequestMapping("/cotas")
@RequiredArgsConstructor
public class CotaController {
    private final CotaService cotaService;

    @GetMapping
    public String listarCotas(Model model) {
        model.addAttribute("cotas", cotaService.listarTodos());
        return "cota/lista";
    }

    @GetMapping("/novo")
    public String novaCota(Model model) {
        model.addAttribute("cota", new Cota());
        return "cota/form";
    }

    @PostMapping
    public String salvarCota(@ModelAttribute Cota cota) {
        cotaService.salvar(cota);
        return "redirect:/cotas";
    }

    @GetMapping("/{id}/editar")
    public String editarCota(@PathVariable Long id, Model model) {
        model.addAttribute("cota", cotaService.buscarPorId(id));
        return "cota/form";
    }

    @GetMapping("/{id}/deletar")
    public String deletarCota(@PathVariable Long id) {
        cotaService.deletar(id);
        return "redirect:/cotas";
    }
}