package com.Uni9.barberia_project.service;

import com.Uni9.barberia_project.dto.*;
import com.Uni9.barberia_project.repository.BarbeariaRepository;
import com.Uni9.barberia_project.repository.ServicoRepository;
import com.Uni9.barberia_project.model.Servico;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class ServicoService {

    private final ServicoRepository servicoRepository;
    private final BarbeariaRepository barbeariaRepository;

    public ServicoService(ServicoRepository servicoRepository, BarbeariaRepository barbeariaRepository) {
        this.servicoRepository = servicoRepository;
        this.barbeariaRepository = barbeariaRepository;
    }

    @Transactional
    public ServicoResponseDto criarServico(CreateServicoDto dto) {
        var barbearia =
                barbeariaRepository.findById(dto.barbeariaId())
                .orElseThrow(() -> new EntityNotFoundException
                        ("Barbearia com ID " + dto.barbeariaId() + " não encontrada"));

        var servico = new Servico (dto.nome(), dto.descricao(), dto.preco(), barbearia);
        return ServicoResponseDto.fromEntity(servicoRepository.save(servico));
    }

    public List<ServicoResponseDto> listarServicos() {
        return servicoRepository.findAll().stream()
                .map(ServicoResponseDto::fromEntity)
                .collect(Collectors.toList());
    }

    public ServicoResponseDto buscarServicoPorId(Integer id) {
        var servico = servicoRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Serviço com ID " + id + " não encontrado"));

        return ServicoResponseDto.fromEntity(servico);
    }

    @Transactional
    public ServicoResponseDto atualizarServicoPorId(Integer id, UpdateServicoDto dto) {
        var servico = servicoRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Serviço com ID " + id + " não encontrado"));

        if(dto.nome() != null && dto.nome().trim().isEmpty()){
            servico.setNome(dto.nome());
        }
        if(dto.descricao() != null && dto.descricao().trim().isEmpty()){
            servico.setDescricao(dto.descricao());
        }
        if(dto.preco() != null){
            servico.setPreco(dto.preco());
        }
        return ServicoResponseDto.fromEntity(servicoRepository.save(servico));
    }

    @Transactional
    public void deletarPorId (Integer id) {
        if(!servicoRepository.existsById(id)) {
            throw new EntityNotFoundException("Serviço com ID " + id + " não encontrado.");
        }
        servicoRepository.deleteById(id);
    }
}
