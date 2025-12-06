package org.blog.core.repository;

import org.blog.infra.data.entity.UsuarioModel;

import java.util.Optional;

public interface IUsuarioRepository {
    Optional<UsuarioModel> findByEmail(String email);
    void persist(UsuarioModel usuario);

}
