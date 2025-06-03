package repository;

import model.Pagamento;
import model.Usuario;
import model.Cota;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface PagamentoRepository extends JpaRepository<Pagamento, Long> {
    List<Pagamento> findByDataPagamentoBetween(LocalDateTime dataInicio, LocalDateTime dataFim);
    List<Pagamento> findByUsuario(Usuario usuario);
    List<Pagamento> findByCota(Cota cota);
    
    @Query("SELECT p FROM Pagamento p LEFT JOIN FETCH p.usuario LEFT JOIN FETCH p.cota")
    List<Pagamento> findAllWithUsuarioAndCota();
}