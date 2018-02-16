package com.modzo.jwt.server.security

import com.modzo.jwt.domain.clients.Client
import com.modzo.jwt.domain.clients.Clients
import groovy.transform.CompileStatic
import org.springframework.security.core.GrantedAuthority
import org.springframework.security.oauth2.provider.ClientDetails
import org.springframework.security.oauth2.provider.ClientDetailsService
import org.springframework.security.oauth2.provider.ClientRegistrationException
import org.springframework.stereotype.Service

import static com.modzo.jwt.domain.clients.exceptions.ClientNotFoundException.byClientId

@Service
@CompileStatic
class LocalClientDetailsService implements ClientDetailsService {

    private final Clients clients;

    LocalClientDetailsService(Clients clients) {
        this.clients = clients;
    }

    @Override
    ClientDetails loadClientByClientId(String clientId) throws ClientRegistrationException {
        Client client = clients.findByClientId(clientId).orElseThrow { -> byClientId(clientId) }
        return new LocalClientDetails(
                clientId: client.clientId,
                clientSecret: client.clientSecret,
                secretRequired: client.secretRequired,
                scoped: client.scoped,
                scope: client.scopes.collect { it.type } as Set,
                resourceIds: client.resourceIds,
                authorizedGrantTypes: client.authorizedGrantTypes.collect { it.type } as Set,
                authorities: client.authorities.collect { it.name() } as Set,
                registeredRedirectUri: client.registeredRedirectUris,
                accessTokenValiditySeconds: client.accessTokenValiditySeconds,
                refreshTokenValiditySeconds: client.refreshTokenValiditySeconds,
                autoApprove: client.autoApprove

        )
    }

    static class LocalClientDetails implements ClientDetails {
        String clientId

        String clientSecret

        boolean secretRequired

        boolean scoped

        Set<String> scope

        Set<String> resourceIds

        Set<String> authorizedGrantTypes

        Set<String> authorities

        Set<String> registeredRedirectUri

        Integer accessTokenValiditySeconds

        Integer refreshTokenValiditySeconds

        boolean autoApprove

        @Override
        Set<GrantedAuthority> getAuthorities() {
            return authorities.collect {
                new GrantedAuthority() {
                    @Override
                    String getAuthority() {
                        return it
                    }
                }
            } as Set<GrantedAuthority>
        }

        @Override
        boolean isAutoApprove(String scope) {
            return autoApprove
        }

        @Override
        Map<String, Object> getAdditionalInformation() {
            return null
        }
    }
}