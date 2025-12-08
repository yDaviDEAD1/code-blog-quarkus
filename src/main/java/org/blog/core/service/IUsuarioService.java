package org.blog.core.service;

import org.blog.core.dto.UsuarioDTO;
import org.blog.infra.data.entity.UsuarioModel;

import java.util.List;
import java.util.Optional;

public interface IUsuarioService {
    Optional<UsuarioModel> buscarModelPorEmail(String email);
    Optional<UsuarioDTO> buscarDTOPorEmail(String email);
    UsuarioDTO criarNovoUsuario(String nome, String email, String senha, String role) throws IllegalAccessException;
    void alterarRole(Long userId, String novaRole);
    boolean checkPassword(String plainPassword, String hashedPassword);
    List<UsuarioDTO> listAllUsersDTO();
}
