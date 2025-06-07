// JS/detalhesBarbearia.js

document.addEventListener('DOMContentLoaded', async () => {
    // Referências aos elementos HTML
    const nomeBarbeariaElem = document.getElementById('nomeBarbearia');
    const enderecoBarbeariaElem = document.getElementById('enderecoBarbearia');
    const telefoneBarbeariaElem = document.getElementById('telefoneBarbearia');
    const descricaoBarbeariaElem = document.getElementById('descricaoBarbearia');
    const containerServicosDisponiveis = document.getElementById('containerServicosDisponiveis');
    const servicoSelect = document.getElementById('servicoSelect');
    const dataHoraAgendamentoInput = document.getElementById('dataHoraAgendamento');
    const formAgendamento = document.getElementById('formAgendamento');

    // Campos ocultos para IDs
    const barbeariaIdInput = document.getElementById('barbeariaIdInput');
    const barbeiroIdInput = document.getElementById('barbeiroIdInput');
    const clienteIdInput = document.getElementById('clienteIdInput');

    let barbeariaAtual = null; // Para armazenar os dados da barbearia
    let servicosDisponiveis = []; // Para armazenar os serviços carregados

    // --- Lógica de Carregamento Inicial ---
    
    // Obter o ID da barbearia da URL
    const urlParams = new URLSearchParams(window.location.search);
    const idBarbearia = urlParams.get('barbeariaId'); 
    
    // CORREÇÃO AQUI: Redirecionar para '/PagHome.html' conforme seu uso.
    if (!idBarbearia) {
        alert('ID da barbearia não encontrado na URL. Retornando para a Home.');
        window.location.href = '/PagHome.html'; // Usando PagHome.html conforme seu código
        return;
    }
    barbeariaIdInput.value = idBarbearia; // Preenche o campo oculto com o ID da barbearia

    // Função para buscar e exibir os detalhes da barbearia
    async function carregarDetalhesBarbearia() {
        try {
            const urlBarbearia = `http://localhost:8080/barbearias/${idBarbearia}`;
            const respostaBarbearia = await fetch(urlBarbearia);
            
            if (!respostaBarbearia.ok) {
                const erroData = await respostaBarbearia.json();
                throw new Error(erroData.message || `Erro ao carregar detalhes da barbearia: ${respostaBarbearia.status}`);
            }

            barbeariaAtual = await respostaBarbearia.json();
            nomeBarbeariaElem.textContent = barbeariaAtual.nome;
            enderecoBarbeariaElem.textContent = `Endereço: ${barbeariaAtual.endereco || 'Não informado'}`;
            telefoneBarbeariaElem.textContent = `Telefone: ${barbeariaAtual.telefone || 'Não informado'}`;
            descricaoBarbeariaElem.textContent = `Descrição: ${barbeariaAtual.descricao || 'Nenhuma descrição disponível.'}`;

        } catch (erro) {
            console.error('Erro ao carregar detalhes da barbearia:', erro);
            alert('Não foi possível carregar os detalhes da barbearia: ' + erro.message);
            window.location.href = '/PagHome.html'; // Usando PagHome.html conforme seu código
        }
    }

    // Função para buscar e exibir os serviços da barbearia no dropdown e na lista
    async function carregarServicosBarbearia() {
        try {
            const urlServicos = `http://localhost:8080/servicos/por-barbearia/${idBarbearia}`;
            const respostaServicos = await fetch(urlServicos);

            if (!respostaServicos.ok) {
                const erroData = await respostaServicos.json();
                throw new Error(erroData.message || `Erro ao carregar serviços da barbearia: ${respostaServicos.status}`);
            }

            servicosDisponiveis = await respostaServicos.json();
            
            servicoSelect.innerHTML = '<option value="">Selecione um serviço...</option>';
            servicosDisponiveis.forEach(servico => {
                const option = document.createElement('option');
                option.value = servico.id;
                option.textContent = `${servico.nome} - R$ ${servico.preco.toFixed(2).replace('.', ',')} (${servico.duracaoMinutos} min)`;
                servicoSelect.appendChild(option);
            });

            containerServicosDisponiveis.innerHTML = ''; 
            if (servicosDisponiveis.length === 0) {
                containerServicosDisponiveis.innerHTML = '<p>Nenhum serviço disponível para esta barbearia.</p>';
            } else {
                servicosDisponiveis.forEach(servico => {
                    const servicoCard = document.createElement('div');
                    servicoCard.classList.add('servico-cliente-card');
                    servicoCard.innerHTML = `
                        <h4>${servico.nome}</h4>
                        <p><strong>Descrição:</strong> ${servico.descricao || 'Sem descrição'}</p>
                        <p><strong>Preço:</strong> R$ ${servico.preco.toFixed(2).replace('.', ',')}</p>
                        <p><strong>Duração:</strong> ${servico.duracaoMinutos} min</p>
                    `;
                    containerServicosDisponiveis.appendChild(servicoCard);
                });
            }

        } catch (erro) {
            console.error('Erro ao carregar serviços:', erro);
            alert('Não foi possível carregar os serviços: ' + erro.message);
            servicoSelect.innerHTML = '<option value="">Erro ao carregar serviços</option>';
            containerServicosDisponiveis.innerHTML = '<p>Erro ao carregar serviços. Tente novamente mais tarde.</p>';
        }
    }

    // --- Lógica de Agendamento ---

    formAgendamento.addEventListener('submit', async (event) => {
        event.preventDefault();

        const idClienteLogado = localStorage.getItem('idUsuarioLogado');
        if (!idClienteLogado) {
            alert('Por favor, faça login como cliente para agendar um serviço.');
            window.location.href = '/LoginCliente.html';
            return;
        }
        clienteIdInput.value = idClienteLogado;

        const servicoId = servicoSelect.value;
        const dataHoraRaw = dataHoraAgendamentoInput.value; 

        if (!servicoId || !dataHoraRaw) {
            alert('Por favor, selecione um serviço e uma data/hora.');
            return;
        }

        const dataHoraFormatada = dataHoraRaw + ':00'; 

        const dadosAgendamento = {
            clienteId: parseInt(idClienteLogado),
            barbeariaId: parseInt(idBarbearia),
            servicoId: parseInt(servicoId),
            dataHora: dataHoraFormatada
        };

        const urlAgendamento = 'http://localhost:8080/agendamentos'; 

        try {
            const resposta = await fetch(urlAgendamento, {
                method: 'POST',
                headers: {
                    'Content-Type': 'application/json'
                },
                body: JSON.stringify(dadosAgendamento)
            });

            if (!resposta.ok) {
                const erroData = await resposta.json();
                throw new Error(erroData.message || `Erro ao agendar serviço: ${resposta.status}`);
            }

            const agendamentoCriado = await resposta.json();
            alert(`Agendamento realizado com sucesso para ${agendamentoCriado.dataHora}!`);
            formAgendamento.reset();

        } catch (erro) {
            console.error('Erro ao agendar serviço:', erro);
            alert('Não foi possível agendar o serviço: ' + erro.message);
        }
    });

    // --- Event Listeners para o Cabeçalho ---
    document.getElementById('linkHomeCliente').addEventListener('click', (e) => {
        e.preventDefault();
        window.location.href = '/PagHome.html'; // Usando PagHome.html conforme seu código
    });

    document.getElementById('linkMeusAgendamentosCliente').addEventListener('click', (e) => {
        e.preventDefault();
        alert('Funcionalidade "Meus Agendamentos" do Cliente em desenvolvimento.');
    });

    document.getElementById('linkSairCliente').addEventListener('click', (e) => {
        e.preventDefault();
        localStorage.clear(); 
        alert('Sessão encerrada. Redirecionando para o login do cliente.');
        window.location.href = '/LoginCliente.html'; 
    });

    // --- Inicialização da Página ---
    await carregarDetalhesBarbearia();
    await carregarServicosBarbearia();
});
