// script.js

const API_BASE_URL = 'http://localhost:8080';
let currentUser = null;

// --- Elementos do DOM ---
const authButtonContainer = document.getElementById('auth-button-container');

// Elementos da Home Page (Index.html)
const postForm = document.getElementById('postForm');
const newsListDiv = document.getElementById('news-list');
const postCreationSection = document.getElementById('post-creation-section');
const postStatus = document.getElementById('post-status'); // Pode ser null na página auth

// Elementos de Status/View (Comuns)
const loggedInView = document.getElementById('logged-in-view');
const loggedOutView = document.getElementById('logged-out-view');
const usernameDisplay = document.getElementById('username-display');
const roleDisplay = document.getElementById('role-display');

// Elementos da Página de Autenticação (Auth.html)
const loginForm = document.getElementById('loginForm');
const registerForm = document.getElementById('registerForm');
const authStatus = document.getElementById('auth-status');
const showLoginBtn = document.getElementById('showLogin');
const showRegisterBtn = document.getElementById('showRegister');
const loginContainer = document.getElementById('login-view-container');
const registerContainer = document.getElementById('register-view-container');
const authToggleDiv = document.querySelector('.auth-toggle');


// ----------------------------------------------------------------
// 1. UTILIDADES DE AUTENTICAÇÃO E UI
// ----------------------------------------------------------------

function getAuthHeader() {
    return localStorage.getItem('authHeader');
}

/**
 * Define o estado da interface E gerencia o botão do header.
 */
function updateUI(user = null) {
    currentUser = user;
    const isHomePage = window.location.pathname === '/' || window.location.pathname === '/index.html';

    if (user) {
        // --- ESTADO LOGADO ---

        // Configura o cabeçalho/botão de SAIR no HEADER
        if (authButtonContainer) {
            authButtonContainer.innerHTML = `<button id="headerLogoutButton" class="header-button">Sair</button>`;
            document.getElementById('headerLogoutButton').addEventListener('click', handleLogout);
        }

        if (isHomePage) {
            // Configura o status de bem-vindo na Home
            usernameDisplay.textContent = user.nome;
            roleDisplay.textContent = user.role;

            if (loggedInView) loggedInView.style.display = 'flex';

            // Exibir a seção de postagem apenas para ADMIN ou EDITOR
            if (user.role === 'ADMIN' || user.role === 'EDITOR') {
                if (postCreationSection) {
                    postCreationSection.style.display = 'block';
                    postCreationSection.classList.add('fade-in');
                }
            } else {
                if (postCreationSection) postCreationSection.style.display = 'none';
            }
        }

    } else {
        // --- ESTADO DESLOGADO/LOGOUT ---

        // Configura o cabeçalho/botão de LOGIN/CADASTRO no HEADER
        if (authButtonContainer) {
            authButtonContainer.innerHTML = `<button onclick="window.location.href='/login1'" class="header-button">Login / Cadastro</button>`;
        }

        // Esconde as áreas logadas na Home
        if (isHomePage) {
            if (loggedInView) loggedInView.style.display = 'none';
            if (postCreationSection) postCreationSection.style.display = 'none';
        }

        localStorage.removeItem('authHeader');
        localStorage.removeItem('userRole');
        localStorage.removeItem('userNome');
    }
}


// ----------------------------------------------------------------
// 2. FUNÇÕES DE API (AUTH, CRUD)
// ----------------------------------------------------------------

/**
 * Lida com o processo de Logout.
 */
function handleLogout() {
    updateUI(null);
    // Redireciona para a home após sair (se estiver em qualquer outra página)
    if (window.location.pathname !== '/') {
        window.location.href = '/';
    }
    // Recarrega notícias na Home (se estiver na Home)
    if (typeof loadNews === 'function') loadNews();
}

/**
 * Realiza o Login. (Apenas em login1.html)
 */
