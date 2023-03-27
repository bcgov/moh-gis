/*******************************************************************************
 * Copyright © 2015, Province of British Columbia.                             *
 *                                                                             *
 * All rights reserved.                                                        *
 *                                                                             *
 * File:                        GisRecipientsFacade.java                       *
 * Date of Last Commit: $Date::                                              $ *
 * Revision Number:      $Rev::                                              $ *
 * Last Commit by:    $Author::                                              $ *
 *                                                                             *
 *******************************************************************************/

/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package ca.bc.hnet.moh.ccims.gis.business;

import ca.bc.hnet.moh.ccims.gis.entity.GisLastLoadDate;
import ca.bc.hnet.moh.ccims.gis.entity.GisRecipients;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.List;
import java.util.Vector;
import java.math.BigDecimal;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

/**
 *
 * @author Dan.Goulet
 *
 * History
 *  August 25, 2010 - John Au - Updated the sin and birth date where clauses in
 *                              the setWhereSql method
 *  September 7, 2010 - John Au - Added the findCount method
 */
@Stateless
public class GisRecipientsFacade
        implements GisRecipientsFacadeLocal {

    @PersistenceContext
    private EntityManager em;

    /**
     * Creates the query from search parameters and returns the result count.
     * This method is used to restrict the search results to prevent 
     * out of memory errors
     *
     * @param sin
     * @param surName
     * @param givenName
     * @param birthDate
     * @param lastUpdate
     * @return
     */
    public int findCount(String sin, String surName,
            String givenName, String birthDate, Calendar lastUpdate) {

        StringBuilder sb = new StringBuilder();
        StringBuilder where = new StringBuilder();

        sb.append(getStaticSqlForCount());

        setWhereSql(where, sin, surName, givenName, birthDate, lastUpdate);
        sb.append(where);

        String query = sb.toString();

        //TODO: remove me
        System.out.println(query);

        Query q = em.createNativeQuery(query);
        
        BigDecimal qr = (BigDecimal)q.getSingleResult();
        
        return qr.intValue();
    }

    public List<GisRecipients> findAll() {
        return em.createQuery("select object(o) from GisRecipients as o").getResultList();
    }

    public String getLastLoadDate() {

        final String SQL = "select * from gis_last_load_vw";

        String result = "";

        try {
            Query q = em.createNativeQuery(SQL, GisLastLoadDate.class);
            GisLastLoadDate lastLoadDate = (GisLastLoadDate) q.getSingleResult();
            result = lastLoadDate.getLastUpdateDate();

        } catch (Exception ex) {
            //swallow
        }

        return result;
    }

    public List<GisRecipients> find(String sin, String surName,
            String givenName, String birthDate, Calendar lastUpdate) {

        StringBuilder sb = new StringBuilder();
        StringBuilder where = new StringBuilder();
        StringBuilder orderBy = new StringBuilder();

        //orderBy.append(" order by row_number");
        orderBy.append(" order by rcpt_surname, " +
                "rcpt_givenname, " +
                "account_id, " +
                "load_id desc)");

        sb.append(getStaticSql());

        setWhereSql(where, sin, surName, givenName, birthDate, lastUpdate);
        sb.append(where);
        sb.append(orderBy);

        String query = sb.toString();
        
        //TODO: remove me
        System.out.println(query);
        
        Query q = em.createNativeQuery(query,
                ca.bc.hnet.moh.ccims.gis.entity.GisRecipients.class);

        return q.getResultList();
    }

    /**
     * Query which returns the recipient count
     *
     * @return
     */
    private String getStaticSqlForCount() {
        String result = "SELECT COUNT(1) " +
                "FROM GIS_RECIPIENTS        GR, " +
                "GIS_MARITAL_STATUS_CD GMS, " +
                "GIS_ACNT_STATUS_CD    GAS, " +
                "GIS_ACNT_CD           GA " +
                "WHERE GMS.MAR_STATUS_CODE = GR.MAR_STATUS_CODE " +
                "AND GAS.ACNT_STATUS_CODE = GR.ACNT_STATUS_CODE " +
                "AND GA.ACNT_CODE = GR.ACNT_CODE";

        return result;
    }

    // <editor-fold defaultstate="collapsed" desc="Private methods">
    private String getStaticSql() {

        //NOTE: I used this static sql statement rather than the existing view
        //  as passing where clause elements as a filter doesn't cause the filter to take
        //  place prior to the view initially executing and this tends to slow things down
        //  if there's a better way of doing this let me know and I'll change it
//        String result =
//                "SELECT GR.ID, " +
//                "( case when nvl(GR.LOAD_ID, null) is not null then substr(GR.LOAD_ID,1,4 )||'-'|| substr(GR.LOAD_ID,5) " +
//                "else '' " +
//                "end ) as LOAD_ID, " +
//                "GR.ACCOUNT_ID, " +
//                "GR.ACNT_CODE, " +
//                "GA.ACNT_DESCRIPTION, " +
//                "GR.ACNT_STATUS_CODE, " +
//                "GAS.ACNT_STATUS_DESC, " +
//                "GR.ADDRESS1, " +
//                "GR.ADDRESS2, " +
//                "GR.ADDRESS3, " +
//                "GR.ADDRESS4, " +
//                "( case when nvl(GR.BIRTHDATE, null) is not null then substr(GR.BIRTHDATE,1,4 )||'-'|| substr(GR.BIRTHDATE,5) " +
//                "else '' " +
//                "end ) as BIRTHDATE, " +
//                "( case when nvl(GR.ENTLMNT_DATE, null) is not null then substr(GR.ENTLMNT_DATE,1,4 )||'-'|| substr(GR.ENTLMNT_DATE,5) " +
//                "else '' " +
//                "end ) as ENTLMNT_DATE, " +
//                "( case when nvl(GR.FINAL_PAY_DATE, null) is not null then substr(GR.FINAL_PAY_DATE,1,4 )||'-'|| substr(GR.FINAL_PAY_DATE,5) " +
//                "else '' " +
//                "end ) as FINAL_PAY_DATE, " +
//                "GR.RCPT_SURNAME, " +
//                "GR.RCPT_GIVENNAME, " +
//                "to_char(GR.LAST_UPDATE_DATE,'YYYY-MM-DD') as LAST_UPDATE_DATE, " +
//                "GR.MAR_STATUS_CODE, " +
//                "GMS.MAR_STATUS_DESCRIPTION, " +
//                "( case when nvl(GR.PAY_DATE, null) is not null then substr(GR.PAY_DATE,1,4 )||'-'|| substr(GR.PAY_DATE,5) " +
//                "else '' " +
//                "end ) as PAY_DATE, " +
//                "( case when nvl(GR.POSTAL_CODE, null) is not null then substr(GR.POSTAL_CODE,1,3 )||' '|| substr(GR.POSTAL_CODE,4) " +
//                "else '' " +
//                "end ) as POSTAL_CODE, " +
//                "GR.SPOUSE_ACT_ID, " +
//                "GR.SPOUSE_GIVEN_NAME, " +
//                "rownum as ROW_NUMBER " +
//                "FROM GIS_RECIPIENTS GR, " +
//                "GIS_MARITAL_STATUS_CD GMS, " +
//                "GIS_ACNT_STATUS_CD GAS, " +
//                "GIS_ACNT_CD GA " +
//                "WHERE GMS.MAR_STATUS_CODE = GR.MAR_STATUS_CODE " +
//                "AND GAS.ACNT_STATUS_CODE = GR.ACNT_STATUS_CODE " +
//                "AND GA.ACNT_CODE = GR.ACNT_CODE ";

        String result = "select ID, " +
                "LOAD_ID, " +
                "ACCOUNT_ID, " +
                "ACNT_CODE, " +
                "ACNT_DESCRIPTION, " +
                "ACNT_STATUS_CODE, " +
                "ACNT_STATUS_DESC, " +
                "ADDRESS1, " +
                "ADDRESS2, " +
                "ADDRESS3, " +
                "ADDRESS4, " +
                "BIRTHDATE, " +
                "ENTLMNT_DATE, " +
                "FINAL_PAY_DATE, " +
                "RCPT_SURNAME, " +
                "RCPT_GIVENNAME, " +
                "LAST_UPDATE_DATE, " +
                "PAY_DATE, " +
                "POSTAL_CODE, " +
                "SPOUSE_ACT_ID, " +
                "SPOUSE_GIVEN_NAME, " +
                "MAR_STATUS_DESCRIPTION, " +
                "rownum " +
                "from( " +
                "SELECT GR.ID, " +
                "(case " +
                "when nvl(GR.LOAD_ID, null) is not null then " +
                "substr(GR.LOAD_ID, 1, 4) || '-' || substr(GR.LOAD_ID, 5) " +
                "else " +
                "'' " +
                "end) as LOAD_ID, " +
                "GR.ACCOUNT_ID, " +
                "GR.ACNT_CODE, " +
                "GA.ACNT_DESCRIPTION, " +
                "GR.ACNT_STATUS_CODE, " +
                "GAS.ACNT_STATUS_DESC, " +
                "GR.ADDRESS1, " +
                "GR.ADDRESS2, " +
                "GR.ADDRESS3, " +
                "GR.ADDRESS4, " +
                "(case " +
                "when nvl(GR.BIRTHDATE, null) is not null then " +
                "substr(GR.BIRTHDATE, 1, 4) || '-' || substr(GR.BIRTHDATE, 5) " +
                "else " +
                "'' " +
                "end) as BIRTHDATE, " +
                "(case " +
                "when nvl(GR.ENTLMNT_DATE, null) is not null then " +
                "substr(GR.ENTLMNT_DATE, 1, 4) || '-' || substr(GR.ENTLMNT_DATE, 5) " +
                "else " +
                "'' " +
                "end) as ENTLMNT_DATE, " +
                "(case " +
                "when nvl(GR.FINAL_PAY_DATE, null) is not null then " +
                "substr(GR.FINAL_PAY_DATE, 1, 4) || '-' || " +
                "substr(GR.FINAL_PAY_DATE, 5) " +
                "else " +
                "'' " +
                "end) as FINAL_PAY_DATE, " +
                "GR.RCPT_SURNAME, " +
                "GR.RCPT_GIVENNAME, " +
                "to_char(GR.LAST_UPDATE_DATE, 'YYYY-MM-DD') as LAST_UPDATE_DATE, " +
                "GR.MAR_STATUS_CODE, " +
                "GMS.MAR_STATUS_DESCRIPTION, " +
                "(case " +
                "when nvl(GR.PAY_DATE, null) is not null then " +
                "substr(GR.PAY_DATE, 1, 4) || '-' || substr(GR.PAY_DATE, 5) " +
                "else " +
                "'' " +
                "end) as PAY_DATE, " +
                "(case " +
                "when nvl(GR.POSTAL_CODE, null) is not null then " +
                "substr(GR.POSTAL_CODE, 1, 3) || ' ' || substr(GR.POSTAL_CODE, 4) " +
                "else " +
                "'' " +
                "end) as POSTAL_CODE, " +
                "GR.SPOUSE_ACT_ID, " +
                "GR.SPOUSE_GIVEN_NAME " +
                "FROM GIS_RECIPIENTS        GR, " +
                "GIS_MARITAL_STATUS_CD GMS, " +
                "GIS_ACNT_STATUS_CD    GAS, " +
                "GIS_ACNT_CD           GA " +
                "WHERE GMS.MAR_STATUS_CODE = GR.MAR_STATUS_CODE " +
                "AND GAS.ACNT_STATUS_CODE = GR.ACNT_STATUS_CODE " +
                "AND GA.ACNT_CODE = GR.ACNT_CODE";

        return result;
    }

    private void setWhereSql(StringBuilder where, String sin, String surName,
            String givenName, String birthDate, Calendar lastUpdate) {

        if (notEmpty(sin)) {
            applySuffix(where);
            if (sin.length()==9){
                where.append("GR.ACCOUNT_ID = '"+fixSql(sin.toUpperCase())+"'");
            }else{
                where.append(String.format("GR.ACCOUNT_ID LIKE '%%%s%%'", fixSql(sin.toUpperCase())));
            }
        }

        if (notEmpty(surName)) {
            applySuffix(where);
            where.append(String.format("GR.RCPT_SURNAME LIKE '%%%s%%'", fixSql(surName.toUpperCase())));
        }

        if (notEmpty(givenName)) {
            applySuffix(where);
            where.append(String.format("GR.RCPT_GIVENNAME LIKE '%%%s%%'", fixSql(givenName.toUpperCase())));
        }

        if (notEmpty(birthDate)) {
            applySuffix(where);
            where.append("GR.BIRTHDATE = '"+fixSql(birthDate.toUpperCase())+"'");
        }

        if (lastUpdate != null) {

            String ltLastUpdateDate = "";
            String gtLastUpdateDate = "";

            gtLastUpdateDate = getFormattedDateString(lastUpdate);

            Calendar ltCal = new GregorianCalendar();
            //ltCal.add(Calendar.MONTH, (lastUpdate.get(Calendar.MONTH) -1) );
            ltLastUpdateDate = getFormattedDateString(ltCal);

            applySuffix(where);

            where.append(String.format("( GR.LOAD_ID > = '%s' AND GR.LOAD_ID < = '%s') ",
                    gtLastUpdateDate, ltLastUpdateDate));
        }

    }

    private String getFormattedDateString(Calendar cal) {

        return getFormattedDateString(cal, "yyyyMM");
    }

    private String getFormattedDateString(Calendar cal, String format) {

        SimpleDateFormat sdf = new SimpleDateFormat(format);
        String result = sdf.format(cal.getTime());

        return result;
    }

    private String fixSql(String s) {
        return s.replace("'", "''");
    }

    private boolean notEmpty(String s) {

        return (s != null && !s.equals(""));
    }

    private void applySuffix(StringBuilder sb) {

        if (sb != null) {
            sb.append(" AND ");
        }
    }    // </editor-fold>
}
