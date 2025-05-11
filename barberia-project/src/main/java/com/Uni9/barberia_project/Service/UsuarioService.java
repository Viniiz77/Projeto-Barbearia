package com.Uni9.barberia_project.Service;

import com.Uni9.barberia_project.model.Usuario;
import com.Uni9.barberia_project.repository.UsuarioRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class UsuarioService {

    // Injeção do repositório para acesso ao banco de dados.
    private final UsuarioRepository usuarioRepository;

    public UsuarioService(UsuarioRepository usuarioRepository){
        this.usuarioRepository = usuarioRepository;
    }

    // Retorna todos os usuários cadastrados no banco.
    public List<Usuario> listarTodos(){
        return usuarioRepository.findAll();
    }

    // Retorna um usuário pelo ID (pode ou não existir).
    public Optional<Usuario> buscarPorId(Integer id){
        return usuarioRepository.findById(id);
    }

    // Salva um novo usuário no banco de dados.
    public Usuario salvar(Usuario usuario) {
        return usuarioRepository.save(usuario);
    }

    // Atualiza apenas os campos preenchidos de um usuário existente.
    // Se o ID não for encontrado, lança exceção.
    public Usuario atualizar(Integer id, Usuario dadosAtualizados){
        return usuarioRepository.findById(id)
        .map(usuario -> {
            if (dadosAtualizados.getNome() != null)
                usuario.setNome(dadosAtualizados.getNome());

            if (dadosAtualizados.getEmail() != null)
                usuario.setEmail(dadosAtualizados.getEmail());

            if (dadosAtualizados.getSenha() != null)
                usuario.setSenha(dadosAtualizados.getSenha());

            if (dadosAtualizados.getTelefone() != null)
                usuario.setTelefone(dadosAtualizados.getTelefone());

            if (dadosAtualizados.getTipo() != null)
                usuario.setTipo(dadosAtualizados.getTipo());
            return usuarioRepository.save(usuario);
        })
        .orElseThrow(() -> new RuntimeException("Usuario Não encontrado com ID: " + id));
    }

    // Deleta o usuário diretamente pelo ID.
    public void deletar(Integer id){
        usuarioRepository.deleteById(id);
    }
}