async function handleLogin(e) {
    e.preventDefault();
    if (!loginForm) return;

    const email = loginForm.querySelector('#email').value;
    const senha = loginForm.querySelector('#senha').value;
    const statusDiv = document.getElementById('auth-status');

    statusDiv.textContent = 'Autenticando...';

    const encoded = btoa(`${email}:${senha}`);
    const authHeader = `Basic ${encoded}`;

    try {
        const response = await fetch(`${API_BASE_URL}/auth/login`, {
            method: 'POST',
            headers: { 'Authorization': authHeader }
        });

        if (response.ok) {
            const userData = await response.json();
            localStorage.setItem('authHeader', authHeader);
            localStorage.setItem('userRole', userData.role);
            localStorage.setItem('userNome', userData.nome);

            statusDiv.textContent = 'Login bem-sucedido. Redirecionando...';

            // 💡 REDIRECIONAR PARA A HOME
            setTimeout(() => {
                window.location.href = '/';
            }, 500);

        } else {
            statusDiv.textContent = 'Falha na autenticação. Email ou senha inválidos.';
        }
    } catch (error) {
        statusDiv.textContent = 'Erro de rede ou servidor indisponível.';
    }
}

/**
 * Lida com o registro de um novo usuário (LEITOR). (Apenas em login1.html)
 */
async function handleRegister(e) {
    e.preventDefault();
    if (!registerForm) return;

    const nome = registerForm.querySelector('#register-nome').value;
    const email = registerForm.querySelector('#register-email').value;
    const senha = registerForm.querySelector('#register-senha').value;
    const registerStatus = document.getElementById('register-status');

    registerStatus.textContent = 'Registrando...';
    const registerURL = `${API_BASE_URL}/auth/register`;

    try {
        const response = await fetch(registerURL, {
            method: 'POST',
            headers: { 'Content-Type': 'application/json' },
            body: JSON.stringify({
                nome: nome,
                email: email,
                senha: senha,
                role: 'LEITOR'
            })
        });

        if (response.status === 201) {
            registerStatus.textContent = 'Registro bem-sucedido! Vá para o Login.';
            registerForm.reset();

            // Muda para o formulário de Login após sucesso
            setTimeout(() => {
                if (showLoginBtn) showLoginBtn.click();
            }, 1000);

        } else if (response.status === 409) {
            registerStatus.textContent = 'Erro: Email já cadastrado.';
        } else {
            registerStatus.textContent = `Falha no registro. Status: ${response.status}`;
        }
    } catch (error) {
        registerStatus.textContent = 'Erro de rede ou servidor indisponível.';
    }
}


/**
 * Envia uma nova notícia. (Apenas na Home)
 */
