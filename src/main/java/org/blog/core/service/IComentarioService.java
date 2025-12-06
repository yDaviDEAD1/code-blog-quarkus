package org.blog.core.service;

import org.blog.core.dto.ComentarioDTO;
import java.util.List;

public interface IComentarioService {
    ComentarioDTO create(Long noticiaId, ComentarioDTO comentarioDTO, String autorEmail);
    ComentarioDTO findById(Long id);
    List<ComentarioDTO> listByNoticiaId(Long noticiaId);
    void delete(Long id);
}