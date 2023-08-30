/*******************************************************************************
 * Copyright © 2015, Province of British Columbia.                             *
 *                                                                             *
 * All rights reserved.                                                        *
 *                                                                             *
 * File:                        GisAcntStatusCd.java                           *
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
@Table(name = "GIS_ACNT_STATUS_CD")
@NamedQueries({@NamedQuery(name = "GisAcntStatusCd.findByAcntStatusCode", query = "SELECT g FROM GisAcntStatusCd g WHERE g.acntStatusCode = :acntStatusCode"), @NamedQuery(name = "GisAcntStatusCd.findByAcntStatusDesc", query = "SELECT g FROM GisAcntStatusCd g WHERE g.acntStatusDesc = :acntStatusDesc"), @NamedQuery(name = "GisAcntStatusCd.findByCreateDatetime", query = "SELECT g FROM GisAcntStatusCd g WHERE g.createDatetime = :createDatetime"), @NamedQuery(name = "GisAcntStatusCd.findByCreateUser", query = "SELECT g FROM GisAcntStatusCd g WHERE g.createUser = :createUser"), @NamedQuery(name = "GisAcntStatusCd.findByLastModifiedDatetime", query = "SELECT g FROM GisAcntStatusCd g WHERE g.lastModifiedDatetime = :lastModifiedDatetime"), @NamedQuery(name = "GisAcntStatusCd.findByLastModifiedUser", query = "SELECT g FROM GisAcntStatusCd g WHERE g.lastModifiedUser = :lastModifiedUser")})
public class GisAcntStatusCd implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @Column(name = "ACNT_STATUS_CODE", nullable = false)
    private String acntStatusCode;
    @Column(name = "ACNT_STATUS_DESC")
    private String acntStatusDesc;
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

    public GisAcntStatusCd() {
    }

    public GisAcntStatusCd(String acntStatusCode) {
        this.acntStatusCode = acntStatusCode;
    }

    public String getAcntStatusCode() {
        return acntStatusCode;
    }

    public void setAcntStatusCode(String acntStatusCode) {
        this.acntStatusCode = acntStatusCode;
    }

    public String getAcntStatusDesc() {
        return acntStatusDesc;
    }

    public void setAcntStatusDesc(String acntStatusDesc) {
        this.acntStatusDesc = acntStatusDesc;
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
        hash += (acntStatusCode != null ? acntStatusCode.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof GisAcntStatusCd)) {
            return false;
        }
        GisAcntStatusCd other = (GisAcntStatusCd) object;
        if ((this.acntStatusCode == null && other.acntStatusCode != null) || (this.acntStatusCode != null && !this.acntStatusCode.equals(other.acntStatusCode))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "ca.bc.hnet.moh.ccims.gis.entity.GisAcntStatusCd[acntStatusCode=" + acntStatusCode + "]";
    }

}
