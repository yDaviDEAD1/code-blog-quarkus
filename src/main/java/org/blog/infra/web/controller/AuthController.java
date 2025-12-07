package org.blog.infra.web.controller;

import io.quarkus.security.identity.SecurityIdentity;
import jakarta.annotation.security.PermitAll;
import jakarta.annotation.security.RolesAllowed; // Use RolesAllowed para forçar a autenticação
import jakarta.inject.Inject;
import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
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
    @RolesAllowed({"ADMIN", "EDITOR", "LEITOR"}) // Força que um usuário autenticado (com qualquer papel) chegue aqui
    public Response login() {
        // Se chegamos aqui, o BlogIdentityProvider já autenticou o usuário
        String email = securityIdentity.getPrincipal().getName();

        java.util.Optional<UsuarioDTO> userDTO = usuarioService.buscarDTOPorEmail(email);

        if (userDTO.isPresent()) {
            // Retorna os dados do usuário logado (exemplo de token ou dados de sessão)
            return Response.ok(userDTO.get()).build();
        }

        // Isso é um fallback, pois o 401 deve ser gerado antes pelo Quarkus se falhar
        return Response.status(Response.Status.UNAUTHORIZED).build();
    }

    @POST
    @Path("/register")
    @PermitAll // 💡 Permite que qualquer um crie um usuário
    public Response register(UsuarioDTO registroDTO) {
        try {
            UsuarioDTO created = usuarioService.criarNovoUsuario(
                    registroDTO.getNome(),
                    registroDTO.getEmail(),
                    registroDTO.getSenha(),
                    "LEITOR" // Atribui a role padrão para o registro público
            );
            return Response.status(Response.Status.CREATED).entity(created).build();
        } catch (IllegalAccessException e) {
            // Email já cadastrado
            return Response.status(Response.Status.CONFLICT).entity(e.getMessage()).build();
        }
    }

    @POST
    @Path("/logout")
    @RolesAllowed({"ADMIN", "EDITOR", "LEITOR"})
    // O logout em Basic Auth é geralmente tratado pelo cliente, que simplesmente
    // para de enviar o cabeçalho Authorization. Este endpoint pode ser mantido
    // para fins de compatibilidade ou para limpar uma sessão se você usar cookies.
    public Response logout() {
        return Response.status(Response.Status.NO_CONTENT).build();
    }
}