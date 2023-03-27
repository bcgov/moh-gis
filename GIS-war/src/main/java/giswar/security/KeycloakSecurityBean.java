/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package giswar.security;

import fish.payara.security.annotations.ClaimsDefinition;
import fish.payara.security.annotations.OpenIdAuthenticationDefinition;

/**
 *
 * @author nathaniel.coster
 */
@OpenIdAuthenticationDefinition(
        providerURI = "#{oidcConfig.providerUri}",
        clientId = "#{oidcConfig.clientId}",
        clientSecret = "#{oidcConfig.clientSecret}",
        redirectURI = "#{oidcConfig.redirectUri}",
        scope = "#{oidcConfig.scope}",
        claimsDefinition = @ClaimsDefinition(callerNameClaim = "#{oidcConfig.callerNameClaim}", callerGroupsClaim = "#{oidcConfig.callerGroupsClaim}"),
        /* Default jwksConnectTimeout values can trigger com.nimbusds.jose.RemoteKeySourceException: Couldn't retrieve remote JWK set: connect timed out */
        jwksConnectTimeout = 2000,
        /* Default jwksReadTimeout values can trigger com.nimbusds.jose.RemoteKeySourceException: Couldn't retrieve remote JWK set: Read timed out */
        jwksReadTimeout = 2000,
        extraParameters = {
            "idps_to_show=idir,phsa" //comma separated list of which identity providers show up on the login page. By default all are shown.
        }
)
public class KeycloakSecurityBean {

}
