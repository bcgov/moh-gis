/*******************************************************************************
 * Copyright © 2015, Province of British Columbia.                             *
 *                                                                             *
 * All rights reserved.                                                        *
 *                                                                             *
 * File:                        MiscellaneousHelper.java                       *
 * Date of Last Commit: $Date::                                              $ *
 * Revision Number:      $Rev::                                              $ *
 * Last Commit by:    $Author::                                              $ *
 *                                                                             *
 *******************************************************************************/

/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package giswar.batch.util;

import giswar.batch.GisEntry;
import java.util.Calendar;

/**
 *
 * @author adebiyi.kuseju
 */
public class MiscellaneousHelper {

    public static String getDateId() {
        Calendar cal = Calendar.getInstance();
        StringBuilder builder = new StringBuilder().append(cal.get(Calendar.YEAR));
        int month = cal.get(Calendar.MONTH) + 1;
        builder.append(month < 10 ? "0" + month : month);

        return builder.toString();
    }

    public static String formatHarsData(String value, int size) {

        String result = null;
        if (value != null) {

            if (value.length() > size) {
                result = value.substring(0, size);
            } else if (value.length() <= size) {
                StringBuilder builder = new StringBuilder(value);

                for (int i = value.length(); i < size; i++) {
                    builder.append(' ');
                }
                result = builder.toString();
            }
        }

        return result;
    }

    public static String[] makeStringArray(GisEntry gisEntry) {

        String[] dataItem = new String[19];

        dataItem[0] = gisEntry.getAccountId();
        dataItem[1] = gisEntry.getSurname();
        dataItem[2] = gisEntry.getMiddlename();
        dataItem[3] = gisEntry.getGivenname();
        dataItem[4] = gisEntry.getBrithDate();
        dataItem[5] = gisEntry.getAccountStatusCode();
        dataItem[6] = gisEntry.getAccountCode();
        dataItem[7] = gisEntry.getEntitlementDate();
        dataItem[8] = gisEntry.getPayDate();
        dataItem[9] = gisEntry.getFinalPayDate();
        dataItem[10] = gisEntry.getAddress1();
        dataItem[11] = gisEntry.getAddress2();
        dataItem[12] = gisEntry.getAddress3();
        dataItem[13] = gisEntry.getAddress4();
        dataItem[14] = gisEntry.getPostalCode();
        dataItem[15] = gisEntry.getMaritalStatusCode();
        dataItem[16] = gisEntry.getSpouseAccountId();
        dataItem[17] = gisEntry.getSpouseGivenName();
        dataItem[18] = gisEntry.getImsStartDate();

        return dataItem;
    }

    public static String toString(String[] array) {

        StringBuilder builder = new StringBuilder();
        if (array != null) {

            for (String s : array) {

                if (s!= null) {
                    builder.append(s).append(", ");
                }
            }

            if (builder.length() > 0) {
                builder.delete(builder.lastIndexOf(", "), builder.length());
            }
        }

        return builder != null ? builder.toString() : null;
    }
}
