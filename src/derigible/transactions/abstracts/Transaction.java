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

package derigible.transactions.abstracts;

import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;
import java.util.GregorianCalendar;

/**
 * @author marphill An abstract class to define transaction objects to be used
 *         in the Transactions object.
 */
public abstract class Transaction {

	private GregorianCalendar date;
	private String description;
	private double Amount;
	private String Category;
	private String Account;
	private boolean DebitOrCredit = false;
	private String origDescription;
	private String GUID = "0";
	private String note = "";
	private PropertyChangeSupport changeSupport = new PropertyChangeSupport(
			this);

	/**
	 * Support for property change listening to the Transaction Objects.
	 * 
	 * @param listener
	 */
	public void addPropertyChangeListener(PropertyChangeListener listener) {
		changeSupport.addPropertyChangeListener(listener);
	}

	/**
	 * Support for property change listening to the Transaction Objects.
	 * 
	 * @param listener
	 */
	public void removePropertyChangeListener(PropertyChangeListener listener) {
		changeSupport.removePropertyChangeListener(listener);
	}

	/**
	 * Support for property change listening to the Transaction Objects.
	 * 
	 * @param listener
	 */
	public void addPropertyChangeListener(String propertyName,
			PropertyChangeListener listener) {
		changeSupport.addPropertyChangeListener(propertyName, listener);
	}

	/**
	 * Support for property change listening to the Transaction Objects.
	 * 
	 * @param listener
	 */
	public void removePropertyChangeListener(String propertyName,
			PropertyChangeListener listener) {
		changeSupport.removePropertyChangeListener(propertyName, listener);
	}

	/**
	 * Support for property change listening to the Transaction Objects.
	 * 
	 * @param listener
	 */
	protected void firePropertyChange(String propertyName, Object oldValue,
			Object newValue) {
		changeSupport.firePropertyChange(propertyName, oldValue, newValue);
	}

	/**
	 * All transactions assumed included.
	 */
	private boolean excluded = false;

	/**
	 * The date of the transaction, represented as a Gregorian Calendar object.
	 * 
	 * @return the date as a GregorianCalendar
	 */
	public GregorianCalendar getDate() {
		return date;
	}

	/**
	 * Change the date of the Transaction. Due to the possibility of some
	 * indexes being thrown off, this should only be called through the
	 * Transactions object that contains it.
	 * 
	 * @param gc
	 *            the new GregorianCalendar representing the date
	 */
	public void setDate(GregorianCalendar date) {
		firePropertyChange("date", this.date, this.date = date);
	}

	/**
	 * The description of the transaction.
	 * 
	 * @return the description of the transaction.
	 */
	public String getDescription() {
		return description;
	}

	/**
	 * Set the Description of the transaction.
	 * 
	 * @param desc
	 *            the description to set
	 */
	public void setDescription(String description) {
		firePropertyChange("description", this.description,
				this.description = description);
	}

	/**
	 * Get the original description of the transaction
	 * 
	 * @return the original description
	 */
	public String getOriginalDescription() {
		return this.origDescription;
	}

	/**
	 * Set o description.
	 * 
	 * @param desc
	 */
	public void setOriginalDescription(String original) {
		firePropertyChange("originalDescription", this.origDescription,
				this.origDescription = original);
	}

	/**
	 * The amount of the transaction.
	 * 
	 * @return the amount of the transaction
	 */
	public double getAmount() {
		return Amount;
	}

	/**
	 * Set the amount.
	 * 
	 * @param amount
	 */
	public void setAmount(double amount) {
		firePropertyChange("amount", this.Amount, Amount = amount);
	}

	/**
	 * The category that the transaction falls under.
	 * 
	 * @return the category of the transaction
	 */
	public String getCategory() {
		return Category;
	}

	/**
	 * Set the category of the transaction. Because of possible indexing issues,
	 * this should be done only through the Transactions object that contains
	 * it.
	 * 
	 * @param category
	 *            the category to set
	 */
	public void setCategory(String category) {
		firePropertyChange("category", this.Category, Category = category);
	}

	/**
	 * The account from which the money was debited/credited.
	 * 
	 * @return the account name
	 */
	public String getAccount() {
		return this.Account;
	};

	/**
	 * Set the account of the transaction. Because of possible indexing issues,
	 * this should be done only through the Transations object that contains it.
	 * 
	 * @param account
	 */
	public void setAccount(String account) {
		firePropertyChange("account", this.Account, this.Account = account);
	};

	/**
	 * Whether or not it was money coming into our accounts or out. Debit for
	 * out, credit for in.
	 * 
	 * @return false for Debit, true for Credit (or reverse if primarily used in
	 *         a sales env)
	 */
	public boolean isCredit() {
		return DebitOrCredit;
	}

	/**
	 * Set whether this is a debit or credit. Due to indexing, should only be
	 * called through the Transactions object that contains it.
	 * 
	 * @param credit
	 */
	public void setDebitOrCredit(boolean debitOrCredit) {
		firePropertyChange("debitOrCredit", this.DebitOrCredit,
				DebitOrCredit = debitOrCredit);
	}

	/**
	 * Set whether or not the transaction has been excluded from use.
	 * Transaction objects should be assumed included.
	 * 
	 * @return true if excluded, false otherwise
	 */
	public boolean isExcluded() {
		return excluded;
	}

	/**
	 * All transactions need to be able to be excluded at any given time.
	 * 
	 * @param exclude
	 *            the exclusion flag to set
	 */
	public void setExcluded(boolean exclude) {
		excluded = exclude;
	}

	/**
	 * Set the guid of the transaction.
	 * 
	 * @param guid
	 *            the guid
	 */
	public void setGUID(String gUID) {
		GUID = gUID;
	}

	/**
	 * Get the guid of the transaction.
	 * 
	 * @return the guid
	 */
	public String getGUID() {
		return GUID;
	}

	/**
	 * Get the notes of the transaction.
	 * 
	 * @return the notes
	 */
	public String getNotes() {
		return note;
	}

	/**
	 * Add a note to the notes of the transaction.
	 * 
	 * @param note
	 *            the note to add
	 */
	public void addNote(String note) {
		this.note += "; " + note;
	}

	/**
	 * Tell whether or not there are any subtransactions to be found.
	 * 
	 * @return true if has a subtransaction
	 */
	public abstract boolean hasSubTransactions();

	/**
	 * Determine if this transaction is a subtransaction. A subtransaction
	 * should not have any Subtransactions.
	 * 
	 * @return true if a SubTransaction
	 */
	public abstract boolean isSubTransaction();
}
