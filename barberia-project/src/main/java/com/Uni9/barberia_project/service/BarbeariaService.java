package com.Uni9.barberia_project.service;

import com.Uni9.barberia_project.model.Barbearia;
import com.Uni9.barberia_project.dto.*;
import com.Uni9.barberia_project.repository.BarbeariaRepository;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class BarbeariaService {

  private final BarbeariaRepository barbeariaRepository;

  public BarbeariaService(BarbeariaRepository barbeariaRepository){
      this.barbeariaRepository = barbeariaRepository;
  }

  @Transactional
  public BarbeariaResponseDto criarBarbearia(CreateBarbeariaDto dto) {
      var entity = new Barbearia(
              dto.nome(),
              dto.endereco(),
              dto.telefone()
      );

      var saved = barbeariaRepository.save(entity);
      return BarbeariaResponseDto.fromEntity(saved);
  }

  public List<BarbeariaResponseDto> listarBarbearias() {
      List<Barbearia> barbearias = barbeariaRepository.findAll();

      return barbearias.stream()
              .map(BarbeariaResponseDto::fromEntity)
              .collect(Collectors.toList());
  }

  public BarbeariaResponseDto buscarPorId(Integer id) {
      Barbearia barbearia = barbeariaRepository.findById(id)
              .orElseThrow(() -> new EntityNotFoundException("Barbearia não encontrada."));

      return BarbeariaResponseDto.fromEntity(barbearia);
  }

  @Transactional
  public BarbeariaResponseDto atualizarBarbearia(Integer id, UpdateBarbeariaDto dto) {
      Barbearia barbearia = barbeariaRepository.findById(id)
              .orElseThrow(() -> new EntityNotFoundException("Barbearia não encontrada com ID: " + id));

      if(dto.nome() != null && !dto.nome().trim().isEmpty()) {
          barbearia.setNome(dto.nome());
      }
      if(dto.endereco() != null && !dto.endereco().trim().isEmpty()) {
          barbearia.setEndereco(dto.endereco());
      }
      if(dto.telefone() != null && !dto.telefone().trim().isEmpty()) {
          barbearia.setTelefone(dto.telefone());
      }

      Barbearia barbeariaAtualizada = barbeariaRepository.save(barbearia);
      return BarbeariaResponseDto.fromEntity(barbeariaAtualizada);
  }

  @Transactional
  public void deletarPorId(Integer id) {
      if(!barbeariaRepository.existsById(id)) {
          throw new EntityNotFoundException("Barbearia não encontrada com ID: " + id);
      }
      barbeariaRepository.deleteById(id);
  }
}
