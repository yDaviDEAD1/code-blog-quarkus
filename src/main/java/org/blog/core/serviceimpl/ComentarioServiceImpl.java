package org.blog.core.serviceimpl;

import org.blog.core.dto.ComentarioDTO;
import org.blog.core.repository.IComentarioRepository;
import org.blog.core.repository.INoticiaRepository;
import org.blog.core.service.IComentarioService;
import org.blog.core.service.IUsuarioService;
import org.blog.infra.data.entity.ComentarioModel;
import org.blog.infra.data.entity.NoticiaModel;
import org.blog.infra.data.entity.UsuarioModel;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;
import java.util.List;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;

@ApplicationScoped
@RequiredArgsConstructor(onConstructor_ = {@Inject})
public class ComentarioServiceImpl implements IComentarioService {

    private final IComentarioRepository comentarioRepository;
    private final INoticiaRepository noticiaRepository; // Para buscar a notícia à qual o comentário pertence
    private final IUsuarioService usuarioService; // Para buscar o autor do comentário

    @Transactional
    @Override
    public ComentarioDTO create(Long noticiaId, ComentarioDTO comentarioDTO, String autorEmail) {
        

        NoticiaModel noticia = noticiaRepository.buscarPorId(noticiaId)
                .orElseThrow(() -> new IllegalArgumentException("Notícia não encontrada."));


        UsuarioModel autor = usuarioService.buscarModelPorEmail(autorEmail)
                .orElseThrow(() -> new IllegalArgumentException("Autor não encontrado no sistema."));

        ComentarioModel novoComentario = new ComentarioModel();
        novoComentario.setTexto(comentarioDTO.getTexto());
        novoComentario.setNoticia(noticia);
        novoComentario.setAutor(autor);
        
        comentarioRepository.persist(novoComentario);
        
        return ComentarioDTO.from(novoComentario);
    }
    
    @Override
    public ComentarioDTO findById(Long id) {
        return comentarioRepository.buscarPorId(id)
                .map(ComentarioDTO::from)
                .orElseThrow(() -> new IllegalArgumentException("Comentário não encontrado."));
    }

    @Override
    public List<ComentarioDTO> listByNoticiaId(Long noticiaId) {
        return comentarioRepository.listByNoticiaId(noticiaId).stream()
                .map(ComentarioDTO::from)
                .collect(Collectors.toList());
    }

    @Transactional
    @Override
    public void delete(Long id) {
        ComentarioModel comentario = comentarioRepository.buscarPorId(id)
                .orElseThrow(() -> new IllegalArgumentException("Comentário não encontrado."));

        comentarioRepository.delete(comentario);
    }
}