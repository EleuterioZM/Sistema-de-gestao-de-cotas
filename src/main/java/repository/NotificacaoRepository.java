package repository;

import model.Notificacao;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface NotificacaoRepository extends JpaRepository<Notificacao, Long> {
    List<Notificacao> findByEntidadeId(Long entidadeId);
    List<Notificacao> findByLidaFalse();
    List<Notificacao> findByEntidadeIdAndLidaFalse(Long entidadeId);
}