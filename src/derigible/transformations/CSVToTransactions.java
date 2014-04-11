/*******************************************************************************
 * Copyright (c) 2014 Derigible Enterprises.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Derigible Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.derigible.com/license
 *
 * Contributors:
 *     Derigible Enterprises - initial API and implementation
 *******************************************************************************/

package derigible.transformations;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.text.DateFormat;
import java.text.ParseException;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;
import java.util.Locale;

import derigible.controller.GUID;
import derigible.transactions.TList;
import derigible.transactions.Transact;
import derigible.transactions.Transaction;
import derigible.transactions.Transactions;
import au.com.bytecode.opencsv.CSVReader;
import derigible.utils.FileU;
import derigible.utils.StringU;

/**
 *
 * @author Guest-temp
 */
public class CSVToTransactions implements TransformToTransactions {

    private CSVReader csv = null;
    private int[] map;
    private String[][] possibleHeaders;
    private static final int NODATE = 1000;
    private int guid = -1;

    /**
     * Set the file to the csv reader and the number of columns that are desired
     * to keep. Must keep at least 9 columns, defined by the following:
     *
     * Date Description Amount Transaction Type Category Account Name Notes
     * Labels
     *
     * CSVToTransactions will attempt to match possible similar names to these
     * columns. The user is encouraged to use the setPossibleHeaders method to
     * define alternate versions of the above names, otherwise CSVToTransactions
     * will attempt a best effort to match the headers to the defined values
     * above.
     *
     * Note that any header not defined in setHeaders will be removed.
     *
     * NOTE: Date formats that do not match MM/DD/YYYY will throw a parse error
     * and will be converted to 12/12/2012.
     *
     * @param file the file to parse
     * @throws java.io.FileNotFoundException file for transactions not found
     */
    public CSVToTransactions(File file) throws FileNotFoundException {
        csv = new CSVReader(FileU.getFileReader(file));
    }

    @Override
    public Transactions data_to_transactions() throws IOException {
        List<String[]> lines = null;
        try {
            lines = csv.readAll();
        } catch (IOException e) {
            throw new IOException("Problem reading the transactions csv. Please make sure"
                    + "that the file is not corrupted in some way.");
        }

        if (map != null) {
            csv.close();
            return new TList(mappedCSVToTransactions(lines));
        } else if (testIfMintCSV(lines.get(0))) {
            return new TList(mint_comCSVToTransactions(lines));
        } else if (this.testIfBasicsInCSV(lines.get(0))) {
            return new TList(mappedCSVToTransactions(lines));
        } else {
            throw new IOException(
                    "Data does not seem to match data requirements.");
        }
    }

    /**
     * To set the possible headers, you are essentially trying to map the
     * following values:
     *
     * Date Description Amount Transaction Type Category Account Name Notes
     * Labels
     *
     * To the equivalent headers found in your csv. To set the headers, pass in
     * an of integers, where the integer contains the array index for the
     * substituted header.
     *
     * The positions of the map array are mapped in the order above, or namely:
     *
     * <pre>
     * 		0 =&gt; Date
     * 		1 =&gt; Description
     * 		2 =&gt; Amount
     * 		3 =&gt; Transaction Type
     * 		4 =&gt; Category
     * 		5 =&gt; Account Name
     * 		6 =&gt; Notes
     * 		7 =&gt; Labels
     * 		8 =&gt; Year
     *      9 =&gt; Moth
     *      10 =&gt; Day
     * </pre>
     *
     * Some CSV exports express date as a Day/Month/Year in different columns.
     * If this is the case, put 1 in the Date position and it will skip to the
     * values in 9, 10, and 11 to make the date.
     *
     * An example of how to use this is as follows:
     *
     * <pre>
     * 		Date =&gt; 1
     * 		Description =&gt; 4
     * 		Amount =&gt; 5
     * 		... (Same pattern for the rest)
     * 		Year =&gt;  0
     * 		Month =&gt; 1
     * 		Day =&gt; 2
     * </pre>
     *
     * If a column does not match the type expected (ie the map for amount is
     * not a double or integer) will throw an IOException.
     *
     * NOTE: Mint.com CSVs are automatically detected and will remove the
     * original description. Therefore, no need to set the headers.
     *
     * @param headerMap the map of values in the csv to the predefined values
     * @throws IOException data type does not match
     */
    public void setHeaders(int[] headerMap) throws IOException {
        if (headerMap.length != 11 || headerMap.length != 8) {
            throw new IOException(
                    "Incorrect  number of columns defined. Must have 8 or 11 defined.");
        }
        map = headerMap;
    }

