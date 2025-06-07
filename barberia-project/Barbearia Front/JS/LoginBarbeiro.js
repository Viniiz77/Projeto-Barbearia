// Obter referências aos elementos HTML
const emailUsuarioInput = document.getElementById('emailUsuario');
const senhaUsuarioInput = document.getElementById('senhaUsuario');
const btnAcessar = document.getElementById('btnAcessar');

btnAcessar.addEventListener('click', async (event) => {
    event.preventDefault(); // Impede o comportamento padrão de submit do botão (recarregar a página)

    const email = emailUsuarioInput.value;
    const senha = senhaUsuarioInput.value;

    // Validação básica no front-end
    if (!email || !senha) {
        alert('Por favor, preencha o e-mail/usuário e a senha.');
        return; // Interrompe a execução
    }

    // Dados para enviar ao back-end
    const dadosLogin = {
        email: email,
        senha: senha,
        tipo: "BARBEIRO"
    };

    // URL do seu endpoint de login no back-end
    const urlLogin = 'http://localhost:8080/usuarios/login'; 

    let dadosUsuario = null;

    try {
        const response = await fetch(urlLogin, {
            method: 'POST',
            headers: {
                'Content-Type': 'application/json' // Informa que o corpo da requisição é JSON
            },
            body: JSON.stringify(dadosLogin) // Converte o objeto JS para uma string JSON
        });

        // Se a resposta não for bem-sucedida (status 2xx)
         if (!response.ok) {
            const errorData = await response.json(); 
            alert('Falha no login: ' + (errorData.message || 'Erro desconhecido.'));
            console.error('Detalhes do erro de login:', errorData);
            return; // Interrompe a execução aqui se o login falhou
        }

        dadosUsuario = await response.json();

         if (dadosUsuario.tipo !== "BARBEIRO") {
            alert('Acesso negado: Este login não corresponde a um barbeiro.');
            return;
        }

        localStorage.setItem('idUsuarioLogado', dadosUsuario.id); 
        localStorage.setItem('tipoUsuarioLogado', dadosUsuario.tipo); 
        localStorage.setItem('nomeUsuarioLogado', dadosUsuario.nome); 
        localStorage.setItem('usuarioLogado', JSON.stringify(dadosUsuario)); 

        console.log('Login de barbeiro bem-sucedido:', dadosUsuario);

        const idBarbeiro = dadosUsuario.id;
        const urlBuscarBarbearia = `http://localhost:8080/barbearias/por-usuario/${idBarbeiro}`;

        const respostaBarbearia = await fetch(urlBuscarBarbearia)
            .catch(erro => { 
                console.error('Erro de conexão ao verificar barbearia:', erro);
                return { status: 0 }; // Simula uma resposta de erro para o fluxo
            });

         if (respostaBarbearia.ok) {
            // Barbeiro já tem uma barbearia cadastrada
            const dadosBarbearia = await respostaBarbearia.json();
            localStorage.setItem('idBarbeariaLogada', dadosBarbearia.id); 
            localStorage.setItem('barbeariaLogada', JSON.stringify(dadosBarbearia)); 
            alert('Login realizado com sucesso! Redirecionando para o Painel da Barbearia.');
            window.location.href = 'PainelControle.html'; // Redireciona para o painel do barbeiro
        } else {
            // Se não for 200 OK (inclui 404 - Barbearia não encontrada, ou outros erros HTTP/rede)
            alert('Login realizado com sucesso! Redirecionando para o Cadastro da Barbearia.');
            window.location.href = 'CadastroBarbearia.html'; // Redireciona para o cadastro da barbearia
        }

    } catch (error) {
        console.error('Erro no processo de login:', error);
        alert('Falha no login: ' + error.message);
    }
});