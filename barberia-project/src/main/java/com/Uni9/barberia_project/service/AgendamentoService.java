package com.Uni9.barberia_project.service;

import com.Uni9.barberia_project.dto.*;
import com.Uni9.barberia_project.model.*;
import com.Uni9.barberia_project.repository.*;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class AgendamentoService {

    //Injeta tudo que vai precisar para conversar com o banco
    private final AgendamentoRepository agendamentoRepository;
    private final UsuarioRepository usuarioRepository;
    private final ServicoRepository servicoRepository;
    private final BarbeariaRepository barbeariaRepository;

    //Construtor para injetar essas dependências automaticamente
    public AgendamentoService(
            AgendamentoRepository agendamentoRepository,
            UsuarioRepository usuarioRepository,
            ServicoRepository servicoRepository,
            BarbeariaRepository barbeariaRepository
    ) {
        this.agendamentoRepository = agendamentoRepository;
        this.usuarioRepository = usuarioRepository;
        this.servicoRepository = servicoRepository;
        this.barbeariaRepository = barbeariaRepository;
    }

    @Transactional
    public AgendamentoResponseDto criarAgendamento(CreateAgendamentoDto dto) {
        //Verifica se o cliente, barbeiro e etc existe
        //Se alguma não existir, avisa com um erro (EntityNotFoundException)
        var cliente = usuarioRepository.findById(dto.clienteId())
                .orElseThrow(() -> new EntityNotFoundException("Cliente com o ID " + dto.clienteId() + " não encontrado"));
        var barbeiro = usuarioRepository.findById(dto.barbeiroId())
                .orElseThrow(() -> new EntityNotFoundException("Barbeiro com o ID " + dto.barbeiroId() + " não encontrado"));
        var servico = servicoRepository.findById(dto.servicoId())
                .orElseThrow(() -> new EntityNotFoundException("Serviço com o ID " + dto.servicoId() + " não encontrado"));
        var barbearia = barbeariaRepository.findById(dto.barbeariaId())
                .orElseThrow(() -> new EntityNotFoundException("Barbearia com o ID " + dto.barbeariaId() + " não encontrado"));

        //Verifica conflito de horário para barbeiro
        // Busca agendamentos existentes para este barbeiro no mesmo dataHora
        boolean temConflitoBarbeiro = agendamentoRepository.existsByBarbeiroAndDataHora(barbeiro, dto.dataHora());
        if(temConflitoBarbeiro) {
            throw new IllegalArgumentException("Barbeiro já possui agendamento para este horário");
        }

        //Verifica conflito de horário para o cliente
        boolean temConflitoCliente = agendamentoRepository.existsByClienteAndDataHora(cliente, dto.dataHora());
        if(temConflitoCliente) {
            throw new IllegalArgumentException("Cliente já possui outro agendamento para este horário");
        }

        //Criação do objeto agendamento
        var agendamento = new Agendamento(
                dto.dataHora(),
                cliente,
                barbeiro,
                servico,
                barbearia
        );

        return AgendamentoResponseDto.fromEntity(agendamentoRepository.save(agendamento));
    }

    //Pega todos os agendamentos do banco, transforma cada um em um dto de resposta
    //Para mostrar para o usuário de uma maneira mais "limpa" e junta tudo em uma lista
    public List<AgendamentoResponseDto> listarTodos() {
        return agendamentoRepository.findAll().stream()
                .map(AgendamentoResponseDto::fromEntity)
                .collect(Collectors.toList());
    }

    // Busca um agendamento pelo id
    // Se não achar lança um erro. Se achar, transforma em dto e devolve
    public AgendamentoResponseDto buscarPorId(Integer id) {
        var agendamento = agendamentoRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Agendamento Com ID " + id + " não encontrado"));
        return AgendamentoResponseDto.fromEntity(agendamento);
    }

    @Transactional
    public AgendamentoResponseDto atualizarPorId(Integer id, UpdateAgendamentoDto dto) {
        var agendamento = agendamentoRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Agendamento Com ID " + id + " não encontrado"));

        if(dto.dataHora() != null){
            agendamento.setDataHora(dto.dataHora());
        }

        if(dto.status() != null){
            agendamento.setStatus(dto.status());
        }

        return AgendamentoResponseDto.fromEntity(agendamentoRepository.save(agendamento));
    }

    @Transactional
    public void deletarPorId(Integer id) {
        if(!agendamentoRepository.existsById(id)){
            throw new EntityNotFoundException("Agendamento Com ID " + id + " não encontrado");
        }
        agendamentoRepository.deleteById(id);
    }
}
