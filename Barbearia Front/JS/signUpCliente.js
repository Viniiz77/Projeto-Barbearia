// Obter referências aos elementos do formulário
const form = document.getElementById("form");
const usernameInput = document.getElementById("username");
const phoneInput = document.getElementById("phone");
const emailInput = document.getElementById("email");
const passwordInput = document.getElementById("password");

// Adicionar um 'ouvinte' de evento ao formulário quando ele for submetido
form.addEventListener("submit", async function (event) {
    event.preventDefault(); // Impede o envio padrão do formulário (que recarregaria a página)

    // Pega os valores dos campos e remove espaços em branco extras
    const nome = usernameInput.value.trim();
    const telefone = phoneInput.value.trim();
    const email = emailInput.value.trim();
    const senha = passwordInput.value.trim();

    // Validação básica no front-end
    if (!nome || !telefone || !email || senha.length < 4) {
        alert("Por favor, preencha todos os campos corretamente.");
        return; 
    }

    // Dados a serem enviados para o back-end, conforme o CreateUsuarioDto
    const dadosCadastro = {
        nome: nome,
        email: email,
        senha: senha,
        telefone: telefone,
        tipo: "CLIENTE", 
        barbeariaId: null 
    };

    // URL do endpoint de criação de usuário no back-end
    const urlCadastro = 'http://localhost:8080/usuarios';

    try {
        // Faz a requisição POST para o back-end
        const response = await fetch(urlCadastro, {
            method: 'POST', 
            headers: {
                'Content-Type': 'application/json' 
            },
            body: JSON.stringify(dadosCadastro) // Converte o objeto JavaScript para uma string JSON
        });

        // Verifica se a resposta do back-end foi bem-sucedida
        if (!response.ok) {
            // Se não foi bem-sucedida, tenta ler a mensagem de erro do back-end
            const errorData = await response.json(); // Espera um JSON de erro do GlobalExceptionHandler
            throw new Error(errorData.message || `Erro desconhecido: ${response.status}`);
        }

        // Se o cadastro foi bem-sucedido
        const usuarioCriado = await response.json(); // Pega os dados do usuário criado que o back-end retorna
        console.log('Usuário cliente cadastrado com sucesso:', usuarioCriado);
        alert('Cadastro de cliente realizado com sucesso! Faça login para continuar.');

        window.location.href = 'LoginCliente.html'; 

    } catch (error) {
        // Captura e exibe qualquer erro na requisição ou na resposta
        console.error('Erro no processo de cadastro:', error);
        alert('Falha no cadastro: ' + error.message);
    }

    // Limpa o formulário após a tentativa de envio 
    form.reset();
});

// Alternar a visibilidade da senha 
const inputPassword = document.getElementById("password");
const togglePassword = document.getElementById("togglePassword");

if (togglePassword) { // Verifica se o elemento existe antes de adicionar o listener
    togglePassword.addEventListener('click', function () {
        const type = inputPassword.type === "password" ? "text" : "password";
        inputPassword.type = type;
        togglePassword.classList.toggle("fa-eye");
        togglePassword.classList.toggle("fa-eye-slash");
    });
}