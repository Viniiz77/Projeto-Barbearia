package com.Uni9.barberia_project.Controller;

import com.Uni9.barberia_project.model.Agendamento;
import com.Uni9.barberia_project.repository.AgendamentoRepository;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/agendamento")
public class AgendamentoController {

    private final AgendamentoRepository agendamentoRepository;

    public AgendamentoController(AgendamentoRepository agendamentoRepository) {
        this.agendamentoRepository = agendamentoRepository;
    }

    @GetMapping
    public List<Agendamento> listarAgendamentos(){
        return agendamentoRepository.findAll();
    }

    @GetMapping("/{id}")
    public ResponseEntity<Agendamento> listarAgendamentoId(@PathVariable Long id) {
        return agendamentoRepository.findById(id)
        .map(agendamento -> ResponseEntity.ok(agendamento))
        .orElse(ResponseEntity.notFound().build());
    }


}

