package com.Uni9.barberia_project.repository;

import com.Uni9.barberia_project.model.Barbearia;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface BarbeariaRepository extends JpaRepository<Barbearia, Integer> {
    boolean existsByNome(String nome);
    boolean existsByTelefone(String telefone);
}
