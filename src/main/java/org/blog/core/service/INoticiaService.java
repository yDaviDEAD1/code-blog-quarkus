package org.blog.core.service;

import org.blog.core.dto.NoticiaDTO;

import java.util.List;

public interface INoticiaService {
    NoticiaDTO create(NoticiaDTO noticiaDTO, String autorEmail);
    NoticiaDTO update(Long id, NoticiaDTO noticiaDTO);
    void delete(Long id);
    NoticiaDTO findById(Long id);
    List<NoticiaDTO> listAll();
}