async function handlePost(e) {
    e.preventDefault();
    if (!postForm) return;
    const authHeader = getAuthHeader();

    // ... (restante da lógica handlePost) ...
    postStatus.textContent = 'Publicando...';
    const titulo = postForm.querySelector('#post-title').value;
    const conteudo = postForm.querySelector('#post-content').value;
    const postData = { titulo, conteudo };

    try {
        const response = await fetch(`${API_BASE_URL}/noticias`, {
            method: 'POST',
            headers: {
                'Authorization': authHeader,
                'Content-Type': 'application/json'
            },
            body: JSON.stringify(postData)
        });

        if (response.status === 201) {
            postStatus.textContent = 'Notícia publicada com sucesso!';
            postForm.reset();
            loadNews();
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

/**
 * Lida com a submissão de um novo comentário. (Apenas na Home)
 */
async function handleCommentSubmission(e) {
    e.preventDefault();
    const form = e.target;
    const authHeader = getAuthHeader();

    if (!authHeader) {
        form.querySelector('.comment-status').textContent = 'Você deve estar logado para comentar.';
        return;
    }

    // ... (restante da lógica handleCommentSubmission) ...
    const noticiaId = form.getAttribute('data-noticia-id');
    const commentStatus = form.querySelector('.comment-status');
    const textArea = form.querySelector('textarea');
    commentStatus.textContent = 'Enviando...';
    const comentarioData = { texto: textArea.value };

    try {
        const response = await fetch(`${API_BASE_URL}/noticias/${noticiaId}/comentarios`, {
            method: 'POST',
            headers: {
                'Authorization': authHeader,
                'Content-Type': 'application/json'
            },
            body: JSON.stringify(comentarioData)
        });

        if (response.status === 201) {
            commentStatus.textContent = 'Comentário publicado!';
            textArea.value = '';
            loadNews();
        } else {
            commentStatus.textContent = `Falha ao comentar. Status: ${response.status}`;
        }
    } catch (error) {
        commentStatus.textContent = 'Erro de rede ao comentar.';
    }
}


/**
 * Carrega e exibe a lista de notícias. (Apenas na Home)
 */
async function loadNews() {
    if (!newsListDiv) return; // Não executa se não estiver na Home

    newsListDiv.innerHTML = '<p>Carregando notícias...</p>';

    try {
        const response = await fetch(`${API_BASE_URL}/noticias`);

        // ... (restante da lógica loadNews - renderização) ...
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

            const commentsHtml = noticia.comentarios.length > 0
                ? noticia.comentarios.map(c => `
                    <div class="comment-item">
                        <p>${c.texto}</p>
                        <small>Por: ${c.autor.nome} em ${new Date(c.dataComentario).toLocaleString()}</small>
                    </div>
                `).join('')
                : '<p style="font-size: 0.9em;">Nenhum comentário ainda.</p>';

            htmlContent += `
                <div class="news-item fade-in" data-noticia-id="${noticia.id}">
                    <h3>${noticia.titulo}</h3>
                    <p>${noticia.conteudo.substring(0, 200)}...</p>
                    <small>Por: ${noticia.autor.nome} em ${data}</small>
                    
                    <div class="comments-section">
                        <h4>Comentários (${noticia.comentarios.length})</h4>
                        <div class="comments-list" id="comments-${noticia.id}">
                            ${commentsHtml}
                        </div>
                        
                        <form class="comment-form" data-noticia-id="${noticia.id}">
                            <textarea placeholder="Adicione um comentário..." required class="comment-input"></textarea>
                            <button type="submit" class="comment-button">Comentar</button>
                            <p class="comment-status error" id="comment-status-${noticia.id}"></p>
                        </form>
                    </div>
                </div>
            `;
        });

        newsListDiv.innerHTML = htmlContent;

        attachCommentListeners();

    } catch (error) {
        console.error("Erro ao carregar notícias:", error);
        newsListDiv.innerHTML = `<p class="error">Erro ao carregar notícias: ${error.message}</p>`;
    }
}

/**
 * Anexa listeners de submissão aos formulários de comentários. (Apenas na Home)
 */
function attachCommentListeners() {
    document.querySelectorAll('.comment-form').forEach(form => {
        form.removeEventListener('submit', handleCommentSubmission);
        form.addEventListener('submit', handleCommentSubmission);
    });
}


// ----------------------------------------------------------------
// 3. EVENT LISTENERS E INICIALIZAÇÃO
// ----------------------------------------------------------------

document.addEventListener('DOMContentLoaded', () => {

    // --- LÓGICA DE EVENTOS (APENAS NA PÁGINA DE AUTENTICAÇÃO: /auth) ---
    if (loginForm) loginForm.addEventListener('submit', handleLogin);
    if (registerForm) registerForm.addEventListener('submit', handleRegister);

    // Lógica de troca de views (Apenas na página /auth)
    if (showLoginBtn && showRegisterBtn) {
        showLoginBtn.addEventListener('click', () => {
            loginContainer.style.display = 'block';
            registerContainer.style.display = 'none';
            document.getElementById('register-status').textContent = '';
        });

        showRegisterBtn.addEventListener('click', () => {
            loginContainer.style.display = 'none';
            registerContainer.style.display = 'block';
            document.getElementById('auth-status').textContent = '';
        });
        // Exibir o login por padrão na página de Auth
        if (loginContainer) loginContainer.style.display = 'block';
        if (registerContainer) registerContainer.style.display = 'none';
    }

    // --- LÓGICA DE EVENTOS (APENAS NA HOME PAGE: /) ---
    if (postForm) postForm.addEventListener('submit', handlePost);

    // --- INICIALIZAÇÃO GLOBAL ---
    const storedRole = localStorage.getItem('userRole');
    const storedNome = localStorage.getItem('userNome');

    if (storedRole && storedNome) {
        const user = { role: storedRole, nome: storedNome };
        updateUI(user);
    } else {
        updateUI(null);
    }

    // Carrega a lista de notícias (executado apenas se newsListDiv existir)
    loadNews();
});