    /**
     * Set the possible headers. This is used for extensibility to help make the
     * CSVToTransactions class able to predict more column headers overtime and
     * experience.
     *
     * Pass in a array of String arrays, where the first position of the nest
     * array refers to the possible value (as a String), and the second refers
     * to the value header defined in the following:
     *
     * <pre>
     * 		0 =&gt; Date
     * 		1 =&gt; Description
     * 		2 =&gt; Amount
     * 		3 =&gt; Transaction Type
     * 		4 =&gt; Category
     * 		5 =&gt; Account Name
     * 		6 =&gt; Notes
     * 		7 =&gt; Labels
     * 		8 =&gt; Year
     *      9 =&gt; Month
     *      10 =&gt; Day
     * </pre>
     *
     * An example of what these nested array will look like follows:
     *
     * <pre>
     * String[][] possibleHeaders = { {"date","0"}, {"description","1"}, {"amount", "2"},
     * {"transaction type", "3"}, {"category", "4"}, {"account name", "5"},
     * {"total", "2"}, {"credits", "2"}, {"debits", "2"}, {"credit", "2"},
     * {"debit", "2"}, {"information", "6"}, {"info", "6"}, {"account", "5"},
     * {"group", "4"}, {"tag","4"} };
     * </pre>
     *
     * @param headers the headers to set
     */
    public void setPossibleHeaders(String[][] headers) {
        possibleHeaders = headers;
    }

    /**
     * Same function as setPossibleHeaders(String[][] headers) but will use a
     * file as a param and the parsing into an array is done in this module.
     * File must be an array.
     *
     * @param file the csv containing the possible arrays
     * @throws java.io.IOException file for possible headers is not readable
     */
    public void setPossibleHeaders(File file) throws IOException {
        List<String[]> lines = null;
        try {
            lines = csv.readAll();
        } catch (IOException e) {
            throw new IOException("Problem reading the possible headers CSV. Please"
                    + "make sure that the file is not corrupted or altered.");
        }
        possibleHeaders = new String[lines.size()][2];
        for (int i = 0; i < lines.size(); i++) {
            possibleHeaders[i][0] = lines.get(i)[0];
            possibleHeaders[i][1] = lines.get(i)[1];
        }
    }
    
    /**
     * Set the column index that has the GUID assigned to it. If this method is not used, the
     * transactions will be provided a new GUID.
     * 
     * @param index - the GUID column
     */
    public void setGUIDLocation(int index){
    	guid = index;
    }
    
    private boolean testIfBasicsInCSV(String[] columns) throws IOException {
        //Hardcoded values should hopefully never be needed.
        String[][] possibleHeadersTemp = {{"date", "0"}, {"description", "1"}, {"amount", "2"},
        {"transaction type", "3"}, {"category", "4"}, {"account name", "5"},
        {"total", "2"}, {"credits", "2"}, {"debits", "2"}, {"credit", "2"},
        {"debit", "2"}, {"information", "6"}, {"info", "6"}, {"account", "5"},
        {"group", "4"}, {"tag", "4"}, {"notes", "6"}, {"labels", "7"}};
        if (possibleHeaders == null) {
            possibleHeaders = possibleHeadersTemp;
        }
        map = new int[8];
        for (int i = 0; i < map.length; i++) {
            map[i] = -1;
        }
        int year = testIfContains("year", columns);
        if (year >= 0) {
            int month = testIfContains("month", columns);
            if (month >= 0) {
                int day = testIfContains("day", columns);
                if (day >= 0) {
                    if ((testIfContains("date", columns)) >= 0) {
                        throw new IOException(
                                "Date and year/month/day defined. Can only have one or the other");
                    }
                    map = new int[11];
                    for (int i = 0; i < map.length; i++) {
                        map[i] = 1;
                    }
                    map[0] = NODATE;
                    map[8] = year;
                    map[9] = month;
                    map[10] = day;
                } else {
                    throw new IOException(
                            "Year and month found but does not contain a day.");
                }
            } else {
                throw new IOException(
                        "Year found but does not contain a month.");
            }
        }
        for (int i = 0; i < columns.length; i++) {
            String test = StringU.lower(columns[i]);
            for (int j = 0; j < possibleHeaders.length; j++) {
                if (test.equals(possibleHeaders[j][0])) {
                    map[Integer.parseInt(possibleHeaders[j][1])] = i;
                }
            }
        }
        for (int i = 0; i < map.length; i++) {
            if (map[i] == -1) {
                throw new IOException("Not all keys set. You are missing some values. Please "
                        + "define the headers of your CSV.");
            }
        }
        return true;
    }

