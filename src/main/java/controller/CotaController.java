package controller;

import lombok.RequiredArgsConstructor;
import model.Cota;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import service.CotaService;
import service.EntidadeService;

import java.util.List;

@Controller
@RequestMapping("/cotas")
@RequiredArgsConstructor
public class CotaController {
    private final CotaService cotaService;

    @Autowired
    private EntidadeService entidadeService;

    @GetMapping
    public String listar(Model model, @RequestParam(defaultValue = "1") int page) {
        int pageSize = 10;
        List<Cota> todasCotas = cotaService.listarTodos();
        int totalItems = todasCotas.size();
        int totalPages = (int) Math.ceil((double) totalItems / pageSize);
        
        int startIndex = (page - 1) * pageSize;
        int endIndex = Math.min(startIndex + pageSize, totalItems);
        
        List<Cota> cotasPagina = todasCotas.subList(startIndex, endIndex);
        
        model.addAttribute("cotas", cotasPagina);
        model.addAttribute("currentPage", page);
        model.addAttribute("totalPages", totalPages);
        
        return "cota/lista";
    }

    @GetMapping("/novo")
    public String novaCota(Model model) {
        try {
            model.addAttribute("cota", new Cota());
            model.addAttribute("entidades", entidadeService.listarTodos());
            return "cota/form";
        } catch (Exception e) {
            model.addAttribute("erro", "Erro ao carregar formulário: " + e.getMessage());
            return "redirect:/cotas";
        }
    }

    @GetMapping("/{id}/editar")
    public String editarCota(@PathVariable Integer id, Model model) {
        try {
            model.addAttribute("cota", cotaService.buscarPorId(id));
            model.addAttribute("entidades", entidadeService.listarTodos());
            return "cota/form";
        } catch (Exception e) {
            model.addAttribute("erro", "Erro ao carregar cota: " + e.getMessage());
            return "redirect:/cotas";
        }
    }

    @PostMapping
    public String salvarCota(@ModelAttribute Cota cota) {
        try {
            cotaService.salvar(cota);
            return "redirect:/cotas";
        } catch (Exception e) {
            return "redirect:/cotas?erro=" + e.getMessage();
        }
    }

    @DeleteMapping("/{id}")
    @ResponseBody
    public String deletarCota(@PathVariable Integer id) {
        try {
            cotaService.deletar(id);
            return "Cota excluída com sucesso";
        } catch (Exception e) {
            return "Erro ao excluir cota: " + e.getMessage();
        }
    }

    @GetMapping("/{id}/visualizar")
    public String visualizar(@PathVariable Integer id, Model model) {
        try {
            model.addAttribute("cota", cotaService.buscarPorId(id));
            return "cota/visualizar";
        } catch (Exception e) {
            model.addAttribute("erro", "Erro ao carregar cota: " + e.getMessage());
            return "redirect:/cotas";
        }
    }
}