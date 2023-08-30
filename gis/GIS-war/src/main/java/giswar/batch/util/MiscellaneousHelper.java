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

        String[] dataItem = new String[18];

        dataItem[0] = gisEntry.getAccountId();
        dataItem[1] = gisEntry.getSurname();
        dataItem[2] = gisEntry.getGivenname();
        dataItem[3] = gisEntry.getBrithDate();
        dataItem[4] = gisEntry.getAccountStatusCode();
        dataItem[5] = gisEntry.getAccountCode();
        dataItem[6] = gisEntry.getEntitlementDate();
        dataItem[7] = gisEntry.getPayDate();
        dataItem[8] = gisEntry.getFinalPayDate();
        dataItem[9] = gisEntry.getAddress1();
        dataItem[10] = gisEntry.getAddress2();
        dataItem[11] = gisEntry.getAddress3();
        dataItem[12] = gisEntry.getAddress4();
        dataItem[13] = gisEntry.getPostalCode();
        dataItem[14] = gisEntry.getMaritalStatusCode();
        dataItem[15] = gisEntry.getSpouseAccountId();
        dataItem[16] = gisEntry.getSpouseGivenName();
        dataItem[17] = gisEntry.getImsStartDate();

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
