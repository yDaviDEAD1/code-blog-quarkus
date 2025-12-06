package org.blog.core.repository;

import org.blog.infra.data.entity.NoticiaModel;
import java.util.List;
import java.util.Optional;

// Contrato para acesso a dados de Notícias
public interface INoticiaRepository {
    
    void persist(NoticiaModel noticia);
    void delete(NoticiaModel noticia);
    Optional<NoticiaModel> buscarPorId(Long id);
    List<NoticiaModel> listarAll();
}