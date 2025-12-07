// script.js

const API_BASE_URL = 'http://localhost:8080';
let currentUser = null; // Armazena dados do usuário logado

// --- Elementos do DOM ---
const loginForm = document.getElementById('loginForm');
const postForm = document.getElementById('postForm');
const newsListDiv = document.getElementById('news-list');
const authStatus = document.getElementById('auth-status');
const postStatus = document.getElementById('post-status');
const loggedInView = document.getElementById('logged-in-view');
const loggedOutView = document.getElementById('logged-out-view');
const usernameDisplay = document.getElementById('username-display');
const roleDisplay = document.getElementById('role-display');
const postCreationSection = document.getElementById('post-creation-section');
const logoutButton = document.getElementById('logoutButton');

// ----------------------------------------------------------------
// 1. UTILIDADES DE AUTENTICAÇÃO
// ----------------------------------------------------------------

/**
 * Retorna o cabeçalho Basic Auth (Base64) ou null.
 */
function getAuthHeader() {
    return localStorage.getItem('authHeader');
}

/**
 * Define o estado da interface (logado/deslogado) e exibe as permissões.
 */
function updateUI(user = null) {
    currentUser = user;

    if (user) {
        // Estado Logado
        usernameDisplay.textContent = user.nome;
        roleDisplay.textContent = user.role;
        loggedOutView.style.display = 'none';
        loggedInView.style.display = 'block';

        // Exibir a seção de postagem apenas para ADMIN ou EDITOR
        if (user.role === 'ADMIN' || user.role === 'EDITOR') {
            postCreationSection.style.display = 'block';
        } else {
            postCreationSection.style.display = 'none';
        }

    } else {
        // Estado Deslogado
        loggedOutView.style.display = 'block';
        loggedInView.style.display = 'none';
        postCreationSection.style.display = 'none';
        localStorage.removeItem('authHeader');
        localStorage.removeItem('userRole');
        localStorage.removeItem('userNome');
    }
}

// ----------------------------------------------------------------
// 2. FUNÇÕES DE API (CRUD)
// ----------------------------------------------------------------

/**
 * Realiza o Login enviando as credenciais no Basic Auth header.
 */
async function handleLogin(email, senha) {
    authStatus.textContent = 'Autenticando...';

    // Codifica email:senha em Base64
    const encoded = btoa(`${email}:${senha}`);
    const authHeader = `Basic ${encoded}`;

    try {
        const response = await fetch(`${API_BASE_URL}/auth/login`, {
            method: 'POST',
            // Envia as credenciais no cabeçalho
            headers: { 'Authorization': authHeader }
        });

        if (response.ok) {
            const userData = await response.json();

            // Armazena o cabeçalho e os dados do usuário para requisições futuras
            localStorage.setItem('authHeader', authHeader);
            localStorage.setItem('userRole', userData.role);
            localStorage.setItem('userNome', userData.nome);

            authStatus.textContent = ''; // Limpa o status
            updateUI(userData);
            loadNews(); // Recarrega notícias (opcional)

        } else if (response.status === 401 || response.status === 403) {
            authStatus.textContent = 'Falha na autenticação. Email ou senha inválidos.';
            updateUI(null);
        } else {
            authStatus.textContent = `Erro no servidor: ${response.status}`;
            updateUI(null);
        }
    } catch (error) {
        authStatus.textContent = 'Erro de rede ou servidor indisponível.';
        updateUI(null);
    }
}

/**
 * Carrega e exibe a lista de notícias.
 */
async function loadNews() {
    newsListDiv.innerHTML = '<p>Carregando notícias...</p>';

    try {
        const response = await fetch(`${API_BASE_URL}/noticias`);

        if (!response.ok) {
            throw new Error(`Falha ao carregar notícias: ${response.statusText}`);
        }

        const noticias = await response.json();

        if (noticias.length === 0) {
            newsListDiv.innerHTML = '<p>Nenhuma notícia encontrada. Publique a primeira!</p>';
            return;
        }

        let htmlContent = '';
        noticias.forEach(noticia => {
            const data = new Date(noticia.dataPublicacao).toLocaleString();
            htmlContent += `
                <div class="news-item">
                    <h3>${noticia.titulo}</h3>
                    <p>${noticia.conteudo.substring(0, 200)}...</p>
                    <small>Por: ${noticia.autor.nome} em ${data}</small>
                </div>
                <hr>
            `;
        });

        newsListDiv.innerHTML = htmlContent;

    } catch (error) {
        console.error("Erro ao carregar notícias:", error);
        newsListDiv.innerHTML = `<p class="error">Erro ao carregar notícias: ${error.message}</p>`;
    }
}

/**
 * Envia uma nova notícia para o servidor.
 */
async function handlePost(titulo, conteudo) {
    postStatus.textContent = 'Publicando...';
    const authHeader = getAuthHeader();

    if (!authHeader) {
        postStatus.textContent = 'Erro: Você precisa estar logado para postar.';
        return;
    }

    const postData = { titulo, conteudo };

    try {
        const response = await fetch(`${API_BASE_URL}/noticias`, {
            method: 'POST',
            headers: {
                'Authorization': authHeader, // Requerido para @RolesAllowed
                'Content-Type': 'application/json'
            },
            body: JSON.stringify(postData)
        });

        if (response.status === 201) {
            postStatus.textContent = 'Notícia publicada com sucesso!';
            postForm.reset();
            loadNews(); // Recarrega a lista para mostrar o novo post
        } else if (response.status === 403) {
            postStatus.textContent = 'Erro: Você não tem permissão (ADMIN/EDITOR) para publicar notícias.';
        } else {
            postStatus.textContent = `Falha ao publicar. Status: ${response.status}`;
        }
    } catch (error) {
        postStatus.textContent = 'Erro de rede ao publicar.';
        console.error(error);
    }
}


// ----------------------------------------------------------------
// 3. EVENT LISTENERS E INICIALIZAÇÃO
// ----------------------------------------------------------------

// A. Login Listener
loginForm.addEventListener('submit', (e) => {
    e.preventDefault();
    const email = document.getElementById('email').value;
    const senha = document.getElementById('senha').value;
    handleLogin(email, senha);
});

// B. Post Listener
postForm.addEventListener('submit', (e) => {
    e.preventDefault();
    const titulo = document.getElementById('post-title').value;
    const conteudo = document.getElementById('post-content').value;
    handlePost(titulo, conteudo);
});

// C. Logout Listener
logoutButton.addEventListener('click', () => {
    updateUI(null);
    loadNews();
});

// D. Inicialização: Verifica se há dados de sessão ao carregar a página
document.addEventListener('DOMContentLoaded', () => {
    // Tenta obter o nome, role, etc., do localStorage
    const storedRole = localStorage.getItem('userRole');
    const storedNome = localStorage.getItem('userNome');

    if (storedRole && storedNome) {
        // Se houver dados básicos, simula que o usuário está logado
        const user = { role: storedRole, nome: storedNome };
        updateUI(user);
    } else {
        updateUI(null);
    }

    // Carrega a lista de notícias na inicialização
    loadNews();
});