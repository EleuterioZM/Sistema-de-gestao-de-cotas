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
@Table(name = "pagamentos")
public class Pagamento {
    public enum Estado {
        confirmado, pendente, falhado
    }

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @ManyToOne
    @JoinColumn(name = "cota_id")
    private Cota cota;

    @Column(name = "data_pagamento")
    private LocalDateTime dataPagamento;

    @Column(name = "valor_pago")
    private Double valorPago;

    @Column(name = "metodo_pagamento")
    private String metodoPagamento;

    @Column(name = "comprovativo_url")
    private String comprovativoUrl;

    @Enumerated(EnumType.STRING)
    private Estado estado;

    @Column(name = "referencia_bancaria")
    private String referenciaBancaria;
}