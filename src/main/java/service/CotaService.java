package service;

import lombok.RequiredArgsConstructor;
import model.Cota;
import model.Entidade;
import model.Notificacao;
import org.springframework.stereotype.Service;
import repository.CotaRepository;
import repository.EntidadeRepository;
import repository.NotificacaoRepository;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class CotaService {
    private final CotaRepository cotaRepository;
    private final EntidadeRepository entidadeRepository;
    private final NotificacaoRepository notificacaoRepository;

    public List<Cota> listarTodos() {
        return cotaRepository.findAll();
    }

    public Cota buscarPorId(Integer id) {
        return cotaRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Cota não encontrada"));
    }

    public Cota salvar(Cota cota) {
        // Se a entidade não existir, cria uma nova
        if (cota.getEntidade() != null && cota.getEntidade().getId() == null) {
            Entidade entidade = cota.getEntidade();
            entidade.setDataCriacao(LocalDateTime.now());
            entidade = entidadeRepository.save(entidade);
            cota.setEntidade(entidade);
        }

        // Define a data de criação se for uma nova cota
        if (cota.getId() == null) {
            cota.setDataCriacao(LocalDateTime.now());
        }

        // Salva a cota
        Cota cotaSalva = cotaRepository.save(cota);

        // Cria uma notificação para a entidade
        Notificacao notificacao = new Notificacao();
        notificacao.setEntidade(cota.getEntidade());
        notificacao.setTitulo("Nova Cota Criada");
        notificacao.setMensagem("Uma nova cota foi criada: " + cota.getNome());
        notificacao.setTipo(Notificacao.Tipo.informativo);
        notificacao.setLida(false);
        notificacao.setDataEnvio(LocalDateTime.now());
        notificacaoRepository.save(notificacao);

        return cotaSalva;
    }

    public void deletar(Integer id) {
        cotaRepository.deleteById(id);
    }
}