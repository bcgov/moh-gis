/*******************************************************************************
 * Copyright © 2015, Province of British Columbia.                             *
 *                                                                             *
 * All rights reserved.                                                        *
 *                                                                             *
 * File:                        GisAccessLog.java                              *
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
import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.Date;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.NamedQueries;
import jakarta.persistence.NamedQuery;
import jakarta.persistence.SequenceGenerator;
import jakarta.persistence.Table;
import jakarta.persistence.Temporal;
import jakarta.persistence.TemporalType;

/**
 *
 * @author Dan.Goulet
 */
@Entity
@Table(name = "GIS_ACCESS_LOG")
@NamedQueries({@NamedQuery(name = "GisAccessLog.findById", query = "SELECT g FROM GisAccessLog g WHERE g.id = :id"), @NamedQuery(name = "GisAccessLog.findByUserId", query = "SELECT g FROM GisAccessLog g WHERE g.userId = :userId"), @NamedQuery(name = "GisAccessLog.findByRcptId", query = "SELECT g FROM GisAccessLog g WHERE g.rcptId = :rcptId"), @NamedQuery(name = "GisAccessLog.findByIpAddress", query = "SELECT g FROM GisAccessLog g WHERE g.ipAddress = :ipAddress"), @NamedQuery(name = "GisAccessLog.findByModule", query = "SELECT g FROM GisAccessLog g WHERE g.module = :module"), @NamedQuery(name = "GisAccessLog.findByCreateDts", query = "SELECT g FROM GisAccessLog g WHERE g.createDts = :createDts")})
public class GisAccessLog implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "log_seq_generator")
    @SequenceGenerator(name = "log_seq_generator", sequenceName="log_seq", allocationSize=1)
    @Column(name = "ID", nullable = false)
    private BigDecimal id;
    @Column(name = "USER_ID", nullable = false)
    private String userId;
    @Column(name = "RCPT_ID", nullable = false)
    private BigInteger rcptId;
    @Column(name = "IP_ADDRESS", nullable = false)
    private String ipAddress;
    @Column(name = "MODULE")
    private String module;
    @Column(name = "CREATE_DTS", nullable = false)
    @Temporal(TemporalType.TIMESTAMP)
    private Date createDts;
    
    public GisAccessLog() {
    }

    public GisAccessLog(BigDecimal id) {
        this.id = id;
    }

    public GisAccessLog(BigDecimal id, String userId, BigInteger rcptId, String ipAddress, Date createDts) {
        this.id = id;
        this.userId = userId;
        this.rcptId = rcptId;
        this.ipAddress = ipAddress;
        this.createDts = createDts;
    }

    public BigDecimal getId() {
        return id;
    }

    public void setId(BigDecimal id) {
        this.id = id;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public BigInteger getRcptId() {
        return rcptId;
    }

    public void setRcptId(BigInteger rcptId) {
        this.rcptId = rcptId;
    }

    public String getIpAddress() {
        return ipAddress;
    }

    public void setIpAddress(String ipAddress) {
        this.ipAddress = ipAddress;
    }

    public String getModule() {
        return module;
    }

    public void setModule(String module) {
        this.module = module;
    }

    public Date getCreateDts() {
        return createDts;
    }

    public void setCreateDts(Date createDts) {
        this.createDts = createDts;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (id != null ? id.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof GisAccessLog)) {
            return false;
        }
        GisAccessLog other = (GisAccessLog) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "ca.bc.hnet.moh.ccims.gis.entity.GisAccessLog[id=" + id + "]";
    }

}
