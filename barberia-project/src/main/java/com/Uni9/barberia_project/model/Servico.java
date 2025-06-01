package com.Uni9.barberia_project.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import com.fasterxml.jackson.annotation.JsonIgnore;

import java.math.BigDecimal;

@Entity
@Getter
@Setter
@Table(name = "servicos")
public class Servico {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "nome", nullable = false, length = 100)
    private String nome;

    @Column(name = "descricao", columnDefinition = "TEXT")
    private String descricao;

    @Column(name = "preco", nullable = false, precision = 10, scale = 2)
    private BigDecimal preco;

    @ManyToOne
    @JoinColumn(name = "barbearia_id", nullable= false)
    @JsonIgnore
    private Barbearia barbearia;

    //Construtor padrão obrigatório para JPA
    public Servico() {
    }

    //Construtor com todos os campos (Exceto id, que é gerado automaticamente)
    public Servico(String nome, String descricao, BigDecimal preco, Barbearia barbearia) {
        this.nome = nome;
        this.descricao = descricao;
        this.preco = preco;
        this.barbearia = barbearia;
    }
}
