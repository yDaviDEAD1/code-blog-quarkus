package org.blog.core.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.blog.infra.data.entity.UsuarioModel;

@Getter
@Setter
@NoArgsConstructor // Gera o construtor sem argumentos, necessário para o Jackson
@AllArgsConstructor
public class UsuarioDTO {
    private Long id;
    private String nome;
    private String email;
    private String role;

    public static UsuarioDTO from(UsuarioModel model) {
        // Usa o construtor AllArgsConstructor ou setters
        return new UsuarioDTO(
                model.getId(),
                model.getNome(),
                model.getEmail(), // ⬅️ CORREÇÃO AQUI! Deve ser getEmail()
                model.getPapel()
        );
    }
}
