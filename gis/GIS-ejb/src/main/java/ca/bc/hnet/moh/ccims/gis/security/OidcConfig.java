package ca.bc.hnet.moh.ccims.gis.security;

import java.util.Properties;
import jakarta.annotation.PostConstruct;
import jakarta.annotation.Resource;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Named;

/**
 *
 * @author nathaniel.coster
 */
@Named
@ApplicationScoped
public class OidcConfig {
    
    @Resource(lookup = "java:app/gis/oidc_properties")
    Properties oidcProperties;
    
    private String providerUri;
    private String clientId;
    private String clientSecret;
    private String redirectUri;
    private String scope;
    private String callerGroupsClaim;
    private String callerNameClaim;
    private String siteminderLogoutUri;
    private String phsaLogoutUri;
    
    @PostConstruct
    private void init() {
        
        providerUri = oidcProperties.getProperty("providerUri");
        clientId = oidcProperties.getProperty("clientId");
        clientSecret = oidcProperties.getProperty("clientSecret");
        redirectUri = oidcProperties.getProperty("redirectUri");
        scope = oidcProperties.getProperty("scope");
        callerGroupsClaim = oidcProperties.getProperty("callerGroupsClaim");
        callerNameClaim = oidcProperties.getProperty("callerNameClaim");
        siteminderLogoutUri = oidcProperties.getProperty("siteminderLogoutUri");
        phsaLogoutUri = oidcProperties.getProperty("phsaLogoutUri");
    }

    public String getProviderUri() {
        return providerUri;
    }

    public String getClientId() {
        return clientId;
    }

    public String getClientSecret() {
        return clientSecret;
    }

    public String getRedirectUri() {
        return redirectUri;
    }

    public String getScope() {
        return scope;
    }

    public String getCallerGroupsClaim() {
        return callerGroupsClaim;
    }

    public String getCallerNameClaim() {
        return callerNameClaim;
    }    

    public String getSiteminderLogoutUri() {
        return siteminderLogoutUri;
    }
    
    public String getPhsaLogoutUri() {
        return phsaLogoutUri;
    }
    
}
