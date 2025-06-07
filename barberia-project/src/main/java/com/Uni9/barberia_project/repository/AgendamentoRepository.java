package com.Uni9.barberia_project.repository;

import com.Uni9.barberia_project.model.Agendamento;
import com.Uni9.barberia_project.model.Usuario;
import org.springframework.data.jpa.repository.JpaRepository;
import java.time.LocalDateTime;
import java.util.List;
import org.springframework.stereotype.Repository;

@Repository
public interface AgendamentoRepository extends JpaRepository<Agendamento, Integer> {
    boolean existsByBarbeiroAndDataHora(Usuario barbeiro, LocalDateTime dataHora);

    boolean existsByClienteAndDataHora(Usuario cliente, LocalDateTime dataHora);

    List<Agendamento> findByBarbeiro_IdOrderByDataHoraAsc(Integer barbeiroId);

    // Lista agendamentos para um cliente, ordenados pela data e hora
    List<Agendamento> findByCliente_IdOrderByDataHoraAsc(Integer clienteId);

    // Lista agendamentos para uma barbearia, ordenados pela data e hora
    List<Agendamento> findByBarbearia_IdOrderByDataHoraAsc(Integer barbeariaId);
}
