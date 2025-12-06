package org.blog.core.serviceimpl;

import org.blog.core.dto.NoticiaDTO;
import org.blog.core.repository.INoticiaRepository;
import org.blog.core.service.INoticiaService;
import org.blog.core.service.IUsuarioService;
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
public class NoticiaServiceImpl implements INoticiaService {

    private final INoticiaRepository noticiaRepository;
    private final IUsuarioService usuarioService;

    @Transactional
    @Override
    public NoticiaDTO create(NoticiaDTO noticiaDTO, String autorEmail) {
        UsuarioModel autor = usuarioService.buscarModelPorEmail(autorEmail)
                .orElseThrow(() -> new IllegalArgumentException("Autor não encontrado no sistema."));

        NoticiaModel novaNoticia = new NoticiaModel();
        novaNoticia.setTitulo(noticiaDTO.getTitulo());
        novaNoticia.setConteudo(noticiaDTO.getConteudo());
        novaNoticia.setAutor(autor);

        noticiaRepository.persist(novaNoticia);
        return NoticiaDTO.from(novaNoticia);
    }
    
    @Transactional
    @Override
    public NoticiaDTO update(Long id, NoticiaDTO noticiaDTO) {
        NoticiaModel noticia = noticiaRepository.buscarPorId(id)
                .orElseThrow(() -> new IllegalArgumentException("Notícia não encontrada."));
        
        noticia.setTitulo(noticiaDTO.getTitulo());
        noticia.setConteudo(noticiaDTO.getConteudo());

        return NoticiaDTO.from(noticia);
    }

    @Transactional
    @Override
    public void delete(Long id) {
        NoticiaModel noticia = noticiaRepository.buscarPorId(id)
                .orElseThrow(() -> new IllegalArgumentException("Notícia não encontrada."));
        noticiaRepository.delete(noticia);
    }

    @Override
    public NoticiaDTO findById(Long id) {
        return noticiaRepository.buscarPorId(id)
                .map(NoticiaDTO::from)
                .orElseThrow(() -> new IllegalArgumentException("Notícia não encontrada."));
    }

    @Override
    public List<NoticiaDTO> listAll() {
        return noticiaRepository.listarAll().stream()
                .map(NoticiaDTO::from)
                .collect(Collectors.toList());
    }
}