package com.Uni9.barberia_project.controller;

import com.Uni9.barberia_project.dto.*;
import com.Uni9.barberia_project.service.BarbeariaService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.List;

@RestController
@CrossOrigin(origins = "*")
@RequestMapping("/barbearias")

public class BarbeariaController {

    private final BarbeariaService barbeariaService;

    public BarbeariaController(BarbeariaService barbeariaService) {
        this.barbeariaService = barbeariaService;
    }

    @PostMapping
    public ResponseEntity<BarbeariaResponseDto> criarBarbearia(@RequestBody @Valid CreateBarbeariaDto dto) {
        BarbeariaResponseDto novaBarbearia = barbeariaService.criarBarbearia(dto);
        
        URI location = ServletUriComponentsBuilder.fromCurrentRequest()
                .path("/{id}").buildAndExpand(novaBarbearia.id()).toUri();

        return ResponseEntity.created(location).body(novaBarbearia);
    }

    @GetMapping
    public ResponseEntity<List<BarbeariaResponseDto>> listarBarbearias() {
        List<BarbeariaResponseDto> barbearias = barbeariaService.listarBarbearias();
        return ResponseEntity.ok(barbearias);
    }

    @GetMapping("/{id}")
    public ResponseEntity<BarbeariaResponseDto> buscarPorId(@PathVariable Integer id) {
            BarbeariaResponseDto barbearia = barbeariaService.buscarPorId(id);
            return ResponseEntity.ok(barbearia);
    }

    @GetMapping("/por-usuario/{usuarioId}")
    public ResponseEntity<BarbeariaResponseDto> buscarBarbeariaPorUsuarioId(@PathVariable Integer usuarioId) {
        BarbeariaResponseDto barbearia = barbeariaService.buscarBarbeariaPorUsuarioId(usuarioId);
        return ResponseEntity.ok(barbearia);
    }

    @PutMapping("/{id}")
    public ResponseEntity<BarbeariaResponseDto> atualizarBarbearia(@PathVariable Integer id,
                                                                   @RequestBody @Valid UpdateBarbeariaDto dto) {
            BarbeariaResponseDto barbeariaAtualizada = barbeariaService.atualizarBarbearia(id, dto);
            return ResponseEntity.ok(barbeariaAtualizada);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deletarPorId(@PathVariable Integer id) {
        barbeariaService.deletarPorId(id);
    }
}
