package com.Uni9.barberia_project.model;

import jakarta.persistence.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;
import lombok.Getter;
import lombok.Setter;

import java.util.List;
import java.time.LocalDateTime;

@Entity
@Getter
@Setter
@Table(name = "barbearias")

public class Barbearia {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "nome", nullable = false)
    private String nome;

    @Column(name = "endereco", nullable = false)
    private String endereco;

    @Column(name = "telefone", nullable = false)
    private String telefone;

    @Column(columnDefinition = "TEXT")
    private String descricao;

    @CreationTimestamp
    @Column(name = "criado_em", updatable = false)
    private LocalDateTime criadoEm;

    @UpdateTimestamp
    @Column(name = "atualizado_em")
    private LocalDateTime atualizadoEm;

    @OneToMany(mappedBy = "barbearia", cascade= CascadeType.ALL, orphanRemoval= true, fetch = FetchType.LAZY)
    private List<Servico> servicos;

    public Barbearia(){
    }

    public Barbearia(String nome, String endereco, String telefone, String descricao) {
        this.nome = nome;
        this.endereco = endereco;
        this.telefone = telefone;
        this.descricao = descricao;
    }

//    public void addServico(Servico servico) {
//        if(this.servicos == null) {
//            this.servicos = new java.util.ArrayList<>();
//        }
//        this.servicos.add(servico);
//        servico.setBarbearia(this);
//    }
//
//    public void removeServico (Servico servico) {
//        if(this.servicos != null) {
//            this.servicos.remove(servico);
//            servico.setBarbearia(null);
//        }
//    }
}
