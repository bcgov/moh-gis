/*******************************************************************************
 * Copyright © 2015, Province of British Columbia.                             *
 *                                                                             *
 * All rights reserved.                                                        *
 *                                                                             *
 * File:                        GisMaritalStatusCd.java                        *
 * Date of Last Commit: $Date::                                              $ *
 * Revision Number:      $Rev::                                              $ *
 * Last Commit by:    $Author::                                              $ *
 *                                                                             *
 *******************************************************************************/

/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package ca.bc.hnet.moh.ccims.gis.entity;

import java.io.Serializable;
import java.util.Collection;
import java.util.Date;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

/**
 *
 * @author Dan.Goulet
 */
@Entity
@Table(name = "GIS_MARITAL_STATUS_CD")
@NamedQueries({@NamedQuery(name = "GisMaritalStatusCd.findByMarStatusCode", query = "SELECT g FROM GisMaritalStatusCd g WHERE g.marStatusCode = :marStatusCode"), @NamedQuery(name = "GisMaritalStatusCd.findByMarStatusDescription", query = "SELECT g FROM GisMaritalStatusCd g WHERE g.marStatusDescription = :marStatusDescription"), @NamedQuery(name = "GisMaritalStatusCd.findByCreateDatetime", query = "SELECT g FROM GisMaritalStatusCd g WHERE g.createDatetime = :createDatetime"), @NamedQuery(name = "GisMaritalStatusCd.findByCreateUser", query = "SELECT g FROM GisMaritalStatusCd g WHERE g.createUser = :createUser"), @NamedQuery(name = "GisMaritalStatusCd.findByLastModifiedDatetime", query = "SELECT g FROM GisMaritalStatusCd g WHERE g.lastModifiedDatetime = :lastModifiedDatetime"), @NamedQuery(name = "GisMaritalStatusCd.findByLastModifiedUser", query = "SELECT g FROM GisMaritalStatusCd g WHERE g.lastModifiedUser = :lastModifiedUser")})
public class GisMaritalStatusCd implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @Column(name = "MAR_STATUS_CODE", nullable = false)
    private String marStatusCode;
    @Column(name = "MAR_STATUS_DESCRIPTION")
    private String marStatusDescription;
    @Column(name = "CREATE_DATETIME")
    @Temporal(TemporalType.TIMESTAMP)
    private Date createDatetime;
    @Column(name = "CREATE_USER")
    private String createUser;
    @Column(name = "LAST_MODIFIED_DATETIME")
    @Temporal(TemporalType.TIMESTAMP)
    private Date lastModifiedDatetime;
    @Column(name = "LAST_MODIFIED_USER")
    private String lastModifiedUser;
   
    public GisMaritalStatusCd() {
    }

    public GisMaritalStatusCd(String marStatusCode) {
        this.marStatusCode = marStatusCode;
    }

    public String getMarStatusCode() {
        return marStatusCode;
    }

    public void setMarStatusCode(String marStatusCode) {
        this.marStatusCode = marStatusCode;
    }

    public String getMarStatusDescription() {
        return marStatusDescription;
    }

    public void setMarStatusDescription(String marStatusDescription) {
        this.marStatusDescription = marStatusDescription;
    }

    public Date getCreateDatetime() {
        return createDatetime;
    }

    public void setCreateDatetime(Date createDatetime) {
        this.createDatetime = createDatetime;
    }

    public String getCreateUser() {
        return createUser;
    }

    public void setCreateUser(String createUser) {
        this.createUser = createUser;
    }

    public Date getLastModifiedDatetime() {
        return lastModifiedDatetime;
    }

    public void setLastModifiedDatetime(Date lastModifiedDatetime) {
        this.lastModifiedDatetime = lastModifiedDatetime;
    }

    public String getLastModifiedUser() {
        return lastModifiedUser;
    }

    public void setLastModifiedUser(String lastModifiedUser) {
        this.lastModifiedUser = lastModifiedUser;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (marStatusCode != null ? marStatusCode.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof GisMaritalStatusCd)) {
            return false;
        }
        GisMaritalStatusCd other = (GisMaritalStatusCd) object;
        if ((this.marStatusCode == null && other.marStatusCode != null) || (this.marStatusCode != null && !this.marStatusCode.equals(other.marStatusCode))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "ca.bc.hnet.moh.ccims.gis.entity.GisMaritalStatusCd[marStatusCode=" + marStatusCode + "]";
    }

}
