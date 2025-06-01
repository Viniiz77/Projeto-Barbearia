package com.Uni9.barberia_project.service;

import com.Uni9.barberia_project.model.Usuario;
import com.Uni9.barberia_project.model.Barbearia;
import com.Uni9.barberia_project.dto.*;
import com.Uni9.barberia_project.repository.BarbeariaRepository;
import com.Uni9.barberia_project.repository.UsuarioRepository;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class UsuarioService {

    // Injeção do repositório para acesso ao banco de dados.
    private final UsuarioRepository usuarioRepository;
    private final BarbeariaRepository barbeariaRepository;

    public UsuarioService(UsuarioRepository usuarioRepository, BarbeariaRepository barbeariaRepository) {
        this.usuarioRepository = usuarioRepository;
        this.barbeariaRepository = barbeariaRepository;
    }

   @Transactional
   public UsuarioResponseDto criarUsuario(CreateUsuarioDto dto) {
        //Checa se o email já está cadastrado
        if(usuarioRepository.existsByEmail(dto.email())) {
            throw new IllegalArgumentException("Email já está em uso.");
        }

        //Checa se o telefone já está cadastrado
        if(usuarioRepository.existsByTelefone(dto.telefone())) {
            throw new IllegalArgumentException("Telefone já está em uso");
        }

        //Lógica para associar à Barbearia, se um id for fornecido
        Barbearia barbearia = null;
        if(dto.barbeariaId() != null) {
            barbearia = barbeariaRepository.findById(dto.barbeariaId())
                    .orElseThrow(() -> new EntityNotFoundException("Barbearia não encontrada."));
        }

        //Cria a entidade usuário usando construtor que foi definido na classe Usuario
        Usuario usuario = new Usuario(
                dto.nome(),
                dto.email(),
                dto.senha(), //Senha irá ser passado em texto puro, não vamos fazer codificação dele (embora, seja crucial num projeto real).
                dto.telefone(),
                dto.tipo(),
                barbearia
        );
        usuarioRepository.save(usuario);

        //Converte a entidade salva em um dto de resposta
        return new UsuarioResponseDto(
                usuario.getId(),
                usuario.getNome(),
                usuario.getEmail(),
                usuario.getTelefone(),
                usuario.getTipo(),
                barbearia != null ? barbearia.getNome() : null,
                usuario.getCriadoEm(),
                usuario.getAtualidoEm()
        );
   }

   public List<UsuarioResponseDto> listarTodosUsuarios() {
        //Busca todos usuários no banco de dados
        return usuarioRepository.findAll().stream() //Transforma a lista de usuários em um stream
                //Para cada usuário no stream, mapeia (converte) para um UsuarioResponseDto
                .map(usuario -> new UsuarioResponseDto(
                        usuario.getId(),
                        usuario.getNome(),
                        usuario.getEmail(),
                        usuario.getTelefone(),
                        usuario.getTipo(),
                        usuario.getBarbearia()!=null ? usuario.getBarbearia().getNome() : null, // Se o usuário tem uma barbearia associada (não é nula), pega o nome dela, senão, retorna null
                        usuario.getCriadoEm(),
                        usuario.getAtualidoEm()
                ))
                //Coleta todos dto mepeados de volta em uma nova lista
                .collect(Collectors.toList());
   }

   public UsuarioResponseDto buscarPorId(Integer id) {
        //Busca um usuário no banco de dados pelo id fornecido
        Usuario usuario = usuarioRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Usuário não encontrado."));

        //Determina o nome da barbearia
        String nomeBarbearia = (usuario.getBarbearia()!=null)?usuario.getBarbearia().getNome():null;

        // Converte a entidade Usuario (encontrada) para um UsuarioResponseDto.
        return new UsuarioResponseDto(
                usuario.getId(),
                usuario.getNome(),
                usuario.getEmail(),
                usuario.getTelefone(),
                usuario.getTipo(),
                nomeBarbearia,
                usuario.getCriadoEm(),
                usuario.getAtualidoEm()
        );
   }

   @Transactional
    public UsuarioResponseDto atualizarUsuario(Integer id, UpdateUsuarioDto dto){
        //Busca um usuário existente pelo id
        //Se não encontrar lança uma exceção
        Usuario usuario = usuarioRepository.findById(id)
        .orElseThrow(() -> new EntityNotFoundException("Usuário não encontrado."));

        //Atualiza os campos de usuários, apenas se eles não forem nulos.
            if (dto.nome() != null)
                usuario.setNome(dto.nome());

            if (dto.email() != null)
                usuario.setEmail(dto.email());

            if (dto.senha() != null)
                usuario.setSenha(dto.senha());

            if (dto.telefone() != null)
                usuario.setTelefone(dto.telefone());

            if (dto.tipo() != null)
                usuario.setTipo(dto.tipo());

            if (dto.barbeariaId() == null) { // Se o id da barbearia for explicitamente nulo no DTO
                usuario.setBarbearia(null); //Desvincula a barbearia
            } else if(dto.barbeariaId() != null){ // Se um novo id de barbearia foi fornecido
                Barbearia barbearia = barbeariaRepository.findById(dto.barbeariaId())
                        .orElseThrow(() -> new EntityNotFoundException("Barbearia não encontrada."));
                usuario.setBarbearia(barbearia);
            }

            usuarioRepository.save(usuario);

            return new UsuarioResponseDto(
                    usuario.getId(),
                    usuario.getNome(),
                    usuario.getEmail(),
                    usuario.getTelefone(),
                    usuario.getTipo(),
                    usuario.getBarbearia()!=null?usuario.getBarbearia().getNome():null,
                    usuario.getCriadoEm(),
                    usuario.getAtualidoEm()
            );
        }

    @Transactional
    // Deleta o usuário diretamente pelo ID.
    public void deletarUsuario(Integer id){
        if(!usuarioRepository.existsById(id)) {
            throw new EntityNotFoundException("Usuário não encontrado.");
        }
        usuarioRepository.deleteById(id);
    }
}
