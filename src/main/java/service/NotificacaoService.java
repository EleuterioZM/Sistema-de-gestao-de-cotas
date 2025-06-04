package service;

import lombok.RequiredArgsConstructor;
import model.Notificacao;
import model.Cota;
import model.Entidade;
import org.springframework.stereotype.Service;
import repository.NotificacaoRepository;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class NotificacaoService {
    private final NotificacaoRepository notificacaoRepository;

    public List<Notificacao> listarTodos() {
        return notificacaoRepository.findAll();
    }

    public List<Notificacao> listarPorEntidade(Integer entidadeId) {
        return notificacaoRepository.findByEntidadeId(entidadeId);
    }

    public List<Notificacao> listarNaoLidas() {
        return notificacaoRepository.findByLidaFalse();
    }

    public List<Notificacao> listarLidas() {
        return notificacaoRepository.findByLidaTrue();
    }

    public List<Notificacao> listarNaoLidasPorEntidade(Integer entidadeId) {
        return notificacaoRepository.findByEntidadeIdAndLidaFalse(entidadeId);
    }

    public Notificacao buscarPorId(Integer id) {
        return notificacaoRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Notificação não encontrada"));
    }

    public Notificacao salvar(Notificacao notificacao) {
        if (notificacao.getId() == null) {
            notificacao.setDataEnvio(LocalDateTime.now());
            notificacao.setLida(false);
        }
        return notificacaoRepository.save(notificacao);
    }

    public void marcarComoLida(Integer id) {
        Notificacao notificacao = buscarPorId(id);
        notificacao.setLida(true);
        notificacaoRepository.save(notificacao);
    }

    public void marcarTodasComoLidas(Integer entidadeId) {
        List<Notificacao> notificacoes = listarNaoLidasPorEntidade(entidadeId);
        notificacoes.forEach(notificacao -> {
            notificacao.setLida(true);
            notificacaoRepository.save(notificacao);
        });
    }

    public void deletar(Integer id) {
        notificacaoRepository.deleteById(id);
    }

    // Métodos para criar notificações automáticas
    public void notificarCotaVencida(Cota cota) {
        Notificacao notificacao = new Notificacao();
        notificacao.setEntidade(cota.getEntidade());
        notificacao.setTipo(Notificacao.Tipo.alerta);
        notificacao.setTitulo("Cota Vencida");
        notificacao.setMensagem("A cota '" + cota.getNome() + "' venceu em " + 
                              cota.getDataVencimento().toLocalDate() + ". Por favor, regularize o pagamento.");
        salvar(notificacao);
    }

    public void notificarCotaProximaVencimento(Cota cota) {
        Notificacao notificacao = new Notificacao();
        notificacao.setEntidade(cota.getEntidade());
        notificacao.setTipo(Notificacao.Tipo.informativo);
        notificacao.setTitulo("Cota Próxima do Vencimento");
        notificacao.setMensagem("A cota '" + cota.getNome() + "' vencerá em " + 
                              cota.getDataVencimento().toLocalDate() + ". Não se esqueça de realizar o pagamento.");
        salvar(notificacao);
    }

    public void notificarPagamentoConfirmado(Cota cota) {
        Notificacao notificacao = new Notificacao();
        notificacao.setEntidade(cota.getEntidade());
        notificacao.setTipo(Notificacao.Tipo.sistema);
        notificacao.setTitulo("Pagamento Confirmado");
        notificacao.setMensagem("O pagamento da cota '" + cota.getNome() + "' foi confirmado com sucesso.");
        salvar(notificacao);
    }
}