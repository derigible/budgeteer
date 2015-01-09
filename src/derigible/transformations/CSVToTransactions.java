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
import java.util.Map;
import java.util.TreeMap;

import au.com.bytecode.opencsv.CSVReader;
import derigible.controller.GUID;
import derigible.transactions.SubTransaction;
import derigible.transactions.TList;
import derigible.transactions.Transact;
import derigible.transactions.abstracts.Transaction;
import derigible.transactions.abstracts.Transactions;
import derigible.transformations.abstracts.TransformToTransactions;
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

    /**
     * Set the file to the csv reader and the number of columns that are desired
     * to keep. Must keep at least 11 columns, defined by the following:
     * 
     * Date GUID Description Amount Transaction Type Category Account Name Notes
     * Labels Paretn Guid
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
     * @param file
     *            the file to parse
     * @throws java.io.FileNotFoundException
     *             file for transactions not found
     */
    public CSVToTransactions(File file) throws FileNotFoundException {
	csv = new CSVReader(FileU.getFileReader(file));
    }

    /**
     * Construct with the string of the filepath to the file.
     * 
     * @param file
     *            the path to the file
     * @throws FileNotFoundException
     */
    public CSVToTransactions(String file) throws FileNotFoundException {
	csv = new CSVReader(FileU.getFileReader(file));
    }

    @Override
    public Transactions data_to_transactions() throws IOException {
	List<String[]> lines = null;
	try {
	    lines = csv.readAll();
	} catch (IOException e) {
	    throw new IOException(
		    "Problem reading the transactions csv. Please make sure"
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
     * 		1 =&gt; GUID
     * 		2 =&gt; Description
     * 		3 =&gt; Amount
     * 		4 =&gt; Transaction Type
     * 		5 =&gt; Category
     * 		6 =&gt; Account Name
     * 		7 =&gt; Notes
     * 		8 =&gt; Labels
     * 		9 =&gt; Parent Guid
     *      10 =&gt; Year
     *      11 =&gt; Month
     *      12 =&gt; Day
     * </pre>
     * 
     * Some CSV exports express date as a Day/Month/Year in different columns.
     * If this is the case, put 1 in the Date position and it will skip to the
     * values in 9, 10, and 11 to make the date.
     * 
     * An example of how to use this is as follows:
     * 
     * <pre>
     * 		Date =&gt; 2
     * 		Description =&gt; 5
     * 		Amount =&gt; 6
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
     * @param headerMap
     *            the map of values in the csv to the predefined values
     * @throws IOException
     *             data type does not match
     */
    public void setHeaders(int[] headerMap) throws IOException {
	if (headerMap.length != 13 || headerMap.length != 10) {
	    throw new IOException(
		    "Incorrect  number of columns defined. Must have 10 or 13 defined.");
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
     * 		1 =&gt; GUID
     * 		2 =&gt; Description
     * 		3 =&gt; Amount
     * 		4 =&gt; Transaction Type
     * 		5 =&gt; Category
     * 		6 =&gt; Account Name
     * 		7 =&gt; Notes
     * 		8 =&gt; Labels
     *      9 =&gt; Parent Guid
     * 		10 =&gt; Year
     *      11 =&gt; Month
     *      12 =&gt; Day
     * </pre>
     * 
     * An example of what these nested array will look like follows:
     * 
     * <pre>
     * String[][] possibleHeaders = { { &quot;date&quot;, &quot;0&quot; }, { &quot;description&quot;, &quot;2&quot; },
     * 	{ &quot;amount&quot;, &quot;3&quot; }, { &quot;transaction type&quot;, &quot;4&quot; }, { &quot;category&quot;, &quot;5&quot; },
     * 	{ &quot;account name&quot;, &quot;6&quot; }, { &quot;total&quot;, &quot;3&quot; }, { &quot;credits&quot;, &quot;3&quot; },
     * 	{ &quot;debits&quot;, &quot;3&quot; }, { &quot;credit&quot;, &quot;3&quot; }, { &quot;debit&quot;, &quot;3&quot; },
     * 	{ &quot;information&quot;, &quot;7&quot; }, { &quot;info&quot;, &quot;7&quot; }, { &quot;account&quot;, &quot;6&quot; },
     * 	{ &quot;group&quot;, &quot;5&quot; }, { &quot;tag&quot;, &quot;5&quot; }, { &quot;notes&quot;, &quot;7&quot; }, { &quot;labels&quot;, &quot;8&quot; },
     * 	{ &quot;guid&quot;, &quot;1&quot; }, { &quot;id&quot;, &quot;1&quot; }, { &quot;p_guid&quot;, &quot;9&quot; },
     * 	{ &quot;parent_guid&quot;, &quot;9&quot; } };
     * </pre>
     * 
     * @param headers
     *            the headers to set
     */
    public void setPossibleHeaders(String[][] headers) {
	possibleHeaders = headers;
    }

    /**
     * Same function as setPossibleHeaders(String[][] headers) but will use a
     * file as a param and the parsing into an array is done in this module.
     * File must be an array.
     * 
     * @param file
     *            the csv containing the possible arrays
     * @throws java.io.IOException
     *             file for possible headers is not readable
     */
    public void setPossibleHeaders(File file) throws IOException {
	List<String[]> lines = null;
	try {
	    lines = csv.readAll();
	} catch (IOException e) {
	    throw new IOException(
		    "Problem reading the possible headers CSV. Please"
			    + "make sure that the file is not corrupted or altered.");
	}
	possibleHeaders = new String[lines.size()][2];
	for (int i = 0; i < lines.size(); i++) {
	    possibleHeaders[i][0] = lines.get(i)[0];
	    possibleHeaders[i][1] = lines.get(i)[1];
	}
    }

    private boolean testIfBasicsInCSV(String[] columns) throws IOException {
	// Hardcoded values should hopefully never be needed.
	String[][] possibleHeadersTemp = { { "date", "0" },
		{ "description", "2" }, { "amount", "3" },
		{ "transaction type", "4" }, { "category", "5" },
		{ "account name", "6" }, { "total", "3" }, { "credits", "3" },
		{ "debits", "3" }, { "credit", "3" }, { "debit", "3" },
		{ "debit_or_credit", "4" }, { "information", "7" },
		{ "info", "7" }, { "account", "6" }, { "group", "5" },
		{ "tag", "5" }, { "notes", "7" }, { "labels", "8" },
		{ "guid", "1" }, { "id", "1" }, { "p_guid", "9" },
		{ "parent_guid", "9" } };
	if (possibleHeaders == null) {
	    possibleHeaders = possibleHeadersTemp;
	}
	map = new int[10];
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
		    map = new int[13];
		    for (int i = 0; i < map.length; i++) {
			map[i] = 1;
		    }
		    map[0] = NODATE;
		    map[1] = -1;
		    map[9] = -1;
		    map[10] = year;
		    map[11] = month;
		    map[12] = day;
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
	    // Don't check the GUID column - check this separately.
	    if (map[i] == -1 && i != 1 && i != 9) {
		throw new IOException(
			"Not all keys set. You are missing some values. Please "
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
	String[] headers = { "Date", "Description", "Original Description",
		"Amount", "Transaction Type", "Category", "Account Name",
		"Labels", "Notes" };
	for (int i = 0; i < columns.length; i++) {
	    if (headers[i] == null ? columns[i] != null : !headers[i]
		    .equals(columns[i])) {
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
	Transact[] trans = new Transact[lines.size() - 1]; // Skip header
	for (int i = 0; i < lines.size() - 1; i++) {
	    String[] column = lines.get(i + 1); // Skip header
	    Transact t = new Transact();
	    String[] dateData = column[0].split("/");
	    int month = Integer.parseInt(dateData[0]) - 1;
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
     * the map: *
     * 
     * <pre>
     * 		0 =&gt; Date
     * 		1 =&gt; GUID
     * 		2 =&gt; Description
     * 		3 =&gt; Amount
     * 		4 =&gt; Transaction Type
     * 		5 =&gt; Category
     * 		6 =&gt; Account Name
     * 		7 =&gt; Notes
     * 		8 =&gt; Labels
     * 		9 =&gt; Parent Guid
     *      10 =&gt; Year
     *      11 =&gt; Month
     *      12 =&gt; Day
     * </pre>
     * 
     * @param lines
     * @return
     */
    private Transaction[] mappedCSVToTransactions(List<String[]> lines)
	    throws IOException {
	// Skipping header
	Transaction[] trans = new Transaction[lines.size() - 1];
	// Track the subtrans
	TreeMap<Integer, String> subtrans = new TreeMap<Integer, String>();
	// Track all the trans and their guids
	TreeMap<String, Transact> transMap = new TreeMap<String, Transact>();

	for (int i = 0; i < lines.size() - 1; i++) {
	    String[] line = lines.get(i + 1); // Skipping header
	    Transact t = new Transact();
	    GregorianCalendar g;
	    if (map[0] != NODATE) {
		g = new GregorianCalendar();
		try {
		    g.setTime(parseDate(line[map[0]]));
		} catch (IOException e) {
		    g = new GregorianCalendar(2012, 11, 12);
		}
		t.setDate(g);
	    } else {
		// GregorianCalendar starts months at 0, decrement months by 1
		g = new GregorianCalendar(Integer.parseInt(line[map[10]]),
			Integer.parseInt(line[map[11]]) - 1,
			Integer.parseInt(line[map[12]]));
		t.setDate(g);
	    }
	    t.setDescription(line[map[2]]);
	    try {
		t.setAmount(Double.parseDouble(line[map[3]]));
	    } catch (NumberFormatException e) {
		t.setAmount(-1);
	    }
	    if (StringU.lower(line[map[4]]).equals("credit")) {
		t.setDebitOrCredit(true);
	    } else {
		t.setDebitOrCredit(false);
	    }
	    t.setCategory(line[map[5]]);
	    t.setAccount(line[map[6]]);
	    if (map[1] == -1) {
		t.setGUID(GUID.generate());
	    } else {
		t.setGUID(line[map[1]]);
	    }
	    if (map[9] != -1 && !line[map[9]].isEmpty()) {
		subtrans.put(i, line[map[9]]);
	    }
	    // Check if user wants to keep notes and labels
	    // Currently just discards them
	    // TODO
	    trans[i] = t;
	    transMap.put(t.getGUID(), t); // Place in map to retrieve later if
					  // SubTransaction is found.
	}
	// Go through list
	for (Map.Entry<Integer, String> sub : subtrans.entrySet()) {
	    Transaction t = trans[sub.getKey()];
	    SubTransaction st = new SubTransaction(
		    transMap.get(sub.getValue()), t.getAmount());
	    trans[sub.getKey()] = st;
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
