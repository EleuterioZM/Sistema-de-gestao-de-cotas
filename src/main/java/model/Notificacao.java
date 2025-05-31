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
@Table(name = "notificacoes")
public class Notificacao {
    public enum Tipo {
        sistema, alerta, informativo
    }

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @ManyToOne
    @JoinColumn(name = "entidade_id")
    private Entidade entidade;

    private String titulo;
    private String mensagem;

    @Enumerated(EnumType.STRING)
    private Tipo tipo;

    private Boolean lida;

    @Column(name = "data_envio")
    private LocalDateTime dataEnvio;
}