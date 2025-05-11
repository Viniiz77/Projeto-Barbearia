package com.Uni9.barberia_project.Controller;

import com.Uni9.barberia_project.model.Usuario;
import com.Uni9.barberia_project.Service.UsuarioService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * Controlador responsável por lidar com as requisições relacionadas aos usuários.
 * Aqui estão os endpoints para listar, buscar, criar, atualizar e deletar.
 */
@RestController
@RequestMapping("/usuario")
public class UsuarioController {

    // Injeta a dependência da classe UsuarioService no controlador.
    private final UsuarioService usuarioService;

    public UsuarioController(UsuarioService usuarioService) {

        this.usuarioService = usuarioService;
    }

    //Retorna a lista completa de usuários cadastrado.
    @GetMapping
    public List<Usuario> listarUsuario() {
        return usuarioService.listarTodos();
    }

    //Busca um usuário específico pelo id.
    //@return Usuário encontrado ou status 404 caso não exista.
    @GetMapping("/{id}")
    public ResponseEntity<Usuario> buscarPorId(@PathVariable Integer id) {
        return usuarioService.buscarPorId(id)
        .map(usuario -> ResponseEntity.ok(usuario))
        .orElse(ResponseEntity.notFound().build());
    }

    //Cadastro um novo usuário no sistema.
    @PostMapping
    public Usuario criarUsuario(@RequestBody Usuario usuario){
        return usuarioService.salvar(usuario);
    }

    /**
     * Atualiza os dados de um usuário já existente.
     * Apenas os campos enviados na requisição serão atualizado.
     */
    @PutMapping("/{id}")
    public ResponseEntity<Usuario> atualizarUsuario(@PathVariable Integer id, @RequestBody Usuario usuarioAtualizado){
        try{
            Usuario atualizado = usuarioService.atualizar(id, usuarioAtualizado);
            return ResponseEntity.ok(atualizado);
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }

    //Remove um usuário do sistema pelo id.
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletarUsuario(@PathVariable Integer id){
         usuarioService.deletar(id);
        //Retorna um status 204 No Content após exclusão.
         return ResponseEntity.noContent().<Void>build();
    }
}
