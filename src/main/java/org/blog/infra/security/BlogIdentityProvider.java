package org.blog.infra.security;

import io.quarkus.security.credential.PasswordCredential;
import io.quarkus.security.identity.AuthenticationRequestContext;
import io.quarkus.security.identity.IdentityProvider;
import io.quarkus.security.identity.SecurityIdentity;
import io.quarkus.security.identity.request.UsernamePasswordAuthenticationRequest;
import io.quarkus.security.runtime.QuarkusSecurityIdentity;
import io.smallrye.mutiny.Uni;
import io.smallrye.mutiny.infrastructure.Infrastructure;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import org.blog.core.service.IUsuarioService;
import org.blog.infra.data.entity.UsuarioModel;

import java.util.Arrays;
import java.util.Optional;
import java.util.Set;

@ApplicationScoped
public class BlogIdentityProvider implements IdentityProvider<UsernamePasswordAuthenticationRequest> {
    @Inject
    IUsuarioService usuarioService;
    @Override
    public Class<UsernamePasswordAuthenticationRequest> getRequestType() {
        return UsernamePasswordAuthenticationRequest.class;
    }

    @Override
    public Uni<SecurityIdentity> authenticate(UsernamePasswordAuthenticationRequest request, AuthenticationRequestContext authenticationRequestContext) {
        String email = request.getUsername();
        PasswordCredential credential = request.getPassword();

        char[] passwordArray = credential.getPassword();
        String plainPassword = new String(passwordArray);
        Arrays.fill(passwordArray, ' ');

        return Uni.createFrom().item(() -> {

                    Optional<UsuarioModel> userOptional = usuarioService.buscarModelPorEmail(email);

                    if (userOptional.isEmpty()) {
                        return null;
                    }

                    UsuarioModel user = userOptional.get();
                    if (usuarioService.checkPassword(plainPassword, user.getSenha())) {

                        return (SecurityIdentity) QuarkusSecurityIdentity.builder()
                                .setPrincipal(user::getEmail)
                                .addRoles(Set.of(user.getPapel()))
                                .build();
                    }
                    return null;
                })
                .runSubscriptionOn(Infrastructure.getDefaultWorkerPool());
    }
    }


