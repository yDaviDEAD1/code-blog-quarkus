package org.blog.infra.security;

import io.quarkus.security.credential.PasswordCredential;
import io.quarkus.security.identity.AuthenticationRequestContext;
import io.quarkus.security.identity.IdentityProvider;
import io.quarkus.security.identity.SecurityIdentity;
import io.quarkus.security.identity.request.UsernamePasswordAuthenticationRequest;
import io.quarkus.security.runtime.QuarkusSecurityIdentity;
import io.smallrye.mutiny.Uni;
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
        PasswordCredential credential = request.getPassword(); // Obtém o objeto PasswordCredential

        char[] passwordArray = credential.getPassword();
        String plainPassword = new String(passwordArray);
        Arrays.fill(passwordArray, ' ');
        Optional<UsuarioModel> userOptional = usuarioService.buscarModelPorEmail(email);

        if (userOptional.isEmpty()) {
            return Uni.createFrom().nullItem();
        }

        UsuarioModel user = userOptional.get();
        if (usuarioService.checkPassword(plainPassword, user.getSenha())) {
            Set<String> roles = Set.of(user.getPapel());

            return Uni.createFrom().item(
                    QuarkusSecurityIdentity.builder()
                            .setPrincipal(user::getEmail)
                            .addRoles(roles)
                            .build()
            );
        }

        return Uni.createFrom().nullItem();
    }
    }


