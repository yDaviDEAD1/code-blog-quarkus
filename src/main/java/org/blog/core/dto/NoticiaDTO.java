package org.blog.core.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.blog.infra.data.entity.NoticiaModel;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

@Getter
@Setter
@NoArgsConstructor // Necessário para Jackson
@AllArgsConstructor
public class NoticiaDTO {
    // Campos que serão expostos publicamente
    private Long id;
    private String titulo;
    private String conteudo;
    private LocalDateTime dataPublicacao;

    // Relações: Usamos DTOs, nunca a Entidade JPA
    private UsuarioDTO autor;
    private List<ComentarioDTO> comentarios; // (Assumindo que você criou o ComentarioDTO)

    /**
     * Mapeador manual (Mapper de Camada):
     * Converte a Entidade JPA (NoticiaModel da INFRA) para o DTO (seguro do CORE).
     */
    public static NoticiaDTO from(NoticiaModel model) {
        List<ComentarioDTO> comentarioDTOs = model.getComentarios() != null
                ? model.getComentarios().stream()
                .map(ComentarioDTO::from)
                .collect(Collectors.toList())
                : Collections.emptyList();

        return new NoticiaDTO(
                model.getId(),
                model.getTitulo(),
                model.getConteudo(),
                model.getDataPublicacao(),
                UsuarioDTO.from(model.getAutor()),
                comentarioDTOs
        );
    }
}
