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
@Table(name = "cotas")
public class Cota {
    public enum Status {
        pendente, paga, vencida
    }

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @ManyToOne
    @JoinColumn(name = "entidade_id")
    private Entidade entidade;

    private String nome;
    
    @Column(columnDefinition = "TEXT")
    private String descricao;
    private Double valor;

    @Column(name = "data_vencimento")
    private LocalDateTime dataVencimento;

    @Column(name = "referencia_pagamento")
    private String referenciaPagamento;

    @Enumerated(EnumType.STRING)
    private Status status;

    @Column(name = "data_criacao")
    private LocalDateTime dataCriacao;

    @ToString.Exclude
    @OneToMany(mappedBy = "cota")
    private List<Pagamento> pagamentos;
}