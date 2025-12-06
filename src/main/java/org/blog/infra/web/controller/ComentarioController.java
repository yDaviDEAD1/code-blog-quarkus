package org.blog.infra.web.controller;

import io.quarkus.security.identity.SecurityIdentity;
import jakarta.annotation.security.PermitAll;
import jakarta.annotation.security.RolesAllowed;
import jakarta.inject.Inject;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import org.blog.core.dto.ComentarioDTO;
import org.blog.core.service.IComentarioService;
import java.util.List;

@Path("/noticias/{noticiaId}/comentarios")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class ComentarioController {

    @Inject
    IComentarioService comentarioService;
    
    @Inject
    SecurityIdentity securityIdentity;
    
    @GET
    @PermitAll
    public List<ComentarioDTO> listByNoticia(
            @PathParam("noticiaId") Long noticiaId) {
        
        return comentarioService.listByNoticiaId(noticiaId);
    }

    @POST
    @RolesAllowed({"ADMIN", "EDITOR", "LEITOR"})
    public Response create(
            @PathParam("noticiaId") Long noticiaId,
            ComentarioDTO comentarioDTO) {
        
        // Obter o email do usuário autenticado
        String autorEmail = securityIdentity.getPrincipal().getName();
        
        ComentarioDTO created = comentarioService.create(noticiaId, comentarioDTO, autorEmail);
        return Response.status(Response.Status.CREATED).entity(created).build();
    }

    @DELETE
    @Path("/{comentarioId}")
    @RolesAllowed({"ADMIN", "EDITOR", "LEITOR"}) // Permite que todos tentem, mas o SERVICE fará a checagem fina.
    public Response delete(@PathParam("comentarioId") Long comentarioId) {

        comentarioService.delete(comentarioId);
        return Response.status(Response.Status.NO_CONTENT).build();
    }
}