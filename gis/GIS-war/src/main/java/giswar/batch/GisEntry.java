/*******************************************************************************
 * Copyright © 2015, Province of British Columbia.                             *
 *                                                                             *
 * All rights reserved.                                                        *
 *                                                                             *
 * File:                        GisEntry.java                                  *
 * Date of Last Commit: $Date::                                              $ *
 * Revision Number:      $Rev::                                              $ *
 * Last Commit by:    $Author::                                              $ *
 *                                                                             *
 *******************************************************************************/

/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package giswar.batch;

import java.util.Calendar;
import java.util.Date;

/**
 *
 * @author grant.shan
 */
public class GisEntry {

    private int recordType;
    private String accountId;
    private String surname;
    private String givenname;
    private String brithDate;//yyyymm
    private String accountStatusCode;
    private String accountCode;
    private String entitlementDate;  //yyyymm
    private String payDate;  //yyyymm
    private String finalPayDate;  //yyyymm
    private String address1;
    private String address2;
    private String address3;
    private String address4;
    private String postalCode;
    private String maritalStatusCode;
    private String spouseAccountId;
    private String spouseGivenName;
    private String imsStartDate; //yyyymm
    private Date lastUpdateDate; //yyyymm

    public String getAccountCode() {
        return accountCode;
    }

    public void setAccountCode(String accountCode) {
        this.accountCode = accountCode;
    }

    public String getAccountStatusCode() {
        return accountStatusCode;
    }

    public void setAccountStatusCode(String accountStatusCode) {
        //Account status code is 1 (A-Active) or 6 (D-Deceased) on the
        //Service Canada file but after merging with CCIMS database a third code
        //is created (Unknown).  Web Tool needs to account for all three accounts status codes and re-create a process for an account status code of “U - Unknown”.

        this.accountStatusCode = "U";
        if (accountStatusCode != null && accountStatusCode.equals("1")) {
            this.accountStatusCode = "A";
        } else if (accountStatusCode != null && accountStatusCode.equals("6")) {
            this.accountStatusCode = "D";
        }
    }

    public String getAccountId() {
        return accountId;
    }

    public void setAccountId(String accountId) {
        //If the ID is the SIN, the GIS-recipient-account-code is set to “S”,
        //otherwise it is set to “A”

        this.accountId = accountId;
    }

