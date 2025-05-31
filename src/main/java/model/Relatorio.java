package model;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "relatorios")
public class Relatorio {
    public enum Tipo {
        financeiro, pagamento, cotas
    }

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @ManyToOne
    @JoinColumn(name = "entidade_id")
    private Entidade entidade;

    private String titulo;

    @Enumerated(EnumType.STRING)
    private Tipo tipo;

    @Column(columnDefinition = "TEXT")
    private String conteudo;

    @ManyToOne
    @JoinColumn(name = "gerado_por")
    private Usuario geradoPor;

    @Column(name = "data_geracao")
    private LocalDateTime dataGeracao;
}