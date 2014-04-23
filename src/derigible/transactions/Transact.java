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
public class Transact implements Splittable {

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
    private double amountAllocated = 0;
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

    @Override
	public String getGUID() {
		return GUID;
	}

	@Override
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
	
	public boolean hasSubTransactions(){
		return subTrans != null ? (subTrans[0] != null ? true : false) : false;
	}

	@Override
	public void addSubTransaction(SubTransaction split) {
		if((amountAllocated + split.getAmount()) > this.Amount){
			split.setAmount(this.Amount - amountAllocated);
			split.addNote("SubTransaction amount truncated to fit allowed amount. Amount changed to: " + (this.Amount - amountAllocated));
		} 
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
		amountAllocated += split.getAmount();
	}
	
	/**
	 * Remove the subtransaction from the transaction.
	 * 
	 * @param split the sub transaction to remove
	 */
	public void removeSubTransaction(SubTransaction split){
		removeSubTransaction(split.getGUID());
	}
	
	/**
	 * Remove the subtransaction from the transaction by its GUID.
	 * 
	 * @param GUID the guid of the subtransaction to remove
	 */
	public void removeSubTransaction(String GUID){
		for(int j = 0; j < subTrans.length; j++){
			if(subTrans[j] == null){ //GUID not found, must not be in subtransaction list
				break;
			}
			if(subTrans[j].getGUID().equals(GUID)){
				amountAllocated -= subTrans[j].getAmount();
				for(int i = j+1; i < subTrans.length; i++){
					subTrans[i - 1] = subTrans[i];
				}
				arrayPoint--;
				break;
			}
		}
	}

	@Override
	public void addSubTransaction(double amount) {
		this.addSubTransaction(new SubTransaction(this, amount));		
	}

	@Override
	public double getUndividedAmount() {
		return this.Amount - this.amountAllocated;
	}

	@Override
	public double getDividedAmount() {
		return this.amountAllocated;
	}

	@Override
	public void updateSubTransactions() {
		amountAllocated = 0;
		for(int i = 0; i < subTrans.length; i++){
			if(subTrans[i] == null){ //no more sub transactions in list
				break;
			}
			amountAllocated += subTrans[i].getAmount();
		}
	}

	@Override
	public SubTransaction getSubTransaction(SubTransaction split) {
		return this.getSubTransaction(split.getGUID());
	}

	@Override
	public SubTransaction getSubTransaction(String GUID) {
		for(int i = 0 ; i < subTrans.length; i++){
			if(subTrans[i] == null){ //GUID not found, must not be in subtransaction list
				break;
			}
			if(subTrans[i].getGUID().equals(subTrans[i].getGUID())){
				return subTrans[i];
			}
		}
		return null;
	}
}
