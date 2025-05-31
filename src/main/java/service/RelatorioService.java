package service;

import lombok.RequiredArgsConstructor;
import model.Relatorio;
import org.springframework.stereotype.Service;
import repository.RelatorioRepository;

import java.util.List;

@Service
@RequiredArgsConstructor
public class RelatorioService {
    private final RelatorioRepository relatorioRepository;

    public List<Relatorio> listarTodos() {
        return relatorioRepository.findAll();
    }

    public Relatorio buscarPorId(Long id) {
        return relatorioRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Relatório não encontrado"));
    }

    public Relatorio salvar(Relatorio relatorio) {
        return relatorioRepository.save(relatorio);
    }

    public void deletar(Long id) {
        relatorioRepository.deleteById(id);
    }
}