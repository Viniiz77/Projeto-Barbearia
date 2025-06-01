package com.Uni9.barberia_project.repository;

import com.Uni9.barberia_project.model.Agendamento;
import com.Uni9.barberia_project.model.Usuario;
import org.springframework.data.jpa.repository.JpaRepository;
import java.time.LocalDateTime;
import org.springframework.stereotype.Repository;

@Repository
public interface AgendamentoRepository extends JpaRepository<Agendamento, Integer> {
    boolean existsByBarbeiroAndDataHora(Usuario barbeiro, LocalDateTime dataHora);

    boolean existsByClienteAndDataHora(Usuario cliente, LocalDateTime dataHora);
}
