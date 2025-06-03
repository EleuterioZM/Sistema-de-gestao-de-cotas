package model;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import java.time.LocalDateTime;
import java.util.List;
import lombok.ToString;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "entidades")
public class Entidade {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    private String nome;
    
    @Column(columnDefinition = "TEXT")
    private String descricao;

    @Column(name = "logo_url")
    private String logoUrl;

    @Column(name = "data_criacao")
    private LocalDateTime dataCriacao;

    @ToString.Exclude
    @OneToMany(mappedBy = "entidade")
    private List<Usuario> usuarios;

    @ToString.Exclude
    @OneToMany(mappedBy = "entidade")
    private List<Cota> cotas;

    @ToString.Exclude
    @OneToMany(mappedBy = "entidade")
    private List<Notificacao> notificacoes;

    @ToString.Exclude
    @OneToMany(mappedBy = "entidade")
    private List<Relatorio> relatorios;
}