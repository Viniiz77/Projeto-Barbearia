package com.Uni9.barberia_project.model;

import jakarta.persistence.*;
import lombok.Data;

@Entity
@Data
@Table(name = "usuarios")

public class Usuario {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Integer id;

    @Column(name = "nome", nullable = false)
    private String nome;

    @Column(name = "email", nullable = false)
    private String email;

    @Column(name = "senha", nullable = false)
    private String senha;

    @Column(name = "telefone", nullable = false)
    private String telefone;

    @Column(name = "tipo", nullable = false)
    private String tipo;
}