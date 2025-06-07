// JS/LoginCliente.js

// Obter referências aos elementos HTML
const emailUsuarioInput = document.getElementById('emailUsuario');
const senhaUsuarioInput = document.getElementById('senhaUsuario');
const btnAcessar = document.getElementById('btnAcessar');

btnAcessar.addEventListener('click', async (event) => {
    event.preventDefault(); // Impede o comportamento padrão de submit do botão (recarregar a página)

    const email = emailUsuarioInput.value.trim(); // Adicionado .trim()
    const senha = senhaUsuarioInput.value.trim(); // Adicionado .trim()

    // Validação básica no front-end
    if (!email || !senha) {
        alert('Por favor, preencha o e-mail/usuário e a senha.');
        return; // Interrompe a execução
    }

    // Dados para enviar ao back-end
    const dadosLogin = {
        email: email,
        senha: senha,
        tipo: "CLIENTE" // Garante que estamos logando como CLIENTE
    };

    // URL do seu endpoint de login no back-end
    const urlLogin = 'http://localhost:8080/usuarios/login'; 

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
            // A mensagem de erro do backend já será formatada (ex: "Email ou senha inválidos.")
            alert('Falha no login: ' + (errorData.message || 'Erro desconhecido ao fazer login.'));
            console.error('Detalhes do erro de login:', errorData);
            return; // Interrompe a execução
        }

        // Se o login for bem-sucedido
        const data = await response.json(); // Pega a resposta do back-end (UsuarioResponseDto)
        console.log('Login bem-sucedido:', data);

        // --- NOVO: Salvar informações do usuário no localStorage ---
        localStorage.setItem('idUsuarioLogado', data.id); // Guarda o ID do usuário
        localStorage.setItem('tipoUsuarioLogado', data.tipo); // Guarda o tipo (CLIENTE)
        localStorage.setItem('nomeUsuarioLogado', data.nome); // Guarda o nome do usuário
        // Opcional: Salvar o objeto completo do usuário (útil para acessar outras props depois)
        localStorage.setItem('usuarioLogado', JSON.stringify(data)); 
        // ------------------------------------------------------------

        alert('Login realizado com sucesso!');
        // Redireciona para a home do cliente após o login
        window.location.href = '/PagHome.html'; // Mantendo PagHome.html conforme seu uso

    } catch (error) {
        console.error('Erro no processo de login:', error);
        alert('Falha no login: ' + error.message);
    }
});
