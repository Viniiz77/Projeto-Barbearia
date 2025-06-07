package com.Uni9.barberia_project.model;

import jakarta.persistence.*;
import lombok.Data;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDateTime;

@Entity
@Data
@Table(name = "usuarios")
public class Usuario {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "nome", nullable = false, length= 100)
    private String nome;

    @Column(name = "email", nullable = false, unique= true, length= 100)
    private String email;

    @Column(name = "senha", nullable = false, length= 100)
    private String senha;

    @Column(name = "telefone", nullable = false, unique= true, length= 15)
    private String telefone;

    @Enumerated(EnumType.STRING)
    @Column(name = "tipo", nullable = false, length= 10)
    private TipoUsuario tipo;

    //Relacionamento com barbearia: um usuário pode ou não estar associado a uma barbearia
    @OneToOne(cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @JoinColumn(name= "barbearia_id", referencedColumnName = "id", unique = true)
    private Barbearia barbearia;

    @CreationTimestamp
    @Column(name= "criadoEm", updatable= false)
    private LocalDateTime criadoEm;

    @UpdateTimestamp
    @Column(name= "atualido_em")
    private LocalDateTime atualidoEm;

    public enum TipoUsuario {
        CLIENTE,
        BARBEIRO
    }

    public Usuario() {
    }

    public Usuario(String nome, String email, String senha, String telefone, TipoUsuario tipo, Barbearia barbearia) {
        this.nome = nome;
        this.email = email;
        this.senha = senha;
        this.telefone = telefone;
        this.tipo = tipo;
        this.barbearia = barbearia;
    }
}