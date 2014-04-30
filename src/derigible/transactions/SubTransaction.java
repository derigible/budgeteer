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

import java.text.SimpleDateFormat;
import java.util.GregorianCalendar;

import derigible.controller.GUID;

/**
 * @author marcphillips
 * This class is used for holding transactions for budgets. A transaction is held within this 
 * container, but the ability to partition how much of the transaction is 
 * placed in different budgets is added. The category, account, date, and debit or credit, excluded,
 * and note are preserved from the original transaction object. The fields that are changed are as follows:
 * 
 * 	amount -&gt; amount - amountFromOriginal
 *  originalDescription -&gt; the description of the original transaction
 *	description -&gt; the description of the sub transaction
 *	GUID -&gt; a unique GUID for the sub transaction
 *	
 * If excluded is set in the original transaction, then this is excluded. If you set this transaction
 * to excluded, nothing will happen as excluding a sub transaction does not make much sense.
 */
public class SubTransaction implements Transaction {
	private Splittable t;
	private double amountFromOriginal;
	private String description;
	private String guid;
	private String notes = "";
	
	public SubTransaction(Splittable t, double amount){
		init(t, amount, "");
	}
	
	public SubTransaction(Splittable t, double amount, String description){
		init(t, amount, description);
	}
	
	private void init(Splittable t, double amount, String description){
		this.t = t;
		amountFromOriginal = amount;
		this.notes = description;
		this.guid = GUID.generate();
		t.addSubTransaction(this);
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy.MM.dd");
		this.description = "SUBTRANSACTION OF: " + sdf.format(t.getDate().getTime()) + " : " + 
				(t.getUndividedAmount() + t.getDividedAmount()) + " : " + t.getAccount() + " : " +
				t.getDescription();
	}
	
	//More constructors for different Transaction types here.

	/* (non-Javadoc)
	 * @see derigible.transactions.Transaction#getDate()
	 */
	@Override
	public GregorianCalendar getDate() {
		return t.getDate();
	}

	/* (non-Javadoc)
	 * @see derigible.transactions.Transaction#getDescription()
	 */
	@Override
	public String getDescription() {
		return description;
	}

	/* (non-Javadoc)
	 * @see derigible.transactions.Transaction#getOriginalDescription()
	 */
	@Override
	public String getOriginalDescription() {
		return t.getDescription();
	}

	/* (non-Javadoc)
	 * @see derigible.transactions.Transaction#getAmount()
	 */
	@Override
	public double getAmount() {
		return amountFromOriginal;
	}

	/* (non-Javadoc)
	 * @see derigible.transactions.Transaction#getCategory()
	 */
	@Override
	public String getCategory() {
		return t.getCategory();
	}

	/* (non-Javadoc)
	 * @see derigible.transactions.Transaction#getAccount()
	 */
	@Override
	public String getAccount() {
		return t.getAccount();
	}

	/* (non-Javadoc)
	 * @see derigible.transactions.Transaction#isCredit()
	 */
	@Override
	public boolean isCredit() {
		return t.isCredit();
	}

	/**
	 * Will return the exclusion flag of the original transaction.
	 * 
	 * @return the exclusion of the transaction
	 */
	public boolean isExcluded() {
		return t.isExcluded();
	}

	/**
	 * This method will do nothing as it does not make sense to exclude a subtransaction.
	 */
	public void setExcluded(boolean exclude) {
		//Do nothing
	}

	/* (non-Javadoc)
	 * @see derigible.transactions.Transaction#setGUID(java.lang.String)
	 */
	@Override
	public void setGUID(String guid) {
		this.guid = guid;
	}

	/* (non-Javadoc)
	 * @see derigible.transactions.Transaction#getGUID()
	 */
	@Override
	public String getGUID() {
		return guid;
	}

	/* (non-Javadoc)
	 * @see derigible.transactions.Transaction#getNotes()
	 */
	@Override
	public String getNotes() {
		return notes;
	}

	/* (non-Javadoc)
	 * @see derigible.transactions.Transaction#addNote(java.lang.String)
	 */
	@Override
	public void addNote(String note) {
		notes += note;
	}

	/**
	 * Set the amount taken from the original transaction. This method should only be used
	 * inside of a Transaction object to reset an overage of a new SubTransaction.
	 * 
	 * @param amountFromOriginal the amount from the original transaction
	 */
	protected void setAmount(double amountFromOriginal) {
		this.amountFromOriginal = amountFromOriginal;
		t.updateSubTransactions();
	}

	/**
	 * Set the description of the subtransaction.
	 * 
	 * @param description the sub-transaction's note
	 */
	public void setDescription(String description) {
		this.description = description;
	}
	
	/**
	 * Get the parent transaction of the subtransaction.
	 * 
	 * @return the parent transaction
	 */
	public Transaction getParent(){
		return t;
	}

}
