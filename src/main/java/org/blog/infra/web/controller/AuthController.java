package org.blog.infra.web.controller;

import io.quarkus.security.identity.SecurityIdentity;
import jakarta.annotation.security.PermitAll;
import jakarta.annotation.security.RolesAllowed;
import jakarta.inject.Inject;
import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import org.blog.core.dto.LoginDTO; // DTO de entrada (email e senha)
import org.blog.core.dto.UsuarioDTO;
import org.blog.core.service.IUsuarioService;

@Path("/auth")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class AuthController {

    @Inject
    SecurityIdentity securityIdentity; 
    
    @Inject
    IUsuarioService usuarioService;

    @POST
    @Path("/login")
    @PermitAll // Permite acesso público para iniciar o processo de autenticação
    public Response login(LoginDTO loginDTO) {
        String email = loginDTO.getEmail();

        if (securityIdentity.isAnonymous()) {
            return Response.status(Response.Status.UNAUTHORIZED).entity("Falha na autenticação ou credenciais inválidas.").build();
        }

        java.util.Optional<UsuarioDTO> userDTO = usuarioService.buscarDTOPorEmail(email);

        if (userDTO.isPresent()) {
            return Response.ok(userDTO.get()).build();
        }

        return Response.status(Response.Status.UNAUTHORIZED).build();
    }

    @POST
    @Path("/logout")
    @RolesAllowed({"ADMIN", "EDITOR", "LEITOR"})
    public Response logout() {
        return Response.status(Response.Status.NO_CONTENT).build();
    }
}