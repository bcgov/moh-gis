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

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Properties;

import javax.sql.DataSource;

import java.util.logging.Level;
import java.util.logging.Logger;
/**
 *
 * @author grant.shan
 */
public class GisEntry{

    private int recordType;
    private String accountId;
    private String givenname;
    private String middlename;
    private String surname;
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
    private static final Logger logger = Logger.getLogger(GisEntry.class.getName());

    public String getAccountCode() {
        return accountCode;
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

    public void setAccountId(String clientID, String SIN, IDatabase db, DataSource ds) {
        // Populate SIN if it exists
        if (SIN != null && SIN != "000000000") {
            this.accountId = SIN;
            setAccountCode("S");
        } else {
            // query db for matching client
            Properties prop = new Properties();
            List<String[]> batch = new ArrayList<String[]>(3);
            batch.add(new String[]{this.givenname, this.middlename, this.surname, this.brithDate});

            prop.put(BatchConstants.DB_DATASOURCE, ds);
            prop.put(BatchConstants.QUERY, 
                "SELECT * FROM gis.gis_recipients WHERE rcpt_givenname=? AND rcpt_middlename=? AND rcpt_surname=? AND birthdate=?");
            prop.put(BatchConstants.PARAMETERS, batch);
            prop.put(BatchConstants.STATEMENT_TYPE, IDatabase.STATEMENT_TYPE.SELECT);
            
            Properties response = null;
            try {
                response = db.execute(prop);

                if (response != null && response.containsKey(BatchConstants.ERROR)) {
                    List<String> errors = (List<String>) response.get(BatchConstants.ERROR);

                    for (String s : errors) {
                        System.out.println(s);
                    }
                }

            } catch (DatabaseException dbe) {
                System.out.println(String.format("Data load error: %1$s", dbe.getMessage()));
            }

            List<String[]> resultSet = (List<String[]>) response.get("resultSet"); 
            if (resultSet.size() > 0){
                // should not get here but added the check just in case
                if(resultSet.size() > 1){
                    logger.log(Level.SEVERE, "Multiple users returned when only one should have");
                }
                // if account code is "O" then its an OAS account
                if(resultSet.get(0)[7].equals("O")){
                    // set accountId to clientID if it exists else 000000000000
                    if(clientID != null && clientID != "000000000000"){
                        this.accountId = clientID;
                    } else {
                        this.accountId = "000000000000";
                    }
                    setAccountCode("O");
                // if account code is "S" then its a SIN account
                } else if(resultSet.get(0)[7].equals("S")){
                    // set accountId to SIN if it exists else 000000000
                    if(resultSet.get(0)[2] != null && resultSet.get(0)[2] != "000000000"){
                        this.accountId = resultSet.get(0)[2];
                    } else {
                        this.accountId = "000000000";
                    }
                    setAccountCode("S");
                // if account code is "A" then its an IA account
                } else {
                    // set accountId to IA account id if it exists else 000000000
                    if(resultSet.get(0)[2] != null && resultSet.get(0)[2] != "000000000"){
                        this.accountId = resultSet.get(0)[2];
                    } else {
                        this.accountId = "000000000";
                    }
                    setAccountCode("A");
                }
            } else {
                // if no matching client is found then we assume its a new OAS account so we set accountId to clientID if it exists else 000000000000
                if(clientID != null && clientID != "000000000000"){
                    this.accountId = clientID;
                } else {
                    this.accountId = "000000000000";
                }
                setAccountCode("O");
            }
        }
    }

    /**
     * method to get the Account Code
     *
     * @param code  account code
     * @return
     */
    public void setAccountCode(String code) {
        // S for SIN
        // A for IA Account Number
        // O for OAS Account Number 
        this.accountCode = code;
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
        this.brithDate = brithDate;
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

    public String getMiddlename() {
        return middlename;
    }

    public void setMiddleName(String middlename) {
        this.middlename = middlename;
    }

    public String getSurname() {
        return surname;
    }

    public void setSurname(String surname) {
        this.surname = surname;
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

    public void setName(String givenname, String middlename, String surname) {
        setGivenname(givenname.trim());
        setMiddleName(middlename.trim());
        setSurname(surname.trim());
    }

    public void setEntry(String recordType1, String recordType2, IDatabase db, DataSource ds) {
        setName(recordType1.substring(17, 82), recordType1.substring(82, 147), recordType1.substring(147, 212));
        setBrithDate(recordType1.substring(387, 393));
        setAccountId(recordType1.substring(3, 15), recordType1.substring(351, 360), db, ds);
        setAccountStatusCode(recordType1.substring(216, 217));
        setEntitlementDate(recordType1.substring(223, 227));
        //For Address:
        //The ‘2’ record is used when the ‘there’ is a second record and 
        //GISRT1-ACNT-CD = 7. Otherwise you will use the existing address that is on record ‘1’.
        String accountType = recordType1.substring(211, 213);
        if (recordType2 != null && "7".equals(accountType)) {
            setAddress1(recordType2.substring(12, 42));
            setAddress2(recordType2.substring(42, 72));
            setAddress3(recordType2.substring(72, 102));
            setAddress4(recordType2.substring(102, 118));
            setPostalCode(recordType2.substring(118, 124));
        } else {
            setAddress1(recordType1.substring(237, 267));
            setAddress2(recordType1.substring(267, 297));
            setAddress3(recordType1.substring(297, 327));
            setAddress4(recordType1.substring(327, 343));
            setPostalCode(recordType1.substring(343, 349));
        }
        
        setMaritalStatusCode(recordType1.substring(383, 384));
        setSpouseAccountId(recordType1.substring(372, 381));
        setSpouseGivenName(recordType1.substring(360, 372));
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
                "middlename = " + middlename + ";" +
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
