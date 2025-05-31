package service;

import lombok.RequiredArgsConstructor;
import model.Notificacao;
import org.springframework.stereotype.Service;
import repository.NotificacaoRepository;

import java.util.List;

@Service
@RequiredArgsConstructor
public class NotificacaoService {
    private final NotificacaoRepository notificacaoRepository;

    public List<Notificacao> listarTodos() {
        return notificacaoRepository.findAll();
    }

    public Notificacao buscarPorId(Long id) {
        return notificacaoRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Notificação não encontrada"));
    }

    public Notificacao salvar(Notificacao notificacao) {
        return notificacaoRepository.save(notificacao);
    }

    public void deletar(Long id) {
        notificacaoRepository.deleteById(id);
    }
}