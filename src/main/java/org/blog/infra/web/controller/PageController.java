package org.blog.infra.web.controller;

import io.quarkus.qute.Template;
import io.quarkus.qute.TemplateInstance;
import jakarta.annotation.security.PermitAll;
import jakarta.inject.Inject;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;

@Path("/")
public class PageController {

    @Inject 
    Template index;

    @Inject
    Template login1;

    @GET
    @Produces(MediaType.TEXT_HTML)
    public TemplateInstance getIndex() {
        return index.data("title", "Bem-Vindo ao Blog Básico");


    }

    @GET
    @Path("login1") // 💡 ESTA É A ROTA QUE ESTAVA FALTANDO!
    @Produces(MediaType.TEXT_HTML)
    @PermitAll
    public TemplateInstance getAuthPage() {
        return login1.data("title", "Acesso - Login ou Registro");
    }

}