package repository;

import model.Notificacao;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface NotificacaoRepository extends JpaRepository<Notificacao, Integer> {
    List<Notificacao> findByEntidadeId(Integer entidadeId);
    List<Notificacao> findByLidaFalse();
    List<Notificacao> findByLidaTrue();
    List<Notificacao> findByEntidadeIdAndLidaFalse(Integer entidadeId);
}