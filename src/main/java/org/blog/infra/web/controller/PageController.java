package org.blog.infra.web.controller;

import io.quarkus.qute.Template;
import io.quarkus.qute.TemplateInstance;
import jakarta.inject.Inject;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;

@Path("/")
public class PageController {

    // Injeta o template (deve estar em src/main/resources/templates/index.html)
    @Inject 
    Template index; 

    @GET
    @Produces(MediaType.TEXT_HTML)
    public TemplateInstance getIndex() {
        return index.data("title", "Bem-Vindo ao Blog Básico");
    }

}