package org.blog.core.repository;

import org.blog.infra.data.entity.ComentarioModel;
import java.util.List;
import java.util.Optional;

public interface IComentarioRepository {

    void persist(ComentarioModel comentario);
    void delete(ComentarioModel comentario);
    
    Optional<ComentarioModel> buscarPorId(Long id);

    List<ComentarioModel> listByNoticiaId(Long noticiaId);
}