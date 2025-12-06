package org.blog.infra.web.controller;

import io.quarkus.security.identity.SecurityIdentity;
import jakarta.annotation.security.PermitAll;
import jakarta.annotation.security.RolesAllowed;
import jakarta.enterprise.context.RequestScoped;
import jakarta.inject.Inject;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import org.blog.core.dto.NoticiaDTO;
import org.blog.core.service.INoticiaService;
import java.util.List;

@Path("/noticias")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class NoticiaController {

    @Inject
    INoticiaService noticiaService;
    
    @Inject
    SecurityIdentity securityIdentity;

    @GET
    @PermitAll
    public List<NoticiaDTO> listAll() {
        return noticiaService.listAll();
    }

    @GET
    @Path("/{id}")
    @PermitAll
    public NoticiaDTO getById(@PathParam("id") Long id) {
        return noticiaService.findById(id);
    }

    @POST
    @RolesAllowed({"ADMIN", "EDITOR"})
    public Response create(NoticiaDTO noticiaDTO) {
        String autorEmail = securityIdentity.getPrincipal().getName();
        
        NoticiaDTO created = noticiaService.create(noticiaDTO, autorEmail);
        return Response.status(Response.Status.CREATED).entity(created).build();
    }

    @PUT
    @Path("/{id}")
    @RolesAllowed({"ADMIN", "EDITOR"})
    public NoticiaDTO update(@PathParam("id") Long id, NoticiaDTO noticiaDTO) {
        return noticiaService.update(id, noticiaDTO);
    }


    @DELETE
    @Path("/{id}")
    @RolesAllowed({"ADMIN", "EDITOR"})
    public Response delete(@PathParam("id") Long id) {
        noticiaService.delete(id);
        return Response.status(Response.Status.NO_CONTENT).build();
    }
}