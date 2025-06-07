package com.Uni9.barberia_project.repository;

import com.Uni9.barberia_project.model.Servico;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ServicoRepository extends JpaRepository<Servico, Integer> {

    List<Servico> findByBarbearia_Id(Integer barbeariaId);
}
