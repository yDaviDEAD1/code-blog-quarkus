package org.blog.infra.web.controller;

import io.quarkus.elytron.security.common.BcryptUtil;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.PathParam;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;

@Path("/util")
public class HashGeneratorResource {
    @GET
    @Path("/hash/{password}")
    @Produces(MediaType.TEXT_PLAIN)
    public String generateHash(@PathParam("password") String password) {
        return BcryptUtil.bcryptHash(password);
    }
}