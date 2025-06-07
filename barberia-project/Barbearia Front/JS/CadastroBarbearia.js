
document.addEventListener('DOMContentLoaded', () => {
    const formCadastroBarbearia = document.getElementById('formCadastroBarbearia');

    const idUsuarioLogado = localStorage.getItem('idUsuarioLogado');

    if (!idUsuarioLogado) {
        alert('Você precisa estar logado como um barbeiro para cadastrar uma barbearia.');
        window.location.href = 'LoginBarbeiro.html'; // Redireciona para o login do barbeiro
        return; 
    }

    formCadastroBarbearia.addEventListener('submit', async (evento) => {
        evento.preventDefault(); 

        const nomeInput = document.getElementById('nome');
        const enderecoInput = document.getElementById('enderecoCompleto'); 
        const telefoneInput = document.getElementById('telefone');
        const descricaoInput = document.getElementById('descricao');

        const nome = nomeInput.value.trim();
        const endereco = enderecoInput.value.trim();
        const telefone = telefoneInput.value.trim();
        const descricao = descricaoInput.value.trim();

        let formularioValido = true;

        if (!nome) {
            nomeInput.style.border = "2px solid red";
            formularioValido = false;
        } else {
            nomeInput.style.border = "2px solid #BFB9B9";
        }

        if (!endereco) {
            enderecoInput.style.border = "2px solid red";
            formularioValido = false;
        } else {
            enderecoInput.style.border = "2px solid #BFB9B9";
        }

        if (!telefone) {
            telefoneInput.style.border = "2px solid red";
            formularioValido = false;
        } else {
            telefoneInput.style.border = "2px solid #BFB9B9";
        }

        if (!formularioValido) {
            alert("Por favor, preencha todos os campos obrigatórios.");
            return; 
        }

        const dadosCadastro = {
            nome: nome,
            endereco: endereco, 
            telefone: telefone,
             descricao: descricao, 
            usuarioId: parseInt(idUsuarioLogado)
        };

        const url = 'http://localhost:8080/barbearias';

        try {
            const resposta = await fetch(url, {
                method: 'POST', 
                headers: {
                    'Content-Type': 'application/json' 
                },
                body: JSON.stringify(dadosCadastro)
            });

            
            if (resposta.ok) {
                const barbeariaCriada = await resposta.json();
                alert(`Barbearia "${barbeariaCriada.nome}" cadastrada com sucesso!`);

                localStorage.setItem('idBarbeariaLogada', barbeariaCriada.id);
                localStorage.setItem('barbeariaLogada', JSON.stringify(barbeariaCriada));
                
                formCadastroBarbearia.reset();
                window.location.href = 'PainelControle.html'; 
            } else {
                // Se a requisição falhou, ler a mensagem de erro do back-end 
                const erroData = await resposta.json();
                const mensagemErro = erroData.message || 'Erro desconhecido ao cadastrar a barbearia.';
                alert('Erro ao cadastrar barbearia: ' + mensagemErro);
                console.error('Detalhes do erro:', erroData);

                nomeInput.style.border = "2px solid #BFB9B9";
                enderecoInput.style.border = "2px solid #BFB9B9";
                telefoneInput.style.border = "2px solid #BFB9B9";
                 descricaoInput.style.border = "2px solid #BFB9B9";
            }
        } catch (erro) {
            alert('Não foi possível conectar ao servidor. Verifique se o back-end está rodando.');
            console.error('Erro na requisição:', erro);

            nomeInput.style.border = "2px solid #BFB9B9";
            enderecoInput.style.border = "2px solid #BFB9B9";
            telefoneInput.style.border = "2px solid #BFB9B9";
             descricaoInput.style.border = "2px solid #BFB9B9";
        }
    });
});