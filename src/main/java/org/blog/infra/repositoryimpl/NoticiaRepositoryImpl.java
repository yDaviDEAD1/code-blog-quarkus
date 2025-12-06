package org.blog.infra.repositoryimpl;

import io.quarkus.hibernate.orm.panache.PanacheRepository;
import org.blog.core.repository.INoticiaRepository;
import org.blog.infra.data.entity.NoticiaModel;
import jakarta.enterprise.context.ApplicationScoped;
import java.util.List;
import java.util.Optional;

@ApplicationScoped // Torna esta classe injetável (RESOLVE O CDI)
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
        return PanacheRepository.super.findByIdOptional(id); // Usa o método Optional do Panache
    }

    @Override
    public List<NoticiaModel> listarAll() {

        return PanacheRepository.super.listAll();
    }
}