/*******************************************************************************
 * Copyright © 2015, Province of British Columbia.                             *
 *                                                                             *
 * All rights reserved.                                                        *
 *                                                                             *
 * File:                        GisRecipients.java                             *
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
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.NamedQueries;
import jakarta.persistence.NamedQuery;
import jakarta.persistence.Table;

/**
 *
 * @author Dan.Goulet
 */
@Entity
@Table(name = "GIS_RECIPIENTS")
@NamedQueries({@NamedQuery(name = "GisRecipients.findById", query = "SELECT g FROM GisRecipients g WHERE g.id = :id"), @NamedQuery(name = "GisRecipients.findByLoadId", query = "SELECT g FROM GisRecipients g WHERE g.loadId = :loadId"), @NamedQuery(name = "GisRecipients.findByAccountId", query = "SELECT g FROM GisRecipients g WHERE g.accountId = :accountId"), @NamedQuery(name = "GisRecipients.findByRcptSurname", query = "SELECT g FROM GisRecipients g WHERE g.rcptSurname = :rcptSurname"), @NamedQuery(name = "GisRecipients.findByRcptGivenname", query = "SELECT g FROM GisRecipients g WHERE g.rcptGivenname = :rcptGivenname"), @NamedQuery(name = "GisRecipients.findByBirthdate", query = "SELECT g FROM GisRecipients g WHERE g.birthdate = :birthdate"), @NamedQuery(name = "GisRecipients.findByAcntStatusCode", query = "SELECT g FROM GisRecipients g WHERE g.acntStatusCode = :acntStatusCode"), @NamedQuery(name = "GisRecipients.findByAcntCode", query = "SELECT g FROM GisRecipients g WHERE g.acntCode = :acntCode"), @NamedQuery(name = "GisRecipients.findByEntlmntDate", query = "SELECT g FROM GisRecipients g WHERE g.entlmntDate = :entlmntDate"), @NamedQuery(name = "GisRecipients.findByPayDate", query = "SELECT g FROM GisRecipients g WHERE g.payDate = :payDate"), @NamedQuery(name = "GisRecipients.findByFinalPayDate", query = "SELECT g FROM GisRecipients g WHERE g.finalPayDate = :finalPayDate"), @NamedQuery(name = "GisRecipients.findByAddress1", query = "SELECT g FROM GisRecipients g WHERE g.address1 = :address1"), @NamedQuery(name = "GisRecipients.findByAddress2", query = "SELECT g FROM GisRecipients g WHERE g.address2 = :address2"), @NamedQuery(name = "GisRecipients.findByAddress3", query = "SELECT g FROM GisRecipients g WHERE g.address3 = :address3"), @NamedQuery(name = "GisRecipients.findByAddress4", query = "SELECT g FROM GisRecipients g WHERE g.address4 = :address4"), @NamedQuery(name = "GisRecipients.findByPostalCode", query = "SELECT g FROM GisRecipients g WHERE g.postalCode = :postalCode"), @NamedQuery(name = "GisRecipients.findByMarStatusCode", query = "SELECT g FROM GisRecipients g WHERE g.marStatusCode = :marStatusCode"), @NamedQuery(name = "GisRecipients.findBySpouseActId", query = "SELECT g FROM GisRecipients g WHERE g.spouseActId = :spouseActId"), @NamedQuery(name = "GisRecipients.findBySpouseGivenName", query = "SELECT g FROM GisRecipients g WHERE g.spouseGivenName = :spouseGivenName"), @NamedQuery(name = "GisRecipients.findByImsStrtDt", query = "SELECT g FROM GisRecipients g WHERE g.imsStrtDt = :imsStrtDt"), @NamedQuery(name = "GisRecipients.findByLastUpdateDate", query = "SELECT g FROM GisRecipients g WHERE g.lastUpdateDate = :lastUpdateDate")})
public class GisRecipients implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @Column(name = "ID", nullable = false)
    private BigDecimal id;
    @Column(name = "LOAD_ID")
    private String loadId;
    @Column(name = "ACCOUNT_ID")
    private String accountId;
    @Column(name = "RCPT_SURNAME")
    private String rcptSurname;
    @Column(name = "RCPT_GIVENNAME")
    private String rcptGivenname;
    @Column(name = "BIRTHDATE")
    private String birthdate;
    @Column(name = "ACNT_STATUS_CODE")
    private String acntStatusCode;
    @Column(name = "ACNT_STATUS_DESC")
    private String acntStatusDesc;
    @Column(name = "ACNT_CODE")
    private String acntCode;
    @Column(name = "ACNT_DESCRIPTION")
    private String acntDescription;
    @Column(name = "ENTLMNT_DATE")
    private String entlmntDate;
    @Column(name = "PAY_DATE")
    private String payDate;
    @Column(name = "FINAL_PAY_DATE")
    private String finalPayDate;
    @Column(name = "ADDRESS1")
    private String address1;
    @Column(name = "ADDRESS2")
    private String address2;
    @Column(name = "ADDRESS3")
    private String address3;
    @Column(name = "ADDRESS4")
    private String address4;
    @Column(name = "POSTAL_CODE")
    private String postalCode;
    @Column(name = "MAR_STATUS_CODE")
    private String marStatusCode;
    @Column(name = "MAR_STATUS_DESCRIPTION")
    private String marStatusDescription;
    @Column(name = "SPOUSE_ACT_ID")
    private String spouseActId;
    @Column(name = "SPOUSE_GIVEN_NAME")
    private String spouseGivenName;
    @Column(name = "IMS_STRT_DT")
    private String imsStrtDt;
    @Column(name = "LAST_UPDATE_DATE")
    //@Temporal(TemporalType.TIMESTAMP)
    private String lastUpdateDate;
    @Column(name = "ROWNUM")
    private BigInteger rownum;
    
    public GisRecipients() {
        loadId = new String();
        accountId = new String();
        rcptSurname = new String();
        rcptGivenname = new String();
        birthdate = new String();
    }

    public GisRecipients(BigDecimal id) {
        this.id = id;
    }

    public BigDecimal getId() {
        return id;
    }

    public void setId(BigDecimal id) {
        this.id = id;
    }

    public String getLoadId() {
        return loadId;
    }

    public void setLoadId(String loadId) {
        this.loadId = loadId;
    }

    public String getAccountId() {
        return accountId;
    }

    public void setAccountId(String accountId) {
        this.accountId = accountId;
    }

    public String getRcptSurname() {
        return rcptSurname;
    }

    public void setRcptSurname(String rcptSurname) {
        this.rcptSurname = rcptSurname;
    }

    public String getRcptGivenname() {
        return rcptGivenname;
    }

    public void setRcptGivenname(String rcptGivenname) {
        this.rcptGivenname = rcptGivenname;
    }

    public String getBirthdate() {
        return birthdate;
    }

    public void setBirthdate(String birthdate) {
        this.birthdate = birthdate;
    }

    public String getAcntStatusCode() {
        return acntStatusCode;
    }

    public void setAcntStatusCode(String acntStatusCode) {
        this.acntStatusCode = acntStatusCode;
    }

    public String getAcntCode() {
        return acntCode;
    }

    public void setAcntCode(String acntCode) {
        this.acntCode = acntCode;
    }

    public String getEntlmntDate() {
        return entlmntDate;
    }

    public void setEntlmntDate(String entlmntDate) {
        this.entlmntDate = entlmntDate;
    }

    public String getPayDate() {
        return payDate;
    }

    public void setPayDate(String payDate) {
        this.payDate = payDate;
    }

    public String getFinalPayDate() {
        return finalPayDate;
    }

    public void setFinalPayDate(String finalPayDate) {
        this.finalPayDate = finalPayDate;
    }

    public String getAddress1() {
        return address1;
    }

    public void setAddress1(String address1) {
        this.address1 = address1;
    }

    public String getAddress2() {
        return address2;
    }

    public void setAddress2(String address2) {
        this.address2 = address2;
    }

    public String getAddress3() {
        return address3;
    }

    public void setAddress3(String address3) {
        this.address3 = address3;
    }

    public String getAddress4() {
        return address4;
    }

    public void setAddress4(String address4) {
        this.address4 = address4;
    }

    public String getPostalCode() {
        return postalCode;
    }

    public void setPostalCode(String postalCode) {
        this.postalCode = postalCode;
    }

    public String getMarStatusCode() {
        return marStatusCode;
    }

    public void setMarStatusCode(String marStatusCode) {
        this.marStatusCode = marStatusCode;
    }

    public String getSpouseActId() {
        return spouseActId;
    }

    public void setSpouseActId(String spouseActId) {
        this.spouseActId = spouseActId;
    }

    public String getSpouseGivenName() {
        return spouseGivenName;
    }

    public void setSpouseGivenName(String spouseGivenName) {
        this.spouseGivenName = spouseGivenName;
    }

    public String getImsStrtDt() {
        return imsStrtDt;
    }

    public void setImsStrtDt(String imsStrtDt) {
        this.imsStrtDt = imsStrtDt;
    }

    public String getLastUpdateDate() {
        return lastUpdateDate;
    }

    public void setLastUpdateDate(String lastUpdateDate) {
        this.lastUpdateDate = lastUpdateDate;
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
        if (!(object instanceof GisRecipients)) {
            return false;
        }
        GisRecipients other = (GisRecipients) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }
    
    @Override
    public Object clone() {

        GisRecipients recipient = new GisRecipients();
        recipient.id = id;

        // Only fields modifiable from the User interface are used for deep cloning
        recipient.loadId = loadId != null ? new String(loadId) : loadId;
        recipient.accountId = accountId != null ? new String(accountId) : accountId;
        recipient.rcptSurname = rcptSurname != null ? new String(rcptSurname) : rcptSurname;
        recipient.rcptGivenname = rcptGivenname != null ? new String(rcptGivenname) : rcptGivenname;
        recipient.birthdate = birthdate != null ? new String(birthdate) : birthdate;

        recipient.acntStatusCode = acntStatusCode;
        recipient.acntStatusDesc = acntStatusDesc;
        recipient.acntCode = acntCode;
        recipient.acntDescription = acntDescription;
        recipient.entlmntDate = entlmntDate;
        recipient.payDate = payDate;
        recipient.finalPayDate = finalPayDate;
        recipient.address1 = address1;
        recipient.address2 = address2;
        recipient.address3 = address3;
        recipient.address4 = address4;
        recipient.postalCode = postalCode;
        recipient.marStatusCode = marStatusCode;
        recipient.marStatusDescription = marStatusDescription;
        recipient.spouseActId = spouseActId;
        recipient.spouseGivenName = spouseGivenName;
        recipient.imsStrtDt = imsStrtDt;
        recipient.lastUpdateDate = lastUpdateDate;
        recipient.rownum = rownum;


        return recipient;
    }

    @Override
    public String toString() {
        return "ca.bc.hnet.moh.ccims.gis.entity.GisRecipients[id=" + id + "]";
    }

    public String getAcntDescription() {
        return acntDescription;
    }

    public void setAcntDescription(String acntDescription) {
        this.acntDescription = acntDescription;
    }

    public String getAcntStatusDesc() {
        return acntStatusDesc;
    }

    public void setAcntStatusDesc(String acntStatusDesc) {
        this.acntStatusDesc = acntStatusDesc;
    }

    public String getMarStatusDescription() {
        return marStatusDescription;
    }

    public void setMarStatusDescription(String marStatusDescription) {
        this.marStatusDescription = marStatusDescription;
    }

    public BigInteger getRownum() {
        return rownum;
    }

    public void setRownum(BigInteger rownum) {
        this.rownum = rownum;
    }
}
