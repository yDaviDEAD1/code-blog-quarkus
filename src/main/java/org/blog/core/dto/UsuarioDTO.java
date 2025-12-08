package org.blog.core.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.blog.infra.data.entity.UsuarioModel;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class UsuarioDTO {
    private Long id;
    private String nome;
    private String email;
    private String role;

    private String senha;

    public static UsuarioDTO from(UsuarioModel model) {
        return new UsuarioDTO(
                model.getId(),
                model.getNome(),
                model.getEmail(),
                model.getPapel(),
                null
        );
    }
}
