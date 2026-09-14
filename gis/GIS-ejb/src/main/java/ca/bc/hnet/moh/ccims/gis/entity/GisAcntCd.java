/*******************************************************************************
 * Copyright © 2015, Province of British Columbia.                             *
 *                                                                             *
 * All rights reserved.                                                        *
 *                                                                             *
 * File:                        GisAcntCd.java                                 *
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
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.NamedQueries;
import jakarta.persistence.NamedQuery;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import jakarta.persistence.Temporal;
import jakarta.persistence.TemporalType;

/**
 *
 * @author Dan.Goulet
 */
@Entity
@Table(name = "GIS_ACNT_CD")
@NamedQueries({@NamedQuery(name = "GisAcntCd.findByAcntCode", query = "SELECT g FROM GisAcntCd g WHERE g.acntCode = :acntCode"), @NamedQuery(name = "GisAcntCd.findByAcntDescription", query = "SELECT g FROM GisAcntCd g WHERE g.acntDescription = :acntDescription"), @NamedQuery(name = "GisAcntCd.findByCreateDatetime", query = "SELECT g FROM GisAcntCd g WHERE g.createDatetime = :createDatetime"), @NamedQuery(name = "GisAcntCd.findByCreateUser", query = "SELECT g FROM GisAcntCd g WHERE g.createUser = :createUser"), @NamedQuery(name = "GisAcntCd.findByLastModifiedDatetime", query = "SELECT g FROM GisAcntCd g WHERE g.lastModifiedDatetime = :lastModifiedDatetime"), @NamedQuery(name = "GisAcntCd.findByLastModifiedUser", query = "SELECT g FROM GisAcntCd g WHERE g.lastModifiedUser = :lastModifiedUser")})
public class GisAcntCd implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @Column(name = "ACNT_CODE", nullable = false)
    private String acntCode;
    @Column(name = "ACNT_DESCRIPTION")
    private String acntDescription;
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
   
    public GisAcntCd() {
    }

    public GisAcntCd(String acntCode) {
        this.acntCode = acntCode;
    }

    public String getAcntCode() {
        return acntCode;
    }

    public void setAcntCode(String acntCode) {
        this.acntCode = acntCode;
    }

    public String getAcntDescription() {
        return acntDescription;
    }

    public void setAcntDescription(String acntDescription) {
        this.acntDescription = acntDescription;
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
        hash += (acntCode != null ? acntCode.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof GisAcntCd)) {
            return false;
        }
        GisAcntCd other = (GisAcntCd) object;
        if ((this.acntCode == null && other.acntCode != null) || (this.acntCode != null && !this.acntCode.equals(other.acntCode))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "ca.bc.hnet.moh.ccims.gis.entity.GisAcntCd[acntCode=" + acntCode + "]";
    }

}
