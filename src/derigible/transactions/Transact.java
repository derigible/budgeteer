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
 *
 * A Solid implementation of the Transaction object. For most use cases you will
 * want to use this object.
 */
public class Transact implements Transaction, Splittable {

    private GregorianCalendar date;
    private String description;
    private double Amount;
    private String Category;
    private String Account;
    private boolean DebitOrCredit = false;
    private String origDescription;
    private String GUID = "0";
    private String note = "";
    private SubTransaction[] subTrans;
    private int arrayPoint = 0;
    /**
     * All transactions assumed included.
     */
    private boolean excluded = false;

    /**
     * Empty constructor. Add data in later.
     */
    public Transact() {
        // do nothing
    }

    /**
     * Empty constructor. Add data in now.
     *
     * @param d Date
     * @param desc Description
     * @param amount Amount
     * @param cat Category
     * @param account Account
     * @param doc Debit or Credit
     */
    public Transact(GregorianCalendar d, String desc, double amount,
            String cat, String account, boolean doc) {
        date = d;
        description = desc;
        Amount = amount;
        Category = cat;
        Account = account;
        DebitOrCredit = doc;
    }

    /**
     * @return the account
     */
    @Override
    public String getAccount() {
        return Account;
    }

    /**
     * @param account the account to set
     */
    public void setAccount(String account) {
        Account = account;
    }

    /**
     * @return the date
     */
    @Override
    public GregorianCalendar getDate() {
        return date;
    }

    /**
     * @param date the date to set
     */
    public void setDate(GregorianCalendar date) {
        this.date = date;
    }

    /**
     * @return the description
     */
    @Override
    public String getDescription() {
        return description;
    }

    /**
     * @param description the description to set
     */
    public void setDescription(String description) {
        this.description = description;
    }

    /**
     * @return the amount
     */
    @Override
    public double getAmount() {
        return Amount;
    }

    /**
     * @param amount the amount to set
     */
    public void setAmount(double amount) {
        Amount = amount;
    }

    /**
     * @return the category
     */
    @Override
    public String getCategory() {
        return Category;
    }

    /**
     * @param category the category to set
     */
    public void setCategory(String category) {
        Category = category;
    }

    /**
     * @return the debitOrCredit
     */
    @Override
    public boolean isCredit() {
        return DebitOrCredit;
    }

    /**
     * @param debitOrCredit the debitOrCredit to set
     */
    public void setDebitOrCredit(boolean debitOrCredit) {
        DebitOrCredit = debitOrCredit;
    }

    @Override
    public boolean isExcluded() {
        return excluded;
    }

    @Override
    public void setExcluded(boolean exclude) {
        excluded = exclude;
    }

    /**
     *
     * @return the original description
     */
    @Override
    public String getOriginalDescription() {
        return this.origDescription;
    }

    /**
     *
     * @param original the original description
     */
    public void setOriginalDescription(String original) {
        this.origDescription = original;
    }

	/**
	 * @return the gUID
	 */
	public String getGUID() {
		return GUID;
	}

	/**
	 * @param gUID the gUID to set
	 */
	public void setGUID(String gUID) {
		GUID = gUID;
	}

	@Override
	public String getNotes() {
		return note;
	}

	@Override
	public void addNote(String note) {
		note += "; " + note;
	}

	/**
	 * Get the sub-transactions associated with this transaction, if any.
	 * 
	 * @return the sub transactions
	 */
	public SubTransaction[] getSubTransactions() {
		return subTrans;
	}

	/**
	 * Add a sub transaction to this transaction.
	 * 
	 * @param split the sub transaction to set
	 */
	public void addSubTransactions(SubTransaction split) {
		if(this.subTrans == null){
			subTrans = new SubTransaction[6];
		} else if(subTrans.length == arrayPoint){ //Array is full, resize
			SubTransaction[] temp = new SubTransaction[subTrans.length + 3];
			for(int i = 0; i < subTrans.length; i++){
				temp[i] = subTrans[i];
			}
		} 
		subTrans[arrayPoint] = split;
		arrayPoint++;
	}
	
	
	public void removeSubTransaction(SubTransaction split){
		removeSubTransaction(split.getGUID());
	}
	
	
	public void removeSubTransaction(String GUID){
		for(int j = 0; j < subTrans.length; j++){
			if(subTrans[j].getGUID().equals(GUID)){
				SubTransaction[] temp = new SubTransaction[subTrans.length + 3];
				for(int i = j; i < subTrans.length; i++){
					temp[i] = subTrans[i];
				}
				arrayPoint--;
				break;
			}
		}
	}
}
