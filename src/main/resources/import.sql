--SET MODE PostgreSQL;

-- Senha 'admin123' hasheada (use um gerador online ou um teste rápido do seu BcryptUtil para gerar o hash real)
-- Exemplo de hash para 'admin123': $2a$10$wS2Wb1F.L6T9xU3o9L0o3uL0o3uL0o3uL0o3uL0o3uL0o3u
--INSERT INTO UsuarioModel (id, nome, email, senha, papel)
--VALUES (1, 'Administrador', 'admin@blog.com', '$2a$10$QsgIRnr.p6r50OzC5n749.iT3BRfJS0XvAn8oPxbIVJKZRIWTr1Ci', 'ADMIN');

-- Senha 'editor123' hasheada
--INSERT INTO UsuarioModel (id, nome, email, senha, papel)
--VALUES (2, 'Editor Chefe', 'editor@blog.com', '$2a$10$wS2Wb1F.L6T9xU3o9L0o3uL0o3uL0o3uL0o3uL0o3uL0o3uL0o3u', 'EDITOR');

-- Senha 'leitor123' hasheada
--INSERT INTO UsuarioModel (id, nome, email, senha, papel)
--VALUES (3, 'Leitor Comum', 'leitor@blog.com', '$2a$10$wS2Wb1F.L6T9xU3o9L0o3uL0o3uL0o3uL0o3uL0o3uL0o3uL0o3u', 'LEITOR');