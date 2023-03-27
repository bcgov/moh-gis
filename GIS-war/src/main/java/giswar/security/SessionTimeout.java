package giswar.security;

import java.io.IOException;
import javax.enterprise.context.RequestScoped;
import javax.faces.context.FacesContext;
import javax.inject.Named;

@RequestScoped
@Named("SessionTimeout")
public class SessionTimeout {

    public void extendSession() {
        // This method doesn't do anything we just call it extend user sessions 
    }

    public void endSession() throws IOException {
        FacesContext.getCurrentInstance().getExternalContext().invalidateSession();        
    }
}
