const campoPesquisa = document.getElementById('searchInput');
const containerListaBarbearias = document.getElementById('barbershopListContainer');
const linkInicio = document.getElementById('inicioLink');
const linkMeusAgendamentos = document.getElementById('meusAgendamentosLink');

let todasBarbearias = [];

async function buscarBarbearias() {
    const url = 'http://localhost:8080/barbearias'

    try {
        const response = await fetch(url);
        if (!response.ok) {
            const errorData = await response.json();
            throw new Error(errorData.message || `Erro ao buscar barbearias: ${response.status}`);
        }
        todasBarbearias = await response.json(); // Armazena a lista completa
        exibirBarbearias(todasBarbearias); // Exibe todas as barbearias inicialmente
    } catch (error) {
        console.error('Erro:', error);
       containerListaBarbearias.innerHTML = `<p style="color: red;">Não foi possível carregar as barbearias: ${error.message}</p>`;
        alert('Erro ao carregar barbearias: ' + error.message);
    }
}

function exibirBarbearias(barbeariasParaExibir) {
    containerListaBarbearias.innerHTML = '';

    if (barbeariasParaExibir.length === 0) {
        containerListaBarbearias.innerHTML = '<p>Nenhuma barbearia encontrada.</p>';
        return;
    }

    barbeariasParaExibir.forEach(barbearia => {
        const cartaoBarbearia = document.createElement('div');
        cartaoBarbearia.className = 'barbershop-card';

        // Conteúdo do cartão da barbearia
        cartaoBarbearia.innerHTML = `
            <h3>${barbearia.nome}</h3>
            <p><strong>Endereço:</strong> ${barbearia.endereco || 'Não informado'}</p>
            <p><strong>Telefone:</strong> ${barbearia.telefone || 'Não informado'}</p>
            <button class="view-details-btn" data-id="${barbearia.id}">Ver Detalhes</button>
        `;

            containerListaBarbearias.appendChild(cartaoBarbearia);
    });

    // Adiciona event listeners para os botões "Ver Detalhes"
    document.querySelectorAll('.view-details-btn').forEach(button => {
        button.addEventListener('click', (event) => {
            const barbeariaId = event.target.dataset.id;
            alert(`Você clicou em Ver Detalhes da Barbearia ID: ${barbeariaId}.`);
            window.location.href = `DetalhesBarbearia.html?barbeariaId=${barbeariaId}`;

        });
    });
}

// Função para lidar com a pesquisa
campoPesquisa.addEventListener('input', () => {
    const termoPesquisa = campoPesquisa.value.toLowerCase();
    const barbeariasFiltradas = todasBarbearias.filter(barbearia =>
        barbearia.nome.toLowerCase().includes(termoPesquisa) ||
        barbearia.endereco.toLowerCase().includes(termoPesquisa) ||
        barbearia.email.toLowerCase().includes(termoPesquisa)
    );
    exibirBarbearias(barbeariasFiltradas);
});

// Lidar com os cliques nos links do cabeçalho
linkInicio.addEventListener('click', (event) => {
    event.preventDefault(); // Impede o comportamento padrão do link
    alert('Você está na página Início. Nada a fazer.');
     window.location.href = 'homeCliente.html';
});

linkMeusAgendamentos.addEventListener('click', (event) => {
    event.preventDefault(); // Impede o comportamento padrão do link
    alert('Redirecionando para Meus Agendamentos.');
    // window.location.href = 'meusAgendamentosCliente.html';
});

// Carregar as barbearias quando a página for carregada
document.addEventListener('DOMContentLoaded', buscarBarbearias);
