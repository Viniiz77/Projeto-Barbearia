package com.Uni9.barberia_project.service;

import com.Uni9.barberia_project.model.Barbearia;
import com.Uni9.barberia_project.model.Usuario;
import com.Uni9.barberia_project.dto.*;
import com.Uni9.barberia_project.repository.BarbeariaRepository;
import com.Uni9.barberia_project.repository.UsuarioRepository;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class BarbeariaService {

  private final BarbeariaRepository barbeariaRepository;
  private final UsuarioRepository usuarioRepository;

  public BarbeariaService(BarbeariaRepository barbeariaRepository, UsuarioRepository usuarioRepository){
      this.barbeariaRepository = barbeariaRepository;
      this.usuarioRepository = usuarioRepository;
  }

  @Transactional
  public BarbeariaResponseDto criarBarbearia(CreateBarbeariaDto dto) {
      if (barbeariaRepository.existsByNome(dto.nome())) {
          throw new IllegalArgumentException("Nome da barbearia já está em uso.");
      }
      if (barbeariaRepository.existsByTelefone(dto.telefone())) {
          throw new IllegalArgumentException("Telefone da barbearia já está em uso.");
      }

      // 1. Buscar e validar o usuário (barbeiro) pelo ID fornecido no DTO
      Usuario barbeiro = usuarioRepository.findById(dto.usuarioId())
              .orElseThrow(() -> new EntityNotFoundException("Usuário (barbeiro) não encontrado com o ID: " + dto.usuarioId()));

      // 2. Validar o tipo de usuário: deve ser um BARBEIRO
      if (barbeiro.getTipo() != Usuario.TipoUsuario.BARBEIRO) {
          throw new IllegalArgumentException("O usuário com ID " + dto.usuarioId() + " não é um barbeiro e não pode cadastrar uma barbearia.");
      }

      // 3. Validar se o barbeiro já possui uma barbearia
      if (barbeiro.getBarbearia() != null) { // Usa o getter 'getBarbearia()' do modelo Usuario
          throw new IllegalArgumentException("Este barbeiro já possui uma barbearia cadastrada.");
      }

      var barbearia = new Barbearia(
              dto.nome(),
              dto.endereco(),
              dto.telefone(),
              dto.descricao()
      );

      barbearia.setCriadoEm(LocalDateTime.now()); // Define a data de criação

      // 5. Salvar a nova barbearia no banco de dados
      Barbearia savedBarbearia = barbeariaRepository.save(barbearia);

      // 6. Associar a barbearia recém-criada ao barbeiro e salvar a atualização no usuário
      barbeiro.setBarbearia(savedBarbearia);
      usuarioRepository.save(barbeiro);

      // 7. Retornar o DTO de resposta da barbearia criada
      return BarbeariaResponseDto.fromEntity(savedBarbearia);
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

      if (dto.nome() != null && !dto.nome().trim().isEmpty() && !dto.nome().equals(barbearia.getNome())) {
          if (barbeariaRepository.existsByNome(dto.nome())) {
              throw new IllegalArgumentException("Nome da barbearia já está em uso por outra barbearia.");
          }
          barbearia.setNome(dto.nome());
      }
      if (dto.endereco() != null && !dto.endereco().trim().isEmpty()) {
          barbearia.setEndereco(dto.endereco());
      }
      if (dto.telefone() != null && !dto.telefone().trim().isEmpty() && !dto.telefone().equals(barbearia.getTelefone())) {
          if (barbeariaRepository.existsByTelefone(dto.telefone())) {
              throw new IllegalArgumentException("Telefone da barbearia já está em uso por outra barbearia.");
          }
          barbearia.setTelefone(dto.telefone());
      }

      if (dto.descricao() != null) { // Permite atualizar para nulo ou string vazia se o DTO permitir
          barbearia.setDescricao(dto.descricao());
      }
      barbearia.setAtualizadoEm(LocalDateTime.now()); // Define a data de atualização

      Barbearia barbeariaAtualizada = barbeariaRepository.save(barbearia);
      return BarbeariaResponseDto.fromEntity(barbeariaAtualizada);
  }

    public List<BarbeariaResponseDto> listarBarbearias() {
        List<Barbearia> barbearias = barbeariaRepository.findAll();
        return barbearias.stream()
                .map(BarbeariaResponseDto::fromEntity)
                .collect(Collectors.toList());
    }

  @Transactional
  public void deletarPorId(Integer id) {
      Barbearia barbearia = barbeariaRepository.findById(id)
              .orElseThrow(() -> new EntityNotFoundException("Barbearia não encontrada com ID: " + id));

      usuarioRepository.findByBarbearia(barbearia).ifPresent(barbeiro -> {
          barbeiro.setBarbearia(null); // Desassocia a barbearia do barbeiro
          usuarioRepository.save(barbeiro); // Salva a alteração no barbeiro
      });
      barbeariaRepository.deleteById(id);
  }

    @Transactional(readOnly = true)
    public BarbeariaResponseDto buscarBarbeariaPorUsuarioId(Integer usuarioId) {
        Usuario barbeiro = usuarioRepository.findById(usuarioId)
                .orElseThrow(() -> new EntityNotFoundException("Usuário (barbeiro) não encontrado com o ID: " + usuarioId));

        if (barbeiro.getTipo() != Usuario.TipoUsuario.BARBEIRO) {
            throw new IllegalArgumentException("O usuário com ID " + usuarioId + " não é um barbeiro.");
        }
        if (barbeiro.getBarbearia() == null) {
            throw new EntityNotFoundException("Este barbeiro ainda não possui uma barbearia cadastrada.");
        }
        return BarbeariaResponseDto.fromEntity(barbeiro.getBarbearia());
    }
}
