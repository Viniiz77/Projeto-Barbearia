package com.Uni9.barberia_project.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;

@Entity
@Getter
@Setter
@Table(name = "agendamentos")

public class Agendamento {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name= "data_hora", nullable= false)
    private LocalDateTime dataHora;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "usuario_cliente_id", nullable = false)
    @JsonIgnore
    private Usuario cliente;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "usuario_barbeiro_id", nullable = false)
    @JsonIgnore
    private Usuario barbeiro;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "servico_id", nullable = false)
    @JsonIgnore
    private Servico servico;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "barbearia_id", nullable = false)
    @JsonIgnore
    private Barbearia barbearia;

    @Enumerated(EnumType.STRING)
    @Column(name= "status_agendamento", nullable= false)
    private StatusAgendamento status = StatusAgendamento.PENDENTE;

    @CreationTimestamp
    @Column(name = "criado_em", updatable = false)
    private LocalDateTime criadoEm;

    public enum StatusAgendamento{
        PENDENTE,
        CONFIRMADO,
        CANCELADO
    }

    public Agendamento() {
    }

    public Agendamento(LocalDateTime dataHora, Usuario cliente, Usuario barbeiro,
                       Servico servico, Barbearia barbearia) {
        this.dataHora = dataHora;
        this.cliente = cliente;
        this.barbeiro = barbeiro;
        this.servico = servico;
        this.barbearia = barbearia;
        this.status = StatusAgendamento.PENDENTE;
    }
}

