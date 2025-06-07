package com.Uni9.barberia_project.service;

import com.Uni9.barberia_project.dto.*;
import com.Uni9.barberia_project.model.*;
import com.Uni9.barberia_project.repository.*;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;
import java.time.LocalDateTime;

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
        // 1. Buscar entidades relacionadas
        Usuario cliente = usuarioRepository.findById(dto.clienteId())
                .orElseThrow(() -> new EntityNotFoundException("Cliente com o ID " + dto.clienteId() + " não encontrado."));

        Servico servico = servicoRepository.findById(dto.servicoId())
                .orElseThrow(() -> new EntityNotFoundException("Serviço com o ID " + dto.servicoId() + " não encontrado."));

        Barbearia barbearia = barbeariaRepository.findById(dto.barbeariaId())
                .orElseThrow(() -> new EntityNotFoundException("Barbearia com o ID " + dto.barbeariaId() + " não encontrada."));

        Usuario barbeiro = usuarioRepository.findByBarbearia(barbearia)
                .orElseThrow(() -> new EntityNotFoundException("Barbeiro não encontrado para a barbearia com ID: " + dto.barbeariaId() + ". Certifique-se de que a barbearia tem um barbeiro associado."));

        // Validações adicionais de consistência:
        // O barbeiro realmente está associado a esta barbearia? (redundante mas seguro após findByBarbearia)
        if (barbeiro.getBarbearia() == null || !barbeiro.getBarbearia().getId().equals(barbearia.getId())) {
            throw new IllegalArgumentException("O barbeiro selecionado não está associado à barbearia informada.");
        }
        // O serviço realmente pertence a esta barbearia?
        if (!servico.getBarbearia().getId().equals(barbearia.getId())) {
            throw new IllegalArgumentException("O serviço selecionado não pertence à barbearia informada.");
        }

        // Verifica conflito de horário para o barbeiro (com o barbeiro encontrado)
        boolean temConflitoBarbeiro = agendamentoRepository.existsByBarbeiroAndDataHora(barbeiro, dto.dataHora());
        if(temConflitoBarbeiro) {
            throw new IllegalArgumentException("Barbeiro já possui agendamento para este horário.");
        }

        // Verifica conflito de horário para o cliente (com o cliente encontrado)
        boolean temConflitoCliente = agendamentoRepository.existsByClienteAndDataHora(cliente, dto.dataHora());
        if(temConflitoCliente) {
            throw new IllegalArgumentException("Cliente já possui outro agendamento para este horário.");
        }

        // Criação do objeto agendamento (agora com o barbeiro encontrado pelo serviço)
        Agendamento agendamento = new Agendamento(
                dto.dataHora(),
                cliente,
                barbeiro, // Barbeiro obtido internamente
                servico,
                barbearia
        );

        return AgendamentoResponseDto.fromEntity(agendamentoRepository.save(agendamento));
    }

    //Pega todos os agendamentos do banco, transforma cada um em um dto de resposta
    //Para mostrar para o usuário de uma maneira mais "limpa" e junta tudo em uma lista
    public List<AgendamentoResponseDto> listarTodosAgendamentos() {
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

    @Transactional(readOnly = true)
    public List<AgendamentoResponseDto> listarAgendamentosPorBarbeiro(Integer barbeiroId) {
        usuarioRepository.findById(barbeiroId)
                .orElseThrow(() -> new EntityNotFoundException("Barbeiro com o ID " + barbeiroId + " não encontrado."));

        List<Agendamento> agendamentos = agendamentoRepository.findByBarbeiro_IdOrderByDataHoraAsc(barbeiroId);
        return agendamentos.stream()
                .map(AgendamentoResponseDto::fromEntity)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public List<AgendamentoResponseDto> listarAgendamentosPorCliente(Integer clienteId) {
        usuarioRepository.findById(clienteId)
                .orElseThrow(() -> new EntityNotFoundException("Cliente com o ID " + clienteId + " não encontrado."));

        List<Agendamento> agendamentos = agendamentoRepository.findByCliente_IdOrderByDataHoraAsc(clienteId);
        return agendamentos.stream()
                .map(AgendamentoResponseDto::fromEntity)
                .collect(Collectors.toList());
    }

    @Transactional
    public AgendamentoResponseDto atualizarStatusAgendamento(Integer id, UpdateAgendamentoDto dto) {
        var agendamento = agendamentoRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Agendamento Com ID " + id + " não encontrado"));

        try {
            agendamento.setStatus(Agendamento.StatusAgendamento.valueOf(dto.status().toUpperCase()));
        } catch (IllegalArgumentException e) {
            throw new IllegalArgumentException("Status inválido fornecido: " + dto.status() + ". Valores aceitos: PENDENTE, CONFIRMADO, CANCELADO.");
        }

        return AgendamentoResponseDto.fromEntity(agendamentoRepository.save(agendamento));
    }

    @Transactional
    public void deletarAgendamento(Integer id) {
        if(!agendamentoRepository.existsById(id)){
            throw new EntityNotFoundException("Agendamento Com ID " + id + " não encontrado");
        }
        agendamentoRepository.deleteById(id);
    }
}
