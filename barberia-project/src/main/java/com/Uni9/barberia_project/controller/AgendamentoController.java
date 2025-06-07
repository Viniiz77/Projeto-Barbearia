package com.Uni9.barberia_project.controller;

import com.Uni9.barberia_project.dto.*;
import com.Uni9.barberia_project.service.AgendamentoService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.util.List;

// Diz pro Spring que essa classe é um controlador REST e que ela vai responder na URL "/agendamento"
@RestController
@CrossOrigin(origins = "*")
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
    public ResponseEntity<List<AgendamentoResponseDto>> listarTodosAgendamentos() {
        List<AgendamentoResponseDto> lista = agendamentoService.listarTodosAgendamentos();
        return ResponseEntity.ok(lista);
    }

    @GetMapping("/{id}")
    public ResponseEntity<AgendamentoResponseDto> buscarPorId(@PathVariable Integer id) {
            AgendamentoResponseDto dto = agendamentoService.buscarPorId(id);
            return ResponseEntity.ok(dto);
    }

    @GetMapping("/por-barbeiro/{barbeiroId}")
    public ResponseEntity<List<AgendamentoResponseDto>> listarAgendamentosPorBarbeiro(@PathVariable Integer barbeiroId) {
        List<AgendamentoResponseDto> agendamentos = agendamentoService.listarAgendamentosPorBarbeiro(barbeiroId);
        return ResponseEntity.ok(agendamentos);
    }

    @GetMapping("/por-cliente/{clienteId}")
    public ResponseEntity<List<AgendamentoResponseDto>> listarAgendamentosPorCliente(@PathVariable Integer clienteId) {
        List<AgendamentoResponseDto> agendamentos = agendamentoService.listarAgendamentosPorCliente(clienteId);
        return ResponseEntity.ok(agendamentos);
    }

    @PutMapping("/{id}/status")
    public ResponseEntity<AgendamentoResponseDto> atualizarStatusAgendamento(@PathVariable Integer id, @Valid @RequestBody UpdateAgendamentoDto dto) {
        AgendamentoResponseDto agendamentoAtualizado = agendamentoService.atualizarStatusAgendamento(id, dto);
        return ResponseEntity.ok(agendamentoAtualizado);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deletarAgendamento(@PathVariable Integer id) {
        agendamentoService.deletarAgendamento(id);
    }
}

