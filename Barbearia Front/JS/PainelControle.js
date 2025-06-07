// JS/painelBarbeiro.js

document.addEventListener('DOMContentLoaded', async () => {
    // Referências aos elementos HTML (já existentes)
    const nomeBarbeariaExibicao = document.getElementById('nomeBarbeariaExibicao');
    const enderecoBarbeariaExibicao = document.getElementById('enderecoBarbeariaExibicao');
    const telefoneBarbeariaExibicao = document.getElementById('telefoneBarbeariaExibicao');
    const descricaoBarbeariaInput = document.getElementById('descricaoBarbearia'); // Textarea
    
    const btnEditarInfoBarbearia = document.getElementById('btnEditarInfoBarbearia');
    const btnSalvarInfoBarbearia = document.getElementById('btnSalvarInfoBarbearia');

    const containerServicos = document.getElementById('containerServicos');
    const btnAdicionarServico = document.getElementById('btnAdicionarServico');
    const formularioServico = document.getElementById('formularioServico');
    const btnSalvarServico = document.getElementById('btnSalvarServico');
    const btnCancelarServico = document.getElementById('btnCancelarServico');
    const servicoIdInput = document.getElementById('servicoId'); 
    const nomeServicoInput = document.getElementById('nomeServico');
    const precoServicoInput = document.getElementById('precoServico');
    const duracaoServicoInput = document.getElementById('duracaoServico'); 

    const containerAgendamentos = document.getElementById('containerAgendamentos'); // NOVO: Container para agendamentos

    const linkPainelBarbeiro = document.getElementById('linkPainelBarbeiro');
    const linkMeusAgendamentosBarbeiro = document.getElementById('linkMeusAgendamentosBarbeiro');
    const linkSair = document.getElementById('linkSair');

    let barbeariaAtual = null; 
    let idUsuarioLogado = null; // Declarar no escopo superior para ser acessível

    // --- Funções de Ajuda ---

    async function carregarInfoBarbearia() {
        idUsuarioLogado = localStorage.getItem('idUsuarioLogado'); // Atribui aqui

        if (!idUsuarioLogado) {
            alert('Você não está logado como barbeiro. Redirecionando para o login.');
            window.location.href = '/LoginBarbeiro.html';
            return;
        }

        try {
            const urlBarbearia = `http://localhost:8080/barbearias/por-usuario/${idUsuarioLogado}`;
            const respostaBarbearia = await fetch(urlBarbearia);

            if (!respostaBarbearia.ok) {
                const erroData = await respostaBarbearia.json();
                throw new Error(erroData.message || `Erro ao carregar barbearia: ${respostaBarbearia.status}`);
            }

            barbeariaAtual = await respostaBarbearia.json();
            
            localStorage.setItem('idBarbeariaLogada', barbeariaAtual.id);
            localStorage.setItem('barbeariaLogada', JSON.stringify(barbeariaAtual));

            exibirInfoBarbearia();
            await carregarServicos(barbeariaAtual.id);
            await carregarAgendamentos(idUsuarioLogado); // Chama para carregar agendamentos do barbeiro
            
        } catch (erro) {
            console.error('Erro ao carregar informações da barbearia:', erro);
            alert('Não foi possível carregar as informações da barbearia: ' + erro.message);
        }
    }

    function exibirInfoBarbearia() {
        if (barbeariaAtual) {
            nomeBarbeariaExibicao.textContent = barbeariaAtual.nome;
            enderecoBarbeariaExibicao.textContent = `Endereço: ${barbeariaAtual.endereco || 'Não informado'}`;
            telefoneBarbeariaExibicao.textContent = `Telefone: ${barbeariaAtual.telefone || 'Não informado'}`;
            descricaoBarbeariaInput.value = barbeariaAtual.descricao || '';

            document.getElementById('localizacaoBarbearia').textContent = barbeariaAtual.endereco || 'Não informado';
            document.getElementById('contatoPrincipal').textContent = barbeariaAtual.telefone || 'Não informado';
        }
    }

    function ativarModoEdicaoInfo() {
        descricaoBarbeariaInput.readOnly = false;
        btnSalvarInfoBarbearia.style.display = 'flex';
        
        btnEditarInfoBarbearia.style.display = 'none';
    }

    async function salvarInfoBarbearia() {
        if (!barbeariaAtual) return;

        const idBarbearia = barbeariaAtual.id;
        const novaDescricao = descricaoBarbeariaInput.value.trim();

        const dadosAtualizacao = {
            nome: barbeariaAtual.nome,
            endereco: barbeariaAtual.endereco,
            telefone: barbeariaAtual.telefone,
            descricao: novaDescricao
        };

        const url = `http://localhost:8080/barbearias/${idBarbearia}`;

        try {
            const resposta = await fetch(url, {
                method: 'PUT',
                headers: {
                    'Content-Type': 'application/json'
                },
                body: JSON.stringify(dadosAtualizacao)
            });

            if (!resposta.ok) {
                const erroData = await resposta.json();
                throw new Error(erroData.message || `Erro ao atualizar barbearia: ${resposta.status}`);
            }

            barbeariaAtual = await resposta.json();
            localStorage.setItem('barbeariaLogada', JSON.stringify(barbeariaAtual));
            
            exibirInfoBarbearia();
            alert('Informações da barbearia atualizadas com sucesso!');
            
            descricaoBarbeariaInput.readOnly = true;
            btnSalvarInfoBarbearia.style.display = 'none';
            btnEditarInfoBarbearia.style.display = 'block';

        } catch (erro) {
            console.error('Erro ao salvar informações da barbearia:', erro);
            alert('Não foi possível salvar as informações: ' + erro.message);
        }
    }

    // --- Funções para Serviços (sem alteração significativa) ---

    async function carregarServicos(idBarbearia) {
        if (!idBarbearia) {
            containerServicos.innerHTML = '<p>ID da barbearia não disponível para carregar serviços.</p>';
            return;
        }
        try {
            const urlServicos = `http://localhost:8080/servicos/por-barbearia/${idBarbearia}`;
            const resposta = await fetch(urlServicos);

            if (!resposta.ok) {
                const erroData = await resposta.json();
                throw new Error(erroData.message || `Erro ao carregar serviços: ${resposta.status}`);
            }

            const servicos = await resposta.json();
            renderizarServicos(servicos);
        } catch (erro) {
            console.error('Erro ao carregar serviços:', erro);
            containerServicos.innerHTML = '<p>Erro ao carregar serviços. Tente novamente mais tarde.</p>';
        }
    }

    function renderizarServicos(servicos) {
        containerServicos.innerHTML = '';

        if (servicos.length === 0) {
            containerServicos.innerHTML = '<p>Nenhum serviço cadastrado para esta barbearia.</p>';
            return;
        }

        servicos.forEach(servico => {
            const servicoCard = document.createElement('div');
            servicoCard.classList.add('servico-card');
            servicoCard.innerHTML = `
                <h4>${servico.nome}</h4>
                <p><strong>Descrição:</strong> ${servico.descricao || 'Sem descrição'}</p>
                <p><strong>Preço:</strong> R$ ${servico.preco.toFixed(2).replace('.', ',')}</p>
                <p><strong>Duração:</strong> ${servico.duracaoMinutos} min</p>
                <div class="acoes">
                    <button class="botao-acao pequeno editar-btn" data-id="${servico.id}">Editar</button>
                    <button class="botao-acao pequeno deletar-btn" data-id="${servico.id}">Excluir</button>
                </div>
            `;
            containerServicos.appendChild(servicoCard);

            servicoCard.querySelector('.editar-btn').addEventListener('click', () => exibirFormularioServico(servico));
            servicoCard.querySelector('.deletar-btn').addEventListener('click', () => deletarServico(servico.id));
        });
    }

    function exibirFormularioServico(servico = null) {
        formularioServico.style.display = 'flex';

        if (servico) {
            servicoIdInput.value = servico.id;
            nomeServicoInput.value = servico.nome;
            precoServicoInput.value = servico.preco;
            duracaoServicoInput.value = servico.duracaoMinutos;
            descricaoBarbeariaInput.value = servico.descricao || '';
            document.querySelector('#formularioServico h3').textContent = 'Editar Serviço';
        } else {
            servicoIdInput.value = '';
            nomeServicoInput.value = '';
            precoServicoInput.value = '';
            duracaoServicoInput.value = '';
            descricaoBarbeariaInput.value = '';
            document.querySelector('#formularioServico h3').textContent = 'Adicionar Novo Serviço';
        }
    }

    function esconderFormularioServico() {
        formularioServico.style.display = 'none';
        
        servicoIdInput.value = ''; 
        nomeServicoInput.value = ''; 
        precoServicoInput.value = '';
        duracaoServicoInput.value = '';
        descricaoBarbeariaInput.value = ''; 
    }

    async function salvarServico() {
        const idServico = servicoIdInput.value;
        const nome = nomeServicoInput.value.trim();
        const preco = parseFloat(precoServicoInput.value);
        const duracaoMinutos = parseInt(duracaoServicoInput.value);
        const descricao = descricaoBarbeariaInput.value.trim();

        if (!nome || isNaN(preco) || preco <= 0 || isNaN(duracaoMinutos) || duracaoMinutos <= 0) {
            alert('Por favor, preencha todos os campos obrigatórios e válidos (nome, preço > 0, duração > 0).');
            return;
        }

        const dadosServico = {
            nome: nome,
            descricao: descricao,
            preco: preco,
            duracaoMinutos: duracaoMinutos,
            barbeariaId: barbeariaAtual.id 
        };

        const url = idServico 
            ? `http://localhost:8080/servicos/${idServico}`
            : `http://localhost:8080/servicos`;

        const metodo = idServico ? 'PUT' : 'POST';

        try {
            const resposta = await fetch(url, {
                method: metodo,
                headers: {
                    'Content-Type': 'application/json'
                },
                body: JSON.stringify(dadosServico)
            });

            if (!resposta.ok) {
                const erroData = await resposta.json();
                throw new Error(erroData.message || `Erro ao ${idServico ? 'atualizar' : 'criar'} serviço: ${resposta.status}`);
            }

            alert(`Serviço ${idServico ? 'atualizado' : 'cadastrado'} com sucesso!`);
            esconderFormularioServico();
            await carregarServicos(barbeariaAtual.id);
        } catch (erro) {
            console.error(`Erro ao salvar serviço:`, erro);
            alert(`Não foi possível ${idServico ? 'atualizar' : 'cadastrar'} o serviço: ` + erro.message);
        }
    }

    async function deletarServico(idServico) {
        if (!confirm('Tem certeza que deseja excluir este serviço?')) {
            return;
        }

        try {
            const url = `http://localhost:8080/servicos/${idServico}`;
            const resposta = await fetch(url, {
                method: 'DELETE'
            });

            if (!resposta.ok) {
                const erroData = await resposta.json();
                throw new Error(erroData.message || `Erro ao excluir serviço: ${resposta.status}`);
            }

            alert('Serviço excluído com sucesso!');
            await carregarServicos(barbeariaAtual.id);
        } catch (erro) {
            console.error('Erro ao excluir serviço:', erro);
            alert('Não foi possível excluir o serviço: ' + erro.message);
        }
    }


    // --- Funções para Agendamentos (AGORA REAIS) ---
    async function carregarAgendamentos(barbeiroId) {
        if (!barbeiroId) {
            containerAgendamentos.innerHTML = '<p>ID do barbeiro não disponível para carregar agendamentos.</p>';
            return;
        }
        try {
            // Busca agendamentos para ESTE BARBEIRO ESPECÍFICO
            const urlAgendamentos = `http://localhost:8080/agendamentos/por-barbeiro/${barbeiroId}`;
            const resposta = await fetch(urlAgendamentos);

            if (!resposta.ok) {
                const erroData = await resposta.json();
                throw new Error(erroData.message || `Erro ao carregar agendamentos: ${resposta.status}`);
            }

            const agendamentos = await resposta.json();
            renderizarAgendamentos(agendamentos); // Chama a função para exibir
        } catch (erro) {
            console.error('Erro ao carregar agendamentos:', erro);
            containerAgendamentos.innerHTML = '<p>Erro ao carregar agendamentos. Tente novamente mais tarde.</p>';
        }
    }

    function renderizarAgendamentos(agendamentos) {
        containerAgendamentos.innerHTML = ''; // Limpa o conteúdo anterior

        if (agendamentos.length === 0) {
            containerAgendamentos.innerHTML = '<p>Nenhum agendamento futuro no momento.</p>';
            return;
        }

        agendamentos.forEach(agendamento => {
            const agendamentoCard = document.createElement('div');
            agendamentoCard.classList.add('agendamento-card');

            // Formatação da data e hora para exibição amigável
            const dataHora = new Date(agendamento.dataHora);
            const dataFormatada = dataHora.toLocaleDateString('pt-BR');
            const horaFormatada = dataHora.toLocaleTimeString('pt-BR', { hour: '2-digit', minute: '2-digit' });

            agendamentoCard.innerHTML = `
                <h4>Agendamento - Cliente: ${agendamento.clienteNome}</h4>
                <p><strong>Serviço:</strong> ${agendamento.servicoNome} (Duração: ${agendamento.servicoDuracaoMinutos} min)</p>
                <p><strong>Preço:</strong> R$ ${agendamento.servicoPreco.toFixed(2).replace('.', ',')}</p>
                <p><strong>Data:</strong> ${dataFormatada} | <strong>Hora:</strong> ${horaFormatada}</p>
                <p><strong>Status:</strong> <span class="status-${agendamento.status.toLowerCase()}">${agendamento.status}</span></p>
                <div class="acoes">
                    ${agendamento.status === 'PENDENTE' ? `
                        <button class="botao-acao pequeno confirmar-btn" data-id="${agendamento.id}">Confirmar</button>
                        <button class="botao-acao pequeno cancelar-btn" data-id="${agendamento.id}">Cancelar</button>
                    ` : agendamento.status === 'CONFIRMADO' ? `
                        <button class="botao-acao pequeno cancelar-btn" data-id="${agendamento.id}">Cancelar</button>
                    ` : ''}
                </div>
            `;
            containerAgendamentos.appendChild(agendamentoCard);

            // Adiciona event listeners aos botões de status (se existirem)
            if (agendamento.status === 'PENDENTE' || agendamento.status === 'CONFIRMADO') {
                const confirmarBtn = agendamentoCard.querySelector('.confirmar-btn');
                const cancelarBtn = agendamentoCard.querySelector('.cancelar-btn');

                if (confirmarBtn) {
                    confirmarBtn.addEventListener('click', () => atualizarStatusAgendamento(agendamento.id, 'CONFIRMADO'));
                }
                if (cancelarBtn) {
                    cancelarBtn.addEventListener('click', () => atualizarStatusAgendamento(agendamento.id, 'CANCELADO'));
                }
            }
        });
    }

    async function atualizarStatusAgendamento(idAgendamento, novoStatus) {
        if (!confirm(`Tem certeza que deseja ${novoStatus === 'CONFIRMADO' ? 'CONFIRMAR' : 'CANCELAR'} este agendamento?`)) {
            return;
        }

        try {
            const url = `http://localhost:8080/agendamentos/${idAgendamento}/status`;
            const resposta = await fetch(url, {
                method: 'PUT',
                headers: {
                    'Content-Type': 'application/json'
                },
                body: JSON.stringify({ status: novoStatus })
            });

            if (!resposta.ok) {
                const erroData = await resposta.json();
                throw new Error(erroData.message || `Erro ao atualizar status: ${resposta.status}`);
            }

            alert(`Agendamento ${novoStatus.toLowerCase()} com sucesso!`);
            await carregarAgendamentos(idUsuarioLogado); // Recarrega a lista de agendamentos
        } catch (erro) {
            console.error('Erro ao atualizar status do agendamento:', erro);
            alert('Não foi possível atualizar o status do agendamento: ' + erro.message);
        }
    }


    // --- Event Listeners ---

    await carregarInfoBarbearia(); // Carrega tudo ao iniciar a página

    // Botões de Ação para Informações da Barbearia
    btnEditarInfoBarbearia.addEventListener('click', ativarModoEdicaoInfo);
    btnSalvarInfoBarbearia.addEventListener('click', salvarInfoBarbearia);

    // Botões de Ação para Serviços
    btnAdicionarServico.addEventListener('click', () => exibirFormularioServico());
    btnSalvarServico.addEventListener('click', salvarServico);
    btnCancelarServico.addEventListener('click', esconderFormularioServico);

    // Event Listeners para botões de edição/exclusão de horários/pagamentos/contatos (apenas alerts por enquanto)
    document.getElementById('btnEditarHorarios').addEventListener('click', () => alert('Funcionalidade de editar horários em desenvolvimento.'));
    document.getElementById('btnEditarPagamentos').addEventListener('click', () => alert('Funcionalidade de editar formas de pagamento em desenvolvimento.'));
    document.getElementById('btnEditarContatos').addEventListener('click', () => alert('Funcionalidade de editar contatos em desenvolvimento.'));


    // Lidar com os cliques nos links do cabeçalho
    linkPainelBarbeiro.addEventListener('click', (event) => {
        event.preventDefault();
        alert('Você já está no Painel do Barbeiro.');
    });

    linkMeusAgendamentosBarbeiro.addEventListener('click', (event) => {
        event.preventDefault();
        alert('Redirecionando para Meus Agendamentos do Barbeiro. (TODO)');
        // window.location.href = '/meusAgendamentosBarbeiro.html'; // Descomente quando a página existir
    });

    linkSair.addEventListener('click', (event) => {
        event.preventDefault();
        localStorage.clear(); // Limpa todos os dados do localStorage
        alert('Sessão encerrada. Redirecionando para a página inicial.');
        window.location.href = '/LoginBarbeiro.html'; // Ou sua página inicial de login
    });
});


