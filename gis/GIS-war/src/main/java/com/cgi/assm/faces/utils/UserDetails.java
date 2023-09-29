package com.cgi.assm.faces.utils;

import jakarta.faces.context.FacesContext;
import jakarta.servlet.http.HttpServletRequest;

public class UserDetails {

    public String getUserName() {
        return FacesContext.getCurrentInstance().getExternalContext().getUserPrincipal().getName();
    }
    
    public String getUserIPAddress() {
        HttpServletRequest request = (HttpServletRequest) FacesContext.getCurrentInstance().getExternalContext().getRequest();
        return request.getRemoteAddr();
    }
    
}
