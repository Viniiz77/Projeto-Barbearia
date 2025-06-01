package com.Uni9.barberia_project.controller;

import com.Uni9.barberia_project.dto.*;
import com.Uni9.barberia_project.service.AgendamentoService;
import jakarta.persistence.EntityNotFoundException;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.util.List;

// Diz pro Spring que essa classe é um controlador REST e que ela vai responder na URL "/agendamento"
@RestController
@RequestMapping("/agendamentos")
public class AgendamentoController {

    //Guarda uma referência para o serviço de agendamentos
    private final AgendamentoService agendamentoService;

    public AgendamentoController(AgendamentoService agendamentoService) {
        this.agendamentoService = agendamentoService;
    }

    /**
     * @param dto Os dados do agendamento que vêm do corpo da requisição (JSON).
     * O @Valid faz as validações definidas no CreateAgendamentoDto.
     * @return Retorna o id do agendamento criado com status HTTP 200 OK.
     */
    @PostMapping
    public ResponseEntity<AgendamentoResponseDto> criarAgendamento(@RequestBody @Valid CreateAgendamentoDto dto) {
        AgendamentoResponseDto novoAgendamento = agendamentoService.criarAgendamento(dto);

        URI location = ServletUriComponentsBuilder.fromCurrentRequest()
                .path("/{id}").buildAndExpand(novoAgendamento.id()).toUri();
        
        return ResponseEntity.created(location).body(novoAgendamento);
    }

    @GetMapping
    public ResponseEntity<List<AgendamentoResponseDto>> listarTodos() {
        List<AgendamentoResponseDto> lista = agendamentoService.listarTodos();
        return ResponseEntity.ok(lista);
    }

    @GetMapping("/{id}")
    public ResponseEntity<AgendamentoResponseDto> buscarPorId(@PathVariable Integer id) {
        try {
            AgendamentoResponseDto dto = agendamentoService.buscarPorId(id);
            return ResponseEntity.ok(dto);
        } catch (EntityNotFoundException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<AgendamentoResponseDto> atualizarPorId(@PathVariable Integer id,
                                                                 @RequestBody @Valid UpdateAgendamentoDto dto) {
       try {
           AgendamentoResponseDto agendamentoAtualizado = agendamentoService.atualizarPorId(id, dto);
           return ResponseEntity.ok(agendamentoAtualizado);
       } catch (EntityNotFoundException e) {
           return ResponseEntity.notFound().build();
       }
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deletarPorId(@PathVariable Integer id) {
        agendamentoService.deletarPorId(id);
    }
}

