package org.blog.infra.repositoryimpl;

import io.quarkus.hibernate.orm.panache.PanacheRepository;
import org.blog.core.repository.IComentarioRepository;
import org.blog.infra.data.entity.ComentarioModel;
import jakarta.enterprise.context.ApplicationScoped;
import java.util.List;
import java.util.Optional;

@ApplicationScoped
public class ComentarioRepositoryImpl implements IComentarioRepository, PanacheRepository<ComentarioModel> {
    
    @Override
    public void persist(ComentarioModel comentario) {
        persist(comentario);
    }

    @Override
    public void delete(ComentarioModel comentario) {
        delete(comentario);
    }

    @Override
    public Optional<ComentarioModel> buscarPorId(Long id) {
        return findByIdOptional(id);
    }

    @Override
    public List<ComentarioModel> listByNoticiaId(Long noticiaId) {
        return find("noticia.id", noticiaId).list();
    }
}