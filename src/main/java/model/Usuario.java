package model;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import java.time.LocalDateTime;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "usuarios", uniqueConstraints = {@UniqueConstraint(columnNames = {"email"})})
public class Usuario {

    public enum Perfil {
        admin, operador, visualizador
    }

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @ManyToOne
    @JoinColumn(name = "entidade_id")
    private Entidade entidade;

    private String nome;
    @Column(unique = true)
    private String email;
    private String senha;

    @Enumerated(EnumType.STRING)
    private Perfil perfil;

    private Boolean ativo;

    @Column(name = "ultimo_login")
    private LocalDateTime ultimoLogin;

    @OneToMany(mappedBy = "geradoPor")
    private List<Relatorio> relatorios;
}

