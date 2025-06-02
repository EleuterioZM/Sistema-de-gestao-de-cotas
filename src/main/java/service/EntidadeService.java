package service;

import lombok.RequiredArgsConstructor;
import model.Entidade;
import org.springframework.stereotype.Service;
import repository.EntidadeRepository;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class EntidadeService {
    private final EntidadeRepository entidadeRepository;

    public List<Entidade> listarTodos() {
        return entidadeRepository.findAll();
    }

    public Entidade buscarPorId(Long id) {
        return entidadeRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Entidade não encontrada"));
    }

    public Entidade salvar(Entidade entidade) {
        if (entidade.getId() == null) {
            entidade.setDataCriacao(LocalDateTime.now());
        }
        return entidadeRepository.save(entidade);
    }

    public void deletar(Long id) {
        entidadeRepository.deleteById(id);
    }
}