    /**
     * method to get teh Accoutn Code
     *
     * @param accoutId  Account ID
     * @param sin  SIN Number
     * @return
     */
    public void setAccountCode(String accoutId, String sin) {
        //If the ID is the SIN, the GIS-recipient-account-code is set to “S”,
        //otherwise it is set to “A”
        if (accountId != null && accountId.equals(sin)) {
            this.accountCode = "S";
        } else {
            this.accountCode = "A";
        }
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

    public String getBrithDate() {
        return brithDate;
    }

    public void setBrithDate(String brithDate) {
        this.brithDate = "19" + brithDate;
    }

    public String getEntitlementDate() {
        return entitlementDate;
    }

    public void setEntitlementDate(String entitlementDate) {
        // 50-99 use 1900
        // 00-49 use 2000
        this.entitlementDate = getY2kCompliantDate(entitlementDate, 50);
    }

    public String getFinalPayDate() {
        return finalPayDate;
    }

    public void setFinalPayDate(String finalPayDate) {
        this.finalPayDate = finalPayDate;
    }

    public String getGivenname() {
        return givenname;
    }

    public void setGivenname(String givenname) {
        this.givenname = givenname;
    }

    public String getImsStartDate() {
        return imsStartDate;
    }

    public void setImsStartDate(String imsStartDate) {
        this.imsStartDate = imsStartDate;
    }

    public Date getLastUpdateDate() {
        return lastUpdateDate;
    }

    public void setLastUpdateDate(Date lastUpdateDate) {
        this.lastUpdateDate = lastUpdateDate;
    }

    public String getMaritalStatusCode() {
        return maritalStatusCode;
    }

    public void setMaritalStatusCode(String maritalStatusCode) {
        this.maritalStatusCode = maritalStatusCode;
    }

    public String getPayDate() {
        return payDate;
    }

    public void setPayDate(String payDate) {
        this.payDate = payDate;
    }

    public String getPostalCode() {
        return postalCode;
    }

    public void setPostalCode(String postalCode) {
        this.postalCode = postalCode;
    }

    public int getRecordType() {
        return recordType;
    }

    public void setRecordType(int recordType) {
        this.recordType = recordType;
    }

    public String getSpouseAccountId() {
        return spouseAccountId;
    }

    public void setSpouseAccountId(String spouseAccountId) {
        this.spouseAccountId = spouseAccountId;
    }

    public String getSpouseGivenName() {
        return spouseGivenName;
    }

    public void setSpouseGivenName(String spouseGivenName) {
        this.spouseGivenName = spouseGivenName;
    }

    public String getSurname() {
        return surname;
    }

    public void setSurname(String surname) {
        this.surname = surname;
    }

    public void setName(String surnameId, String name) {
        surnameId = surnameId == null ? surnameId : surnameId.trim();
        if (name != null && name.lastIndexOf(surnameId) > 0) {
            int lastNameIndex = name.lastIndexOf(surnameId);
            surname = name.substring(lastNameIndex).trim();
            givenname = name.substring(0, lastNameIndex > 15 ? 15 : lastNameIndex).trim();
            if (givenname != null && (givenname.startsWith("MR ") || givenname.startsWith("MRS "))) {
                givenname = givenname.substring(givenname.indexOf(" ") + 1);
            }

        }
    }

    public void setEntry(String recordType1, String recordType2) {
        setAccountId(recordType1.substring(3, 12));
        setAccountCode(getAccountId(), recordType1.substring(198, 207));
        setName(recordType1.substring(12, 17), recordType1.substring(17, 59));
        setBrithDate(recordType1.substring(234, 238));
        setAccountStatusCode(recordType1.substring(63, 64));
        setEntitlementDate(recordType1.substring(70, 74));
        //For Address:
        //The ‘2’ record is used when the ‘there’ is a second record and 
        //GISRT1-ACNT-CD = 7. Otherwise you will use the existing address that is on record ‘1’.
        String accountType = recordType1.substring(60, 61);
        if (recordType2 != null && "7".equals(accountType)) {
            setAddress1(recordType2.substring(12, 42));
            setAddress2(recordType2.substring(42, 72));
            setAddress3(recordType2.substring(72, 102));
            setAddress4(recordType2.substring(102, 118));
            setPostalCode(recordType2.substring(118, 124));
        } else {
            setAddress1(recordType1.substring(84, 114));
            setAddress2(recordType1.substring(114, 144));
            setAddress3(recordType1.substring(144, 174));
            setAddress4(recordType1.substring(174, 190));
            setPostalCode(recordType1.substring(190, 196));
        }
        
        setMaritalStatusCode(recordType1.substring(230, 231));
        setSpouseAccountId(recordType1.substring(219, 228));
        setSpouseGivenName(recordType1.substring(207, 219));
    }

    public String getY2kCompliantDate(String monthYear, int sepaYear) {

        if (monthYear == null || monthYear.length() != 4) {
            return monthYear;
        }

        try {
            String yearStr = monthYear.substring(2, 4);
            int year = Integer.parseInt(yearStr);

            if (year <= sepaYear) {
                return "20" + yearStr + monthYear.substring(0, 2);
            } else {
                return "19" + yearStr + monthYear.substring(0, 2);
            }
        } catch (NumberFormatException e) {
            return monthYear;
        }

    }

    private int getCurrentYear() {
        Calendar cal = Calendar.getInstance();
        return Integer.parseInt(String.valueOf(cal.get(Calendar.YEAR)).substring(2));
    }

    @Override
    public String toString() {
        return "accountId = " + accountId + ";" +
                "surname = " + surname + ";" +
                "givenname = " + givenname + ";" +
                "brithDate = " + brithDate + ";" +
                "accountStatusCode = " + accountStatusCode + ";" +
                "accountCode = " + accountCode + ";" +
                "entitlementDate = " + entitlementDate + ";" +
                "payDate = " + payDate + ";" +
                "finalPayDate = " + finalPayDate + ";" +
                "address1 = " + address1 + ";" +
                "address2 = " + address2 + ";" +
                "address3 = " + address3 + ";" +
                "address4 = " + address4 + ";" +
                "postalCode = " + postalCode + ";" +
                "maritalStatusCode = " + maritalStatusCode + ";" +
                "spouseAccountId = " + spouseAccountId + ";" +
                "spouseGivenName = " + spouseGivenName + ";" +
                "imsStartDate = " + imsStartDate + ";" +
                "lastUpdateDate = " + lastUpdateDate;
    }
}
