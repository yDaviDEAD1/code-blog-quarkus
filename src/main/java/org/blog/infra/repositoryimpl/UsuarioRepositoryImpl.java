package org.blog.infra.repositoryimpl;

import io.quarkus.hibernate.orm.panache.PanacheRepository;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.transaction.Transactional;
import org.blog.core.repository.IUsuarioRepository;
import org.blog.infra.data.entity.UsuarioModel;

import java.util.List;
import java.util.Optional;

@ApplicationScoped
public class UsuarioRepositoryImpl implements IUsuarioRepository, PanacheRepository<UsuarioModel> {
    @Transactional
    @Override
    public Optional<UsuarioModel> findByEmail(String email) {
        return find("email", email).firstResultOptional();
    }

    @Transactional
    @Override
    public void persist(UsuarioModel usuario) {
        PanacheRepository.super.persist(usuario);    }


    @Override
    public List<UsuarioModel> listarAlll() {
        return listAll();
    }

    @Override
    public Optional<UsuarioModel> findByIdOptionall(Long id) {
        return findByIdOptional(id);
    }
}
