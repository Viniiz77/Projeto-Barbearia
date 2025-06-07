package com.Uni9.barberia_project.repository;

import com.Uni9.barberia_project.model.Barbearia;
import com.Uni9.barberia_project.model.Usuario;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UsuarioRepository extends JpaRepository<Usuario, Integer> {

    Optional<Usuario> findByEmail(String email);
    Optional<Usuario> findByTelefone(String telefone);

    boolean existsByEmail(String email);

    boolean existsByTelefone(String telefone);

    Optional<Usuario> findByBarbearia(Barbearia barbearia);
}
