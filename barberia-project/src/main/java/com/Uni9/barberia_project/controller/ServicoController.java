package com.Uni9.barberia_project.controller;

import com.Uni9.barberia_project.dto.*;
import com.Uni9.barberia_project.model.Servico;
import com.Uni9.barberia_project.service.ServicoService;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.http.ResponseEntity;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.net.URI;

@RestController
@RequestMapping("/servicos")
public class ServicoController {

    private final ServicoService servicoService;

    public ServicoController(ServicoService servicoService) {
        this.servicoService = servicoService;
    }

    @PostMapping
    public ResponseEntity<ServicoResponseDto> criarServico(@RequestBody @Valid CreateServicoDto dto) {
        ServicoResponseDto nossoServico = servicoService.criarServico(dto);

        URI location = ServletUriComponentsBuilder.fromCurrentRequest()
                .path("/{id}").buildAndExpand(nossoServico.id()).toUri();

        return ResponseEntity.created(location).body(nossoServico);
    }

    @GetMapping
    public ResponseEntity<List<ServicoResponseDto>> listarServicos() {
        List<ServicoResponseDto> servicos = servicoService.listarServicos();
        return ResponseEntity.ok(servicos);
    }

    @GetMapping("/{id}")
    public ResponseEntity<ServicoResponseDto> buscarServicoPorId(@PathVariable Integer id) {
        try {
            ServicoResponseDto servico =  servicoService.buscarServicoPorId(id);
            return ResponseEntity.ok(servico);
        } catch (EntityNotFoundException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<ServicoResponseDto> atualizarServicoPorId(@PathVariable Integer id,
                                                      @RequestBody @Valid UpdateServicoDto dto) {
        try {
            ServicoResponseDto servicoAtualizado = servicoService.atualizarServicoPorId(id, dto);
            return ResponseEntity.ok(servicoAtualizado);
        } catch (EntityNotFoundException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deletarPorId(@PathVariable Integer id) {
        servicoService.deletarPorId(id);
    }
}
