package org.blog.core.serviceimpl;

import io.quarkus.elytron.security.common.BcryptUtil;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;
import org.blog.core.dto.UsuarioDTO;
import org.blog.core.repository.IUsuarioRepository;
import org.blog.core.service.IUsuarioService;
import org.blog.infra.data.entity.UsuarioModel;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@ApplicationScoped
public class UsuarioServiceImpl implements IUsuarioService {

    private final IUsuarioRepository usuarioRepository;

    @Inject
    public UsuarioServiceImpl(IUsuarioRepository usuarioRepository) {
        this.usuarioRepository = usuarioRepository;
    }

    @Override
    public Optional<UsuarioModel> buscarModelPorEmail(String email) {
        return usuarioRepository.findByEmail(email);
    }

    @Override
    public Optional<UsuarioDTO> buscarDTOPorEmail(String email) {
        return usuarioRepository.findByEmail(email)
                .map(UsuarioDTO::from);
    }

    @Transactional
    @Override
    public UsuarioDTO criarNovoUsuario(String nome, String email, String senha, String role) throws IllegalAccessException {
        if (usuarioRepository.findByEmail(email).isPresent()){
            throw new IllegalAccessException("Email ja cadastrado");
        }
        UsuarioModel novoUsuario = new UsuarioModel();
        novoUsuario.setNome(nome);
        novoUsuario.setEmail(email);
        novoUsuario.setSenha(BcryptUtil.bcryptHash(senha));
        novoUsuario.setPapel(role);
        usuarioRepository.persist(novoUsuario);
        return UsuarioDTO.from(novoUsuario);
    }

    @Transactional
    @Override
    public void alterarRole(Long userId, String novaRole) {
        UsuarioModel usuario = usuarioRepository.findByIdOptionall(userId)
                .orElseThrow(() -> new IllegalArgumentException("Usuário não encontrado."));

        usuario.setPapel(novaRole);
    }

    @Override
    public boolean checkPassword(String plainPassword, String hashedPassword) {
        return BcryptUtil.matches(plainPassword, hashedPassword);    }

    @Override
    public List<UsuarioDTO> listAllUsersDTO() {
        return usuarioRepository.listarAlll().stream()
                .map(UsuarioDTO::from)
                .collect(Collectors.toList());
    }
}
