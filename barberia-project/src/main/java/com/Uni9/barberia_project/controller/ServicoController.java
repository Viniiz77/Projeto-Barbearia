package com.Uni9.barberia_project.controller;

import com.Uni9.barberia_project.dto.*;
import com.Uni9.barberia_project.service.ServicoService;
import org.springframework.http.ResponseEntity;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.net.URI;

@RestController
@CrossOrigin(origins = "*")
@RequestMapping("/servicos")
public class ServicoController {

    private final ServicoService servicoService;

    public ServicoController(ServicoService servicoService) {
        this.servicoService = servicoService;
    }

    @PostMapping
    public ResponseEntity<ServicoResponseDto> criarServico(@RequestBody @Valid CreateServicoDto dto) {
        ServicoResponseDto novoServico = servicoService.criarServico(dto);

        URI location = ServletUriComponentsBuilder.fromCurrentRequest()
                .path("/{id}").buildAndExpand(novoServico.id()).toUri();

        return ResponseEntity.created(location).body(novoServico);
    }

    @GetMapping
    public ResponseEntity<List<ServicoResponseDto>> listarServicos() {
        List<ServicoResponseDto> servicos = servicoService.listarServicos();
        return ResponseEntity.ok(servicos);
    }

    @GetMapping("/por-barbearia/{barbeariaId}")
    public ResponseEntity<List<ServicoResponseDto>> listarServicosPorBarbearia(@PathVariable Integer barbeariaId) {
        List<ServicoResponseDto> servicos = servicoService.listarServicosPorBarbearia(barbeariaId);
        return ResponseEntity.ok(servicos);
    }

    @GetMapping("/{id}")
    public ResponseEntity<ServicoResponseDto> buscarServico(@PathVariable Integer id) {
            ServicoResponseDto servico =  servicoService.buscarServicoPorId(id);
            return ResponseEntity.ok(servico);
    }

    @PutMapping("/{id}")
    public ResponseEntity<ServicoResponseDto> atualizarServico(@PathVariable Integer id,
                                                      @RequestBody @Valid UpdateServicoDto dto) {
            ServicoResponseDto servicoAtualizado = servicoService.atualizarServico(id, dto);
            return ResponseEntity.ok(servicoAtualizado);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deletarServico(@PathVariable Integer id) {
        servicoService.deletarServico(id);
    }
}









