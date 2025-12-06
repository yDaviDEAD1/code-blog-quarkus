package org.blog.infra.repositoryimpl;

import io.quarkus.hibernate.orm.panache.PanacheRepository;
import jakarta.enterprise.context.ApplicationScoped;
import org.blog.core.repository.IUsuarioRepository;
import org.blog.infra.data.entity.UsuarioModel;

import java.util.Optional;

@ApplicationScoped
public class UsuarioRepositoryImpl implements IUsuarioRepository, PanacheRepository<UsuarioModel> {
    @Override
    public Optional<UsuarioModel> findByEmail(String email) {
        return find("email", email).firstResultOptional();
    }

    @Override
    public void persist(UsuarioModel usuario) {
        PanacheRepository.super.persist(usuario);
    }
}
