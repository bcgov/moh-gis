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

        return Integer.parseInt(q.getSingleResult().toString());
        

    }

    public List<GisRecipients> findAll() {
        return em.createQuery("select object(o) from GisRecipients as o").getResultList();
    }

    public String getLastLoadDate() {

        final String SQL = "select * from gis.gis_last_load_vw";

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

        //orderBy.append(" order by row_number");
        String orderBy = " order by rcpt_surname, rcpt_givenname, account_id, load_id desc) recSubquery ";

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

        StringBuilder queryBuilder = new StringBuilder("SELECT COUNT(1) ");
        queryBuilder.append("FROM GIS.GIS_RECIPIENTS        GR, ")
                .append("GIS.GIS_MARITAL_STATUS_CD GMS, ")
                .append("GIS.GIS_ACNT_STATUS_CD    GAS, ")
                .append("GIS.GIS_ACNT_CD           GA ")
                .append("WHERE GMS.MAR_STATUS_CODE = GR.MAR_STATUS_CODE ")
                .append("AND GAS.ACNT_STATUS_CODE = GR.ACNT_STATUS_CODE ")
                .append("AND GA.ACNT_CODE = GR.ACNT_CODE");
        return queryBuilder.toString();
    }

    // <editor-fold defaultstate="collapsed" desc="Private methods">
    private String getStaticSql() {

        //NOTE: I used this static sql statement rather than the existing view
        //  as passing where clause elements as a filter doesn't cause the filter to take
        //  place prior to the view initially executing and this tends to slow things down
        //  if there's a better way of doing this let me know and I'll change it

        String result = "select ID, " +
                "LOAD_ID," +
                "ACCOUNT_ID," +
                "ACNT_CODE," +
                "ACNT_DESCRIPTION," +
                "ACNT_STATUS_CODE," +
                "ACNT_STATUS_DESC," +
                "ADDRESS1," +
                "ADDRESS2," +
                "ADDRESS3," +
                "ADDRESS4," +
                "BIRTHDATE," +
                "ENTLMNT_DATE," +
                "FINAL_PAY_DATE," +
                "RCPT_SURNAME," +
                "RCPT_GIVENNAME," +
                "LAST_UPDATE_DATE," +
                "PAY_DATE," +
                "POSTAL_CODE," +
                "SPOUSE_ACT_ID," +
                "SPOUSE_GIVEN_NAME," +
                "MAR_STATUS_DESCRIPTION," +
                " ROW_NUMBER() OVER (order by ID ) AS row_num" +
                " from(" +
                "SELECT" +
                " GR.ID," +
                "(CASE" +
                "    WHEN COALESCE(GR.LOAD_ID, '') <> '' THEN" +
                "        CONCAT(SUBSTR(GR.LOAD_ID, 1, 4), '-', SUBSTR(GR.LOAD_ID, 5))" +
                "    ELSE ''" +
                "END ) AS LOAD_ID," +
                "(CASE" +
                "    WHEN COALESCE(GR.BIRTHDATE, '') <> '' THEN" +
                "        CONCAT(SUBSTR(GR.BIRTHDATE, 1, 4), '-', SUBSTR(GR.BIRTHDATE, 5))" +
                "    ELSE ''" +
                "END) AS BIRTHDATE," +
                "(CASE" +
                "    WHEN COALESCE(GR.ENTLMNT_DATE, '') <> '' THEN" +
                "        CONCAT(SUBSTRING(GR.ENTLMNT_DATE FROM 1 FOR 4), '-', SUBSTRING(GR.ENTLMNT_DATE FROM 5))" +
                "    ELSE" +
                "        ''" +
                "END) AS ENTLMNT_DATE," +
                "(CASE  " +
                "    WHEN COALESCE(GR.FINAL_PAY_DATE, '') <> '' THEN" +
                "        CONCAT(SUBSTRING(GR.FINAL_PAY_DATE FROM 1 FOR 4), '-', SUBSTRING(GR.FINAL_PAY_DATE FROM 5))" +
                "    ELSE" +
                "        ''" +
                "END) AS FINAL_PAY_DATE," +
                "GR.ACCOUNT_ID," +
                "GR.ACNT_CODE," +
                "GA.ACNT_DESCRIPTION," +
                "GR.ACNT_STATUS_CODE," +
                "GAS.ACNT_STATUS_DESC," +
                "GR.ADDRESS1," +
                "GR.ADDRESS2," +
                "GR.ADDRESS3," +
                "GR.ADDRESS4," +
                "GR.SPOUSE_ACT_ID," +
                "GR.RCPT_SURNAME," +
                "GR.RCPT_GIVENNAME," +
                "to_char(GR.LAST_UPDATE_DATE, 'YYYY-MM-DD') as LAST_UPDATE_DATE," +
                "(CASE" +
                "    WHEN COALESCE(GR.PAY_DATE, '') <> '' THEN" +
                "        CONCAT(SUBSTRING(GR.PAY_DATE FROM 1 FOR 4), '-', SUBSTRING(GR.PAY_DATE FROM 5))" +
                "    ELSE" +
                "        ''" +
                "END) AS PAY_DATE," +
                "(CASE  " +
                "    WHEN COALESCE(GR.POSTAL_CODE, '') <> '' THEN" +
                "        CONCAT(SUBSTRING(GR.POSTAL_CODE FROM 1 FOR 3), ' ', SUBSTRING(GR.POSTAL_CODE FROM 4))" +
                "    ELSE" +
                "        ''" +
                "END) AS POSTAL_CODE," +
                "GR.SPOUSE_GIVEN_NAME," +
                "GR.MAR_STATUS_CODE," +
                "GMS.MAR_STATUS_DESCRIPTION" +
                " FROM GIS.GIS_RECIPIENTS GR," +
                "GIS.GIS_MARITAL_STATUS_CD GMS," +
                "GIS.GIS_ACNT_STATUS_CD GAS," +
                "GIS.GIS_ACNT_CD GA" +
                " WHERE GMS.MAR_STATUS_CODE = GR.MAR_STATUS_CODE" +
                " AND GAS.ACNT_STATUS_CODE = GR.ACNT_STATUS_CODE" +
                " AND GA.ACNT_CODE = GR.ACNT_CODE" ;
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

            where.append(String.format("( GR.LOAD_ID >= '%s' AND GR.LOAD_ID <= '%s') ",
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
