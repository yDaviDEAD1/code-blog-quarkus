package org.blog.core.repository;

import org.blog.infra.data.entity.ComentarioModel;
import java.util.List;
import java.util.Optional;

public interface IComentarioRepository {

    void persist(ComentarioModel comentario);
    void delete(ComentarioModel comentario);
    
    // Método para buscar um comentário específico (para edição/deleção)
    Optional<ComentarioModel> buscarPorId(Long id);

    // Método para listar todos os comentários de uma notícia específica
    List<ComentarioModel> listByNoticiaId(Long noticiaId);
}