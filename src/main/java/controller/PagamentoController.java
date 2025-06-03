package controller;

import model.Pagamento;
import model.Usuario;
import model.Cota;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import repository.PagamentoRepository;
import repository.UsuarioRepository;
import repository.CotaRepository;

import java.time.LocalDateTime;
import java.util.List;

@Controller
@RequestMapping("/pagamentos")
public class PagamentoController {

    @Autowired
    private PagamentoRepository pagamentoRepository;

    @Autowired
    private UsuarioRepository usuarioRepository;

    @Autowired
    private CotaRepository cotaRepository;

    @GetMapping
    public String listar(Model model, @RequestParam(defaultValue = "1") int page) {
        int pageSize = 10;
        List<Pagamento> todosPagamentos = pagamentoRepository.findAllWithUsuarioAndCota();
        int totalItems = todosPagamentos.size();
        int totalPages = (int) Math.ceil((double) totalItems / pageSize);
        
        int startIndex = (page - 1) * pageSize;
        int endIndex = Math.min(startIndex + pageSize, totalItems);
        
        List<Pagamento> pagamentosPagina = todosPagamentos.subList(startIndex, endIndex);
        
        model.addAttribute("pagamentos", pagamentosPagina);
        model.addAttribute("currentPage", page);
        model.addAttribute("totalPages", totalPages);
        
        return "pagamento/lista";
    }

    @GetMapping("/novo")
    public String novo(Model model) {
        model.addAttribute("pagamento", new Pagamento());
        model.addAttribute("usuarios", usuarioRepository.findAll());
        model.addAttribute("cotas", cotaRepository.findAll());
        return "pagamento/form";
    }

    @PostMapping("/salvar")
    public String salvar(Pagamento pagamento, RedirectAttributes redirectAttributes) {
        if (pagamento.getId() == null) {
            pagamento.setDataPagamento(LocalDateTime.now());
        }
        pagamentoRepository.save(pagamento);
        redirectAttributes.addFlashAttribute("mensagem", "Pagamento salvo com sucesso!");
        return "redirect:/pagamentos";
    }

    @GetMapping("/{id}/editar")
    public String editar(@PathVariable Long id, Model model) {
        Pagamento pagamento = pagamentoRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("ID inválido:" + id));
        model.addAttribute("pagamento", pagamento);
        model.addAttribute("usuarios", usuarioRepository.findAll());
        model.addAttribute("cotas", cotaRepository.findAll());
        return "pagamento/form";
    }

    @GetMapping("/{id}/visualizar")
    public String visualizar(@PathVariable Long id, Model model) {
        Pagamento pagamento = pagamentoRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("ID inválido:" + id));
        model.addAttribute("pagamento", pagamento);
        return "pagamento/visualizar";
    }

    @PostMapping("/{id}/excluir")
    public String excluir(@PathVariable Long id, RedirectAttributes redirectAttributes) {
        pagamentoRepository.deleteById(id);
        redirectAttributes.addFlashAttribute("mensagem", "Pagamento excluído com sucesso!");
        return "redirect:/pagamentos";
    }
}