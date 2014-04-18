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

package derigible.transactions;

import java.util.GregorianCalendar;

/**
 * @author marphill
 *	An interface to define transaction objects to be used in the Transactions
 *	object. Since it is unclear at this point how a Transaction may need to be
 *	stored, it is best to just define an interface and let the implementation
 *	determine the structure.
 *
 *	Some Transactions classes can use this to call themselves to get data.
 */
public interface Transaction {
	/**
	 * The date of the transaction, represented as a Gregorian Calendar object.
	 * 
	 * @return the date as a GregorianCalendar
	 */
	public abstract GregorianCalendar getDate();
	
	/**
	 * The description of the transaction.
	 * 
	 * @return the description of the transaction.
	 */
	public abstract String getDescription();
	
    /**
     *
     * @return the original description
     */
    public abstract String getOriginalDescription();
	
	/**
	 * The amount of the transaction.
	 * 
	 * @return the amount of the transaction
	 */
	public abstract double getAmount();
	
	/**
	 * The category that the transaction falls under.
	 * 
	 * @return the category of the transaction
	 */
	public abstract String getCategory();
	
	/**
	 * The account from which the money was debited/credited.
	 * 
	 * @return the account name
	 */
	public abstract String getAccount();
	
	/**
	 * Whether or not it was money coming into our accounts or out. Debit for out, credit for in.
	 * 
	 * @return false for Debit, true for Credit (or reverse if primarily used in a sales env)
	 */
	public abstract boolean isCredit();
	
	/**
	 * Set whether or not the transaction has been excluded from use. Transaction objects should
	 * be assumed included.
	 * 
	 * @return true if excluded, false otherwise
	 */
	public abstract boolean isExcluded();
	
	/**
	 * All transactions need to be able to be excluded at any given time.
     * @param exclude the exclusion flag to set
	 */
	public abstract void setExcluded(boolean exclude);
	
	/**
	 * Set the guid of the transaction.
	 * 
	 * @param guid the guid
	 */
	public abstract void setGUID(String guid);
	
	/**
	 * Get the guid of the transaction.
	 * 
	 * @return the guid
	 */
	public abstract String getGUID();
	
	/**
	 * Get the notes of the transaction.
	 * 
	 * @return the notes
	 */
	public abstract String getNotes();
	
	/**
	 * Add a note to the notes of the transaction.
	 * 
	 * @param note the note to add
	 */
	public abstract void addNote(String note);
}
