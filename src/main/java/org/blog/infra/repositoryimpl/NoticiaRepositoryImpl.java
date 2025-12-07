package org.blog.infra.repositoryimpl;

import io.quarkus.hibernate.orm.panache.PanacheRepository;
import org.blog.core.repository.INoticiaRepository;
import org.blog.infra.data.entity.NoticiaModel;
import jakarta.enterprise.context.ApplicationScoped;
import java.util.List;
import java.util.Optional;

@ApplicationScoped
public class NoticiaRepositoryImpl implements INoticiaRepository, PanacheRepository<NoticiaModel> {

    @Override
    public void persist(NoticiaModel noticia) {
       PanacheRepository.super.persist(noticia);
    }

    @Override
    public void delete(NoticiaModel noticia) {
       PanacheRepository.super.delete(noticia);
    }

    @Override
    public Optional<NoticiaModel> buscarPorId(Long id) {
        return findByIdOptional(id);
    }

    @Override
    public List<NoticiaModel> listarAll() {

        return listAll();
    }
}