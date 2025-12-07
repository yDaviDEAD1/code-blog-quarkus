package org.blog.infra.web.controller;

import jakarta.annotation.security.RolesAllowed;
import jakarta.inject.Inject;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import org.blog.core.dto.UsuarioDTO;
import org.blog.core.service.IUsuarioService;

@Path("/admin/usuarios")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
@RolesAllowed("ADMIN")
public class UsuarioAdminController {

    @Inject
    IUsuarioService usuarioService;

    @POST
    @Path("/register")
    public Response create(UsuarioDTO novoUsuario) { 
        try {
            UsuarioDTO created = usuarioService.criarNovoUsuario(
                novoUsuario.getNome(), 
                novoUsuario.getEmail(), 
                novoUsuario.getSenha(),
                novoUsuario.getRole()
            );
            return Response.status(Response.Status.CREATED).entity(created).build();
        } catch (IllegalAccessException e) {
            return Response.status(Response.Status.CONFLICT).entity(e.getMessage()).build();
        }
    }

    @PUT
    @Path("/{id}/role")
    public Response updateRole(@PathParam("id") Long id, UsuarioDTO updateDTO) {
        usuarioService.alterarRole(id, updateDTO.getRole());
        return Response.status(Response.Status.NO_CONTENT).build();
    }
    

}