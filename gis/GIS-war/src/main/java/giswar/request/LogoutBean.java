package giswar.request;

import ca.bc.hnet.moh.ccims.gis.security.OidcConfig;
import jakarta.security.enterprise.identitystore.openid.OpenIdContext;
import java.io.IOException;
import java.io.Serializable;
import java.net.URLEncoder;
import jakarta.enterprise.context.RequestScoped;
import jakarta.faces.context.FacesContext;
import jakarta.inject.Inject;
import jakarta.inject.Named;
import jakarta.servlet.ServletException;

/**
 * Logout bean
 *
 * @author CGI Information Management Consultants Inc.
 */
@RequestScoped
@Named("Logout")
public class LogoutBean implements Serializable {

    private static final long serialVersionUID = -2300227033759520822L;

    @Inject
    private OpenIdContext context;

    @Inject
    private OidcConfig oidcConfig;

    /**
     * Method to invalidate user session
     *
     * @throws jakarta.servlet.ServletException
     * @throws java.io.IOException
     */
    public void logout() throws ServletException, IOException {

        String idToken = "";
        if (context.getIdentityToken() != null) {
            idToken = context.getIdentityToken().getToken();
        }
        
        String logoutUrl;
        String keycloakLogoutUrl
                = oidcConfig.getProviderUri()
                + "protocol/openid-connect/logout?post_logout_redirect_uri="
                + URLEncoder.encode(oidcConfig.getRedirectUri(), "UTF-8")
                + "&id_token_hint=" + idToken;

        if (context.getAccessToken() != null) {
            String idp = (String) context.getAccessToken().getClaim("identity_provider");
            if (idp != null) {
                switch (idp) {
                    case "idir":
                        /**
                         * Currently Keycloak does not support logging out of SiteMinder IDP's automatically so we set
                         * the Keycloak Logout redirect_uri= parameter to be the SiteMinder logout and we set the
                         * SiteMinder returl= parameter to be application which chains both logouts for full Single Sign
                         * Out. https://github.com/bcgov/ocp-sso/issues/4
                         */
                        logoutUrl
                                = oidcConfig.getSiteminderLogoutUri()
                                + "?retnow=1&returl="
                                + oidcConfig.getRedirectUri();
                        keycloakLogoutUrl
                                = oidcConfig.getProviderUri()
                                + "protocol/openid-connect/logout?post_logout_redirect_uri="
                                + URLEncoder.encode(logoutUrl, "UTF-8")
                                + "&id_token_hint=" + idToken;
                        break;
                    case "phsa":
                        keycloakLogoutUrl = oidcConfig.getPhsaLogoutUri();
                        break;
                    case "moh_idp":
                    default:
                        break;
                }
            }
        }

        FacesContext.getCurrentInstance().getExternalContext().redirect(keycloakLogoutUrl);
        FacesContext.getCurrentInstance().getExternalContext().invalidateSession();
    }
}
