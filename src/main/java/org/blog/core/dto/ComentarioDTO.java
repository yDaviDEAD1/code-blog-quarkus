package org.blog.core.dto;

import org.blog.infra.data.entity.ComentarioModel;
import java.time.LocalDateTime;
import lombok.Getter;
import lombok.Setter;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ComentarioDTO {
    
    private Long id;
    private String texto;
    private LocalDateTime dataComentario;
    private UsuarioDTO autor;

    public static ComentarioDTO from(ComentarioModel model) {
        return new ComentarioDTO(
            model.getId(),
            model.getTexto(),
            model.getDataComentario(),
            UsuarioDTO.from(model.getAutor())
        );
    }
}