    private int testIfContains(String test, String[] target) {
        for (int i = 0; i < target.length; i++) {
            if (StringU.lower(target[i]).equals(test)) {
                return i;
            }
        }
        return -1;
    }

    /**
     * test if the passed in csv is a mint.com csv. If so, remove the original
     * description.
     *
     * @param columns
     * @return
     */
    private boolean testIfMintCSV(String[] columns) {
        if (columns.length != 9) {
            return false;
        }
        String[] headers = {"Date", "Description", "Original Description",
            "Amount", "Transaction Type", "Category", "Account Name",
            "Labels", "Notes"};
        for (int i = 0; i < columns.length; i++) {
            if (headers[i] == null ? columns[i] != null : !headers[i].equals(columns[i])) {
                return false;
            }
        }
        map = new int[8];
        map[0] = 0;
        map[1] = 1;
        map[2] = 3;
        map[3] = 4;
        map[4] = 5;
        map[5] = 6;
        map[6] = 7;
        map[7] = 8;
        return true;
    }

    private Transaction[] mint_comCSVToTransactions(List<String[]> lines) {
        Transact[] trans = new Transact[lines.size() - 1]; //Skip header
        for (int i = 0; i < lines.size() - 1; i++) {
            String[] column = lines.get(i + 1); //Skip header
            Transact t = new Transact();
            String[] dateData = column[0].split("/");
            int month = Integer.parseInt(dateData[0]);
            int day = Integer.parseInt(dateData[1]);
            int year = Integer.parseInt(dateData[2]);
            t.setDate(new GregorianCalendar(year, month, day));
            t.setDescription(column[1]);
            t.setOriginalDescription(column[2]);
            t.setAmount(Double.parseDouble(column[3]));
            if (column[4].equals("credit")) {
                t.setDebitOrCredit(true);
            }
            t.setCategory(column[5]);
            t.setAccount(column[6]);
            t.setGUID(GUID.generate());
            trans[i] = t;
        }
        return trans;
    }

    /**
     * the map:
     * * <pre>
     * 		0 =&gt; Date
     * 		1 =&gt; Description
     * 		2 =&gt; Amount
     * 		3 =&gt; Transaction Type
     * 		4 =&gt; Category
     * 		5 =&gt; Account Name
     * 		6 =&gt; Notes
     * 		7 =&gt; Labels
     * 		8 =&gt; Year
     *      9 =&gt; Moth
     *      10 =&gt; Day
     * </pre>
     *
     * @param lines
     * @return
     */
    private Transaction[] mappedCSVToTransactions(List<String[]> lines) throws IOException {
        Transact[] trans = new Transact[lines.size() - 1]; //Skipping header

        for (int i = 0; i < lines.size() - 1; i++) {
            String[] line = lines.get(i + 1); //Skipping header
            Transact t = new Transact();
            GregorianCalendar g;
            if (map[0] != NODATE) {
                g = new GregorianCalendar();
                try {
                    g.setTime(parseDate(line[map[0]]));
                } catch (IOException e) {
                    g = new GregorianCalendar(2012, 12, 12);
                }
                t.setDate(g);
            } else {
                g = new GregorianCalendar(Integer.parseInt(line[map[8]]),
                        Integer.parseInt(line[map[9]]), Integer.parseInt(line[map[10]]));
                t.setDate(g);
            }
            t.setDescription(line[map[1]]);
            try {
                t.setAmount(Double.parseDouble(line[map[2]]));
            } catch (NumberFormatException e) {
                t.setAmount(-1);
            }
            if (StringU.lower(line[map[3]]).equals("credit")) {
                t.setDebitOrCredit(true);
            }
            t.setCategory(line[map[4]]);
            t.setAccount(line[map[5]]);
            if(guid == -1){
            	t.setGUID(GUID.generate());
            } else {
            	t.setGUID(line[guid]);
            }
            
	    // Check if user wants to keep notes and labels
            //Currently just discards them
            //TODO
            trans[i] = t;
        }
        return trans;
    }

    private Date parseDate(String dateString) throws IOException {
        for (Locale locale : DateFormat.getAvailableLocales()) {
            for (int style = DateFormat.FULL; style <= DateFormat.SHORT; style++) {
                DateFormat df = DateFormat.getDateInstance(style, locale);
                try {
                    return df.parse(dateString);
                } catch (ParseException ex) {
                    continue; // unparseable, try the next one
                }
            }
        }
        throw new IOException("Date Unparseable, switching to default date.");
    }
}
