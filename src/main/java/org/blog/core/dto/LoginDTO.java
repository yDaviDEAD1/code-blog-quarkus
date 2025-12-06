package org.blog.core.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

// DTO de entrada para o endpoint de login
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class LoginDTO {
    
    // O email do usuário
    private String email;
    
    // A senha em texto puro fornecida pelo usuário
    private String senha;
}