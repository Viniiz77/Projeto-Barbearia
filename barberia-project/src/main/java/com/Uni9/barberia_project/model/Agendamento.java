package com.Uni9.barberia_project.model;

import jakarta.persistence.*;
import lombok.Data;
import java.time.LocalDateTime;

@Entity
@Data
@Table(name = "agendamentos")

public class Agendamento {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private LocalDateTime dataHora;

    @ManyToOne
    @JoinColumn(name = "usuario_id", nullable = false)
    private Usuario cliente;

    @ManyToOne
    @JoinColumn(name = "barbeiro_id", nullable = false)
    private Usuario barbeiro;

//    @ManyToOne
//    @JoinColumn(name = "servico_id", nullable = false)
//    private Servico servico;
//
//    @ManyToOne
//    @JoinColumn(name = "barbearia_id", nullable = false)
//    private Barbearia barbearia;

    @Enumerated(EnumType.STRING)
    private StatusAgendamento status = StatusAgendamento.PENDENTE;

    @Column(name = "criado_em", updatable = false)
    private LocalDateTime criadoEm;

    @PrePersist
    protected void onCreate(){
        criadoEm = LocalDateTime.now();
    }

    public enum StatusAgendamento{
        PENDENTE,
        CONFIRMADO,
        CANCELADO
    }
}

