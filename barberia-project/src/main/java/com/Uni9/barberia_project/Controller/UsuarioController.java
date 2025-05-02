package com.Uni9.barberia_project.Controller;

import com.Uni9.barberia_project.model.Usuario;
import com.Uni9.barberia_project.repository.UsuarioRepository;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * Controlador responsável por lidar com as requisições relacionadas aos usuários.
 * Aqui estão os endpoints para listar, buscar, criar, atualizar e deletar.
 */
@RestController
@RequestMapping("/api/usuario")
public class UsuarioController {

    //Injetamos o repositório via construtor para acessar os dados do usuários.
    private final UsuarioRepository usuarioRepository;

    public UsuarioController(UsuarioRepository usuarioRepository) {
        this.usuarioRepository = usuarioRepository;
    }

    //Retorna a lista completa de usuários cadastrado.
    @GetMapping
    public List<Usuario> listarUsuario() {
        return usuarioRepository.findAll();
    }

    //Busca um usuário específico pelo id.
    //@return Usuário encontrado ou status 404 caso não exista.
    @GetMapping("/{id}")
    public ResponseEntity<Usuario> buscarPorId(@PathVariable Long id) {
        return usuarioRepository.findById(id)
        .map(usuario -> ResponseEntity.ok(usuario))
        .orElse(ResponseEntity.notFound().build());
    }

    //Cadastro um novo usuário no sistema.
    @PostMapping
    public Usuario criarUsuario(@RequestBody Usuario usuario){
        return usuarioRepository.save(usuario);
    }

    /**
     * Atualiza os dados de um usuário já existente.
     * Apenas os campos enviados na requisição serão atualizado.
     */
    @PutMapping("/{id}")
    public ResponseEntity<Usuario> atualizarUsuario(@PathVariable Long id, @RequestBody Usuario usuarioAtualizado){
        return usuarioRepository.findById(id)
        .map(usuario -> {
            if (usuarioAtualizado.getNome() != null)
                usuario.setNome(usuarioAtualizado.getNome());

            if (usuarioAtualizado.getEmail() != null)
                usuario.setEmail(usuarioAtualizado.getEmail());

            if (usuarioAtualizado.getSenha() != null)
                usuario.setSenha(usuarioAtualizado.getSenha());

            if (usuarioAtualizado.getTelefone() != null)
                usuario.setTelefone(usuarioAtualizado.getTelefone());

            if (usuarioAtualizado.getTipo() != null)
                usuario.setTipo(usuarioAtualizado.getTipo());

            Usuario usuarioSalvo = usuarioRepository.save(usuario);
            return ResponseEntity.ok(usuarioSalvo);
        })
        .orElse(ResponseEntity.notFound().build());
    }

    //Remove um usuário do sistema pelo id.
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletarUsuario(@PathVariable Long id){
        return usuarioRepository.findById(id)
        .map(usuario -> {
            usuarioRepository.delete(usuario);
            //Retorna um status 204 No Content após exclusão.
            return ResponseEntity.noContent().<Void>build();
        })
        .orElse(ResponseEntity.notFound().build());
    }
}
